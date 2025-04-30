package com.example.apisample.product.service.impl;

import com.example.apisample.config.BucketConfiguration;
import com.example.apisample.product.entity.Product;
import com.example.apisample.product.exception.ProductDeletedException;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.product.model.dto.ProductCreateDTO;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.model.dto.ProductUpdateDTO;
import com.example.apisample.product.model.mapper.ProductMapper;
import com.example.apisample.product.repository.ProductRepository;
import com.example.apisample.product.service.ProductService;
import com.example.apisample.product.service.S3Service;
import com.example.apisample.rating.repository.RatingRepository;
import com.example.apisample.rating.service.RatingService;
import com.example.apisample.user.exception.NotImageFileException;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RatingService ratingService;

    private static final String SORTFIELD_PRICE = "price";
    private static final String SORTFIELD_PRICE_DESC = "priceDesc";
    private static final String SORTFIELD_RATING = "rating";
    private static final String SORTFIELD_DEFAULT = "createOn";

    @Value("${aws.s3.bucket.url}")
    String S3URL;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private BucketConfiguration s3Bucket;

    @Override
    public APIPageableResponseDTO<ProductResponseDTO> getALlProduct(int pageNo, int pageSize, String search, String sortField) {
        Sort sort = switch (sortField) {
            case SORTFIELD_PRICE -> Sort.by(Sort.Direction.ASC, SORTFIELD_PRICE);
            case SORTFIELD_PRICE_DESC -> Sort.by(Sort.Direction.DESC, SORTFIELD_PRICE);
            default -> Sort.by(Sort.Direction.DESC, SORTFIELD_DEFAULT);
        };

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<Product> page = productRepository.findByNameContainingAndDeletedFalse(pageable, search);

        List<ProductResponseDTO> productList = page.getContent().stream()
                .map(product -> {
                    Double avgRating = ratingService.calculateAverageRating(product.getId());
                    return ProductMapper.productToDTO(product, avgRating);
                })
                .collect(Collectors.toList());

        if (sortField.equals(SORTFIELD_RATING)) {
            productList.sort((a, b) -> Double.compare(
                    b.getAverageRating() != null ? b.getAverageRating() : 0,
                    a.getAverageRating() != null ? a.getAverageRating() : 0
            ));
        }

        Page<ProductResponseDTO> sortedProductPage = new PageImpl<>(
                productList,
                pageable,
                page.getTotalElements()
        );

        return new APIPageableResponseDTO<>(sortedProductPage);
    }




    @Override
    public APIPageableResponseDTO<ProductResponseDTO> getALlFeaturedProduct(int pageNo, int pageSize, String search, String sortField) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortField).descending());

        Page<Product> page = productRepository.findByDeletedFalseAndFeaturedTrue(pageable);

        Page<ProductResponseDTO> productDtoPage = page.map(product -> {
            Double avgRating = ratingService.calculateAverageRating(product.getId());
            return ProductMapper.productToDTO(product, avgRating);
        });

        return new APIPageableResponseDTO<>(productDtoPage);
    }


    @Override
    public ProductResponseDTO getProductById(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        if (product.getDeleted()) {
            throw new ProductDeletedException();
        }

        return ProductMapper.productToDTO(product, ratingService.calculateAverageRating(product.getId()));
    }

    @Override
    @Transactional
    public void createProduct(ProductCreateDTO dto) {

        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .featured(dto.getFeatured())
                .deleted(Boolean.FALSE)
                .build();

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, ProductUpdateDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if (product.getDeleted()) {
            throw new ProductDeletedException();
        }

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getFeatured() != null) product.setFeatured(dto.getFeatured());

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        product.setDeleted(Boolean.TRUE);

        productRepository.save(product);
    }

    @Override
    @Transactional
    public void restoreProduct(Integer id) {
        Product product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);

        product.setDeleted(Boolean.FALSE);

        productRepository.save(product);
    }

    public String uploadProductImage(Integer productId, MultipartFile file) throws IOException {
        if(ImageIO.read(file.getInputStream()) == null) throw new NotImageFileException();

        //store image
        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        String key = "product-image/" + product.getId() + "/" + UUID.randomUUID();

        s3Service.putObject(
                s3Bucket.getCustomer(),
                key,
                file.getBytes()
        );

        product.setImage(S3URL + key);

        productRepository.save(product);

        return S3URL + key;
    }

}

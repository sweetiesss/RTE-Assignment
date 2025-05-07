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
import java.util.Comparator;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final RatingService ratingService;

    private static final String SORTFIELD_PRICE = "price";
    private static final String SORTFIELD_PRICE_DESC = "priceDesc";
    private static final String SORTFIELD_RATING = "rating";

    @Value("${aws.s3.bucket.url}")
    private String S3URL;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private BucketConfiguration s3Bucket;

    @Override
    public APIPageableResponseDTO<ProductResponseDTO> getAllProduct(
            int pageNo,
            int pageSize,
            String search,
            String sortField
    ) {
        List<Product> products = productRepository
                .findByNameContainingIgnoreCaseAndDeletedFalse(search);

        List<ProductResponseDTO> productDTOs = products.stream()
                .map(product -> {
                    Double avgRating = ratingService.calculateAverageRating(product.getId());
                    return ProductMapper.productToDTO(product, avgRating);
                })
                .toList();

        // Comparator based on sortField
        Comparator<ProductResponseDTO> comparator = getComparator(sortField);
        List<ProductResponseDTO> sorted = productDTOs.stream()
                .sorted(comparator)
                .toList();

        // Manual pagination
        int total = sorted.size();
        int start = pageNo * pageSize;
        int end = Math.min(start + pageSize, total);
        List<ProductResponseDTO> pageContent = (start >= total) ? List.of() : sorted.subList(start, end);

        Page<ProductResponseDTO> page = new PageImpl<>(pageContent, PageRequest.of(pageNo, pageSize), total);
        return new APIPageableResponseDTO<>(page);
    }

    private Comparator<ProductResponseDTO> getComparator(String sortField) {
        return switch (sortField) {
            case SORTFIELD_PRICE -> Comparator.comparing(
                    ProductResponseDTO::getPrice,
                    Comparator.nullsLast(Long::compareTo)
            );
            case SORTFIELD_PRICE_DESC -> Comparator.comparing(
                    ProductResponseDTO::getPrice,
                    Comparator.nullsLast(Long::compareTo)
            ).reversed();
            case SORTFIELD_RATING -> Comparator.comparing(
                    ProductResponseDTO::getAverageRating,
                    Comparator.nullsLast(Double::compareTo)
            ).reversed();
            default -> Comparator.comparing(
                    ProductResponseDTO::getCreateOn,
                    Comparator.nullsLast(Comparable::compareTo)
            ).reversed();
        };
    }

    @Override
    public APIPageableResponseDTO<ProductResponseDTO> getALlFeaturedProduct(int pageNo, int pageSize, String search, String sort) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(sort).descending());

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

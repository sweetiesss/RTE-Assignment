package com.example.apisample.service;


import com.example.apisample.config.BucketConfiguration;
import com.example.apisample.product.entity.Product;
import com.example.apisample.product.exception.ProductDeletedException;
import com.example.apisample.product.model.dto.ProductCreateDTO;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.model.dto.ProductUpdateDTO;
import com.example.apisample.product.repository.ProductRepository;
import com.example.apisample.product.service.S3Service;
import com.example.apisample.product.service.impl.ProductServiceImpl;
import com.example.apisample.rating.service.RatingService;
import com.example.apisample.user.exception.NotImageFileException;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RatingService ratingService;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl(productRepository, ratingService);
        ReflectionTestUtils.setField(productService, "S3URL", "http://example.com/");
        pageable = PageRequest.of(0, 10, Sort.by("name").descending());
    }

    @Test
    void testGetAllProduct_returnsPaginatedSortedResponse() {
        Product product = Product.builder().id(1).name("Test").price(100L).deleted(false).build();
        when(productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(anyString())).thenReturn(List.of(product));
        when(ratingService.calculateAverageRating(1)).thenReturn(4.5);

        APIPageableResponseDTO<ProductResponseDTO> response = productService.getAllProduct(0, 10, "", "createOn");

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().getFirst().getAverageRating()).isEqualTo(4.5);
    }

    @Test
    public void testGetAllFeaturedProduct_returnsPageResponse() {
        // Create mock products to return in the page
        List<Product> mockProductList = List.of(
                new Product(1, "Product 1", "Description 1", 100L, "image1.jpg", true, false, Instant.now(), Instant.now(),1L),
                new Product(2, "Product 2", "Description 2", 200L, "image2.jpg", true, false, Instant.now(), Instant.now(),1L)
        );

        // Mock average rating calculation
        when(ratingService.calculateAverageRating(1)).thenReturn(4.5);
        when(ratingService.calculateAverageRating(2)).thenReturn(4.7);

        // Create a mock Page object using PageImpl
        Page<Product> mockPage = new PageImpl<>(mockProductList, pageable, mockProductList.size());

        // Mock the repository method to return the mock Page
        when(productRepository.findByDeletedFalseAndFeaturedTrue(pageable)).thenReturn(mockPage);

        // Call the service method
        APIPageableResponseDTO<ProductResponseDTO> response = productService.getALlFeaturedProduct(0, 10, null, "name");

        // Assertions
        assertNotNull(response);
        assertEquals(mockProductList.size(), response.getContent().size());
        assertEquals(mockProductList.get(0).getName(), response.getContent().get(0).getName());
        assertEquals(mockProductList.get(1).getName(), response.getContent().get(1).getName());
        assertEquals(4.5, response.getContent().get(0).getAverageRating());
        assertEquals(4.7, response.getContent().get(1).getAverageRating());
    }

    @Test
    void testGetProductById_validProduct_returnsDTO() {
        Product product = Product.builder().id(1).deleted(false).build();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));
        when(ratingService.calculateAverageRating(1)).thenReturn(4.0);

        var result = productService.getProductById(1);

        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getAverageRating()).isEqualTo(4.0);
    }

    @Test
    void testGetProductById_deletedProduct_throwsException() {
        Product product = Product.builder().id(1).deleted(true).build();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.getProductById(1)).isInstanceOf(ProductDeletedException.class);
    }

    @Test
    void testCreateProduct_savesNewProduct() {
        ProductCreateDTO dto = new ProductCreateDTO();
        dto.setName("New");
        dto.setDescription("desc");
        dto.setPrice(100L);
        dto.setFeatured(true);

        productService.createProduct(dto);

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testUpdateProduct_validUpdate_savesUpdatedProduct() {
        ProductUpdateDTO dto = ProductUpdateDTO.builder().name("Updated").price(200L).build();
        Product product = Product.builder().id(1).deleted(false).build();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.updateProduct(1, dto);

        assertThat(product.getName()).isEqualTo("Updated");
        assertThat(product.getPrice()).isEqualTo(200L);
        verify(productRepository).save(product);
    }

    @Test
    void testUpdateProduct_deletedProduct_throwsException() {
        ProductUpdateDTO dto = new ProductUpdateDTO();
        Product product = Product.builder().id(1).deleted(true).build();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        assertThatThrownBy(() -> productService.updateProduct(1, dto)).isInstanceOf(ProductDeletedException.class);
    }

    @Test
    void testDeleteProduct_setsDeletedTrue() {
        Product product = Product.builder().id(1).deleted(false).build();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.deleteProduct(1);

        assertThat(product.getDeleted()).isTrue();
        verify(productRepository).save(product);
    }

    @Test
    void testRestoreProduct_setsDeletedFalse() {
        Product product = Product.builder().id(1).deleted(true).build();
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        productService.restoreProduct(1);

        assertThat(product.getDeleted()).isFalse();
        verify(productRepository).save(product);
    }

    @Test
    void testUploadProductImage_validImage_returnsURL() throws IOException {
        // Create a small in-memory image and write it to a byte array
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        byte[] imageBytes = baos.toByteArray();

        // Create mock MultipartFile with valid PNG data
        MockMultipartFile file = new MockMultipartFile("file", "test.png", "image/png", imageBytes);

        // Create a mock Product object
        Product product = Product.builder().id(1).build();

        // Mock the dependencies
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // Mock the BucketConfiguration behavior
        BucketConfiguration mockS3Bucket = mock(BucketConfiguration.class);
        when(mockS3Bucket.getCustomer()).thenReturn("bucket");

        // Mock the S3Service behavior
        S3Service mockS3Service = mock(S3Service.class);
        doNothing().when(mockS3Service).putObject(anyString(), anyString(), any(byte[].class));

        // Inject the mocked S3Service and BucketConfiguration
        ReflectionTestUtils.setField(productService, "s3Service", mockS3Service);
        ReflectionTestUtils.setField(productService, "s3Bucket", mockS3Bucket);
        ReflectionTestUtils.setField(productService, "S3URL", "http://example.com/");

        // Call the method
        String url = productService.uploadProductImage(1, file);

        // Assertions
        assertThat(url).startsWith("http://example.com/");
        verify(productRepository).save(product);
        verify(mockS3Service).putObject(anyString(), anyString(), any(byte[].class));  // Verify that S3 service was called
    }


    @Test
    void testUploadProductImage_invalidImage_throwsException() {
        MockMultipartFile file = new MockMultipartFile("file", "bad.txt", "text/plain", "not image".getBytes());

        assertThatThrownBy(() -> productService.uploadProductImage(1, file))
                .isInstanceOf(NotImageFileException.class);
    }
}


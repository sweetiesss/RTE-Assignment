package com.example.apisample.service;

import com.example.apisample.category.entity.Category;
import com.example.apisample.category.exception.CategoryNotFoundException;
import com.example.apisample.category.repository.CategoryRepository;
import com.example.apisample.product.entity.Product;
import com.example.apisample.product.exception.ProductNotFoundException;
import com.example.apisample.product.model.dto.ProductResponseDTO;
import com.example.apisample.product.repository.ProductRepository;
import com.example.apisample.productcategory.entity.ProductCategory;
import com.example.apisample.productcategory.entity.ProductCategoryId;
import com.example.apisample.productcategory.exception.ProductCategoryNotFoundException;
import com.example.apisample.productcategory.model.dto.ProductCategoryRequestDTO;
import com.example.apisample.productcategory.model.dto.ProductCategoryResponseDTO;
import com.example.apisample.productcategory.repository.ProductCategoryRepository;
import com.example.apisample.productcategory.service.impl.ProductCategoryServiceImpl;
import com.example.apisample.rating.service.RatingService;
import com.example.apisample.utils.pagination.APIPageableResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class ProductCategoryServiceTest {

    @Mock
    private ProductCategoryRepository productCategoryRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private RatingService ratingService;

    @InjectMocks
    private ProductCategoryServiceImpl productCategoryService;

    private Product product;
    private Category category;
    private ProductCategoryRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        product = Product.builder().id(1).name("Product 1").build();
        category = Category.builder().id(1).name("Category 1").build();
        requestDTO = ProductCategoryRequestDTO.builder()
                .productId(1)
                .categoryId(Collections.singletonList(1))
                .build();
    }

    @Test
    void addCategoryToProduct_ShouldAddCategoryToProduct() {
        // Arrange
        when(productRepository.findById(requestDTO.getProductId())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(requestDTO.getCategoryId().get(0))).thenReturn(Optional.of(category));

        // Act
        productCategoryService.addCategoryToProduct(requestDTO);

        // Assert
        verify(productCategoryRepository, times(1)).deleteByProduct_Id(product.getId());
        verify(productCategoryRepository, times(1)).saveAll(anyList());
    }

    @Test
    void addCategoryToProduct_ShouldThrowProductNotFoundException_WhenProductDoesNotExist() {
        // Arrange
        when(productRepository.findById(requestDTO.getProductId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> productCategoryService.addCategoryToProduct(requestDTO));
    }

    @Test
    void addCategoryToProduct_ShouldThrowCategoryNotFoundException_WhenCategoryDoesNotExist() {
        // Arrange
        when(productRepository.findById(requestDTO.getProductId())).thenReturn(Optional.of(product));
        when(categoryRepository.findById(requestDTO.getCategoryId().get(0))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CategoryNotFoundException.class, () -> productCategoryService.addCategoryToProduct(requestDTO));
    }

    @Test
    void removeCategoryFromProduct_ShouldRemoveCategoryFromProduct() {
        // Arrange
        ProductCategoryId productCategoryId = new ProductCategoryId(1, 1);
        ProductCategory productCategory = ProductCategory.builder()
                .id(productCategoryId)
                .product(product)
                .category(category)
                .build();

        when(productCategoryRepository.findById(productCategoryId)).thenReturn(Optional.of(productCategory));

        // Act
        productCategoryService.removeCategoryFromProduct(1, 1);

        // Assert
        verify(productCategoryRepository, times(1)).delete(productCategory);
    }

    @Test
    void removeCategoryFromProduct_ShouldThrowProductCategoryNotFoundException_WhenProductCategoryDoesNotExist() {
        // Arrange
        when(productCategoryRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ProductCategoryNotFoundException.class, () -> productCategoryService.removeCategoryFromProduct(1, 1));
    }

    @Test
    void getAllProductCategories_ShouldReturnPagedCategories() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createOn").ascending()); // Adjust the sort if needed

        // Create ProductCategory entities with mock data
        Product product = new Product(); // Mock Product
        product.setId(1);

        Category category = new Category(); // Mock Category
        category.setId(1);
        category.setName("Category 1");

        ProductCategory productCategory = ProductCategory.builder()
                .product(product)
                .category(category)
                .build();

        List<ProductCategory> productCategories = new ArrayList<>();
        productCategories.add(productCategory);

        // Create a PageImpl with the list of ProductCategory objects
        Page<ProductCategory> productCategoryPage = new PageImpl<>(productCategories, pageable, productCategories.size());

        // Mock the repository method to return the page
        when(productCategoryRepository.findAll(pageable)).thenReturn(productCategoryPage);

        // Act
        APIPageableResponseDTO<ProductCategoryResponseDTO> response = productCategoryService.getAllProductCategories(0, 10, "createOn", "");

        // Assert
        assertNotNull(response, "Response should not be null");
        assertNotNull(response.getContent(), "Content should not be null");
        assertEquals(1, response.getContent().size(), "Should return 1 category");

        // Verify that the content of the response matches expected values
        ProductCategoryResponseDTO responseDTO = response.getContent().get(0);
        assertEquals(1, responseDTO.getProductId(), "Product ID should be 1");
        assertEquals("Category 1", responseDTO.getCategories().get(0).getName(), "Category name should be 'Category 1'");
    }


    @Test
    void getProductsByCategory_ShouldReturnPagedProductsByCategory() {
        int pageNo = 0;
        int pageSize = 10;
        String sortField = "price";
        String search = "";

        Product product1 = Product.builder().id(1).name("Product 1").price(100L).createOn(Instant.now()).build();
        Product product2 = Product.builder().id(2).name("Product 2").price(200L).createOn(Instant.now()).build();

        ProductCategory productCategory1 = ProductCategory.builder().product(product1).category(category).build();
        ProductCategory productCategory2 = ProductCategory.builder().product(product2).category(category).build();

        List<ProductCategory> productCategories = Arrays.asList(productCategory1, productCategory2);

        when(productCategoryRepository.findAllByCategory_Id(1)).thenReturn(productCategories);

        when(ratingService.calculateAverageRating(anyInt())).thenReturn(4.5);

        APIPageableResponseDTO<ProductResponseDTO> response = productCategoryService.getProductsByCategory(1, pageNo, pageSize, sortField, search);

        assertNotNull(response);
        assertEquals(2, response.getContent().size());
        assertEquals(1, response.getContent().get(0).getId());
        assertEquals(100L, response.getContent().get(0).getPrice());
        assertEquals(2, response.getContent().get(1).getId());
        assertEquals(200L, response.getContent().get(1).getPrice());
    }

    @Test
    void getProductCategoriesByProductId_ShouldReturnCategories() {
        // Arrange
        Product product = new Product();
        product.setId(1); // Set a valid Product ID

        Category category = new Category();
        category.setId(1); // Set a valid Category ID

        ProductCategory productCategory1 = ProductCategory.builder()
                .product(product)  // Set the mocked product
                .category(category)  // Set the mocked category
                .build();

        ProductCategory productCategory2 = ProductCategory.builder()
                .product(product)
                .category(category)
                .build();

        List<ProductCategory> productCategories = Arrays.asList(productCategory1, productCategory2);

        when(productCategoryRepository.findAllByProduct_Id(1)).thenReturn(productCategories);

        ProductCategoryResponseDTO response = productCategoryService.getProductCategoriesByProductId(1);

        assertNotNull(response);
        assertEquals(2, response.getCategories().size(), "Should return 2 categories");  // Assert the number of categories is correct
    }


    @Test
    void getProductCategoriesByProductId_ShouldThrowProductCategoryNotFoundException_WhenNoCategoriesExist() {
        when(productCategoryRepository.findAllByProduct_Id(1)).thenReturn(Collections.emptyList());

        assertThrows(ProductCategoryNotFoundException.class, () -> productCategoryService.getProductCategoriesByProductId(1));
    }
}


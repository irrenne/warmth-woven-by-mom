package com.warmth.woven.by.mom.productservice.service;

import com.warmth.woven.by.mom.productservice.dto.ProductRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductResponse;
import com.warmth.woven.by.mom.productservice.model.Product;
import com.warmth.woven.by.mom.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setAmount(100);

        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setAmount(100);
    }

    @Test
    void testCreateProduct() {
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        ProductResponse result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
    }

    @Test
    void testGetProductInStock() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        Boolean result = productService.getProductInStock(1L, 50);

        assertTrue(result);
    }

    @Test
    void testUpdateProduct() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse result = productService.updateProduct(1L, productRequest);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testRestockProductAmount() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse result = productService.restockProductAmount(1L, 50);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testDecreaseProductAmount() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductResponse result = productService.decreaseProductAmount(1L, 50);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Collections.singletonList(product));

        List<ProductResponse> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).getName());
    }

    @Test
    void testGetRandomProducts() {
        when(productRepository.findAllByAmountGreaterThan(0)).thenReturn(Collections.singletonList(product));

        List<ProductResponse> result = productService.getRandomProducts(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).getName());
    }

    @Test
    void testFindAllProducts() {
        Page<Product> productPage = new PageImpl<>(Collections.singletonList(product));
        Pageable pageable = PageRequest.of(0, 1, Sort.by("name"));

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        Page<ProductResponse> result = productService.findAllProducts(null, null, 0, 1, "name", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(product.getName(), result.getContent().get(0).getName());
    }
}

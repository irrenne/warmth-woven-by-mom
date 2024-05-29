package com.warmth.woven.by.mom.productservice.service;

import com.warmth.woven.by.mom.productservice.ProductServiceApplication;
import com.warmth.woven.by.mom.productservice.dto.ProductRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductResponse;
import com.warmth.woven.by.mom.productservice.model.Product;
import com.warmth.woven.by.mom.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ProductServiceApplication.class)
@ActiveProfiles("test")
@Transactional
public class ProductServiceIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    private Product product;
    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal(400));
        product.setImageUrl("example.com/image.jpg");
        product.setAmount(100);

        productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(new BigDecimal(400));
        productRequest.setImageUrl("example.com/image.jpg");
        productRequest.setAmount(100);

        productRepository.deleteAll();
    }

    @Test
    void testCreateProduct() {
        ProductResponse result = productService.createProduct(productRequest);

        assertNotNull(result);
        assertEquals(productRequest.getName(), result.getName());
        assertEquals(productRequest.getDescription(), result.getDescription());
        assertEquals(productRequest.getPrice(), result.getPrice());
        assertEquals(productRequest.getImageUrl(), result.getImageUrl());
        assertEquals(productRequest.getAmount(), result.getAmount());

        Optional<Product> savedProduct = productRepository.findById(result.getId());
        assertTrue(savedProduct.isPresent());
        assertEquals(productRequest.getName(), savedProduct.get().getName());
        assertEquals(productRequest.getDescription(), savedProduct.get().getDescription());
        assertEquals(productRequest.getPrice(), savedProduct.get().getPrice());
        assertEquals(productRequest.getImageUrl(), savedProduct.get().getImageUrl());
        assertEquals(productRequest.getAmount(), savedProduct.get().getAmount());
    }

    @Test
    void testGetProductById() {
        Product savedProduct = productRepository.save(product);

        ProductResponse result = productService.getProductById(savedProduct.getId());

        assertNotNull(result);
        assertEquals(savedProduct.getName(), result.getName());
        assertEquals(savedProduct.getDescription(), result.getDescription());
        assertEquals(savedProduct.getPrice(), result.getPrice());
        assertEquals(savedProduct.getImageUrl(), result.getImageUrl());
        assertEquals(savedProduct.getAmount(), result.getAmount());
    }

    @Test
    void testGetProductInStock() {
        Product savedProduct = productRepository.save(product);

        Boolean result = productService.getProductInStock(savedProduct.getId(), 50);

        assertTrue(result);
    }

    @Test
    void testUpdateProduct() {
        Product savedProduct = productRepository.save(product);
        productRequest.setName("Updated Product");
        productRequest.setDescription("Updated Description");
        productRequest.setPrice(new BigDecimal(400));
        productRequest.setImageUrl("example.com/updated_image.jpg");

        ProductResponse result = productService.updateProduct(savedProduct.getId(), productRequest);

        assertNotNull(result);
        assertEquals(productRequest.getName(), result.getName());
        assertEquals(productRequest.getDescription(), result.getDescription());
        assertEquals(productRequest.getPrice(), result.getPrice());
        assertEquals(productRequest.getImageUrl(), result.getImageUrl());

        Optional<Product> updatedProduct = productRepository.findById(result.getId());
        assertTrue(updatedProduct.isPresent());
        assertEquals(productRequest.getName(), updatedProduct.get().getName());
        assertEquals(productRequest.getDescription(), updatedProduct.get().getDescription());
        assertEquals(productRequest.getPrice(), updatedProduct.get().getPrice());
        assertEquals(productRequest.getImageUrl(), updatedProduct.get().getImageUrl());
    }

    @Test
    void testRestockProductAmount() {
        Product savedProduct = productRepository.save(product);

        ProductResponse result = productService.restockProductAmount(savedProduct.getId(), 50);

        assertNotNull(result);
        assertEquals(savedProduct.getAmount(), result.getAmount());

        Optional<Product> updatedProduct = productRepository.findById(result.getId());
        assertTrue(updatedProduct.isPresent());
        assertEquals(savedProduct.getAmount(), updatedProduct.get().getAmount());
    }

    @Test
    void testDecreaseProductAmount() {
        Product savedProduct = productRepository.save(product);

        ProductResponse result = productService.decreaseProductAmount(savedProduct.getId(), 50);

        assertNotNull(result);
        assertEquals(savedProduct.getAmount(), result.getAmount());

        Optional<Product> updatedProduct = productRepository.findById(result.getId());
        assertTrue(updatedProduct.isPresent());
        assertEquals(savedProduct.getAmount(), updatedProduct.get().getAmount());
    }

    @Test
    void testGetAllProducts() {
        productRepository.save(product);

        List<ProductResponse> result = productService.getAllProducts();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).getName());
        assertEquals(product.getDescription(), result.get(0).getDescription());
        assertEquals(product.getPrice(), result.get(0).getPrice());
        assertEquals(product.getImageUrl(), result.get(0).getImageUrl());
        assertEquals(product.getAmount(), result.get(0).getAmount());
    }

    @Test
    void testGetRandomProducts() {
        productRepository.save(product);

        List<ProductResponse> result = productService.getRandomProducts(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(product.getName(), result.get(0).getName());
        assertEquals(product.getDescription(), result.get(0).getDescription());
        assertEquals(product.getPrice(), result.get(0).getPrice());
        assertEquals(product.getImageUrl(), result.get(0).getImageUrl());
        assertEquals(product.getAmount(), result.get(0).getAmount());
    }

    @Test
    void testFindAllProducts() {
        productRepository.save(product);

        Page<ProductResponse> result = productService.findAllProducts(null, null, 0, 1, "name", "asc");

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(product.getName(), result.getContent().get(0).getName());
        assertEquals(product.getDescription(), result.getContent().get(0).getDescription());
        assertEquals(product.getPrice(), result.getContent().get(0).getPrice());
        assertEquals(product.getImageUrl(), result.getContent().get(0).getImageUrl());
        assertEquals(product.getAmount(), result.getContent().get(0).getAmount());
    }
}

package com.warmth.woven.by.mom.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmth.woven.by.mom.productservice.dto.ProductRequest;
import com.warmth.woven.by.mom.productservice.model.Product;
import com.warmth.woven.by.mom.productservice.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        productRepository.deleteAll();
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(BigDecimal.valueOf(100));
        product.setImageUrl("example.com/image.jpg");
        product.setAmount(100);
        product = productRepository.save(product);
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("New Product");
        productRequest.setDescription("New Description");
        productRequest.setPrice(BigDecimal.valueOf(200));
        productRequest.setImageUrl("example.com/newimage.jpg");
        productRequest.setAmount(50);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.description").value("New Description"))
                .andExpect(jsonPath("$.price").value(200))
                .andExpect(jsonPath("$.imageUrl").value("example.com/newimage.jpg"))
                .andExpect(jsonPath("$.amount").value(50));
    }

    @Test
    void testGetProductById() throws Exception {
        mockMvc.perform(get("/api/product/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice().intValue()))
                .andExpect(jsonPath("$.imageUrl").value(product.getImageUrl()))
                .andExpect(jsonPath("$.amount").value(product.getAmount()));
    }

    @Test
    void testGetProductPriceById() throws Exception {
        mockMvc.perform(get("/api/product/price/{id}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(product.getPrice().intValue()));
    }

    @Test
    void testCheckStock() throws Exception {
        mockMvc.perform(get("/api/product/{productId}/isInStock", product.getId())
                        .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product");
        productRequest.setDescription("Updated Description");
        productRequest.setPrice(BigDecimal.valueOf(150));
        productRequest.setImageUrl("example.com/updatedimage.jpg");
        productRequest.setAmount(80);

        mockMvc.perform(put("/api/product/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(productRequest.getName()))
                .andExpect(jsonPath("$.description").value(productRequest.getDescription()))
                .andExpect(jsonPath("$.price").value(productRequest.getPrice()))
                .andExpect(jsonPath("$.imageUrl").value(productRequest.getImageUrl()))
                .andExpect(jsonPath("$.amount").value(productRequest.getAmount()));
    }

    @Test
    void testRestockProductAmount() throws Exception {
        mockMvc.perform(put("/api/product/restock/{id}", product.getId())
                        .param("quantity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(120));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getAmount()).isEqualTo(120);
    }

    @Test
    void testDecreaseProductAmount() throws Exception {
        mockMvc.perform(put("/api/product/update/{id}", product.getId())
                        .param("quantity", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(80));

        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getAmount()).isEqualTo(80);
    }

    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()));
    }

    @Test
    void testGetRandomProducts() throws Exception {
        mockMvc.perform(get("/api/product/random")
                        .param("amount", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(product.getId()))
                .andExpect(jsonPath("$[0].name").value(product.getName()));
    }

    @Test
    void testGetProductsPaged() throws Exception {
        mockMvc.perform(get("/api/product/paged")
                        .param("page", "0")
                        .param("size", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(1))
                .andExpect(jsonPath("$.content[0].id").value(product.getId()))
                .andExpect(jsonPath("$.content[0].name").value(product.getName()));
    }
}

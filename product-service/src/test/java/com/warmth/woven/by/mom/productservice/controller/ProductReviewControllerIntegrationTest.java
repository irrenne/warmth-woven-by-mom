package com.warmth.woven.by.mom.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmth.woven.by.mom.productservice.dto.ProductReviewRequest;
import com.warmth.woven.by.mom.productservice.model.Product;
import com.warmth.woven.by.mom.productservice.model.ProductReview;
import com.warmth.woven.by.mom.productservice.repository.ProductRepository;
import com.warmth.woven.by.mom.productservice.repository.ProductReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        productReviewRepository.deleteAll();
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
    void testCreateProductReview() throws Exception {
        ProductReviewRequest reviewRequest = new ProductReviewRequest();
        reviewRequest.setUserId("user123");
        reviewRequest.setComment("Great product!");
        reviewRequest.setProductId(product.getId());

        mockMvc.perform(post("/api/product/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(reviewRequest.getUserId()))
                .andExpect(jsonPath("$.comment").value(reviewRequest.getComment()));
    }

    @Test
    void testGetProductReviewsByProductId() throws Exception {
        ProductReview review1 = new ProductReview();
        review1.setUserId("user1");
        review1.setComment("Excellent!");
        review1.setProduct(product);
        productReviewRepository.save(review1);

        ProductReview review2 = new ProductReview();
        review2.setUserId("user2");
        review2.setComment("Not bad");
        review2.setProduct(product);
        productReviewRepository.save(review2);

        mockMvc.perform(get("/api/product/review/product/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].userId").value(review1.getUserId()))
                .andExpect(jsonPath("$[0].comment").value(review1.getComment()))
                .andExpect(jsonPath("$[1].userId").value(review2.getUserId()))
                .andExpect(jsonPath("$[1].comment").value(review2.getComment()));
    }
}

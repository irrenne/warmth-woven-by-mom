package com.warmth.woven.by.mom.productservice.service;

import com.warmth.woven.by.mom.productservice.dto.ProductReviewRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductReviewResponse;
import com.warmth.woven.by.mom.productservice.model.Product;
import com.warmth.woven.by.mom.productservice.model.ProductReview;
import com.warmth.woven.by.mom.productservice.repository.ProductRepository;
import com.warmth.woven.by.mom.productservice.repository.ProductReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ProductReviewServiceIntegrationTest {

    @Autowired
    private ProductReviewService productReviewService;

    @Autowired
    private ProductReviewRepository productReviewRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setName("Test Product");
        product.setDescription("Test Description");
        product.setPrice(new BigDecimal(400));
        product.setImageUrl("example.com/image.jpg");
        product.setAmount(100);
        product = productRepository.save(product);
    }

    @Test
    void testCreateProductReview() {
        ProductReviewRequest productReviewRequest = new ProductReviewRequest();
        productReviewRequest.setUserId("user123");
        productReviewRequest.setComment("Great product!");
        productReviewRequest.setProductId(product.getId());

        ProductReviewResponse result = productReviewService.createProductReview(productReviewRequest);

        assertNotNull(result);
        assertEquals("user123", result.getUserId());
        assertEquals("Great product!", result.getComment());
    }

    @Test
    void testGetProductReviewsByProductId() {
        ProductReview productReview = new ProductReview();
        productReview.setUserId("user123");
        productReview.setComment("Great product!");
        productReview.setProduct(product);
        productReviewRepository.save(productReview);

        List<ProductReviewResponse> result = productReviewService.getProductReviewsByProductId(product.getId());

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("user123", result.get(0).getUserId());
        assertEquals("Great product!", result.get(0).getComment());
    }
}

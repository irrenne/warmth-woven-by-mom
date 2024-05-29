package com.warmth.woven.by.mom.productservice.service;

import com.warmth.woven.by.mom.productservice.dto.ProductReviewRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductReviewResponse;
import com.warmth.woven.by.mom.productservice.model.Product;
import com.warmth.woven.by.mom.productservice.model.ProductReview;
import com.warmth.woven.by.mom.productservice.repository.ProductReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductReviewServiceTest {

    @Mock
    private ProductReviewRepository productReviewRepository;

    @InjectMocks
    private ProductReviewService productReviewService;

    private ProductReview productReview;
    private ProductReviewRequest productReviewRequest;
    private ProductReviewResponse productReviewResponse;

    @BeforeEach
    void setUp() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        productReview = new ProductReview();
        productReview.setId(1L);
        productReview.setUserId("user123");
        productReview.setComment("Great product!");
        productReview.setProduct(product);

        productReviewRequest = new ProductReviewRequest();
        productReviewRequest.setUserId("user123");
        productReviewRequest.setComment("Great product!");
        productReviewRequest.setProductId(1L);

        productReviewResponse = new ProductReviewResponse();
        productReviewResponse.setId(1L);
        productReviewResponse.setUserId("user123");
        productReviewResponse.setComment("Great product!");
    }

    @Test
    void testCreateProductReview() {
        when(productReviewRepository.save(any(ProductReview.class))).thenReturn(productReview);

        ProductReviewResponse result = productReviewService.createProductReview(productReviewRequest);

        assertNotNull(result);
        assertEquals(productReviewResponse.getId(), result.getId());
        assertEquals(productReviewResponse.getUserId(), result.getUserId());
        assertEquals(productReviewResponse.getComment(), result.getComment());

        verify(productReviewRepository, times(1)).save(any(ProductReview.class));
    }

    @Test
    void testGetProductReviewsByProductId() {
        List<ProductReview> productReviews = new ArrayList<>();
        productReviews.add(productReview);

        when(productReviewRepository.findAllByProductId(1L)).thenReturn(productReviews);

        List<ProductReviewResponse> result = productReviewService.getProductReviewsByProductId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(productReviewResponse.getId(), result.get(0).getId());
        assertEquals(productReviewResponse.getUserId(), result.get(0).getUserId());
        assertEquals(productReviewResponse.getComment(), result.get(0).getComment());

        verify(productReviewRepository, times(1)).findAllByProductId(1L);
    }
}

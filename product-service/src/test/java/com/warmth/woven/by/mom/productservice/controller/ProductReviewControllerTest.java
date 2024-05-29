package com.warmth.woven.by.mom.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmth.woven.by.mom.productservice.dto.ProductReviewRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductReviewResponse;
import com.warmth.woven.by.mom.productservice.service.ProductReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductReviewController.class)
class ProductReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductReviewService productReviewService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductReviewResponse productReviewResponse;

    @BeforeEach
    void setUp() {
        productReviewResponse = new ProductReviewResponse();
        productReviewResponse.setId(1L);
        productReviewResponse.setUserId("user123");
        productReviewResponse.setComment("Great product!");
    }

    @Test
    void testCreateProductReview() throws Exception {
        ProductReviewRequest productReviewRequest = new ProductReviewRequest();
        productReviewRequest.setUserId("user123");
        productReviewRequest.setComment("Great product!");
        productReviewRequest.setProductId(1L);

        Mockito.when(productReviewService.createProductReview(any(ProductReviewRequest.class)))
                .thenReturn(productReviewResponse);

        mockMvc.perform(post("/api/product/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productReviewRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productReviewResponse.getId()))
                .andExpect(jsonPath("$.userId").value(productReviewResponse.getUserId()))
                .andExpect(jsonPath("$.comment").value(productReviewResponse.getComment()));
    }

    @Test
    void testGetProductReviewsByProductId() throws Exception {
        List<ProductReviewResponse> reviews = Collections.singletonList(productReviewResponse);
        Mockito.when(productReviewService.getProductReviewsByProductId(anyLong())).thenReturn(reviews);

        mockMvc.perform(get("/api/product/review/product/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(productReviewResponse.getId()))
                .andExpect(jsonPath("$[0].userId").value(productReviewResponse.getUserId()))
                .andExpect(jsonPath("$[0].comment").value(productReviewResponse.getComment()));
    }
}

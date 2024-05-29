package com.warmth.woven.by.mom.productservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmth.woven.by.mom.productservice.dto.ProductRequest;
import com.warmth.woven.by.mom.productservice.dto.ProductResponse;
import com.warmth.woven.by.mom.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductResponse productResponse;

    @BeforeEach
    void setUp() {
        productResponse = new ProductResponse();
        productResponse.setId(1L);
        productResponse.setName("Test Product");
        productResponse.setDescription("Test Description");
        productResponse.setPrice(new BigDecimal(400));
        productResponse.setImageUrl("example.com/image.jpg");
        productResponse.setAmount(100);
    }

    @Test
    void testCreateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Test Product");
        productRequest.setDescription("Test Description");
        productRequest.setPrice(new BigDecimal(400));
        productRequest.setImageUrl("example.com/image.jpg");
        productRequest.setAmount(100);

        Mockito.when(productService.createProduct(any(ProductRequest.class))).thenReturn(productResponse);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.name").value(productResponse.getName()));
    }

    @Test
    void testGetProductById() throws Exception {
        Mockito.when(productService.getProductById(anyLong())).thenReturn(productResponse);

        mockMvc.perform(get("/api/product/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.name").value(productResponse.getName()));
    }

    @Test
    void testGetProductPriceById() throws Exception {
        Mockito.when(productService.getProductById(anyLong())).thenReturn(productResponse);

        mockMvc.perform(get("/api/product/price/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string(productResponse.getPrice().toString()));
    }

    @Test
    void testCheckStock() throws Exception {
        Mockito.when(productService.getProductInStock(anyLong(), anyInt())).thenReturn(true);

        mockMvc.perform(get("/api/product/{productId}/isInStock", 1L)
                        .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void testUpdateProduct() throws Exception {
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Updated Product");
        productRequest.setDescription("Updated Description");
        productRequest.setPrice(new BigDecimal(400));
        productRequest.setImageUrl("example.com/image.jpg");
        productRequest.setAmount(200);

        ProductResponse updatedProductResponse = new ProductResponse();
        updatedProductResponse.setId(1L);
        updatedProductResponse.setName("Updated Product");
        updatedProductResponse.setDescription("Updated Description");
        updatedProductResponse.setPrice(new BigDecimal(400));
        updatedProductResponse.setImageUrl("example.com/image.jpg");
        updatedProductResponse.setAmount(200);

        Mockito.when(productService.updateProduct(anyLong(), any(ProductRequest.class))).thenReturn(updatedProductResponse);

        mockMvc.perform(put("/api/product/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(updatedProductResponse.getId()))
                .andExpect(jsonPath("$.name").value(updatedProductResponse.getName()));
    }

    @Test
    void testRestockProductAmount() throws Exception {
        Mockito.when(productService.restockProductAmount(anyLong(), anyInt())).thenReturn(productResponse);

        mockMvc.perform(put("/api/product/restock/{id}", 1L)
                        .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.amount").value(productResponse.getAmount()));
    }

    @Test
    void testDecreaseProductAmount() throws Exception {
        Mockito.when(productService.decreaseProductAmount(anyLong(), anyInt())).thenReturn(productResponse);

        mockMvc.perform(put("/api/product/update/{id}", 1L)
                        .param("quantity", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productResponse.getId()))
                .andExpect(jsonPath("$.amount").value(productResponse.getAmount()));
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<ProductResponse> productList = Collections.singletonList(productResponse);
        Mockito.when(productService.getAllProducts()).thenReturn(productList);

        mockMvc.perform(get("/api/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(productResponse.getId()));
    }

    @Test
    void testGetRandomProducts() throws Exception {
        List<ProductResponse> productList = Collections.singletonList(productResponse);
        Mockito.when(productService.getRandomProducts(anyInt())).thenReturn(productList);

        mockMvc.perform(get("/api/product/random")
                        .param("amount", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(productResponse.getId()));
    }

    @Test
    void testGetProductsPaged() throws Exception {
        Pageable pageable = PageRequest.of(0, 9, Sort.by(Sort.Direction.ASC, "price"));
        Page<ProductResponse> productPage = new PageImpl<>(List.of(productResponse), pageable, 1);
        Mockito.when(productService.findAllProducts(any(), any(), anyInt(), anyInt(), any(), any())).thenReturn(productPage);

        mockMvc.perform(get("/api/product/paged")
                        .param("page", "0")
                        .param("size", "9")
                        .param("sortBy", "price")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk());
    }
}

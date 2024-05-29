package com.warmth.woven.by.mom.orderservice.controller;

import com.warmth.woven.by.mom.orderservice.dto.OrderRequest;
import com.warmth.woven.by.mom.orderservice.dto.OrderResponse;
import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import com.warmth.woven.by.mom.orderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    private OrderResponse orderResponse;

    @BeforeEach
    void setUp() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setUserId("user1");
        orderRequest.setWithShipping(true);
        orderRequest.setItems(Collections.emptyList());
        orderRequest.setStatus(OrderStatus.IN_PROGRESS);

        orderResponse = new OrderResponse();
        orderResponse.setId("order1");
        orderResponse.setUserId("user1");
        orderResponse.setWithShipping(true);
        orderResponse.setItems(Collections.emptyList());
        orderResponse.setStatus(OrderStatus.IN_PROGRESS);
        orderResponse.setPrice(BigDecimal.valueOf(40));
    }

    @Test
    void testCreateOrder() throws Exception {
        when(orderService.placeOrder(any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"user1\", \"withShipping\": true, \"items\": [], \"status\": \"IN_PROGRESS\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("order1"))
                .andExpect(jsonPath("$.userId").value("user1"))
                .andExpect(jsonPath("$.withShipping").value(true));
    }

    @Test
    void testGetOrdersByUserId() throws Exception {
        when(orderService.getOrdersByUserId(anyString())).thenReturn(List.of(orderResponse));

        mockMvc.perform(get("/api/order/user/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("order1"))
                .andExpect(jsonPath("$[0].userId").value("user1"));
    }

    @Test
    void testGetOrderById() throws Exception {
        when(orderService.getById(anyString())).thenReturn(orderResponse);

        mockMvc.perform(get("/api/order/order1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("order1"))
                .andExpect(jsonPath("$.userId").value("user1"));
    }

    @Test
    void testGetOrdersPagedByUserId() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "price"));
        Page<OrderResponse> orderPage = new PageImpl<>(List.of(orderResponse), pageable, 1);
        when(orderService.findAllOrdersByUserId(anyString(), any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(orderPage);

        mockMvc.perform(get("/api/order/user/paged/user1")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "price")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("order1"))
                .andExpect(jsonPath("$.content[0].userId").value("user1"));
    }

    @Test
    void testGetOrdersPaged() throws Exception {
        Pageable pageable = PageRequest.of(0, 5, Sort.by(Sort.Direction.ASC, "price"));
        Page<OrderResponse> orderPage = new PageImpl<>(List.of(orderResponse), pageable, 1);
        when(orderService.findAllOrders(any(), anyInt(), anyInt(), anyString(), anyString())).thenReturn(orderPage);

        mockMvc.perform(get("/api/order/paged")
                        .param("page", "0")
                        .param("size", "5")
                        .param("sortBy", "price")
                        .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value("order1"))
                .andExpect(jsonPath("$.content[0].userId").value("user1"));
    }

    @Test
    void testGetOrders() throws Exception {
        when(orderService.getOrders()).thenReturn(List.of(orderResponse));

        mockMvc.perform(get("/api/order/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("order1"))
                .andExpect(jsonPath("$[0].userId").value("user1"));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        when(orderService.updateOrder(anyString(), any(OrderRequest.class))).thenReturn(orderResponse);

        mockMvc.perform(put("/api/order/order1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"user1\", \"withShipping\": true, \"items\": [], \"status\": \"IN_PROGRESS\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("order1"))
                .andExpect(jsonPath("$.userId").value("user1"));
    }
}

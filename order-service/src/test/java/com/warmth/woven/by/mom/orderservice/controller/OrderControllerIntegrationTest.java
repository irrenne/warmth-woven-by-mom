package com.warmth.woven.by.mom.orderservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.warmth.woven.by.mom.orderservice.client.ProductClient;
import com.warmth.woven.by.mom.orderservice.dto.OrderItemDTO;
import com.warmth.woven.by.mom.orderservice.dto.OrderRequest;
import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import com.warmth.woven.by.mom.orderservice.model.Order;
import com.warmth.woven.by.mom.orderservice.model.OrderItem;
import com.warmth.woven.by.mom.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private ProductClient productClient;

    private Order order;
    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(1L);
        orderItem.setQuantity(2);

        order = new Order();
        order.setUserId("user1");
        order.setWithShipping(true);
        order.setItems(new HashSet<>(Collections.singleton(orderItem)));
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setPrice(new BigDecimal("40.00"));

        order = orderRepository.save(order);

        orderRequest = new OrderRequest();
        orderRequest.setUserId("user1");
        orderRequest.setWithShipping(true);
        orderRequest.setItems(Collections.singletonList(new OrderItemDTO(1L, 2)));
        orderRequest.setStatus(OrderStatus.IN_PROGRESS);
    }

    @Test
    void testCreateOrder() throws Exception {
        when(productClient.checkInStock(anyLong(), anyInt())).thenReturn(true);
        when(productClient.getProductPriceById(anyLong())).thenReturn(BigDecimal.TEN);

        mockMvc.perform(post("/api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId", is(orderRequest.getUserId())))
                .andExpect(jsonPath("$.status", is(orderRequest.getStatus().toString())))
                .andExpect(jsonPath("$.price", is(40)));
    }

    @Test
    void testGetOrdersByUserId() throws Exception {
        mockMvc.perform(get("/api/order/user/{userId}", "user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(order.getId())))
                .andExpect(jsonPath("$[0].userId", is(order.getUserId())));
    }

    @Test
    void testGetOrderById() throws Exception {
        mockMvc.perform(get("/api/order/{id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order.getId())))
                .andExpect(jsonPath("$.userId", is(order.getUserId())));
    }

    @Test
    void testGetOrdersPagedByUserId() throws Exception {
        mockMvc.perform(get("/api/order/user/paged/{userId}", "user1")
                .param("page", "0")
                .param("size", "5")
                .param("sortBy", "price")
                .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(order.getId())))
                .andExpect(jsonPath("$.content[0].userId", is(order.getUserId())));
    }

    @Test
    void testGetOrdersPaged() throws Exception {
        mockMvc.perform(get("/api/order/paged")
                .param("page", "0")
                .param("size", "5")
                .param("sortBy", "price")
                .param("sortDirection", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id", is(order.getId())))
                .andExpect(jsonPath("$.content[0].userId", is(order.getUserId())));
    }

    @Test
    void testGetOrders() throws Exception {
        mockMvc.perform(get("/api/order/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(order.getId())))
                .andExpect(jsonPath("$[0].userId", is(order.getUserId())));
    }

    @Test
    void testUpdateOrderStatus() throws Exception {
        orderRequest.setStatus(OrderStatus.COMPLETE);

        mockMvc.perform(put("/api/order/{id}", order.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(order.getId())))
                .andExpect(jsonPath("$.status", is(OrderStatus.COMPLETE.toString())));
    }
}

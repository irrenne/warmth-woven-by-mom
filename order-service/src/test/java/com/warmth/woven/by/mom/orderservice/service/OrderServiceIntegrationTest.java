package com.warmth.woven.by.mom.orderservice.service;

import com.warmth.woven.by.mom.orderservice.client.ProductClient;
import com.warmth.woven.by.mom.orderservice.dto.OrderItemDTO;
import com.warmth.woven.by.mom.orderservice.dto.OrderRequest;
import com.warmth.woven.by.mom.orderservice.dto.OrderResponse;
import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import com.warmth.woven.by.mom.orderservice.model.Order;
import com.warmth.woven.by.mom.orderservice.model.OrderItem;
import com.warmth.woven.by.mom.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(properties = "spring.config.name=application-test")
@Transactional
class OrderServiceIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @MockBean
    private ProductClient productClient;

    private OrderRequest orderRequest;

    @BeforeEach
    void setUp() {
        orderRequest = new OrderRequest();
        orderRequest.setUserId("user1");
        orderRequest.setWithShipping(true);
        orderRequest.setItems(Collections.singletonList(new OrderItemDTO(1L, 2)));
        orderRequest.setStatus(OrderStatus.IN_PROGRESS);
    }

    @Test
    void testPlaceOrder() {
        when(productClient.checkInStock(anyLong(), anyInt())).thenReturn(true);
        when(productClient.getProductPriceById(anyLong())).thenReturn(BigDecimal.TEN);

        OrderResponse response = orderService.placeOrder(orderRequest);

        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals(new BigDecimal(40), response.getPrice());
        assertEquals("user1", response.getUserId());
        assertEquals(OrderStatus.IN_PROGRESS, response.getStatus());
    }

    @Test
    void testGetOrdersByUserId() {
        Order savedOrder = saveOrder();

        List<OrderResponse> responses = orderService.getOrdersByUserId("user1");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(savedOrder.getId(), responses.get(0).getId());
    }

    @Test
    void testGetById() {
        Order savedOrder = saveOrder();

        OrderResponse response = orderService.getById(savedOrder.getId());

        assertNotNull(response);
        assertEquals(savedOrder.getId(), response.getId());
    }

    @Test
    void testUpdateOrder() {
        Order savedOrder = saveOrder();

        orderRequest.setStatus(OrderStatus.COMPLETE);

        OrderResponse response = orderService.updateOrder(savedOrder.getId(), orderRequest);

        assertNotNull(response);
        assertEquals(savedOrder.getId(), response.getId());
        assertEquals(OrderStatus.COMPLETE, response.getStatus());
    }

    @Test
    void testGetOrders() {
        Order savedOrder = saveOrder();

        List<OrderResponse> responses = orderService.getOrders();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(savedOrder.getId(), responses.get(0).getId());
    }

    @Test
    void testFindAllOrdersByUserId() {
        Order savedOrder = saveOrder();

        Page<OrderResponse> responses = orderService.findAllOrdersByUserId("user1", null, 0, 5, "price", "asc");

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        assertEquals(savedOrder.getId(), responses.getContent().get(0).getId());
    }

    @Test
    void testFindAllOrders() {
        Order savedOrder = saveOrder();

        Page<OrderResponse> responses = orderService.findAllOrders(null, 0, 5, "price", "asc");

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        assertEquals(savedOrder.getId(), responses.getContent().get(0).getId());
    }

    private Order saveOrder() {
        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(1L);
        orderItem.setQuantity(2);

        Order order = new Order();
        order.setUserId("user1");
        order.setWithShipping(true);
        order.setItems(new HashSet<>(Collections.singletonList(orderItem)));
        order.setStatus(OrderStatus.IN_PROGRESS);
        order.setPrice(new BigDecimal("40.00"));

        return orderRepository.save(order);
    }
}

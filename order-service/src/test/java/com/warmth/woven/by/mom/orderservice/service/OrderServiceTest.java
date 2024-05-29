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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductClient productClient;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderRequest orderRequest;
    private Order order;

    @BeforeEach
    void setUp() {
        OrderItemDTO orderItemDTO = new OrderItemDTO(1L, 2);

        List<OrderItemDTO> items = new ArrayList<>();
        items.add(orderItemDTO);

        orderRequest = new OrderRequest(items, BigDecimal.valueOf(40), true, "user1", OrderStatus.IN_PROGRESS);

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId(1L);
        orderItem.setQuantity(2);

        List<OrderItem> orderItems = new ArrayList<>();
        orderItems.add(orderItem);

        order = new Order("order1", BigDecimal.valueOf(40), true, "user1", OrderStatus.IN_PROGRESS, null, null, new HashSet<>(orderItems));
    }

    @Test
    void testPlaceOrder() {
        when(productClient.checkInStock(anyLong(), anyInt())).thenReturn(true);
        when(productClient.getProductPriceById(anyLong())).thenReturn(BigDecimal.TEN);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.placeOrder(orderRequest);

        assertNotNull(response);
        assertEquals("order1", response.getId());
        verify(productClient, times(1)).decreaseProductAmount(anyLong(), anyInt());
    }

    @Test
    void testGetOrdersByUserId() {
        when(orderRepository.findAllByUserId(anyString())).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getOrdersByUserId("user1");

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("order1", responses.get(0).getId());
    }

    @Test
    void testGetById() {
        when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));

        OrderResponse response = orderService.getById("order1");

        assertNotNull(response);
        assertEquals("order1", response.getId());
    }

    @Test
    void testUpdateOrder() {
        when(orderRepository.findById(anyString())).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        OrderResponse response = orderService.updateOrder("order1", orderRequest);

        assertNotNull(response);
        assertEquals("order1", response.getId());
        verify(productClient, never()).restockProductAmount(anyLong(), anyInt());
    }

    @Test
    void testGetOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));

        List<OrderResponse> responses = orderService.getOrders();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("order1", responses.get(0).getId());
    }

    @Test
    void testFindAllOrdersByUserId() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Order> orderPage = new PageImpl<>(List.of(order), pageable, 1);
        when(orderRepository.findAllByUserId(anyString(), eq(pageable))).thenReturn(orderPage);

        Page<OrderResponse> responses = orderService.findAllOrdersByUserId("user1", null, 0, 10, "id", "asc");

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        assertEquals("order1", responses.getContent().get(0).getId());
    }

    @Test
    void testFindAllOrders() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "id"));
        Page<Order> orderPage = new PageImpl<>(List.of(order), pageable, 1);
        when(orderRepository.findAll(eq(pageable))).thenReturn(orderPage);

        Page<OrderResponse> responses = orderService.findAllOrders(null, 0, 10, "id", "asc");

        assertNotNull(responses);
        assertEquals(1, responses.getTotalElements());
        assertEquals("order1", responses.getContent().get(0).getId());
    }
}

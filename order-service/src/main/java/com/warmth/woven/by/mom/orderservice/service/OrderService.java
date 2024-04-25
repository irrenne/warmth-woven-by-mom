package com.warmth.woven.by.mom.orderservice.service;

import com.warmth.woven.by.mom.orderservice.client.ProductClient;
import com.warmth.woven.by.mom.orderservice.dto.OrderRequest;
import com.warmth.woven.by.mom.orderservice.dto.OrderResponse;
import com.warmth.woven.by.mom.orderservice.model.Order;
import com.warmth.woven.by.mom.orderservice.repository.OrderRepository;
import com.warmth.woven.by.mom.orderservice.util.mapper.OrderMapper;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final ProductClient productClient;
  private final OrderRepository orderRepository;
  private final BigDecimal SHIPPING_COST = BigDecimal.valueOf(20L);


  public OrderResponse placeOrder(OrderRequest orderRequest) {
    Long productId = orderRequest.getProductId();
    boolean isInStock = Boolean.TRUE.equals(productClient.checkInStock(productId));
    if (!isInStock) {
      log.info("Product is out of stock {}", productId);
      throw new RuntimeException("Product is out of stock");
    }

    Order order = OrderMapper.INSTANCE.mapOrderRequestToOrder(orderRequest);
    if (order.getWithShipping()) {
      order.setPrice(order.getPrice().add(SHIPPING_COST));
    }
    Order savedOrder = orderRepository.save(order);

    productClient.decreaseProductAmount(productId);
    log.info("Order {} is saved", order.getId());

    return OrderMapper.INSTANCE.mapOrderToOrderResponse(savedOrder);
  }

  public List<OrderResponse> getOrdersByUserId(String userId) {
    List<Order> allByUserId = orderRepository.findAllByUserId(userId);
    return allByUserId.stream()
        .map(OrderMapper.INSTANCE::mapOrderToOrderResponse)
        .toList();
  }

  public OrderResponse getById(Long id) {
    Order order = orderRepository.findById(id).orElseThrow();
    return OrderMapper.INSTANCE.mapOrderToOrderResponse(order);
  }
}


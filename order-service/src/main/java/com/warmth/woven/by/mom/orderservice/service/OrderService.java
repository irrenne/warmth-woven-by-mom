package com.warmth.woven.by.mom.orderservice.service;

import com.warmth.woven.by.mom.orderservice.client.ProductClient;
import com.warmth.woven.by.mom.orderservice.dto.OrderItemDTO;
import com.warmth.woven.by.mom.orderservice.dto.OrderRequest;
import com.warmth.woven.by.mom.orderservice.dto.OrderResponse;
import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import com.warmth.woven.by.mom.orderservice.model.Order;
import com.warmth.woven.by.mom.orderservice.model.OrderItem;
import com.warmth.woven.by.mom.orderservice.repository.OrderRepository;
import com.warmth.woven.by.mom.orderservice.util.mapper.OrderMapper;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

  private final ProductClient productClient;
  private final OrderRepository orderRepository;
  private final BigDecimal SHIPPING_COST = BigDecimal.valueOf(20L);


  public OrderResponse placeOrder(OrderRequest orderRequest) {
    BigDecimal totalCost = BigDecimal.ZERO;
    Order order = new Order();
    order.setWithShipping(orderRequest.getWithShipping());
    order.setUserId(orderRequest.getUserId());
    order.setStatus(OrderStatus.IN_PROGRESS);

    for (OrderItemDTO item : orderRequest.getItems()) {
      boolean isInStock = productClient.checkInStock(item.getProductId(), item.getQuantity());
      if (!isInStock) {
        log.info("Product is out of stock {}", item.getProductId());
        throw new RuntimeException("Product " + item.getProductId() + " is out of stock");
      }

      BigDecimal price = productClient.getProductPriceById(item.getProductId());
      BigDecimal lineCost = price.multiply(BigDecimal.valueOf(item.getQuantity()));
      totalCost = totalCost.add(lineCost);

      OrderItem orderItem = new OrderItem();
      orderItem.setProductId(item.getProductId());
      orderItem.setQuantity(item.getQuantity());
      order.getItems().add(orderItem);
    }

    if (order.getWithShipping()) {
      totalCost = totalCost.add(SHIPPING_COST);
    }
    order.setPrice(totalCost);

    Order savedOrder = orderRepository.save(order);
    for (OrderItemDTO item : orderRequest.getItems()) {
      productClient.decreaseProductAmount(item.getProductId(), item.getQuantity());
    }
    log.info("Order {} is saved", savedOrder.getId());

    return OrderMapper.INSTANCE.mapOrderToOrderResponse(savedOrder);
  }


  public List<OrderResponse> getOrdersByUserId(String userId) {
    List<Order> allByUserId = orderRepository.findAllByUserId(userId);
    return allByUserId.stream()
        .map(OrderMapper.INSTANCE::mapOrderToOrderResponse)
        .toList();
  }

  public OrderResponse getById(String id) {
    Order order = orderRepository.findById(id).orElseThrow();
    return OrderMapper.INSTANCE.mapOrderToOrderResponse(order);
  }

  public OrderResponse updateOrder(String id, OrderRequest orderRequest) {
    Order order = orderRepository.findById(id).orElseThrow();
    order.setStatus(orderRequest.getStatus());
    Order updatedOrder = orderRepository.save(order);
    if (updatedOrder.getStatus().equals(OrderStatus.CANCELED)) {
      for (OrderItemDTO item : orderRequest.getItems()) {
        productClient.restockProductAmount(item.getProductId(), item.getQuantity());
      }
    }
    log.info("Order {} status is changed to {}", updatedOrder.getId(), updatedOrder.getStatus());
    return OrderMapper.INSTANCE.mapOrderToOrderResponse(updatedOrder);
  }

  public List<OrderResponse> getOrders() {
    List<Order> orders = orderRepository.findAll();
    return orders.stream()
        .map(OrderMapper.INSTANCE::mapOrderToOrderResponse)
        .toList();
  }

  public Page<OrderResponse> findAllOrdersByUserId(String userId, OrderStatus status, int page,
      int size,
      String sortBy, String sortDirection) {
    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Page<Order> ordersPage;
    if (status != null) {
      ordersPage = orderRepository.findAllByUserIdAndStatus(userId, status, pageable);
      return OrderMapper.INSTANCE.mapOrdersPagedToOrdersResponsePaged(ordersPage);
    }
    ordersPage = orderRepository.findAllByUserId(userId, pageable);
    return OrderMapper.INSTANCE.mapOrdersPagedToOrdersResponsePaged(ordersPage);
  }

  public Page<OrderResponse> findAllOrders(OrderStatus status, int page, int size, String sortBy,
      String sortDirection) {
    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
    Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

    Page<Order> ordersPage;
    if (status != null) {
      ordersPage = orderRepository.findAllByStatus(status, pageable);
      return OrderMapper.INSTANCE.mapOrdersPagedToOrdersResponsePaged(ordersPage);
    }
    ordersPage = orderRepository.findAll(pageable);
    return OrderMapper.INSTANCE.mapOrdersPagedToOrdersResponsePaged(ordersPage);
  }
}


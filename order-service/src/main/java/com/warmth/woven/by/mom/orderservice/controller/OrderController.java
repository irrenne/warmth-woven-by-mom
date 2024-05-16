package com.warmth.woven.by.mom.orderservice.controller;

import com.warmth.woven.by.mom.orderservice.dto.OrderRequest;
import com.warmth.woven.by.mom.orderservice.dto.OrderResponse;
import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import com.warmth.woven.by.mom.orderservice.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
@Slf4j
public class OrderController {

  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public OrderResponse createOrder(@RequestBody OrderRequest orderRequest) {
    return orderService.placeOrder(orderRequest);
  }

  @GetMapping("/user/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public List<OrderResponse> getOrdersByUserId(@PathVariable String userId) {
    return orderService.getOrdersByUserId(userId);
  }

  @GetMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public OrderResponse getOrderById(@PathVariable String id) {
    return orderService.getById(id);
  }

  @GetMapping("/user/paged/{userId}")
  @ResponseStatus(HttpStatus.OK)
  public Page<OrderResponse> getOrdersPagedByUserId(@PathVariable String userId,
      @RequestParam(required = false) OrderStatus status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "price") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDirection) {
    return orderService.findAllOrdersByUserId(userId, status, page, size, sortBy, sortDirection);
  }

  @GetMapping("/paged")
  @ResponseStatus(HttpStatus.OK)
  public Page<OrderResponse> getOrdersPaged(
      @RequestParam(required = false) OrderStatus status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "5") int size,
      @RequestParam(defaultValue = "price") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDirection) {
    return orderService.findAllOrders(status, page, size, sortBy, sortDirection);
  }

  @GetMapping("/all")
  @ResponseStatus(HttpStatus.OK)
  public List<OrderResponse> getOrders(){
    return orderService.getOrders();
  }

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  public OrderResponse updateOrderStatus(@PathVariable String id,
      @RequestBody OrderRequest orderRequest) {
    return orderService.updateOrder(id, orderRequest);
  }
}

package com.warmth.woven.by.mom.orderservice.repository;

import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import com.warmth.woven.by.mom.orderservice.model.Order;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

  Page<Order> findAllByUserId(String userId, Pageable pageable);

  Page<Order> findAllByUserIdAndStatus(String userId, OrderStatus orderStatus,
      Pageable pageable);

  Page<Order> findAll(Pageable pageable);

  Page<Order> findAllByStatus(OrderStatus orderStatus, Pageable pageable);

  List<Order> findAllByUserId(String userId);
}

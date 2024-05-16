package com.warmth.woven.by.mom.orderservice.model;

import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import com.warmth.woven.by.mom.orderservice.util.OrderIdGeneratorUtil;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Order {

  @Id
  private String id;
  private BigDecimal price;
  private Boolean withShipping;
  private String userId;
  @Enumerated(EnumType.STRING)
  private OrderStatus status;
  @Column(updatable = false)
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  @ElementCollection
  @CollectionTable(name = "order_items", joinColumns = @JoinColumn(name = "order_id"))
  @AttributeOverrides({
      @AttributeOverride(name = "productId", column = @Column(name = "product_id")),
      @AttributeOverride(name = "quantity", column = @Column(name = "quantity"))
  })
  private Set<OrderItem> items = new HashSet<>();

  @PrePersist
  protected void onCreate() {
    LocalDateTime now = LocalDateTime.now();
    this.createdAt = now;
    this.updatedAt = now;
    this.id = OrderIdGeneratorUtil.generateOrderId(now);
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = LocalDateTime.now();
  }
}

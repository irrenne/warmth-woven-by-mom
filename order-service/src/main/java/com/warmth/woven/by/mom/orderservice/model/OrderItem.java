package com.warmth.woven.by.mom.orderservice.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class OrderItem {
  private Long productId;
  private Integer quantity;
}

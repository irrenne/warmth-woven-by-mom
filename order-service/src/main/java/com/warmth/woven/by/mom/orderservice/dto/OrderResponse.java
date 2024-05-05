package com.warmth.woven.by.mom.orderservice.dto;

import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

  private Long id;
  private List<OrderItemDTO> items;
  private BigDecimal price;
  private Boolean withShipping;
  private OrderStatus status;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}

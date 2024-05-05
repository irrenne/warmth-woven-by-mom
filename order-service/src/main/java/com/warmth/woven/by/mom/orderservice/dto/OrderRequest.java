package com.warmth.woven.by.mom.orderservice.dto;

import com.warmth.woven.by.mom.orderservice.enums.OrderStatus;
import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

  private List<OrderItemDTO> items;
  private BigDecimal price;
  private Boolean withShipping;
  private String userId;
  private OrderStatus status;
}


package com.warmth.woven.by.mom.orderservice.util.mapper;

import com.warmth.woven.by.mom.orderservice.dto.OrderRequest;
import com.warmth.woven.by.mom.orderservice.dto.OrderResponse;
import com.warmth.woven.by.mom.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

  OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

  OrderRequest mapOrderToOrderRequest(Order order);

  Order mapOrderRequestToOrder(OrderRequest orderRequest);

  OrderResponse mapOrderToOrderResponse(Order order);
}

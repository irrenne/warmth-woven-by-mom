package com.warmth.woven.by.mom.orderservice.util.mapper;

import com.warmth.woven.by.mom.orderservice.dto.OrderItemDTO;
import com.warmth.woven.by.mom.orderservice.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderItemMapper {

  OrderItemMapper INSTANCE = Mappers.getMapper(OrderItemMapper.class);

  OrderItemDTO mapOrderItemToOrderItemDTO(OrderItem orderItem);

  OrderItem mapOrderItemDTOToOrderItem(OrderItemDTO orderItemDTO);

}

package com.foodordering.mapper;

import com.foodordering.dto.OrderItemDto;
import com.foodordering.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "menuItemId", source = "menuItem.id")
    @Mapping(target = "menuItemName", source = "menuItem.name")
    OrderItemDto toDto(OrderItem orderItem);

    @Mapping(target = "menuItem", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem toEntity(OrderItemDto dto);
}

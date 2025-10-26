package com.foodordering.mapper;

import com.foodordering.dto.DeliveryDto;
import com.foodordering.entity.Delivery;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryDto toDto(Delivery delivery);
    Delivery toEntity(DeliveryDto dto);
}

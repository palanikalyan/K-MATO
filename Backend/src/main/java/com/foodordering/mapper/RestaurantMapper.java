package com.foodordering.mapper;

import com.foodordering.dto.RestaurantDto;
import com.foodordering.entity.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {
    @Mapping(target = "approvalStatus", expression = "java(restaurant.getApprovalStatus() != null ? restaurant.getApprovalStatus().name() : null)")
    @Mapping(target = "ownerId", expression = "java(restaurant.getOwner() != null ? restaurant.getOwner().getId() : null)")
    RestaurantDto toDto(Restaurant restaurant);

    @Mapping(target = "approvalStatus", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "menuItems", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Restaurant toEntity(RestaurantDto dto);
}

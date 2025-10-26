package com.foodordering.mapper;

import com.foodordering.dto.MenuItemDto;
import com.foodordering.entity.MenuItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MenuItemMapper {
    @Mapping(target = "restaurantId", source = "restaurant.id")
    MenuItemDto toDto(MenuItem menuItem);

    // When converting DTO -> entity we can't set the restaurant here (need service to set it)
    @Mapping(target = "restaurant", ignore = true)
    MenuItem toEntity(MenuItemDto dto);
}

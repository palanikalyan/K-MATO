package com.foodordering.service;

import com.foodordering.dto.MenuItemDto;
import com.foodordering.entity.MenuItem;
import com.foodordering.entity.Restaurant;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.MenuItemRepository;
import com.foodordering.repository.RestaurantRepository;
import com.foodordering.mapper.MenuItemMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemMapper menuItemMapper;

    public MenuItemService(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository, MenuItemMapper menuItemMapper) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemMapper = menuItemMapper;
    }

    @Transactional
    public MenuItemDto createMenuItem(MenuItemDto dto) {
        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        // Ensure authenticated user is the owner of the restaurant
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        if (restaurant.getOwner() == null || !email.equals(restaurant.getOwner().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("Not authorized to create menu items for this restaurant");
        }

        // Use mapper to create entity, then set relations/enum and persist
        MenuItem menuItem = menuItemMapper.toEntity(dto);
        menuItem.setRestaurant(restaurant);
        menuItem.setCategory(com.foodordering.enums.Category.valueOf(dto.getCategory().toUpperCase()));
        if (menuItem.getCreatedAt() == null) {
            menuItem.setCreatedAt(LocalDateTime.now());
        }

        menuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(menuItem);
    }

    public List<MenuItemDto> getMenuItemsByRestaurant(Long restaurantId) {
    return menuItemRepository.findByRestaurantId(restaurantId).stream()
        .map(menuItemMapper::toDto)
        .collect(Collectors.toList());
    }

    public List<MenuItemDto> getAllMenuItems() {
        return menuItemRepository.findAll().stream()
                .map(menuItemMapper::toDto)
                .collect(Collectors.toList());
    }

    public MenuItemDto getMenuItemById(Long id) {
    MenuItem menuItem = menuItemRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));
    return menuItemMapper.toDto(menuItem);
    }

    @Transactional
    public MenuItemDto updateMenuItem(Long id, MenuItemDto dto) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        // Ensure authenticated user is owner of the restaurant this menu item belongs to
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        if (menuItem.getRestaurant() == null || menuItem.getRestaurant().getOwner() == null || !email.equals(menuItem.getRestaurant().getOwner().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("Not authorized to update this menu item");
        }

        menuItem.setName(dto.getName());
        menuItem.setDescription(dto.getDescription());
        menuItem.setPrice(dto.getPrice());
        menuItem.setImageUrl(dto.getImageUrl());
        menuItem.setCategory(com.foodordering.enums.Category.valueOf(dto.getCategory().toUpperCase()));
        menuItem.setIsAvailable(dto.getIsAvailable());
        menuItem.setIsVegetarian(dto.getIsVegetarian());
        menuItem.setUpdatedAt(LocalDateTime.now());

        menuItem = menuItemRepository.save(menuItem);
        return menuItemMapper.toDto(menuItem);
    }

    @Transactional
    public void deleteMenuItem(Long id) {
        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        if (menuItem.getRestaurant() == null || menuItem.getRestaurant().getOwner() == null || !email.equals(menuItem.getRestaurant().getOwner().getEmail())) {
            throw new org.springframework.security.access.AccessDeniedException("Not authorized to delete this menu item");
        }

        menuItemRepository.deleteById(id);
    }

    // Mapping handled by MenuItemMapper
}

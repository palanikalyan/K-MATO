package com.foodordering.controller;

import com.foodordering.dto.MenuItemDto;
import com.foodordering.dto.ApiResponse;
import com.foodordering.service.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<ApiResponse> getMenuItemsByRestaurant(@PathVariable Long restaurantId) {
        List<MenuItemDto> menuItems = menuItemService.getMenuItemsByRestaurant(restaurantId);
        ApiResponse response = new ApiResponse(true, "Menu items retrieved successfully", menuItems);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMenuItems() {
        List<MenuItemDto> menuItems = menuItemService.getAllMenuItems();
        ApiResponse response = new ApiResponse(true, "Menu items retrieved successfully", menuItems);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMenuItemById(@PathVariable Long id) {
        MenuItemDto menuItem = menuItemService.getMenuItemById(id);
        ApiResponse response = new ApiResponse(true, "Menu item retrieved successfully", menuItem);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createMenuItem(@RequestBody MenuItemDto dto) {
        MenuItemDto menuItem = menuItemService.createMenuItem(dto);
        ApiResponse response = new ApiResponse(true, "Menu item created successfully", menuItem);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMenuItem(@PathVariable Long id, @RequestBody MenuItemDto dto) {
        MenuItemDto menuItem = menuItemService.updateMenuItem(id, dto);
        ApiResponse response = new ApiResponse(true, "Menu item updated successfully", menuItem);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        ApiResponse response = new ApiResponse(true, "Menu item deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}

package com.foodordering.controller;

import com.foodordering.dto.*;
import com.foodordering.service.MenuItemService;
import com.foodordering.service.OrderService;
import com.foodordering.service.RestaurantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant-owner")
public class RestaurantOwnerController {

    private final RestaurantService restaurantService;
    private final MenuItemService menuItemService;
    private final OrderService orderService;

    public RestaurantOwnerController(RestaurantService restaurantService, MenuItemService menuItemService,
                                    OrderService orderService) {
        this.restaurantService = restaurantService;
        this.menuItemService = menuItemService;
        this.orderService = orderService;
    }

    @PostMapping("/restaurants")
    public ResponseEntity<ApiResponse> createRestaurant(@Valid @RequestBody RestaurantDto dto) {
        RestaurantDto restaurant = restaurantService.createRestaurant(dto);
        ApiResponse response = new ApiResponse(true, "Restaurant created successfully", restaurant);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<ApiResponse> updateRestaurant(@PathVariable Long id,
                                                        @Valid @RequestBody RestaurantDto dto) {
        RestaurantDto restaurant = restaurantService.updateRestaurant(id, dto);
        ApiResponse response = new ApiResponse(true, "Restaurant updated successfully", restaurant);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<ApiResponse> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        ApiResponse response = new ApiResponse(true, "Restaurant deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/menu-items")
    public ResponseEntity<ApiResponse> createMenuItem(@Valid @RequestBody MenuItemDto dto) {
        MenuItemDto menuItem = menuItemService.createMenuItem(dto);
        ApiResponse response = new ApiResponse(true, "Menu item created successfully", menuItem);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/menu-items/{id}")
    public ResponseEntity<ApiResponse> updateMenuItem(@PathVariable Long id,
                                                      @Valid @RequestBody MenuItemDto dto) {
        MenuItemDto menuItem = menuItemService.updateMenuItem(id, dto);
        ApiResponse response = new ApiResponse(true, "Menu item updated successfully", menuItem);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<ApiResponse> deleteMenuItem(@PathVariable Long id) {
        menuItemService.deleteMenuItem(id);
        ApiResponse response = new ApiResponse(true, "Menu item deleted successfully", null);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurants")
    public ResponseEntity<ApiResponse> getOwnerRestaurants() {
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsForOwner();
        ApiResponse response = new ApiResponse(true, "Owner restaurants retrieved successfully", restaurants);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/restaurants/orders")
    public ResponseEntity<ApiResponse> getRestaurantOrders() {
        List<OrderDto> orders = orderService.getRestaurantOrdersForOwner();
        ApiResponse response = new ApiResponse(true, "Orders retrieved successfully for owner restaurants", orders);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long id,
                                                         @RequestParam String status) {
        OrderDto order = orderService.updateOrderStatus(id, status);
        ApiResponse response = new ApiResponse(true, "Order status updated successfully", order);
        return ResponseEntity.ok(response);
    }
}

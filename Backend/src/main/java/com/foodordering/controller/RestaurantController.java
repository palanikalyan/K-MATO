package com.foodordering.controller;

import com.foodordering.dto.*;
import com.foodordering.service.RestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllRestaurants() {
        List<RestaurantDto> restaurants = restaurantService.getAllRestaurants();
        ApiResponse response = new ApiResponse(true, "Restaurants retrieved successfully", restaurants);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createRestaurant(@RequestBody RestaurantDto dto) {
        RestaurantDto created = restaurantService.createRestaurant(dto);
        ApiResponse response = new ApiResponse(true, "Restaurant created successfully", created);
        return ResponseEntity.status(201).body(response);
    }

    // Returns restaurants owned by the authenticated user (owner)
    @GetMapping("/owner")
    public ResponseEntity<ApiResponse> getRestaurantsForOwner() {
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsForOwner();
        ApiResponse response = new ApiResponse(true, "Owner restaurants retrieved successfully", restaurants);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getRestaurantById(@PathVariable Long id) {
        RestaurantDto restaurant = restaurantService.getRestaurantById(id);
        ApiResponse response = new ApiResponse(true, "Restaurant retrieved successfully", restaurant);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/city/{city}")
    public ResponseEntity<ApiResponse> getRestaurantsByCity(@PathVariable String city) {
        List<RestaurantDto> restaurants = restaurantService.getRestaurantsByCity(city);
        ApiResponse response = new ApiResponse(true, "Restaurants retrieved successfully", restaurants);
        return ResponseEntity.ok(response);
    }

    // Owner endpoints
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDto dto) {
        RestaurantDto updated = restaurantService.updateRestaurant(id, dto);
        ApiResponse response = new ApiResponse(true, "Restaurant updated successfully", updated);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRestaurant(@PathVariable Long id) {
        restaurantService.deleteRestaurant(id);
        ApiResponse response = new ApiResponse(true, "Restaurant deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}

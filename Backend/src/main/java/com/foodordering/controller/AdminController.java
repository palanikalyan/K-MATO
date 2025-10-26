package com.foodordering.controller;

import com.foodordering.dto.*;
import com.foodordering.entity.Restaurant;
import com.foodordering.entity.User;
import com.foodordering.enums.Role;
import com.foodordering.repository.*;
import com.foodordering.mapper.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;

    private final UserMapper userMapper;
    private final RestaurantMapper restaurantMapper;
    private final MenuItemMapper menuItemMapper;
    private final OrderMapper orderMapper;
    private final DeliveryMapper deliveryMapper;

    public AdminController(UserRepository userRepository,
                           RestaurantRepository restaurantRepository,
                           MenuItemRepository menuItemRepository,
                           OrderRepository orderRepository,
                           PaymentRepository paymentRepository,
                           DeliveryRepository deliveryRepository,
                           UserMapper userMapper,
                           RestaurantMapper restaurantMapper,
                           MenuItemMapper menuItemMapper,
                           OrderMapper orderMapper,
                           DeliveryMapper deliveryMapper) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.deliveryRepository = deliveryRepository;
        this.userMapper = userMapper;
        this.restaurantMapper = restaurantMapper;
        this.menuItemMapper = menuItemMapper;
        this.orderMapper = orderMapper;
        this.deliveryMapper = deliveryMapper;
    }

    // --- Users ---
    @GetMapping("/users")
    public ResponseEntity<ApiResponse> listUsers() {
        List<UserDto> users = userRepository.findAll().stream().map(userMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(true, "Users retrieved", users));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return ResponseEntity.ok(new ApiResponse(true, "User retrieved", userMapper.toDto(user)));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long id, @RequestBody UserDto dto) {
        User user = userRepository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (dto.getFullName() != null) user.setFullName(dto.getFullName());
        if (dto.getPhoneNumber() != null) user.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getRole() != null) user.setRole(Role.valueOf(dto.getRole()));
        if (dto.getIsActive() != null) user.setIsActive(dto.getIsActive());
        user = userRepository.save(user);
        return ResponseEntity.ok(new ApiResponse(true, "User updated", userMapper.toDto(user)));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(true, "User deleted", null));
    }

    // --- Restaurants ---
    @GetMapping("/restaurants")
    public ResponseEntity<ApiResponse> listRestaurants() {
        List<RestaurantDto> items = restaurantRepository.findAll().stream().map(restaurantMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(true, "Restaurants retrieved", items));
    }

    @GetMapping("/restaurants/{id}")
    public ResponseEntity<ApiResponse> getRestaurant(@PathVariable Long id) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        return ResponseEntity.ok(new ApiResponse(true, "Restaurant retrieved", restaurantMapper.toDto(r)));
    }

    @PostMapping("/restaurants")
    public ResponseEntity<ApiResponse> createRestaurant(@RequestBody RestaurantDto dto) {
        Restaurant r = restaurantMapper.toEntity(dto);
        // Note: admin can create without owner; owner assignment could be done by setting ownerId in dto in future
        r = restaurantRepository.save(r);
        return ResponseEntity.status(201).body(new ApiResponse(true, "Restaurant created", restaurantMapper.toDto(r)));
    }

    @PutMapping("/restaurants/{id}")
    public ResponseEntity<ApiResponse> updateRestaurant(@PathVariable Long id, @RequestBody RestaurantDto dto) {
        Restaurant r = restaurantRepository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        if (dto.getName() != null) r.setName(dto.getName());
        if (dto.getDescription() != null) r.setDescription(dto.getDescription());
        if (dto.getAddress() != null) r.setAddress(dto.getAddress());
        if (dto.getCity() != null) r.setCity(dto.getCity());
        if (dto.getPhoneNumber() != null) r.setPhoneNumber(dto.getPhoneNumber());
        if (dto.getIsOpen() != null) r.setIsOpen(dto.getIsOpen());
        r = restaurantRepository.save(r);
        return ResponseEntity.ok(new ApiResponse(true, "Restaurant updated", restaurantMapper.toDto(r)));
    }

    @DeleteMapping("/restaurants/{id}")
    public ResponseEntity<ApiResponse> deleteRestaurant(@PathVariable Long id) {
        restaurantRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Restaurant deleted", null));
    }

    // --- Restaurant Approval ---
    @GetMapping("/restaurants/pending")
    public ResponseEntity<ApiResponse> getPendingRestaurants() {
        List<RestaurantDto> pending = restaurantRepository.findAll().stream()
                .filter(r -> r.getApprovalStatus() == com.foodordering.enums.ApprovalStatus.PENDING)
                .map(restaurantMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(true, "Pending restaurants retrieved", pending));
    }

    @PostMapping("/restaurants/{id}/approve")
    public ResponseEntity<ApiResponse> approveRestaurant(@PathVariable Long id) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        r.setApprovalStatus(com.foodordering.enums.ApprovalStatus.APPROVED);
        r = restaurantRepository.save(r);
        return ResponseEntity.ok(new ApiResponse(true, "Restaurant approved", restaurantMapper.toDto(r)));
    }

    @PostMapping("/restaurants/{id}/reject")
    public ResponseEntity<ApiResponse> rejectRestaurant(@PathVariable Long id) {
        Restaurant r = restaurantRepository.findById(id)
                .orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurant not found"));
        r.setApprovalStatus(com.foodordering.enums.ApprovalStatus.REJECTED);
        r = restaurantRepository.save(r);
        return ResponseEntity.ok(new ApiResponse(true, "Restaurant rejected", restaurantMapper.toDto(r)));
    }

    // --- Menu items ---
    @GetMapping("/menu-items")
    public ResponseEntity<ApiResponse> listMenuItems() {
        var items = menuItemRepository.findAll().stream().map(menuItemMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(true, "Menu items retrieved", items));
    }

    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<ApiResponse> deleteMenuItem(@PathVariable Long id) {
        menuItemRepository.deleteById(id);
        return ResponseEntity.ok(new ApiResponse(true, "Menu item deleted", null));
    }

    // --- Orders ---
    @GetMapping("/orders")
    public ResponseEntity<ApiResponse> listOrders() {
        var items = orderRepository.findAll().stream().map(orderMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(true, "Orders retrieved", items));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long id) {
        var order = orderRepository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        return ResponseEntity.ok(new ApiResponse(true, "Order retrieved", orderMapper.toDto(order)));
    }

    @PutMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse> updateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        var order = orderRepository.findById(id).orElseThrow(() -> new org.springframework.web.server.ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        try {
            order.setStatus(com.foodordering.enums.OrderStatus.valueOf(status));
        } catch (Exception ex) {
            throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid status");
        }
        order = orderRepository.save(order);
        return ResponseEntity.ok(new ApiResponse(true, "Order status updated", orderMapper.toDto(order)));
    }

    // --- Payments & Deliveries (read/update) ---
    @GetMapping("/payments")
    public ResponseEntity<ApiResponse> listPayments() {
        var p = paymentRepository.findAll();
        return ResponseEntity.ok(new ApiResponse(true, "Payments retrieved", p));
    }

    @GetMapping("/deliveries")
    public ResponseEntity<ApiResponse> listDeliveries() {
        var d = deliveryRepository.findAll().stream().map(deliveryMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse(true, "Deliveries retrieved", d));
    }

}

package com.foodordering.controller;

import com.foodordering.dto.*;
import com.foodordering.service.OrderService;
import com.foodordering.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final PaymentService paymentService;

    public OrderController(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@Valid @RequestBody CreateOrderDto dto) {
        OrderDto order = orderService.createOrder(dto);
        ApiResponse response = new ApiResponse(true, "Order created successfully", order);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/customer")
    public ResponseEntity<ApiResponse> getCustomerOrders() {
        List<OrderDto> orders = orderService.getCustomerOrders();
        ApiResponse response = new ApiResponse(true, "Orders retrieved successfully", orders);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long id) {
        OrderDto order = orderService.getOrderById(id);
        ApiResponse response = new ApiResponse(true, "Order retrieved successfully", order);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse> cancelOrder(@PathVariable Long id) {
        OrderDto order = orderService.cancelOrder(id);
        ApiResponse response = new ApiResponse(true, "Order cancelled successfully", order);
        return ResponseEntity.ok(response);
    }

    // --- User-scoped convenience endpoints (no id required) ---
    // Returns the authenticated user's latest order
    @GetMapping("/current")
    public ResponseEntity<ApiResponse> getCurrentOrder() {
        OrderDto dto = orderService.getLatestCustomerOrder();
        ApiResponse resp = new ApiResponse(true, "Latest order retrieved", dto);
        return ResponseEntity.ok(resp);
    }

    // Pay for the authenticated user's latest order
    @PostMapping("/current/pay")
    public ResponseEntity<ApiResponse> payForCurrentOrder(@Valid @RequestBody com.foodordering.dto.PaymentRequestDto request) {
        OrderDto current = orderService.getLatestCustomerOrder();
        OrderDto updated = paymentService.processPayment(current.getId(), request);
        ApiResponse resp = new ApiResponse(true, "Payment processed", updated);
        return ResponseEntity.ok(resp);
    }

    // Get delivery info for current order
    @GetMapping("/current/delivery")
    public ResponseEntity<ApiResponse> getCurrentDelivery() {
        OrderDto current = orderService.getLatestCustomerOrder();
        com.foodordering.dto.DeliveryDto delivery = current.getDelivery();
        ApiResponse resp = new ApiResponse(true, "Delivery info retrieved", delivery);
        return ResponseEntity.ok(resp);
    }
}

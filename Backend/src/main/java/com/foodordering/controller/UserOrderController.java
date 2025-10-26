package com.foodordering.controller;

import com.foodordering.dto.ApiResponse;

import com.foodordering.dto.PaymentRequestDto;
// This controller is a lightweight compatibility wrapper that redirects
// old `/api/user/orders/*` calls to the consolidated `/api/orders/*` endpoints.
// It intentionally has no service dependencies.
import org.springframework.http.ResponseEntity;
import java.net.URI;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/user/orders")
public class UserOrderController {

    // No-op constructor: controller only issues HTTP redirects to new endpoints.
    public UserOrderController() {
        // intentionally empty
    }

    // Returns the authenticated user's latest order (no id required)
    @GetMapping("/current")
    public ResponseEntity<ApiResponse> getCurrentOrder() {
        // Redirect to consolidated OrderController endpoint
        return ResponseEntity.status(301).location(URI.create("/api/orders/current")).build();
    }

    // Pay for the authenticated user's latest order (no id required)
    @PostMapping("/current/pay")
    public ResponseEntity<ApiResponse> payForCurrentOrder(@Valid @RequestBody PaymentRequestDto request) {
        // Redirect to consolidated OrderController endpoint
        return ResponseEntity.status(301).location(URI.create("/api/orders/current/pay")).build();
    }

    // Get delivery info for current order
    @GetMapping("/current/delivery")
    public ResponseEntity<ApiResponse> getCurrentDelivery() {
        // Redirect to consolidated OrderController endpoint
        return ResponseEntity.status(301).location(URI.create("/api/orders/current/delivery")).build();
    }
}

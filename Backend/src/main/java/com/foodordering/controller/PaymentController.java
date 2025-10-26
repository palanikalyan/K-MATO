package com.foodordering.controller;

import com.foodordering.dto.ApiResponse;
import com.foodordering.dto.OrderDto;
import com.foodordering.dto.PaymentRequestDto;
import com.foodordering.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}/pay")
    public ResponseEntity<ApiResponse> payForOrder(@PathVariable Long orderId, @Valid @RequestBody PaymentRequestDto request) {
        OrderDto updated = paymentService.processPayment(orderId, request);
        ApiResponse resp = new ApiResponse(true, "Payment processed", updated);
        return ResponseEntity.ok(resp);
    }
}

package com.foodordering.service;

import com.foodordering.dto.PaymentRequestDto;
import com.foodordering.dto.OrderDto;

public interface PaymentService {
    // Process payment for orderId. Returns updated OrderDto for convenience.
    OrderDto processPayment(Long orderId, PaymentRequestDto request);
}

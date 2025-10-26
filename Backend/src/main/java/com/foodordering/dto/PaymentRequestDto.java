package com.foodordering.dto;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private String method; // e.g. CARD, CASH
    // For demo: allow forcing result: SUCCESS or FAILED
    private String mockResult;
}

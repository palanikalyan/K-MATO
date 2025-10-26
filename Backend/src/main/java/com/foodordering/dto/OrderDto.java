package com.foodordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private Long customerId;
    private String customerName;
    private Long restaurantId;
    private String restaurantName;
    private List<OrderItemDto> items;
    private AddressDto deliveryAddress;
    private Double totalAmount;
    private Double deliveryFee;
    private Double taxAmount;
    private String status;
    private String paymentMethod;
    private String paymentStatus;
    private String specialInstructions;
    private LocalDateTime createdAt;
    private LocalDateTime deliveredAt;
    private DeliveryDto delivery;
}

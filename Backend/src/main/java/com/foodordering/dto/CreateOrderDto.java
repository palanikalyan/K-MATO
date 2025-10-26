package com.foodordering.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateOrderDto {
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;

    @NotNull(message = "Delivery address ID is required")
    private Long deliveryAddressId;

    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequestDto> items;

    @NotNull(message = "Payment method is required")
    private String paymentMethod;

    private String specialInstructions;
}

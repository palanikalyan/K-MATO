package com.foodordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String email;
    private String fullName;
    private String phoneNumber;
    private String role;
    private List<AddressDto> addresses;
    private List<OrderDto> orders;
    private List<RestaurantDto> restaurants;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive;
}

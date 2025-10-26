package com.foodordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String address;
    private String city;
    private String phoneNumber;
    private Double rating;
    private Integer totalReviews;
    private Boolean isOpen;
    private String approvalStatus; // PENDING, APPROVED, REJECTED
    private Long ownerId; // Owner user ID
    private LocalDateTime createdAt;
}

package com.foodordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeliveryDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String status;
    private String assignedDriver;
    private Integer etaMinutes;
    private Integer etaSeconds;
    private LocalDateTime scheduledAt;
    private LocalDateTime updatedAt;
}

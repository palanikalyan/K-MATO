package com.foodordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDto {
    private String token;
    @Builder.Default
    private String type = "Bearer";
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String email;
    private String fullName;
    private String role;
}

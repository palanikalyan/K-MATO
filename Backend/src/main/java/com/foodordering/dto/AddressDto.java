package com.foodordering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String landmark;
    private Boolean isDefault;
}

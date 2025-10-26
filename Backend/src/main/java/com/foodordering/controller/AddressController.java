package com.foodordering.controller;

import com.foodordering.dto.AddressDto;
import com.foodordering.dto.ApiResponse;
import com.foodordering.service.AddressService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createAddress(@Valid @RequestBody AddressDto dto) {
        AddressDto address = addressService.createAddress(dto);
        ApiResponse response = new ApiResponse(true, "Address created successfully", address);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    public ResponseEntity<ApiResponse> getUserAddresses() {
        List<AddressDto> addresses = addressService.getUserAddresses();
        ApiResponse response = new ApiResponse(true, "Addresses retrieved successfully", addresses);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateAddress(@PathVariable Long id,
                                                     @Valid @RequestBody AddressDto dto) {
        AddressDto address = addressService.updateAddress(id, dto);
        ApiResponse response = new ApiResponse(true, "Address updated successfully", address);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        ApiResponse response = new ApiResponse(true, "Address deleted successfully", null);
        return ResponseEntity.ok(response);
    }
}

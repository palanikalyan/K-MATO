package com.foodordering.controller;

import com.foodordering.dto.*;
import com.foodordering.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody UserRegistrationDto dto) {
        AuthResponseDto response = authService.register(dto);
        ApiResponse apiResponse = new ApiResponse(true, "User registered successfully", response);
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginDto dto) {
        AuthResponseDto response = authService.login(dto);
        ApiResponse apiResponse = new ApiResponse(true, "Login successful", response);
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> getCurrentUser() {
        UserDto user = authService.getCurrentUser();
        ApiResponse apiResponse = new ApiResponse(true, "User retrieved", user);
        return ResponseEntity.ok(apiResponse);
    }
}

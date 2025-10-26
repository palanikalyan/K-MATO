package com.foodordering.controller;

import com.foodordering.dto.ApiResponse;
import com.foodordering.dto.DeliveryDto;
import com.foodordering.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse> updateDeliveryStatus(@PathVariable Long id, @RequestParam String status) {
        DeliveryDto dto = deliveryService.updateDeliveryStatus(id, status);
        ApiResponse resp = new ApiResponse(true, "Delivery status updated", dto);
        return ResponseEntity.ok(resp);
    }
}

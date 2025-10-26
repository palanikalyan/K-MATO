package com.foodordering.entity;

import com.foodordering.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false, unique = true)
    private Order order;

    @Enumerated(EnumType.STRING)
    @lombok.Builder.Default
    private DeliveryStatus status = DeliveryStatus.SCHEDULED;

    // For demo, a simple assigned driver name or id
    private String assignedDriver;

    // ETA in seconds for demo fast scheduling (30 - 120 seconds)
    private Integer etaSeconds;

    private LocalDateTime scheduledAt;

    private LocalDateTime updatedAt;
}

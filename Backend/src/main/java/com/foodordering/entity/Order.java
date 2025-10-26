package com.foodordering.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_address_id", nullable = false)
    private Address deliveryAddress;

    @Column(nullable = false)
    private Double totalAmount;

    private Double deliveryFee = 0.0;

    private Double taxAmount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private com.foodordering.enums.OrderStatus status = com.foodordering.enums.OrderStatus.PENDING;

    @Enumerated(EnumType.STRING)
    private com.foodordering.enums.PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private com.foodordering.enums.PaymentStatus paymentStatus = com.foodordering.enums.PaymentStatus.PENDING;

    private String specialInstructions;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    private LocalDateTime deliveredAt;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private com.foodordering.entity.Delivery delivery;
}

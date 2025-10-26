package com.foodordering.service;

import com.foodordering.dto.PaymentRequestDto;
import com.foodordering.dto.OrderDto;
import com.foodordering.entity.Order;
import com.foodordering.entity.Payment;
import com.foodordering.enums.PaymentStatus;
import com.foodordering.enums.OrderStatus;
import com.foodordering.repository.OrderRepository;
import com.foodordering.repository.PaymentRepository;
import com.foodordering.mapper.OrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class MockPaymentService implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final com.foodordering.service.DeliveryService deliveryService;

    public MockPaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository, OrderMapper orderMapper,
                              com.foodordering.service.DeliveryService deliveryService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.deliveryService = deliveryService;
    }

    @Override
    @Transactional
    public OrderDto processPayment(Long orderId, PaymentRequestDto request) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order not found"));
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(() -> new IllegalArgumentException("Payment record not found for order"));

        String result = (request.getMockResult() == null) ? "COMPLETED" : request.getMockResult().toUpperCase();

            if ("COMPLETED".equals(result) || "SUCCESS".equals(result)) {
            payment.setStatus(PaymentStatus.COMPLETED);
            payment.setTransactionId("MOCK_TXN_" + UUID.randomUUID());
            payment.setUpdatedAt(java.time.LocalDateTime.now());
            paymentRepository.save(payment);
            order.setPaymentStatus(com.foodordering.enums.PaymentStatus.COMPLETED);
            order.setStatus(OrderStatus.CONFIRMED);
            order.setUpdatedAt(java.time.LocalDateTime.now());
            orderRepository.save(order);
                // schedule delivery for confirmed order
                try {
                    deliveryService.scheduleDelivery(order);
                } catch (Exception ex) {
                    // ignore delivery scheduling errors for demo
                }
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            payment.setTransactionId("MOCK_TXN_FAILED_" + UUID.randomUUID());
            payment.setUpdatedAt(java.time.LocalDateTime.now());
            paymentRepository.save(payment);
            order.setPaymentStatus(com.foodordering.enums.PaymentStatus.FAILED);
            order.setStatus(OrderStatus.CANCELLED);
            order.setUpdatedAt(java.time.LocalDateTime.now());
            orderRepository.save(order);
        }

        return orderMapper.toDto(order);
    }
}

package com.foodordering.service;

import com.foodordering.entity.Delivery;
import com.foodordering.entity.Order;
import com.foodordering.mapper.DeliveryMapper;
import com.foodordering.repository.DeliveryRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final DeliveryMapper deliveryMapper;
    private final SimpMessagingTemplate messagingTemplate;
    private final com.foodordering.repository.OrderRepository orderRepository;
    private final com.foodordering.mapper.OrderMapper orderMapper;
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private static final Random RANDOM = new Random();

    public DeliveryService(DeliveryRepository deliveryRepository, DeliveryMapper deliveryMapper,
                           SimpMessagingTemplate messagingTemplate, com.foodordering.repository.OrderRepository orderRepository,
                           com.foodordering.mapper.OrderMapper orderMapper) {
        this.deliveryRepository = deliveryRepository;
        this.deliveryMapper = deliveryMapper;
        this.messagingTemplate = messagingTemplate;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    // Helper used by scheduled tasks to apply status changes (best-effort)
    private void applyStatusChangeById(Long deliveryId, com.foodordering.enums.DeliveryStatus newStatus) {
        Delivery delivery = deliveryRepository.findById(deliveryId).orElse(null);
        if (delivery == null) return;
        delivery.setStatus(newStatus);
        delivery.setUpdatedAt(java.time.LocalDateTime.now());
        delivery = deliveryRepository.save(delivery);

        // publish delivery update
        try {
            messagingTemplate.convertAndSend("/topic/orders/" + delivery.getOrder().getId(), deliveryMapper.toDto(delivery));
            if (delivery.getOrder().getCustomer()!=null && delivery.getOrder().getCustomer().getEmail()!=null) {
                messagingTemplate.convertAndSendToUser(delivery.getOrder().getCustomer().getEmail(), "/queue/order-updates", deliveryMapper.toDto(delivery));
            }
        } catch (Exception ex) {
            // best-effort
        }

        if (newStatus == com.foodordering.enums.DeliveryStatus.DELIVERED) {
            Order order = delivery.getOrder();
            order.setStatus(com.foodordering.enums.OrderStatus.DELIVERED);
            order.setDeliveredAt(java.time.LocalDateTime.now());
            order.setUpdatedAt(java.time.LocalDateTime.now());
            order = orderRepository.save(order);

            try {
                messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), orderMapper.toDto(order));
                if (order.getCustomer()!=null && order.getCustomer().getEmail()!=null) {
                    messagingTemplate.convertAndSendToUser(order.getCustomer().getEmail(), "/queue/order-updates", orderMapper.toDto(order));
                }
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    @Transactional
    public Delivery scheduleDelivery(Order order) {
        int etaSeconds = 30 + RANDOM.nextInt(91); // 30..120 seconds
        Delivery delivery = Delivery.builder()
                .order(order)
                .assignedDriver("Driver-" + (RANDOM.nextInt(90) + 10))
                .etaSeconds(etaSeconds)
                .scheduledAt(LocalDateTime.now())
                .build();

        delivery = deliveryRepository.save(delivery);

        // link back to order in memory
        try {
            order.setDelivery(delivery);
        } catch (Exception ex) {
            // ignore
        }

        // publish delivery info to order/topic and user queue
        try {
            messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), deliveryMapper.toDto(delivery));
            if (order.getCustomer() != null && order.getCustomer().getEmail() != null) {
                messagingTemplate.convertAndSendToUser(order.getCustomer().getEmail(), "/queue/order-updates", deliveryMapper.toDto(delivery));
            }
        } catch (Exception ex) {
            // ignore - best effort
        }

        // Schedule demo updates: PICKED_UP after 5s, IN_TRANSIT after half ETA, DELIVERED after ETA
        int pickedUpDelay = 5;
        int inTransitDelay = Math.max(10, etaSeconds / 2);
        int deliveredDelay = etaSeconds;

        final Long deliveryId = delivery.getId();
        scheduler.schedule(() -> {
            try { applyStatusChangeById(deliveryId, com.foodordering.enums.DeliveryStatus.PICKED_UP); } catch (Exception ignored) {}
        }, pickedUpDelay, TimeUnit.SECONDS);

        scheduler.schedule(() -> {
            try { applyStatusChangeById(deliveryId, com.foodordering.enums.DeliveryStatus.IN_TRANSIT); } catch (Exception ignored) {}
        }, inTransitDelay, TimeUnit.SECONDS);

        scheduler.schedule(() -> {
            try { applyStatusChangeById(deliveryId, com.foodordering.enums.DeliveryStatus.DELIVERED); } catch (Exception ignored) {}
        }, deliveredDelay, TimeUnit.SECONDS);

        return delivery;
    }

    @Transactional
    public com.foodordering.dto.DeliveryDto updateDeliveryStatus(Long deliveryId, String statusStr) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found"));

        com.foodordering.enums.DeliveryStatus newStatus = com.foodordering.enums.DeliveryStatus.valueOf(statusStr.toUpperCase());
        delivery.setStatus(newStatus);
        delivery.setUpdatedAt(java.time.LocalDateTime.now());
        delivery = deliveryRepository.save(delivery);

        // publish delivery update
        try {
            messagingTemplate.convertAndSend("/topic/orders/" + delivery.getOrder().getId(), deliveryMapper.toDto(delivery));
            if (delivery.getOrder().getCustomer()!=null && delivery.getOrder().getCustomer().getEmail()!=null) {
                messagingTemplate.convertAndSendToUser(delivery.getOrder().getCustomer().getEmail(), "/queue/order-updates", deliveryMapper.toDto(delivery));
            }
        } catch (Exception ex) {
            // best-effort
        }

        // If delivered, update order status and deliveredAt, publish order update
        if (newStatus == com.foodordering.enums.DeliveryStatus.DELIVERED) {
            Order order = delivery.getOrder();
            order.setStatus(com.foodordering.enums.OrderStatus.DELIVERED);
            order.setDeliveredAt(java.time.LocalDateTime.now());
            order.setUpdatedAt(java.time.LocalDateTime.now());
            order = orderRepository.save(order);

            try {
                messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), orderMapper.toDto(order));
                if (order.getCustomer()!=null && order.getCustomer().getEmail()!=null) {
                    messagingTemplate.convertAndSendToUser(order.getCustomer().getEmail(), "/queue/order-updates", orderMapper.toDto(order));
                }
            } catch (Exception ex) {
                // ignore
            }
        }

        return deliveryMapper.toDto(delivery);
    }
}

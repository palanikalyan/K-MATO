package com.foodordering.service;

import com.foodordering.dto.*;
import com.foodordering.entity.*;
import com.foodordering.enums.*;
import com.foodordering.exception.ResourceNotFoundException;
import com.foodordering.repository.*;
import com.foodordering.mapper.OrderMapper;
import com.foodordering.mapper.OrderItemMapper;
import com.foodordering.mapper.AddressMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

@Service
public class OrderService {

        private final OrderRepository orderRepository;
        private final UserRepository userRepository;
        private final RestaurantRepository restaurantRepository;
        private final MenuItemRepository menuItemRepository;
        private final AddressRepository addressRepository;
        private final PaymentRepository paymentRepository;
        private final OrderMapper orderMapper;
        private final OrderItemMapper orderItemMapper;
        private final AddressMapper addressMapper;
        private final SimpMessagingTemplate messagingTemplate;
        private final DeliveryRepository deliveryRepository;

        public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                                           RestaurantRepository restaurantRepository, MenuItemRepository menuItemRepository,
                                           AddressRepository addressRepository, PaymentRepository paymentRepository,
                                           OrderMapper orderMapper, OrderItemMapper orderItemMapper, AddressMapper addressMapper,
                                           SimpMessagingTemplate messagingTemplate, DeliveryRepository deliveryRepository) {
                this.orderRepository = orderRepository;
                this.userRepository = userRepository;
                this.restaurantRepository = restaurantRepository;
                this.menuItemRepository = menuItemRepository;
                this.addressRepository = addressRepository;
                this.paymentRepository = paymentRepository;
                this.orderMapper = orderMapper;
                this.orderItemMapper = orderItemMapper;
                this.addressMapper = addressMapper;
                this.messagingTemplate = messagingTemplate;
                this.deliveryRepository = deliveryRepository;
        }

        @Transactional
        public OrderDto createOrder(CreateOrderDto dto) {
                String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
                if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
                User customer = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        Restaurant restaurant = restaurantRepository.findById(dto.getRestaurantId())
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found"));

        Address deliveryAddress = addressRepository.findById(dto.getDeliveryAddressId())
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

                if (!deliveryAddress.getUser().getId().equals(customer.getId())) {
                        throw new IllegalArgumentException("Address does not belong to the customer");
                }

        Order order = Order.builder()
                .customer(customer)
                .restaurant(restaurant)
                .deliveryAddress(deliveryAddress)
                .createdAt(java.time.LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .paymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase()))
                .paymentStatus(PaymentStatus.PENDING)
                .specialInstructions(dto.getSpecialInstructions())
                .build();

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequestDto itemDto : dto.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemDto.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("Menu item not found"));

            if (!menuItem.getIsAvailable()) {
                throw new IllegalArgumentException("Menu item " + menuItem.getName() + " is not available");
            }

            double subtotal = menuItem.getPrice() * itemDto.getQuantity();
            totalAmount += subtotal;

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(menuItem)
                    .quantity(itemDto.getQuantity())
                    .price(menuItem.getPrice())
                    .subtotal(subtotal)
                    .build();

            orderItems.add(orderItem);
        }

        double deliveryFee = 30.0;
        double taxAmount = totalAmount * 0.05;
        totalAmount += deliveryFee + taxAmount;

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        order.setDeliveryFee(deliveryFee);
        order.setTaxAmount(taxAmount);

                order = orderRepository.save(order);

                Payment payment = Payment.builder()
                .order(order)
                .amount(totalAmount)
                .paymentMethod(PaymentMethod.valueOf(dto.getPaymentMethod().toUpperCase()))
                        .status(PaymentStatus.PENDING)
                        .createdAt(java.time.LocalDateTime.now())
                .build();
        paymentRepository.save(payment);

                // Notify via WebSocket: full order topic and restaurant topic
                try {
                        messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), orderMapper.toDto(order));
                        messagingTemplate.convertAndSend("/topic/restaurants/" + order.getRestaurant().getId() + "/orders", orderMapper.toDto(order));
                        // Also send to user-specific destination (if client connected with a principal name)
                        if (order.getCustomer()!=null && order.getCustomer().getEmail()!=null) {
                                messagingTemplate.convertAndSendToUser(order.getCustomer().getEmail(), "/queue/order-updates", orderMapper.toDto(order));
                        }
                } catch (Exception ex) {
                        // swallow to avoid breaking order creation; logging could be added
                }

                // Start automatic order progression
                autoProgressOrder(order.getId());

                return orderMapper.toDto(order);
    }

    public List<OrderDto> getCustomerOrders() {
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        return orderRepository.findByCustomerIdOrderByCreatedAtDesc(customer.getId()).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<OrderDto> getRestaurantOrders(Long restaurantId) {
        return orderRepository.findByRestaurantId(restaurantId).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

        public List<OrderDto> getRestaurantOrdersForOwner() {
                String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
                if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
                User owner = userRepository.findByEmail(email)
                                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));

                // Find restaurants for owner and aggregate orders
                List<Long> restaurantIds = restaurantRepository.findByOwnerId(owner.getId()).stream()
                                .map(Restaurant::getId)
                                .collect(Collectors.toList());

                List<OrderDto> result = new ArrayList<>();
                for (Long rid : restaurantIds) {
                        List<Order> orders = orderRepository.findByRestaurantId(rid);
                        orders.forEach(o -> result.add(orderMapper.toDto(o)));
                }
                return result;
        }

    public OrderDto getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        return orderMapper.toDto(order);
    }

    public OrderDto getLatestCustomerOrder() {
        String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
        if (email == null) throw new ResourceNotFoundException("Authenticated user not found");
        User customer = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Order order = orderRepository.findFirstByCustomerIdOrderByCreatedAtDesc(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No orders found for user"));
        return orderMapper.toDto(order);
    }

        public List<OrderDto> getAllOrders() {
                return orderRepository.findAll().stream()
                                .map(orderMapper::toDto)
                                .collect(Collectors.toList());
        }

    @Transactional
    public OrderDto updateOrderStatus(Long id, String status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

                // If the caller is a restaurant owner, ensure they own the restaurant for this order
                String email = com.foodordering.security.SecurityUtils.getCurrentUserEmail();
                if (email != null) {
                        if (order.getRestaurant() == null || order.getRestaurant().getOwner() == null || !email.equals(order.getRestaurant().getOwner().getEmail())) {
                                throw new org.springframework.security.access.AccessDeniedException("Not authorized to update status for this order");
                        }
                }

        OrderStatus newStatus = OrderStatus.valueOf(status.toUpperCase());
        order.setStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());

        if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveredAt(LocalDateTime.now());
        }

                                order = orderRepository.save(order);

                                // publish status update
                                try {
                                        messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), orderMapper.toDto(order));
                                        messagingTemplate.convertAndSend("/topic/restaurants/" + order.getRestaurant().getId() + "/orders", orderMapper.toDto(order));
                                        if (order.getCustomer()!=null && order.getCustomer().getEmail()!=null) {
                                                messagingTemplate.convertAndSendToUser(order.getCustomer().getEmail(), "/queue/order-updates", orderMapper.toDto(order));
                                        }
                                } catch (Exception ex) {
                                        // ignore
                                }

                                return orderMapper.toDto(order);
    }

    @Transactional
    public OrderDto cancelOrder(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("Cannot cancel delivered order");
        }

        if (order.getStatus() == OrderStatus.OUT_FOR_DELIVERY) {
            throw new IllegalArgumentException("Cannot cancel order that is out for delivery");
        }

                order.setStatus(OrderStatus.CANCELLED);
                order.setUpdatedAt(LocalDateTime.now());
                order = orderRepository.save(order);

                try {
                        messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), orderMapper.toDto(order));
                        messagingTemplate.convertAndSend("/topic/restaurants/" + order.getRestaurant().getId() + "/orders", orderMapper.toDto(order));
                        if (order.getCustomer()!=null && order.getCustomer().getEmail()!=null) {
                                messagingTemplate.convertAndSendToUser(order.getCustomer().getEmail(), "/queue/order-updates", orderMapper.toDto(order));
                        }
                } catch (Exception ex) {
                        // ignore
                }

                return orderMapper.toDto(order);
    }

    /**
     * Automatically progress order through stages:
     * PENDING -> CONFIRMED (10s) -> PREPARING (30s) -> OUT_FOR_DELIVERY (30s) -> DELIVERED (20s)
     * Total time: ~90 seconds (1.5 minutes)
     * Also updates delivery status in parallel
     */
    @Async
    @Transactional
    public void autoProgressOrder(Long orderId) {
        try {
            // Stage 1: PENDING -> CONFIRMED (10 seconds)
            TimeUnit.SECONDS.sleep(10);
            updateOrderStatusInternal(orderId, OrderStatus.CONFIRMED);

            // Stage 2: CONFIRMED -> PREPARING (30 seconds)
            TimeUnit.SECONDS.sleep(30);
            updateOrderStatusInternal(orderId, OrderStatus.PREPARING);

            // Stage 3: PREPARING -> OUT_FOR_DELIVERY (30 seconds)
            // Also update delivery to PICKED_UP
            TimeUnit.SECONDS.sleep(30);
            updateOrderStatusInternal(orderId, OrderStatus.OUT_FOR_DELIVERY);
            updateDeliveryStatus(orderId, DeliveryStatus.PICKED_UP);

            // Stage 4: Delivery IN_TRANSIT (10 seconds)
            TimeUnit.SECONDS.sleep(10);
            updateDeliveryStatus(orderId, DeliveryStatus.IN_TRANSIT);

            // Stage 5: OUT_FOR_DELIVERY -> DELIVERED (10 seconds)
            TimeUnit.SECONDS.sleep(10);
            updateDeliveryStatus(orderId, DeliveryStatus.DELIVERED);
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order != null && order.getStatus() != OrderStatus.CANCELLED) {
                order.setStatus(OrderStatus.DELIVERED);
                order.setDeliveredAt(LocalDateTime.now());
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
                notifyOrderUpdate(order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            // Log error but don't fail
        }
    }

    @Transactional
    protected void updateOrderStatusInternal(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && order.getStatus() != OrderStatus.CANCELLED) {
            order.setStatus(newStatus);
            order.setUpdatedAt(LocalDateTime.now());
            orderRepository.save(order);
            notifyOrderUpdate(order);
        }
    }

    @Transactional
    protected void updateDeliveryStatus(Long orderId, DeliveryStatus newStatus) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null && order.getDelivery() != null) {
            Delivery delivery = order.getDelivery();
            delivery.setStatus(newStatus);
            delivery.setUpdatedAt(LocalDateTime.now());
            deliveryRepository.save(delivery);
            
            // Notify delivery update via WebSocket
            try {
                messagingTemplate.convertAndSend("/topic/orders/" + orderId + "/delivery", delivery);
                if (order.getCustomer() != null && order.getCustomer().getEmail() != null) {
                    messagingTemplate.convertAndSendToUser(order.getCustomer().getEmail(), "/queue/delivery-updates", delivery);
                }
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    private void notifyOrderUpdate(Order order) {
        try {
            messagingTemplate.convertAndSend("/topic/orders/" + order.getId(), orderMapper.toDto(order));
            messagingTemplate.convertAndSend("/topic/restaurants/" + order.getRestaurant().getId() + "/orders", orderMapper.toDto(order));
            if (order.getCustomer() != null && order.getCustomer().getEmail() != null) {
                messagingTemplate.convertAndSendToUser(order.getCustomer().getEmail(), "/queue/order-updates", orderMapper.toDto(order));
            }
        } catch (Exception ex) {
            // ignore
        }
    }
    // Mapping handled by OrderMapper and OrderItemMapper
}

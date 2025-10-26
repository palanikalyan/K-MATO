package com.foodordering.mapper;

import com.foodordering.dto.OrderDto;
import com.foodordering.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class, AddressMapper.class, com.foodordering.mapper.DeliveryMapper.class})
public interface OrderMapper {
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "customerName", source = "customer.fullName")
    @Mapping(target = "restaurantId", source = "restaurant.id")
    @Mapping(target = "restaurantName", source = "restaurant.name")
    @Mapping(target = "items", source = "orderItems")
    @Mapping(target = "deliveryAddress", source = "deliveryAddress")
    @Mapping(target = "delivery", source = "delivery")
    @Mapping(target = "status", expression = "java(order.getStatus()!=null?order.getStatus().name():null)")
    @Mapping(target = "paymentMethod", expression = "java(order.getPaymentMethod()!=null?order.getPaymentMethod().name():null)")
    @Mapping(target = "paymentStatus", expression = "java(order.getPaymentStatus()!=null?order.getPaymentStatus().name():null)")
    OrderDto toDto(Order order);

    // Creating/updating orders typically requires services to set relations; ignore complex relations here
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "restaurant", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "deliveryAddress", ignore = true)
    Order toEntity(OrderDto dto);
}

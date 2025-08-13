package com.ecom.order.services;


import com.ecom.order.dtos.OrderCreatedEvent;
import com.ecom.order.dtos.OrderItemDto;
import com.ecom.order.dtos.OrderResponse;
import com.ecom.order.entities.CartItem;
import com.ecom.order.entities.Order;
import com.ecom.order.entities.OrderItem;
import com.ecom.order.enums.OrderStatus;
import com.ecom.order.repositories.CartItemRepository;
import com.ecom.order.repositories.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Data
@RequiredArgsConstructor
public class OrderService {


    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final StreamBridge streamBridge;

//    @Value("${rabbitmq.queue.name}")
//    private String queueName;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchangeName;

//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;

    public Optional<OrderResponse> createOrder(Long userId) {


        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        if (cartItems.isEmpty()) {
            return Optional.empty();
        }

        List<OrderItem> orderItems = new ArrayList<>();


        cartItems.forEach(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartItem.getProductId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());


            orderItems.add(orderItem);
        });

        BigDecimal totalAmount = cartItems.stream()
                .filter(cartItem -> cartItem.getPrice() != null && cartItem.getQuantity() > 0)
                .map(c -> c.getPrice().multiply(new BigDecimal(c.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.PENDING);
        order.setTotalAmount(totalAmount);
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        cartService.clearCart(userId);

        OrderCreatedEvent orderCreatedEvent = mapToOrderCreatedEvent(order);

        streamBridge.send("createOrder-out-0", orderCreatedEvent);

        return Optional.of(mapToOrderResponse(savedOrder));

    }

    public OrderResponse mapToOrderResponse(Order order) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        order.getItems().forEach(orderItem -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setProductId(orderItem.getProductId());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setPrice(orderItem.getPrice());
            orderItemDto.setId(orderItem.getId());
            orderItemDtos.add(orderItemDto);
        });

        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderStatus(order.getOrderStatus());
        response.setTotalAmount(order.getTotalAmount());
        response.setItems(orderItemDtos);

        return response;
    }

    public OrderCreatedEvent mapToOrderCreatedEvent(Order order) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        order.getItems().forEach(orderItem -> {
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setProductId(orderItem.getProductId());
            orderItemDto.setQuantity(orderItem.getQuantity());
            orderItemDto.setPrice(orderItem.getPrice());
            orderItemDto.setId(orderItem.getId());
            orderItemDtos.add(orderItemDto);
        });

        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        orderCreatedEvent.setOrderId(order.getId());
        orderCreatedEvent.setOrderStatus(order.getOrderStatus());
        orderCreatedEvent.setTotalAmount(order.getTotalAmount());
        orderCreatedEvent.setOrderItems(orderItemDtos);
        orderCreatedEvent.setUserId(order.getUserId());
        orderCreatedEvent.setCreatedAt(order.getCreationDate());


        return orderCreatedEvent;
    }
}

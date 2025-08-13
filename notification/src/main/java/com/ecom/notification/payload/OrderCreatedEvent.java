package com.ecom.notification.payload;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderCreatedEvent {
    private Long orderId;
    private Long userId;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItems;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
}

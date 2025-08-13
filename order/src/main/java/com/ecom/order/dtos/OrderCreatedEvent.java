package com.ecom.order.dtos;

import com.ecom.order.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

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
    @CreationTimestamp
    private LocalDateTime createdAt;
}

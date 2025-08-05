package com.ecom.order.entities;



import com.ecom.order.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus = OrderStatus.PENDING;
    @OneToMany(mappedBy = "order")
    private List<OrderItem> items = new ArrayList<>();

    private LocalDateTime creationDate;
    private LocalDateTime updateDate;


}

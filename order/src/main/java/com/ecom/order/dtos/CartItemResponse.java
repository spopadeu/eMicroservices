package com.ecom.order.dtos;

import lombok.Data;

import java.math.BigDecimal;


@Data
public class CartItemResponse {
    private Long id;
    private Long productId;
    private Integer quantity;
    private BigDecimal price;
}

package com.ecom.order.dtos;

import lombok.Data;


@Data
public class CartItemRequest {
    private Long productId;
    private Integer quantity;
}

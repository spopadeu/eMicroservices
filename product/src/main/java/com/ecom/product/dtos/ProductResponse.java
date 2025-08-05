package com.ecom.product.dtos;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductResponse {
    private String id;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private Boolean active = true;
    private Integer stockQuantity;
    private String productCategory;
    private String imageUrl;
}

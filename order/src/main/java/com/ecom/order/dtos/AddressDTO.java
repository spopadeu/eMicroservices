package com.ecom.order.dtos;


import lombok.Data;

@Data
public class AddressDTO {
    private Long id;
    private String street;
    private String city;
    private String zipCode;
}

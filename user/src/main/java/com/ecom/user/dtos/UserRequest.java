package com.ecom.user.dtos;

import com.ecom.user.enums.UserRole;
import lombok.Data;


@Data
public class UserRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private UserRole role;
    private AddressDTO address;
}

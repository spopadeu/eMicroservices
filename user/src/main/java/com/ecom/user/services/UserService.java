package com.ecom.user.services;


import com.ecom.user.dtos.AddressDTO;
import com.ecom.user.dtos.UserRequest;
import com.ecom.user.dtos.UserResponse;
import com.ecom.user.entities.Address;
import com.ecom.user.entities.User;
import com.ecom.user.repositories.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll().stream().map(this::mapToUserResponse).collect(Collectors.toList());

    }


    public void addUser(UserRequest userRequest) {
        User user = new User();
        this.updateUserFromRequest(user, userRequest);
        userRepository.save(user);
    }

    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToUserResponse);
    }

    public boolean updateUser(Long id, UserRequest userRequest) {
        return userRepository.findById(id).map(existingUser -> {
                   updateUserFromRequest(existingUser, userRequest);
                    userRepository.save(existingUser);
                    return true;
                })
                .orElse(false);


    }

    private UserResponse mapToUserResponse(User user) {
        UserResponse response = new UserResponse();
        response.setId(String.valueOf(user.getId()));
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());

        if (user.getAddress() != null) {
            AddressDTO addressDTO = new AddressDTO();
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setZipCode(user.getAddress().getZipCode());
            response.setAddress(addressDTO);
        }
        return response;
    }

    private void updateUserFromRequest(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setLastName(userRequest.getLastName());
        user.setEmail(userRequest.getEmail());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        if (userRequest.getAddress() != null) {
            Address address = new Address();
            address.setStreet(userRequest.getAddress().getStreet());
            address.setZipCode((userRequest.getAddress().getZipCode()));
            address.setCity(userRequest.getAddress().getCity());
            user.setAddress(address);
        }
    }
}

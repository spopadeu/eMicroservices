package com.ecom.order.services;


import com.ecom.order.dtos.CartItemRequest;
import com.ecom.order.dtos.CartItemResponse;
import com.ecom.order.entities.CartItem;
import com.ecom.order.repositories.CartItemRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class CartService {

    private CartItemRepository cartItemRepository;


    public CartService(CartItemRepository cartItemRepository) {
        this.cartItemRepository = cartItemRepository;
    }

    public boolean addToCart(Long userId, CartItemRequest request) {
        Long productId = request.getProductId();
        Optional<CartItem> existingCartItemOptional = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (existingCartItemOptional.isPresent()) {
            CartItem existingCartItem = existingCartItemOptional.get();
            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(BigDecimal.valueOf(1000));
            cartItemRepository.save(existingCartItem);
            return true;
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(BigDecimal.valueOf(1000));
        cartItemRepository.save(cartItem);

        return true;
    }

    public List<CartItemResponse> getAllCartItems(Long userId) {

        return cartItemRepository.findByUserId(userId).stream().map(this::mapToCartItemResponse).collect(Collectors.toList());
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setProductId(cartItem.getProductId());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(cartItem.getPrice());

        return response;
    }

    public boolean deleteCartItem(Long userId, Long productId) {


        Optional<CartItem> existingCartItemOptional = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (existingCartItemOptional.isEmpty()) {
            return false;
        }

        cartItemRepository.delete(existingCartItemOptional.get());
        return true;
    }

    @Transactional
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }

}


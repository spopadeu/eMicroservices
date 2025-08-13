package com.ecom.order.services;


import com.ecom.order.dtos.CartItemRequest;
import com.ecom.order.dtos.CartItemResponse;
import com.ecom.order.dtos.ProductResponse;
import com.ecom.order.dtos.UserResponse;
import com.ecom.order.entities.CartItem;
import com.ecom.order.repositories.CartItemRepository;
import com.ecom.order.clients.restclient.RestClientService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final RestClientService restClientService;


    public boolean addToCart(Long userId, CartItemRequest request) {
        Long productId = request.getProductId();

        ProductResponse productResponse = restClientService.getProduct(String.valueOf(productId));
        if (productResponse == null) {
            return false;
        }

        if(productResponse.getStockQuantity() < request.getQuantity()){
            return false;
        }

        UserResponse userResponse = restClientService.getUser(String.valueOf(userId));
        if (userResponse == null) {
            return false;
        }


        Optional<CartItem> existingCartItemOptional = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (existingCartItemOptional.isPresent()) {
            CartItem existingCartItem = existingCartItemOptional.get();
            int totalQuantity = existingCartItem.getQuantity() + request.getQuantity();
            existingCartItem.setQuantity(totalQuantity);
            BigDecimal bigTotalQ = BigDecimal.valueOf(totalQuantity);
            existingCartItem.setPrice(productResponse.getProductPrice().multiply(bigTotalQ));
            cartItemRepository.save(existingCartItem);
            return true;
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setQuantity(request.getQuantity());
        cartItem.setPrice(productResponse.getProductPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
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


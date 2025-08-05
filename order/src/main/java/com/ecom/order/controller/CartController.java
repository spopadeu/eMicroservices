package com.ecom.order.controller;

import com.ecom.order.dtos.CartItemRequest;
import com.ecom.order.dtos.CartItemResponse;
import com.ecom.order.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping()
    public ResponseEntity<String> addToCart(@RequestHeader("X-User-ID") Long userId,
                                          @RequestBody CartItemRequest request) {
        if(!cartService.addToCart(userId,request)){
            return ResponseEntity.badRequest().body("Product out of stock or User not found or Product not found");
        }
            return ResponseEntity.status(HttpStatus.CREATED).body(" Cart item added successfully");
    }

    @GetMapping()
    public ResponseEntity<List<CartItemResponse>> getCart(@RequestHeader("X-User-ID") Long userId){
        return new ResponseEntity<>(cartService.getAllCartItems(userId), HttpStatus.OK);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long productId, @RequestHeader("X-User-ID")  Long userId) {
        boolean deleted = cartService.deleteCartItem(userId,productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();

    }


}

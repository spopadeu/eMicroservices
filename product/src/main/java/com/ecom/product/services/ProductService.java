package com.ecom.product.services;

import com.ecom.product.dtos.ProductRequest;
import com.ecom.product.dtos.ProductResponse;
import com.ecom.product.entities.Product;
import com.ecom.product.repositories.ProductRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Data
public class ProductService {

    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveIsTrue().stream().map(this::mapToProductResponse).collect(Collectors.toList());

    }


    public void addProduct(ProductRequest productRequest) {
        Product product = new Product();
        this.updateProductFromRequest(product, productRequest);
        productRepository.save(product);
    }

    public Optional<ProductResponse> getProductById(Long id) {
        return productRepository.findById(id).map(this::mapToProductResponse);
    }

    public boolean updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id).map(existingProduct -> {
                    updateProductFromRequest(existingProduct, productRequest);
                    productRepository.save(existingProduct);
                    return true;
                })
                .orElse(false);


    }

    public boolean deleteProduct(Long id) {
        return this.productRepository.findById(id)
                .map(
                        prod -> {
                            prod.setActive(false);
                            productRepository.save(prod);
                            return true;
                        }
                )
                .orElse(false);

    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.findByActiveAndStockQuantityGreaterThanAndProductNameIgnoreCaseLike(true,0, keyword)
                .stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    public ProductResponse mapToProductResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(String.valueOf(product.getId()));
        response.setProductName(product.getProductName());
        response.setProductCategory(product.getProductCategory());
        response.setProductDescription(product.getProductDescription());
        response.setProductPrice(product.getProductPrice());
        response.setActive(product.getActive());
        response.setImageUrl(product.getImageUrl());
        response.setStockQuantity(product.getStockQuantity());


        return response;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setProductName(productRequest.getProductName());
        product.setProductCategory(productRequest.getProductCategory());
        product.setProductDescription(productRequest.getProductDescription());
        product.setProductPrice(productRequest.getProductPrice());
        product.setActive(productRequest.getActive());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());

    }


}

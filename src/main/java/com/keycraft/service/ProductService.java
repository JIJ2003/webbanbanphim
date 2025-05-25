package com.keycraft.service;

import com.keycraft.model.Product;
import com.keycraft.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public List<Product> getProductsWithFilters(String category, String brand, String switchType, 
                                               BigDecimal minPrice, BigDecimal maxPrice, String search) {
        return productRepository.findProductsWithFilters(category, brand, switchType, minPrice, maxPrice, search);
    }

    public List<Product> getFeaturedProducts() {
        return productRepository.findByFeaturedTrue();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product productDetails) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(productDetails.getName());
                    product.setDescription(productDetails.getDescription());
                    product.setPrice(productDetails.getPrice());
                    product.setImageUrl(productDetails.getImageUrl());
                    product.setCategory(productDetails.getCategory());
                    product.setBrand(productDetails.getBrand());
                    product.setSwitchType(productDetails.getSwitchType());
                    product.setLayout(productDetails.getLayout());
                    product.setStock(productDetails.getStock());
                    product.setFeatured(productDetails.getFeatured());
                    return productRepository.save(product);
                })
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new RuntimeException("Product not found with id: " + id);
        }
    }
}
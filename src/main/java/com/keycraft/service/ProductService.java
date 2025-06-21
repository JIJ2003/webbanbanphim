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
    
    @Autowired
    private OrderItemService orderItemService;

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
    
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Long id, Product updated) {
        return productRepository.findById(id).map(p -> {
            p.setName(updated.getName());
            p.setDescription(updated.getDescription());
            p.setPrice(updated.getPrice());
            p.setImageUrl(updated.getImageUrl());
            p.setCategory(updated.getCategory());
            p.setBrand(updated.getBrand());
            p.setSwitchType(updated.getSwitchType());
            p.setLayout(updated.getLayout());
            p.setStock(updated.getStock());
            p.setFeatured(updated.getFeatured());
            p.setDiscontinued(updated.isDiscontinued());
            return productRepository.save(p);
        });
    }

    public boolean deleteProduct(Long id) {
        return productRepository.findById(id).map(p -> {
            if (orderItemService.existsInActiveOrders(id)) {
                return false; // còn đơn hàng chưa huỷ -> không xoá
            }
            p.setDiscontinued(true);
            productRepository.save(p);
            return true;
        }).orElse(false);
    }
    public boolean discontinueProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return false;

        // Kiểm tra xem sản phẩm có đang nằm trong order chưa huỷ không
        boolean usedInActiveOrders = orderItemService.existsInActiveOrders(id);
        if (usedInActiveOrders) return false;

        product.setDiscontinued(true);
        productRepository.save(product);
        return true;
    }
    
    
}
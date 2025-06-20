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

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        Optional<Product> existing = productRepository.findById(id);
        if (existing.isEmpty()) return Optional.empty();

        Product product = existing.get();
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setImageUrl(updatedProduct.getImageUrl());
                    product.setCategory(updatedProduct.getCategory());
                    product.setBrand(updatedProduct.getBrand());
                    product.setSwitchType(updatedProduct.getSwitchType());
                    product.setLayout(updatedProduct.getLayout());
                    product.setStock(updatedProduct.getStock());
                    product.setFeatured(updatedProduct.getFeatured());
                    product.setDiscontinued(updatedProduct.isDiscontinued());

                    return Optional.of(productRepository.save(product));

    }

    public boolean deleteProduct(Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) return false;

        if (orderItemService.existsInActiveOrders(id)) {
            return false; // Không xoá được nếu còn trong đơn chưa huỷ
        }

        product.setDiscontinued(true);
        productRepository.save(product);
        return true;
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
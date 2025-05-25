package com.keycraft.config;

import com.keycraft.model.Product;
import com.keycraft.model.User;
import com.keycraft.repository.ProductRepository;
import com.keycraft.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0) {
            initializeProducts();
        }
        initializeUsers();
    }

    private void initializeProducts() {
        // Featured mechanical keyboards
        Product keyboard1 = new Product();
        keyboard1.setName("Keychron K2 Wireless Mechanical Keyboard");
        keyboard1.setDescription("75% layout wireless mechanical keyboard with RGB backlight and hot-swappable switches");
        keyboard1.setPrice(new BigDecimal("89.99"));
        keyboard1.setImageUrl("https://images.unsplash.com/photo-1541140532154-b024d705b90a?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=300");
        keyboard1.setCategory("mechanical-keyboards");
        keyboard1.setBrand("Keychron");
        keyboard1.setSwitchType("tactile");
        keyboard1.setLayout("75%");
        keyboard1.setStock(25);
        keyboard1.setFeatured(true);
        productRepository.save(keyboard1);

        Product keyboard2 = new Product();
        keyboard2.setName("Ducky One 3 TKL");
        keyboard2.setDescription("Tenkeyless mechanical keyboard with Cherry MX switches and PBT keycaps");
        keyboard2.setPrice(new BigDecimal("149.99"));
        keyboard2.setImageUrl("https://images.unsplash.com/photo-1587829741301-dc798b83add3?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=300");
        keyboard2.setCategory("mechanical-keyboards");
        keyboard2.setBrand("Ducky");
        keyboard2.setSwitchType("linear");
        keyboard2.setLayout("TKL");
        keyboard2.setStock(15);
        keyboard2.setFeatured(true);
        productRepository.save(keyboard2);

        Product keyboard3 = new Product();
        keyboard3.setName("Leopold FC750R PD");
        keyboard3.setDescription("Professional TKL keyboard with Cherry MX switches and sound dampening");
        keyboard3.setPrice(new BigDecimal("179.99"));
        keyboard3.setImageUrl("https://images.unsplash.com/photo-1595044426077-d36d9236d54a?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=300");
        keyboard3.setCategory("mechanical-keyboards");
        keyboard3.setBrand("Leopold");
        keyboard3.setSwitchType("tactile");
        keyboard3.setLayout("TKL");
        keyboard3.setStock(8);
        keyboard3.setFeatured(true);
        productRepository.save(keyboard3);

        // Additional products
        Product switches = new Product();
        switches.setName("Cherry MX Brown Switches (70 pack)");
        switches.setDescription("Tactile mechanical switches with 2mm actuation point");
        switches.setPrice(new BigDecimal("49.99"));
        switches.setImageUrl("https://images.unsplash.com/photo-1551698618-1dfe5d97d256?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=300");
        switches.setCategory("switches");
        switches.setBrand("Cherry");
        switches.setSwitchType("tactile");
        switches.setStock(50);
        switches.setFeatured(false);
        productRepository.save(switches);

        Product keycaps = new Product();
        keycaps.setName("PBT Double Shot Keycaps Set");
        keycaps.setDescription("108-key PBT keycap set with double-shot legends");
        keycaps.setPrice(new BigDecimal("79.99"));
        keycaps.setImageUrl("https://images.unsplash.com/photo-1558618666-fcd25c85cd64?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=300");
        keycaps.setCategory("keycaps");
        keycaps.setBrand("Varmilo");
        keycaps.setStock(30);
        keycaps.setFeatured(false);
        productRepository.save(keycaps);

        Product compact = new Product();
        compact.setName("HHKB Professional Hybrid Type-S");
        compact.setDescription("60% Topre switch keyboard with Bluetooth connectivity");
        compact.setPrice(new BigDecimal("329.99"));
        compact.setImageUrl("https://images.unsplash.com/photo-1587829741301-dc798b83add3?ixlib=rb-4.0.3&auto=format&fit=crop&w=400&h=300");
        compact.setCategory("mechanical-keyboards");
        compact.setBrand("HHKB");
        compact.setSwitchType("tactile");
        compact.setLayout("60%");
        compact.setStock(5);
        compact.setFeatured(true);
        productRepository.save(compact);
    }

    private void initializeUsers() {
        try {
            // Create admin user if doesn't exist
            authService.registerUser("admin@keycraft.com", "admin123", "Admin", "User", User.UserRole.ADMIN);
            System.out.println("Created admin user: admin@keycraft.com / admin123");
        } catch (Exception e) {
            System.out.println("Admin user already exists or error creating: " + e.getMessage());
        }

        try {
            // Create customer user if doesn't exist
            authService.registerUser("customer@keycraft.com", "customer123", "John", "Doe", User.UserRole.CUSTOMER);
            System.out.println("Created customer user: customer@keycraft.com / customer123");
        } catch (Exception e) {
            System.out.println("Customer user already exists or error creating: " + e.getMessage());
        }
    }
}
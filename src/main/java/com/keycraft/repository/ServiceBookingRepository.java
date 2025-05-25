package com.keycraft.repository;

import com.keycraft.model.ServiceBooking;
import com.keycraft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceBookingRepository extends JpaRepository<ServiceBooking, Long> {
    
    List<ServiceBooking> findByUser(User user);
    
    List<ServiceBooking> findByStatus(String status);
    
    List<ServiceBooking> findByServiceType(ServiceBooking.ServiceType serviceType);
    
    List<ServiceBooking> findByOrderByCreatedAtDesc();
}
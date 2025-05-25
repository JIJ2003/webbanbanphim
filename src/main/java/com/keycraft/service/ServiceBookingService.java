package com.keycraft.service;

import com.keycraft.model.ServiceBooking;
import com.keycraft.repository.ServiceBookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceBookingService {

    @Autowired
    private ServiceBookingRepository serviceBookingRepository;

    public List<ServiceBooking> getAllServiceBookings() {
        return serviceBookingRepository.findByOrderByCreatedAtDesc();
    }

    public Optional<ServiceBooking> getServiceBookingById(Long id) {
        return serviceBookingRepository.findById(id);
    }

    public ServiceBooking saveServiceBooking(ServiceBooking serviceBooking) {
        return serviceBookingRepository.save(serviceBooking);
    }

    public ServiceBooking updateStatus(Long id, String status) {
        return serviceBookingRepository.findById(id)
                .map(booking -> {
                    booking.setStatus(status);
                    return serviceBookingRepository.save(booking);
                })
                .orElseThrow(() -> new RuntimeException("Service booking not found with id: " + id));
    }

    public void deleteServiceBooking(Long id) {
        if (serviceBookingRepository.existsById(id)) {
            serviceBookingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Service booking not found with id: " + id);
        }
    }
}
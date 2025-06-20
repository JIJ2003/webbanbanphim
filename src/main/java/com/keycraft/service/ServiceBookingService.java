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
    private ServiceBookingRepository repo;

    public List<ServiceBooking> findAll() {
        return repo.findAllByOrderByCreatedAtDesc();
    }

    public Optional<ServiceBooking> findById(Long id) {
        return repo.findById(id);
    }

    public ServiceBooking save(ServiceBooking b) {
        return repo.save(b);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }


}

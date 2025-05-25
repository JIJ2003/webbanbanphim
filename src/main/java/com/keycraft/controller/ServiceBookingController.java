package com.keycraft.controller;

import com.keycraft.model.ServiceBooking;
import com.keycraft.service.ServiceBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceBookingController {

    @Autowired
    private ServiceBookingService serviceBookingService;

    @GetMapping
    public ResponseEntity<List<ServiceBooking>> getAllServiceBookings() {
        List<ServiceBooking> bookings = serviceBookingService.getAllServiceBookings();
        return ResponseEntity.ok(bookings);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceBooking> getServiceBookingById(@PathVariable Long id) {
        Optional<ServiceBooking> booking = serviceBookingService.getServiceBookingById(id);
        return booking.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceBooking> createServiceBooking(@Valid @RequestBody ServiceBooking serviceBooking) {
        ServiceBooking savedBooking = serviceBookingService.saveServiceBooking(serviceBooking);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBooking);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ServiceBooking> updateServiceBookingStatus(
            @PathVariable Long id, 
            @RequestBody StatusUpdateRequest request) {
        try {
            ServiceBooking updatedBooking = serviceBookingService.updateStatus(id, request.getStatus());
            return ResponseEntity.ok(updatedBooking);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceBooking(@PathVariable Long id) {
        try {
            serviceBookingService.deleteServiceBooking(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Inner class for status update requests
    public static class StatusUpdateRequest {
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
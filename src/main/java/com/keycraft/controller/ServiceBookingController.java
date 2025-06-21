package com.keycraft.controller;

import com.keycraft.model.ServiceBooking;
import com.keycraft.service.ServiceBookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/services")
@CrossOrigin(origins = "*")
public class ServiceBookingController {

    @Autowired
    private ServiceBookingService svc;

    @GetMapping
    public List<ServiceBooking> all() {
        return svc.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceBooking> one(@PathVariable Long id) {
        return svc.findById(id)
                  .map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ServiceBooking> create(@Valid @RequestBody ServiceBooking booking) {
        ServiceBooking saved = svc.save(booking);
        return ResponseEntity.status(201).body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceBooking> update(
        @PathVariable Long id,
        @Valid @RequestBody ServiceBooking booking
    ) {
        return svc.findById(id).map(existing -> {
            booking.setId(id);
            ServiceBooking updated = svc.save(booking);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}

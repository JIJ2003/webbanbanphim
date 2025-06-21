package com.keycraft.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "service_bookings")
public class ServiceBooking {

    public enum ServiceType {
        CUSTOM_BUILD, CLEANING, REPAIR
    }

    public enum ServiceStatus {
        PENDING, IN_PROGRESS, CONFIRMED, COMPLETED, CANCELLED
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ServiceStatus status = ServiceStatus.PENDING;

    @Column(name = "estimated_price", precision = 10, scale = 2)
    private BigDecimal estimatedPrice;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = updatedAt = LocalDateTime.now();
    }

    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    

    
    public ServiceType getServiceType() {
        return serviceType;
    }
    
    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    

    

	public String getCustomerEmail() {
		return customerEmail;
	}


	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}


	public String getCustomerPhone() {
		return customerPhone;
	}


	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}


	public ServiceStatus getStatus() {
		return status;
	}


	public void setStatus(ServiceStatus status) {
		this.status = status;
	}


	public BigDecimal getEstimatedPrice() {
        return estimatedPrice;
    }
    
    public void setEstimatedPrice(BigDecimal estimatedPrice) {
        this.estimatedPrice = estimatedPrice;
    }
  
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
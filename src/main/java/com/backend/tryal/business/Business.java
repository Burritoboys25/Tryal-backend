package com.backend.tryal.business;

import com.backend.tryal.experience.Experience;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "businesses")
public class Business {
    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "business_id")
    private UUID businessId;

    @Column(name = "stripe_account_id")
    private String stripeAccountId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "website")
    private String website;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public Business() {
    }

    public Business(UUID businessId, String stripeAccountId, String name, String email, String passwordHash, String website, String address, String phoneNumber, List<Experience> experiences) {
        this.businessId = businessId;
        this.stripeAccountId = stripeAccountId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.website = website;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.experiences = experiences;
    }

    public UUID getBusinessId() {
        return businessId;
    }

    public void setBusinessId(UUID businessId) {
        this.businessId = businessId;
    }

    public String getStripeAccountId() {
        return stripeAccountId;
    }

    public void setStripeAccountId(String stripeAccountId) {
        this.stripeAccountId = stripeAccountId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public List<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(List<Experience> experiences) {
        this.experiences = experiences;
    }
}

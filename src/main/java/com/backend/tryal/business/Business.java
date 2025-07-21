package com.backend.tryal.business;

import com.backend.tryal.experience.Experience;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
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

    @JsonManagedReference
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Experience> experiences = new ArrayList<>();

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(name = "longitude")
    private Double longitude;

    @Column(name = "latitude")
    private Double latitude;

    public Business() {
    }

    public Business(UUID businessId, String stripeAccountId, String name, String email, String passwordHash, String website, String address, String phoneNumber, List<Experience> experiences, Double longitude, Double latitude) {
        this.businessId = businessId;
        this.stripeAccountId = stripeAccountId;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.website = website;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.experiences = experiences;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}

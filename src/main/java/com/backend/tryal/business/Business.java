package com.backend.tryal.business;

import com.backend.tryal.experience.Experience;
import com.backend.tryal.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.*;

@Data
@Entity
@Table(name = "businesses")
public class Business {
    public enum OnboardingStatus {
        INVITED,               // Business has been vetted and sent a one-time signup link
        ACCOUNT_CREATED,       // Business has set a password and activated account
        BUSINESS_INFO_COMPLETED,  // Filled out "About your business" form
        EXPERIENCES_ADDED,        // Added experiences/workshops
        SUBMITTED_FOR_REVIEW,     // Finished onboarding and submitted for review
        APPROVED,                 // Admin approved the business
        REJECTED                  // (Optional) Admin rejected the business for some reason
    }

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

    @ManyToMany(mappedBy = "businesses")
    @JsonBackReference
    private Set<User> users = new HashSet<>();

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

    @Enumerated(EnumType.STRING)
    private OnboardingStatus onboardingStatus = OnboardingStatus.INVITED;

    public Business() {
    }

    public Business(UUID businessId, String stripeAccountId, String name, String email, String passwordHash, String website, String address, String phoneNumber, List<Experience> experiences, Double longitude, Double latitude, User user, OnboardingStatus onboardingStatus) {
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
        this.users = Collections.singleton(user);
        this.onboardingStatus = onboardingStatus != null ? onboardingStatus : OnboardingStatus.INVITED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Business)) return false;
        Business b = (Business) o;
        return businessId != null && businessId.equals(b.businessId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessId);
    }
}

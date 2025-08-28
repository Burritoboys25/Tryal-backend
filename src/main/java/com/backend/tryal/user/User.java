package com.backend.tryal.user;

import com.backend.tryal.business.Business;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "users")
public class User {

    public enum Gender {
        male,
        female,
        other;

        @JsonCreator
        public static Gender fromValue(String value) {
            for (Gender gender : Gender.values()) {
                if (gender.name().equalsIgnoreCase(value)) {
                    return gender;
                }
            }
            throw new IllegalArgumentException("Invalid gender: " + value);
        }

        @JsonValue
        public String toValue() {
            return this.name();
        }
    }

    @Id
    @UuidGenerator(style = UuidGenerator.Style.RANDOM)
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "first_name", nullable = true)
    private String firstName;

    @Column(name = "last_name", nullable = true)
    private String lastName;

    @Column(name = "email", unique = true, nullable = true)
    private String email;

    @Column(name = "phone_number", unique = true, nullable = true)
    private String phoneNumber;

    @Column(name = "password_hash", nullable = true)
    private String passwordHash;

    @Column(name = "date_of_birth", nullable = true)
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = true)
    private Gender gender;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(name = "credit_balance")
    private Integer creditBalance;

    @Column(name = "stripe_customer_id")
    private String stripeCustomerId;

    @ManyToMany
    @JoinTable(
            name = "user_bookmarks", // name of bridge table
            joinColumns = @JoinColumn(name = "user_id"), // FK from users
            inverseJoinColumns = @JoinColumn(name = "business_id") // FK from businesses
    )
    @JsonManagedReference
    @JsonIgnore
    private Set<Business> businesses = new HashSet<>();

    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public User() {
    }

    public User(String firstName, String lastName, String email, String phoneNumber, String passwordHash, LocalDate dateOfBirth, Gender gender, String profileImageUrl, Integer creditBalance, String stripeCustomerId, Business business) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.passwordHash = passwordHash;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.profileImageUrl = profileImageUrl;
        this.creditBalance = creditBalance;
        this.stripeCustomerId = stripeCustomerId;
        this.businesses = Collections.singleton(business);
    }
}

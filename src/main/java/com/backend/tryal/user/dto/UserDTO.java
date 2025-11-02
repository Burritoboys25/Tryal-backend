package com.backend.tryal.user.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String profileImageUrl;
    private Integer creditBalance;
    private String stripeCustomerId;
    private Integer createdAtYear;
}

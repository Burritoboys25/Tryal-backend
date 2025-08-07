package com.backend.tryal.user.mapper;

import com.backend.tryal.business.Business;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.user.dto.UserProfileBookmarkDTO;
import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.User;
import com.backend.tryal.user.dto.UserDTO;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserMapper {
    public static UserDTO mapUserDTO(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setDateOfBirth(user.getDateOfBirth());
        userDTO.setGender(String.valueOf(user.getGender()));
        userDTO.setProfileImageUrl(user.getProfileImageUrl());
        userDTO.setCreditBalance(user.getCreditBalance());
        userDTO.setStripeCustomerId(user.getStripeCustomerId());

        return userDTO;
    }

    public static User mapSignupDTOToUser(UserSignupDTO signupDto) {
        User user = new User();

        user.setFirstName(signupDto.getFirstName());
        user.setLastName(signupDto.getLastName());
        user.setEmail(signupDto.getEmail());

        return user;
    }

    public static UserProfileBookmarkDTO mapToUserProfileBookmarkDTO(UUID userId, Business business, List<Experience> experiences) {
        UserProfileBookmarkDTO dto = new UserProfileBookmarkDTO();
        dto.setUserId(userId);
        dto.setBusinessId(business.getBusinessId());
        dto.setBusinessName(business.getName());

        List<Integer> creditValues = experiences.stream()
                .map(Experience::getCreditPrice)
                .toList();
        dto.setMinCredits(Collections.min(creditValues));
        dto.setMaxCredits(Collections.max(creditValues));

        return dto;
    }
}

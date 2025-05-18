package com.backend.h2ak.user.mapper;

import com.backend.h2ak.user.User;
import com.backend.h2ak.user.dto.UserDTO;

public class UserMapper {
    public static UserDTO mapUserDto(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setUserId(user.getUserId());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setPasswordHash(user.getPasswordHash());
        userDTO.setDateOfBirth(user.getDataOfBirth());
        userDTO.setGender(String.valueOf(user.getGender()));
        userDTO.setProfileImageUrl(user.getProfileImageUrl());
        userDTO.setCreditBalance(user.getCreditBalance());
        userDTO.setStripeCustomerId(user.getStripeCustomerId());

        return userDTO;
    }
}

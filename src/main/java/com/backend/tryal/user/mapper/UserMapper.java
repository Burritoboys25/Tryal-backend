package com.backend.tryal.user.mapper;

import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.User;
import com.backend.tryal.user.dto.UserDTO;

public class UserMapper {
    public static UserDTO mapUserDto(User user) {
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

    public static User mapSignupDtoToUser(UserSignupDTO signupDto) {
        User user = new User();

        user.setFirstName(signupDto.getFirstName());
        user.setLastName(signupDto.getLastName());
        user.setEmail(signupDto.getEmail());

        return user;
    }
}

package com.backend.tryal.user.service;

import com.backend.tryal.user.User;
import com.backend.tryal.user.dto.UserSignupDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(UUID userId);
    User createUser(UserSignupDTO signupDTO) throws IllegalArgumentException;
    User updateUserById(UUID userId, User user);
    boolean deleteUserById(UUID userId);
}

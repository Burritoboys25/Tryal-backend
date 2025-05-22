package com.backend.h2ak.user.service;

import com.backend.h2ak.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(UUID userId);
    User createUser(User user);
    User updateUserById(UUID userId, User user);
    boolean deleteUserById(UUID userId);
}

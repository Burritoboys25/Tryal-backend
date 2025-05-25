package com.backend.tryal.user.service;

import com.backend.tryal.user.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(UUID userId);
    User createUser(User user);
    User updateUserById(UUID userId, User user);
    boolean deleteUserById(UUID userId);
}

package com.backend.tryal.user.service;

import com.backend.tryal.security.dto.RefreshTokenRequest;
import com.backend.tryal.security.dto.TokenPair;
import com.backend.tryal.user.User;
import com.backend.tryal.user.dto.UserBookmarkRequestDTO;
import com.backend.tryal.user.dto.UserLoginDTO;
import com.backend.tryal.user.dto.UserProfileBookmarkDTO;
import com.backend.tryal.user.dto.UserSignupDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {
    List<User> getAllUsers();
    User getUserById(UUID userId);
    User createUser(UserSignupDTO signupDTO) throws IllegalArgumentException;
    TokenPair loginUser(UserLoginDTO loginDTO);
    TokenPair refreshToken(RefreshTokenRequest refreshTokenRequest);
    User updateUserById(UUID userId, User user);
    void deleteUserById(UUID userId);

    void addUserBookmark(UserBookmarkRequestDTO bookmarkRequestDTO);

    void removeUserBookmark(UUID userId, UUID businessId);

    List<UserProfileBookmarkDTO> getAllUserBookmarksByUserId(UUID userId);
}

package com.backend.tryal.user.service;

import com.backend.tryal.security.dto.RefreshTokenRequestDTO;
import com.backend.tryal.security.dto.TokenPairDTO;
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
    TokenPairDTO loginUser(UserLoginDTO loginDTO);
    TokenPairDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO);
    User updateUserById(UUID userId, User user);
    void deleteUserById(UUID userId);

    void addUserBookmark(UserBookmarkRequestDTO bookmarkRequestDTO);

    void removeUserBookmark(UUID userId, UUID businessId);

    List<UserProfileBookmarkDTO> getAllUserBookmarksByUserId(UUID userId);
}

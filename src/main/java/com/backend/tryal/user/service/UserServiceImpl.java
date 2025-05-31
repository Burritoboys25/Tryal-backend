package com.backend.tryal.user.service;

import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.mapper.UserMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(UUID userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User createUser(UserSignupDTO signupDTO) throws IllegalArgumentException{
        if (userRepository.existsByEmail(signupDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        User user = UserMapper.mapSignupDTOToUser(signupDTO);
        String encodedPassword = this.passwordEncoder.encode(signupDTO.getPassword());
        user.setPasswordHash(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public User updateUserById(UUID userId, User user) {
        if (getUserById(userId) != null) {
            User updatedUser = getUserById(userId);

            if (user.getFirstName() != null) {
                updatedUser.setFirstName(user.getFirstName());
            }

            if (user.getLastName() != null) {
                updatedUser.setLastName(user.getLastName());
            }

            if (user.getEmail() != null) {
                updatedUser.setEmail(user.getEmail());
            }

            if (user.getPhoneNumber() != null) {
                updatedUser.setPhoneNumber(user.getPhoneNumber());
            }

            if (user.getPasswordHash() != null) {
                updatedUser.setPasswordHash(user.getPasswordHash());
            }

            if (user.getDateOfBirth() != null) {
                updatedUser.setDateOfBirth(user.getDateOfBirth());
            }

            if (user.getGender() != null) {
                updatedUser.setGender(user.getGender());
            }

            if (user.getProfileImageUrl() != null) {
                updatedUser.setProfileImageUrl(user.getProfileImageUrl());
            }

            if (user.getCreditBalance() != null) {
                updatedUser.setCreditBalance(user.getCreditBalance());
            }

            if (user.getStripeCustomerId() != null) {
                updatedUser.setStripeCustomerId(user.getStripeCustomerId());
            }

            userRepository.save(updatedUser);
            return updatedUser;
        }

        return null;
    }

    @Override
    public boolean deleteUserById(UUID userId) {
        if (getUserById(userId) != null) {
            userRepository.deleteById(userId);
            return true;
        }

        return false;
    }
}

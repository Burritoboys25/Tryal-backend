package com.backend.tryal.user.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.security.dto.RefreshTokenRequest;
import com.backend.tryal.security.dto.TokenPair;
import com.backend.tryal.security.service.JwtService;
import com.backend.tryal.shared.utils.ExceptionUtil;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.backend.tryal.user.dto.UserBookmarkRequestDTO;
import com.backend.tryal.user.dto.UserLoginDTO;
import com.backend.tryal.user.dto.UserProfileBookmarkDTO;
import com.backend.tryal.user.dto.UserSignupDTO;
import com.backend.tryal.user.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    @Qualifier("customUserDetailsService")
    UserDetailsService userDetailsService;

    @Autowired
    BusinessRepository businessRepository;

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
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new EntityNotFoundException("Could not find user with id: " + userId);
        }
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public User createUser(UserSignupDTO signupDTO) throws IllegalArgumentException{
        if (userRepository.existsByEmail(signupDTO.getEmail()) || businessRepository.existsByEmail(signupDTO.getEmail())) {
            throw new IllegalArgumentException("Email is already taken.");
        }

        User user = UserMapper.mapSignupDTOToUser(signupDTO);
        String encodedPassword = this.passwordEncoder.encode(signupDTO.getPassword());
        user.setPasswordHash(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public TokenPair loginUser(UserLoginDTO loginDTO) {
        // Authenticate user
        Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));

        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate Token Pair
        return jwtService.generateTokenPair(authentication);
    }

    public TokenPair refreshToken(@Valid RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();

        // check if still valid refresh token
        if (!jwtService.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException("Invalid refresh token");
        }

        String user = jwtService.extractUsernameFromToken(refreshToken);
        boolean isBusiness = jwtService.isBusinessUser(refreshToken);

        if (isBusiness) {
            throw new IllegalArgumentException("Invalid account type refresh token");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(user);

        if (userDetails == null) {
            throw new IllegalArgumentException("User not found");
        }

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        String accessToken = jwtService.generateAccessToken(authentication);
        return new TokenPair(accessToken, refreshToken);
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

    @Override
    public void addUserBookmark(UserBookmarkRequestDTO bookmarkRequestDTO) {
        UUID userId = bookmarkRequestDTO.getUserId();
        UUID businessId = bookmarkRequestDTO.getBusinessId();

        User user = userRepository.findById(userId).orElse(null);
        Business business = businessRepository.findById(businessId).orElse(null);

        if (user == null || business == null) {
            throw new EntityNotFoundException("User ID or business ID does not exist.");
        }

        user.getBusinesses().add(business);
        userRepository.save(user);
    }

    @Override
    public void removeUserBookmark(UUID userId, UUID businessId) {

        User user = userRepository.findById(userId).orElse(null);
        Business business = businessRepository.findById(businessId).orElse(null);

        if (user == null || business == null) {
            throw new EntityNotFoundException("User ID or business ID does not exist.");
        }

        boolean removed = user.getBusinesses().remove(business); // directly
        if (removed) {
            userRepository.save(user); // this persists join table change
        }
    }

    @Override
    public List<UserProfileBookmarkDTO> getAllUserBookmarksByUserId(UUID userId) {
        ExceptionUtil.validateUUIDOrThrow(userId);

        if (userRepository.findById(userId).orElse(null) == null) {
            throw new EntityNotFoundException("Could not find user with id: " + userId);
        }

        List<UserProfileBookmarkDTO> response = new ArrayList<>();
        List<UserBookmarkRequestDTO> bookmarks = userRepository.getAllUserBookmarksByUserId(userId);

        for (UserBookmarkRequestDTO bookmark: bookmarks) {
            Business business = businessRepository.findById(bookmark.getBusinessId()).orElse(null);
            assert business != null;
            List<Experience> experiences = business.getExperiences();

            UserProfileBookmarkDTO dto = UserMapper.mapToUserProfileBookmarkDTO(userId, business, experiences);
            response.add(dto);
        }

        return response;
    }
}

package com.backend.tryal.user.service;

import com.backend.tryal.business.Business;
import com.backend.tryal.business.BusinessRepository;
import com.backend.tryal.experience.Experience;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.backend.tryal.user.dto.UserBookmarkRequestDTO;
import com.backend.tryal.user.dto.UserProfileBookmarkDTO;
import com.backend.tryal.user.mapper.UserMapper;
import jakarta.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;

  @Autowired
  BusinessRepository businessRepository;

  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
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
    return user;
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
    } else {
      throw new EntityNotFoundException("Could not find user with id: " + userId);
    }
  }

  @Override
  public void deleteUserById(UUID userId) {
    if (getUserById(userId) == null) {
      throw new EntityNotFoundException("Could not find user with id: " + userId);
    }
    userRepository.deleteById(userId);
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
    if (userRepository.findById(userId).orElse(null) == null) {
      throw new EntityNotFoundException("Could not find user with id: " + userId);
    }

    List<UserProfileBookmarkDTO> response = new ArrayList<>();
    List<UserBookmarkRequestDTO> bookmarks = userRepository.getAllUserBookmarksByUserId(userId);

    for (UserBookmarkRequestDTO bookmark : bookmarks) {
      Business business = businessRepository.findById(bookmark.getBusinessId()).orElse(null);
      assert business != null;
      List<Experience> experiences = business.getExperiences();

      UserProfileBookmarkDTO dto = UserMapper.mapToUserProfileBookmarkDTO(userId, business,
          experiences);
      response.add(dto);
    }

    return response;
  }
}

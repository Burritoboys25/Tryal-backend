package com.backend.tryal.subscription.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.SubscriptionRepository;
import com.backend.tryal.subscription.dto.SubscriptionDTO;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final PlanRepository planRepository;

  public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository,
      UserRepository userRepository, PlanRepository planRepository) {
    this.subscriptionRepository = subscriptionRepository;
    this.userRepository = userRepository;
    this.planRepository = planRepository;
  }

  @Override
  public List<Subscription> getAllSubscriptions() {
    return subscriptionRepository.findAll();
  }

  @Override
  public Subscription getSubscriptionById(String subscriptionId) {
    Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
    if (subscription == null) {
      throw new EntityNotFoundException("Subscription not found with id: " + subscriptionId);
    }
    return subscription;
  }

  @Override
  public List<Subscription> getSubscriptionsByUser(UUID userId) {
    if (userRepository.findById(userId).orElse(null) == null) {
      throw new EntityNotFoundException("User not found with id: " + userId);
    }
    return subscriptionRepository.findByUserId(userId);
  }

  @Override
  public List<Subscription> getActiveSubscriptionsByUser(UUID userId) {
    if (userRepository.findById(userId).orElse(null) == null) {
      throw new EntityNotFoundException("User not found with id: " + userId);
    }
    return subscriptionRepository.findByUserIdAndActive(userId);
  }

  @Override
  public Subscription createSubscription(UUID userId, SubscriptionDTO subscriptionRequestDTO) {
    User user = userRepository.findById(userId).orElse(null);
    Plan plan = planRepository.findById(subscriptionRequestDTO.getPlanId()).orElse(null);
    if (user == null) {
      throw new EntityNotFoundException("User not found with id: " + userId);
    }
    if (plan == null) {
      throw new EntityNotFoundException(
          "Plan not found with id: " + subscriptionRequestDTO.getPlanId());
    }
    Subscription subscription = new Subscription();
    subscription.setSubscriptionId(subscriptionRequestDTO.getSubscriptionId());
    subscription.setUser(user);
    subscription.setPlan(plan);
    subscription.setSubscriptionStatus(subscriptionRequestDTO.getSubscriptionStatus());
    subscription.setAutoRenew(subscriptionRequestDTO.getAutoRenew());
    subscription.setStartAt(subscriptionRequestDTO.getStartAt());
    subscription.setEndAt(subscriptionRequestDTO.getEndAt());

    return subscriptionRepository.save(subscription);
  }

  @Override
  public Subscription updateSubscriptionById(String subscriptionId,
      SubscriptionDTO subscriptionRequestDTO) {
    Subscription updatedSubscription = getSubscriptionById(subscriptionId);

    if (updatedSubscription == null) {
      throw new EntityNotFoundException("Subscription not found with id: " + subscriptionId);
    }

    if (subscriptionRequestDTO.getAutoRenew() == null) {
      throw new IllegalArgumentException("Invalid null input for field: Auto Renew");
    } else if (subscriptionRequestDTO.getEndAt() == null) {
      throw new IllegalArgumentException("Invalid null input for field: End At");
    } else if (subscriptionRequestDTO.getSubscriptionStatus() == null) {
      throw new IllegalArgumentException("Invalid null input for field: Subscription Status");
    }

    updatedSubscription.setSubscriptionStatus(subscriptionRequestDTO.getSubscriptionStatus());
    updatedSubscription.setAutoRenew(subscriptionRequestDTO.getAutoRenew());
    updatedSubscription.setEndAt(subscriptionRequestDTO.getEndAt());

    return subscriptionRepository.save(updatedSubscription);
  }
}

package com.backend.tryal.subscription.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.plan.PlanRepository;
import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.SubscriptionRepository;
import com.backend.tryal.subscription.dto.SubscriptionDTO;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserRepository userRepository, PlanRepository planRepository) {
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
        return subscriptionRepository.findById(subscriptionId).orElse(null);
    }

    @Override
    public List<Subscription> getSubscriptionsByUser(UUID userId) {
        return subscriptionRepository.findByUserId(userId);
    }

    @Override
    public List<Subscription> getSubscriptionsByUser(UUID userId, Boolean activeSubscription) {
        return subscriptionRepository.findByUserIdAndActive(userId, activeSubscription);
    }

    @Override
    public Subscription createSubscription(UUID userId, SubscriptionDTO subscriptionRequestDTO) {
        User user = userRepository.findById(userId).orElse(null);
        Plan plan = planRepository.findById(subscriptionRequestDTO.getPlanId()).orElse(null);

        if(user == null){
            return null;
        }else if(plan == null){
            return null;
        }

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setSubscriptionStatus(subscriptionRequestDTO.getSubscriptionStatus());
        subscription.setAutoRenew(subscriptionRequestDTO.getAutoRenew());
        subscription.setStartAt(subscriptionRequestDTO.getStartAt());
        subscription.setEndAt(subscriptionRequestDTO.getEndAt());

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription updateSubscriptionById(String subscriptionId, SubscriptionDTO subscriptionRequestDTO) {
        if(getSubscriptionById(subscriptionId) == null || subscriptionRequestDTO.getAutoRenew() == null || subscriptionRequestDTO.getEndAt() == null || subscriptionRequestDTO.getSubscriptionStatus() == null){
            return null;
        }

        Subscription updatedSubscription = getSubscriptionById(subscriptionId);

        if(subscriptionRequestDTO.getSubscriptionStatus() != null){
            updatedSubscription.setSubscriptionStatus(subscriptionRequestDTO.getSubscriptionStatus());
        }

        if(subscriptionRequestDTO.getAutoRenew() != null){
            updatedSubscription.setAutoRenew(subscriptionRequestDTO.getAutoRenew());
        }

        if(subscriptionRequestDTO.getEndAt() != null){
            updatedSubscription.setEndAt(subscriptionRequestDTO.getEndAt());
        }

        return subscriptionRepository.save(updatedSubscription);
    }
}

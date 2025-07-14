package com.backend.tryal.subscription.service;

import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.SubscriptionRepository;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<Subscription> getAllSubscriptions() {
        return subscriptionRepository.findAll();
    }

    @Override
    public Subscription getSubscriptionById(UUID subscriptionId) {
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
    public Subscription createSubscription(UUID userId, Subscription subscription) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            return null;
        }

        subscription.setUser(user);

        return subscriptionRepository.save(subscription);
    }

    @Override
    public Subscription updateSubscriptionById(UUID subscriptionId, Subscription subscription) {
        if(getSubscriptionById(subscriptionId) == null || subscription.getAutoRenew() == null || subscription.getEndAt() == null || subscription.getSubscriptionStatus() == null){
            return null;
        }

        Subscription updatedSubscription = getSubscriptionById(subscriptionId);

        if(subscription.getSubscriptionStatus() != null){
            updatedSubscription.setSubscriptionStatus(subscription.getSubscriptionStatus());
        }

        if(subscription.getAutoRenew() != null){
            updatedSubscription.setAutoRenew(subscription.getAutoRenew());
        }

        if(subscription.getEndAt() != null){
            updatedSubscription.setEndAt(subscription.getEndAt());
        }

        return subscriptionRepository.save(updatedSubscription);
    }
}

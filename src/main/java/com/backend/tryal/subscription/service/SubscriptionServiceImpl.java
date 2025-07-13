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
    public List<Subscription> getSubscriptionsByUser(UUID userId, Boolean activeSubscription) {
        if (activeSubscription == null) {
            return subscriptionRepository.findByUserId(userId);
        }

        return subscriptionRepository.findByUserIdAndActive(userId, activeSubscription);
    }

    @Override
    public Subscription createSubscription(UUID userId, Subscription subcriptionRequestDTO) {
        User user = userRepository.findById(userId).orElse(null);

        if(user == null){
            return null;
        }

        //TODO:
        //Subscription subscription = SubscriptionMapper.mapRequestDTOToSubscription(subcriptionRequestDTO, user);

        return subscriptionRepository.save(subcriptionRequestDTO);
    }

    @Override
    public Subscription updateSubscriptionById(UUID subscriptionId, Subscription subcriptionRequestDTO) {
        if(getSubscriptionById(subscriptionId) != null){
            Subscription updatedSubscription = getSubscriptionById(subscriptionId);

            if(subcriptionRequestDTO.getSubscriptionStatus() != null){
                updatedSubscription.setSubscriptionStatus(subcriptionRequestDTO.getSubscriptionStatus());
            }

            if(subcriptionRequestDTO.getAutoRenew() != null){
                updatedSubscription.setAutoRenew(subcriptionRequestDTO.getAutoRenew());
            }

            if(subcriptionRequestDTO.getEndAt() != null){
                updatedSubscription.setEndAt(subcriptionRequestDTO.getEndAt());
            }

            return subscriptionRepository.save(updatedSubscription);
        }

        return null;
    }
}

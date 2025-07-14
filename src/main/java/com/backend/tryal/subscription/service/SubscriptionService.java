package com.backend.tryal.subscription.service;

import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.dto.SubscriptionDTO;

import java.util.List;
import java.util.UUID;

public interface SubscriptionService {

    List<Subscription> getAllSubscriptions();

    Subscription getSubscriptionById(UUID subscriptionId);
    List<Subscription> getSubscriptionsByUser(UUID userId);
    List<Subscription> getSubscriptionsByUser(UUID userId, Boolean activeSubscription);
    Subscription createSubscription(UUID userId, SubscriptionDTO subscriptionRequestDTO);
    Subscription updateSubscriptionById(UUID subscriptionId, SubscriptionDTO subscriptionRequestDTO);
}

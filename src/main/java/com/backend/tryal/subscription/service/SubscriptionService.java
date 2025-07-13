package com.backend.tryal.subscription.service;

import com.backend.tryal.subscription.Subscription;
import org.springframework.web.bind.annotation.PathVariable;


import java.util.List;
import java.util.UUID;

public interface SubscriptionService {

    List<Subscription> getAllSubscriptions();

    Subscription getSubscriptionById(UUID subscriptionId);
    List<Subscription> getSubscriptionsByUser(UUID userId, Boolean activeSubscription);
    Subscription createSubscription(UUID userId, Subscription subcriptionRequestDTO);
    Subscription updateSubscriptionById(UUID subscriptionId, Subscription subcriptionRequestDTO);
}

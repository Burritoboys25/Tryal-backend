package com.backend.tryal.subscription.mapper;

import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.dto.SubscriptionDTO;

import java.util.UUID;

public class SubscriptionMapper {
    public static SubscriptionDTO mapSubscriptionDTO(UUID userId, Subscription subscription) {
        SubscriptionDTO subscriptionDTO = new SubscriptionDTO();

        if (subscription.getPlan() != null) {
            subscriptionDTO.setPlanId(subscription.getPlan().getPlanId());
        }

        subscriptionDTO.setUserId(userId);
        subscriptionDTO.setStripeSubscriptionId(subscription.getStripeSubscriptionId());
        subscriptionDTO.setSubscriptionId(subscription.getSubscriptionId());
        subscriptionDTO.setSubscriptionStatus(subscription.getSubscriptionStatus());
        subscriptionDTO.setAutoRenew(subscription.getAutoRenew());
        subscriptionDTO.setStartAt(subscription.getStartAt());
        subscriptionDTO.setEndAt(subscription.getEndAt());

        return subscriptionDTO;
    }
}

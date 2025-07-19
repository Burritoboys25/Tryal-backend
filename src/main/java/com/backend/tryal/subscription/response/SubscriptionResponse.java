package com.backend.tryal.subscription.response;

import com.backend.tryal.subscription.dto.SubscriptionDTO;

public class SubscriptionResponse {
    private SubscriptionDTO subscriptionDTO;
    private String message;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(SubscriptionDTO subscriptionDTO, String message){
        this.subscriptionDTO = subscriptionDTO;
        this.message = message;
    }

    public SubscriptionDTO getSubscriptionDTO() {
        return subscriptionDTO;
    }

    public void setSubscriptionDTO(SubscriptionDTO subscriptionDTO) {
        this.subscriptionDTO = subscriptionDTO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

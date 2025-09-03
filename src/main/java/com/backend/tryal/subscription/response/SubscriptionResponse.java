package com.backend.tryal.subscription.response;

import com.backend.tryal.subscription.dto.SubscriptionDTO;
import lombok.Data;

@Data
public class SubscriptionResponse {
    private SubscriptionDTO subscriptionDTO;
    private String message;

    public SubscriptionResponse() {
    }

    public SubscriptionResponse(SubscriptionDTO subscriptionDTO, String message){
        this.subscriptionDTO = subscriptionDTO;
        this.message = message;
    }

}

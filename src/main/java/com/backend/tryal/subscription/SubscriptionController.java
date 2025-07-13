package com.backend.tryal.subscription;

import com.backend.tryal.subscription.service.SubscriptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService){
        this.subscriptionService = subscriptionService;
    }

    //get all subscription
    @GetMapping()
    public ResponseEntity<List<Subscription>> getAllSubscriptions(){
        return null;
    }

    //get subscription by id
    @GetMapping("/{subscriptionId}")
    public ResponseEntity<Subscription> getSubscriptionById(@PathVariable UUID subscriptionId){
        return null;
    }

    //get all user's subscription
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<Subscription>> getSubscriptionsByUser(@PathVariable UUID userId, @RequestParam(required = false) Boolean active){
        return null;
    }

    //create a user subscription
    @PostMapping("/users/{userId}")
    public ResponseEntity<Subscription> createSubscription(@PathVariable UUID userId, @RequestBody Subscription subcriptionRequestDTO) {
        return null;
    }

    //patch subscription
    @PatchMapping("/{subscriptionId}")
    public ResponseEntity<Subscription> updateSubscriptionById(@PathVariable UUID subscriptionId, @RequestBody Subscription subcriptionRequestDTO) {
        return null;
    }
}

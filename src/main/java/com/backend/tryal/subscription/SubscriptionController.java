package com.backend.tryal.subscription;

import com.backend.tryal.subscription.dto.SubscriptionDTO;
import com.backend.tryal.subscription.mapper.SubscriptionMapper;
import com.backend.tryal.subscription.service.SubscriptionService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  public SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  //get all subscription
  @GetMapping()
  public List<SubscriptionDTO> getAllSubscriptions() {
    return subscriptionService
        .getAllSubscriptions()
        .stream()
        .map(sub -> SubscriptionMapper.mapSubscriptionDTO(
            sub.getUser().getUserId(),
            sub))
        .collect(Collectors.toList());
  }

  //get subscription by id
  @GetMapping("/{subscriptionId}")
  public SubscriptionDTO getSubscriptionById(@PathVariable String subscriptionId) {
    Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);
    return SubscriptionMapper.mapSubscriptionDTO(subscription.getUser().getUserId(), subscription);
  }

  //get all user's subscription
  @GetMapping("/user/{userId}")
  public List<SubscriptionDTO> getSubscriptionsByUser(@PathVariable UUID userId,
      @RequestParam(required = false) Boolean active) {
    List<SubscriptionDTO> subscriptions = new ArrayList<>();

    if (active != null && active) {
      subscriptions = subscriptionService
          .getActiveSubscriptionsByUser(userId)
          .stream()
          .map(sub -> SubscriptionMapper.mapSubscriptionDTO(
              sub.getUser().getUserId(),
              sub))
          .collect(Collectors.toList());
    } else {
      subscriptions = subscriptionService
          .getSubscriptionsByUser(userId)
          .stream()
          .map(sub -> SubscriptionMapper.mapSubscriptionDTO(
              sub.getUser().getUserId(),
              sub))
          .collect(Collectors.toList());
    }
    return subscriptions;
  }

  //create a user subscription
  @PostMapping("/user/{userId}")
  public SubscriptionDTO createSubscription(@PathVariable UUID userId,
      @RequestBody SubscriptionDTO subscriptionRequestDTO) {
    return SubscriptionMapper
        .mapSubscriptionDTO(userId,
            subscriptionService.createSubscription(userId, subscriptionRequestDTO));
  }

  //patch subscription
  @PatchMapping("/{subscriptionId}")
  public SubscriptionDTO updateSubscriptionById(
      @PathVariable String subscriptionId, @RequestBody SubscriptionDTO subscriptionRequestDTO) {
    Subscription updatedSubscription = subscriptionService.updateSubscriptionById(subscriptionId,
        subscriptionRequestDTO);
    return SubscriptionMapper.mapSubscriptionDTO(
        updatedSubscription.getUser().getUserId(), updatedSubscription);
  }
}

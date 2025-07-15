package com.backend.tryal.subscription;

import com.backend.tryal.subscription.dto.SubscriptionDTO;
import com.backend.tryal.subscription.mapper.SubscriptionMapper;
import com.backend.tryal.subscription.response.SubscriptionResponse;
import com.backend.tryal.subscription.service.SubscriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    public SubscriptionController(SubscriptionService subscriptionService){
        this.subscriptionService = subscriptionService;
    }

    //get all subscription
    @GetMapping()
    public ResponseEntity<List<SubscriptionDTO>> getAllSubscriptions(){
        try{
            List<SubscriptionDTO> subscriptions = subscriptionService
                    .getAllSubscriptions()
                    .stream()
                    .map(sub -> SubscriptionMapper.mapSubscriptionDTO(
                            sub.getUser().getUserId(),
                            sub))
                    .collect(Collectors.toList());

            if(subscriptions.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(subscriptions, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get subscription by id
    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscriptionById(@PathVariable UUID subscriptionId){
        try{
            Subscription subscription = subscriptionService.getSubscriptionById(subscriptionId);

            if(subscription == null){
                return new ResponseEntity<>(new SubscriptionResponse(null, "Subscription not found."),HttpStatus.NOT_FOUND);
            }

            SubscriptionDTO subscriptionDTO = SubscriptionMapper.mapSubscriptionDTO(subscription.getUser().getUserId(), subscription);

            return new ResponseEntity<>(new SubscriptionResponse(subscriptionDTO, "Subscription found."), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get all user's subscription
    @GetMapping("/users/{userId}")
    public ResponseEntity<List<SubscriptionDTO>> getSubscriptionsByUser(@PathVariable UUID userId, @RequestParam(required = false) Boolean active){
        try{
            List<SubscriptionDTO> subscriptions = new ArrayList<>();

            if(active != null){
                subscriptions = subscriptionService
                        .getSubscriptionsByUser(userId, active)
                        .stream()
                        .map(sub -> SubscriptionMapper.mapSubscriptionDTO(
                                sub.getUser().getUserId(),
                                sub))
                        .collect(Collectors.toList());
            }else{
                subscriptions = subscriptionService
                        .getSubscriptionsByUser(userId)
                        .stream()
                        .map(sub -> SubscriptionMapper.mapSubscriptionDTO(
                                sub.getUser().getUserId(),
                                sub))
                        .collect(Collectors.toList());
            }

            return new ResponseEntity<>(subscriptions, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //create a user subscription
    @PostMapping("/users/{userId}")
    public ResponseEntity<SubscriptionResponse> createSubscription(@PathVariable UUID userId, @RequestBody SubscriptionDTO subscriptionRequestDTO) {
        try{
            SubscriptionDTO subscriptionDTO = SubscriptionMapper
                    .mapSubscriptionDTO(userId, subscriptionService.createSubscription(userId, subscriptionRequestDTO));

            if(subscriptionDTO == null){
                return new ResponseEntity<>(new SubscriptionResponse(null, "User or plan not found."), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new SubscriptionResponse(subscriptionDTO, "Subscription created successfully."), HttpStatus.CREATED);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //patch subscription
    @PatchMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> updateSubscriptionById(@PathVariable UUID subscriptionId, @RequestBody SubscriptionDTO subscriptionRequestDTO) {
        try{
            Subscription updatedSubscription = subscriptionService.updateSubscriptionById(subscriptionId, subscriptionRequestDTO);

            if(updatedSubscription == null){
                return new ResponseEntity<>(new SubscriptionResponse(null, "Subscription not found or the update request was invalid."), HttpStatus.NOT_FOUND);
            }

            SubscriptionDTO subscriptionDTO = SubscriptionMapper.mapSubscriptionDTO(updatedSubscription.getUser().getUserId(), updatedSubscription);

            return new ResponseEntity<>(new SubscriptionResponse(subscriptionDTO, "Subscription updated successfully.."), HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

package com.backend.tryal.payment.service;

import com.backend.tryal.plan.Plan;
import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.SubscriptionRepository;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import com.stripe.model.Invoice;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService{
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public PaymentServiceImpl(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public boolean handleInvoicePaid(Invoice invoice) {
        //TODO: instead of returning boolean, return custom response
        System.out.println("INVOICE INVOICE INVOICE");
        System.out.println(invoice.getParent().getSubscriptionDetails().getSubscription());
        String subscriptionId = invoice.getParent().getSubscriptionDetails().getSubscription();

        if(subscriptionId == null){
            return false;
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);

        if(subscription == null){
            //TODO: refund?
            return false;
        }

        switch (subscription.getSubscriptionStatus()){
            case PENDING:
                break;
            case INCOMPLETE:
                break;
            case INCOMPLETE_EXPIRED:
                break;
            case PAST_DUE:
                break;
            case UNPAID:
                break;
            case CANCELLED:
                break;
            case PAUSED:
                break;
            case ACTIVE:
                User user = subscription.getUser();
                Plan plan = subscription.getPlan();

                if(user == null){
                    //TODO: do something
                    return false;
                }else if(plan == null){
                    //TODO: do something
                    return false;
                }

                int creditsToAdd = plan.getMonthlyCredits();
                int currentCredits = user.getCreditBalance();

                user.setCreditBalance(currentCredits + creditsToAdd);
                userRepository.save(user);

                return true;
            default:
                break;
        }

        return true;
    }
}

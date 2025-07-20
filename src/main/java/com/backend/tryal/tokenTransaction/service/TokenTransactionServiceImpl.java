package com.backend.tryal.tokenTransaction.service;

import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.SubscriptionRepository;
import com.backend.tryal.tokenTransaction.TokenTransaction;
import com.backend.tryal.tokenTransaction.TokenTransactionRepository;
import com.backend.tryal.tokenTransaction.dto.TokenTransactionDTO;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TokenTransactionServiceImpl implements TokenTransactionService{
    private final TokenTransactionRepository tokenTransactionRepository;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public TokenTransactionServiceImpl(TokenTransactionRepository tokenTransactionRepository, UserRepository userRepository, SubscriptionRepository subscriptionRepository){
        this.tokenTransactionRepository = tokenTransactionRepository;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<TokenTransaction> getAllTokenTransactions() {
        return tokenTransactionRepository.findAll();
    }

    @Override
    public List<TokenTransaction> getAllTokenTransactions(UUID userId) {
        return tokenTransactionRepository.findByUserId(userId);
    }

    @Override
    public TokenTransaction getTokenTransactionById(UUID transactionId) {
        return tokenTransactionRepository.findById(transactionId).orElse(null);
    }

    @Override
    public TokenTransaction createTokenTransaction(UUID userId, TokenTransactionDTO tokenTransactionDTO) {
        User user = userRepository.findById(userId).orElse(null);
        //TODO:
        //Booking booking = bookingRepository.findById(tokenTransactionDTO.getBookingId()).orElse(null);
        Subscription subscription = subscriptionRepository.findById(tokenTransactionDTO.getSubscriptionId()).orElse(null);

        if(user == null){
            return null;
        }else if(subscription == null){
            return null;
        }

        TokenTransaction transaction = new TokenTransaction();
        transaction.setUser(user);
        transaction.setTransactionReason(tokenTransactionDTO.getTransactionReason());
        transaction.setCreditAmount(tokenTransactionDTO.getCreditAmount());
        transaction.setSubscription(subscription);

        return tokenTransactionRepository.save(transaction);
    }
}

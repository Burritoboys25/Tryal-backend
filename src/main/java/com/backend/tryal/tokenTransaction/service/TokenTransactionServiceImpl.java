package com.backend.tryal.tokenTransaction.service;

import com.backend.tryal.booking.Booking;
import com.backend.tryal.booking.BookingRepository;
import com.backend.tryal.subscription.Subscription;
import com.backend.tryal.subscription.SubscriptionRepository;
import com.backend.tryal.tokenTransaction.TokenTransaction;
import com.backend.tryal.tokenTransaction.TokenTransactionRepository;
import com.backend.tryal.tokenTransaction.dto.TokenTransactionDTO;
import com.backend.tryal.user.User;
import com.backend.tryal.user.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class TokenTransactionServiceImpl implements TokenTransactionService {

  private final TokenTransactionRepository tokenTransactionRepository;
  private final UserRepository userRepository;
  private final SubscriptionRepository subscriptionRepository;
  private final BookingRepository bookingRepository;

  public TokenTransactionServiceImpl(TokenTransactionRepository tokenTransactionRepository,
      UserRepository userRepository, SubscriptionRepository subscriptionRepository,
      BookingRepository bookingRepository) {
    this.tokenTransactionRepository = tokenTransactionRepository;
    this.userRepository = userRepository;
    this.subscriptionRepository = subscriptionRepository;
    this.bookingRepository = bookingRepository;
  }

  @Override
  public List<TokenTransaction> getAllTokenTransactions() {
    return tokenTransactionRepository.findAll();
  }

  @Override
  public List<TokenTransaction> getAllTokenTransactions(UUID userId) {
    if (userRepository.findById(userId).orElse(null) == null) {
      throw new EntityNotFoundException("Could not find user with id: " + userId);
    }

    return tokenTransactionRepository.findByUserId(userId);
  }

  @Override
  public TokenTransaction getTokenTransactionById(UUID transactionId) {
    if (tokenTransactionRepository.findById(transactionId).orElse(null) == null) {
      throw new EntityNotFoundException("Could not find token with id: " + transactionId);
    }

    return tokenTransactionRepository.findById(transactionId).orElse(null);
  }

  @Override
  public TokenTransaction createTokenTransaction(UUID userId,
      TokenTransactionDTO tokenTransactionDTO) {
    User user = userRepository.findById(userId).orElse(null);
    if (user == null) {
      throw new EntityNotFoundException("Could not find user with id: " + userId);
    }

    TokenTransaction transaction = new TokenTransaction();

    Booking booking = null;
    if (tokenTransactionDTO.getBookingId() != null) {
      booking = bookingRepository.findById(tokenTransactionDTO.getBookingId()).orElse(null);
      if (booking == null) {
        throw new EntityNotFoundException("Could not find booking with id: " + userId);
      }
      transaction.setBooking(booking);
    }
    Subscription subscription = null;
    if (tokenTransactionDTO.getSubscriptionId() != null) {
      subscription = subscriptionRepository.findById(
          tokenTransactionDTO.getSubscriptionId()).orElse(null);
      if (subscription == null) {
        throw new EntityNotFoundException("Could not find subscription with id: " + userId);
      }
      transaction.setSubscription(subscription);
    }

    transaction.setUser(user);
    transaction.setTransactionReason(tokenTransactionDTO.getTransactionReason());
    transaction.setCreditAmount(tokenTransactionDTO.getCreditAmount());

    return tokenTransactionRepository.save(transaction);
  }
}

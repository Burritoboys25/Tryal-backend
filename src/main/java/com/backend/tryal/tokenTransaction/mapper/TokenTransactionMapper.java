package com.backend.tryal.tokenTransaction.mapper;

import com.backend.tryal.tokenTransaction.TokenTransaction;
import com.backend.tryal.tokenTransaction.dto.TokenTransactionDTO;

import java.util.UUID;

public class TokenTransactionMapper {
    public static TokenTransactionDTO mapTokenTransactionDTO(UUID userId, TokenTransaction tokenTransaction){
        TokenTransactionDTO tokenTransactionDTO = new TokenTransactionDTO();

        tokenTransactionDTO.setUserId(userId);
        tokenTransactionDTO.setCreditAmount(tokenTransaction.getCreditAmount());
        tokenTransactionDTO.setTransactionReason(tokenTransaction.getTransactionReason());

        if(tokenTransaction.getBooking() != null){
            tokenTransactionDTO.setBookingId(tokenTransaction.getBooking().getBookingId());
        }

        if(tokenTransaction.getSubscription() != null){
            tokenTransactionDTO.setSubscriptionId(tokenTransaction.getSubscription().getSubscriptionId());
        }

        return tokenTransactionDTO;
    }
}

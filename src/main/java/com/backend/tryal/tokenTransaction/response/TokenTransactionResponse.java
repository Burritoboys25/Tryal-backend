package com.backend.tryal.tokenTransaction.response;

import com.backend.tryal.tokenTransaction.dto.TokenTransactionDTO;
import lombok.Data;

@Data
public class TokenTransactionResponse {
    private TokenTransactionDTO tokenTransactionDTO;
    private String message;

    public TokenTransactionResponse() {
    }

    public TokenTransactionResponse(TokenTransactionDTO tokenTransactionDTO, String message) {
        this.tokenTransactionDTO = tokenTransactionDTO;
        this.message = message;
    }
}

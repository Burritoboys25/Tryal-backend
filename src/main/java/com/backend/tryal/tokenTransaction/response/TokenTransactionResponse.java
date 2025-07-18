package com.backend.tryal.tokenTransaction.response;

import com.backend.tryal.tokenTransaction.dto.TokenTransactionDTO;

public class TokenTransactionResponse {
    private TokenTransactionDTO tokenTransactionDTO;
    private String message;

    public TokenTransactionResponse() {
    }

    public TokenTransactionResponse(TokenTransactionDTO tokenTransactionDTO, String message) {
        this.tokenTransactionDTO = tokenTransactionDTO;
        this.message = message;
    }

    public TokenTransactionDTO getTokenTransactionDTO() {
        return tokenTransactionDTO;
    }

    public void setTokenTransactionDTO(TokenTransactionDTO tokenTransactionDTO) {
        this.tokenTransactionDTO = tokenTransactionDTO;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

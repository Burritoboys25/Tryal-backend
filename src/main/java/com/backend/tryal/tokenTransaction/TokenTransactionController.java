package com.backend.tryal.tokenTransaction;

import com.backend.tryal.tokenTransaction.dto.TokenTransactionDTO;
import com.backend.tryal.tokenTransaction.mapper.TokenTransactionMapper;
import com.backend.tryal.tokenTransaction.response.TokenTransactionResponse;
import com.backend.tryal.tokenTransaction.service.TokenTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TokenTransactionController {
    private final TokenTransactionService tokenTransactionService;

    public TokenTransactionController(TokenTransactionService tokenTransactionService){
        this.tokenTransactionService = tokenTransactionService;
    }

    //get all transactions (optional parameter by userId)
    @GetMapping()
    public ResponseEntity<List<TokenTransactionDTO>> getAllTokenTransactions(@RequestParam(required = false) UUID userId){
        try{
            List<TokenTransactionDTO> transactions = new ArrayList<>();
            if(userId == null){
                transactions = tokenTransactionService.getAllTokenTransactions().stream().map(transaction ->
                    TokenTransactionMapper.mapTokenTransactionDTO(transaction.getUser().getUserId(), transaction)
                ).collect(Collectors.toList());
            }else{
                transactions = tokenTransactionService.getAllTokenTransactions(userId).stream().map(transaction ->
                        TokenTransactionMapper.mapTokenTransactionDTO(userId, transaction)
                ).collect(Collectors.toList());
            }

            if(transactions.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(transactions, HttpStatus.OK);

        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //get transaction by id
    @GetMapping("/{transactionId}")
    public ResponseEntity<TokenTransactionResponse> getTokenTransactionById(@PathVariable UUID transactionId){
        try{
            TokenTransaction transaction = tokenTransactionService.getTokenTransactionById(transactionId);

            if(transaction == null){
                return new ResponseEntity<>(new TokenTransactionResponse(null, "Transaction with id: " + transactionId +  " not found."), HttpStatus.NOT_FOUND);
            }

            TokenTransactionDTO transactionDTO = TokenTransactionMapper.mapTokenTransactionDTO(transaction.getUser().getUserId(), transaction);

            return new ResponseEntity<>(new TokenTransactionResponse(transactionDTO, "Transaction found."), HttpStatus.CREATED);

        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //create a transaction
    @PostMapping("/users/{userId}")
    public ResponseEntity<TokenTransactionResponse> createTokenTransaction(@PathVariable UUID userId, @RequestBody TokenTransactionDTO tokenTransactionRequestDTO){
        try{
            TokenTransactionDTO transactionDTO = TokenTransactionMapper.mapTokenTransactionDTO(userId, tokenTransactionService.createTokenTransaction(userId, tokenTransactionRequestDTO));

            if(transactionDTO == null){
                return new ResponseEntity<>(new TokenTransactionResponse(null, "User with id: " + userId +  " not found."), HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(new TokenTransactionResponse(transactionDTO, "Transaction created successfully."), HttpStatus.CREATED);

        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

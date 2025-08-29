package com.backend.tryal.tokenTransaction;

import com.backend.tryal.tokenTransaction.dto.TokenTransactionDTO;
import com.backend.tryal.tokenTransaction.mapper.TokenTransactionMapper;
import com.backend.tryal.tokenTransaction.response.TokenTransactionResponse;
import com.backend.tryal.tokenTransaction.service.TokenTransactionService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TokenTransactionController {

  private final TokenTransactionService tokenTransactionService;

  public TokenTransactionController(TokenTransactionService tokenTransactionService) {
    this.tokenTransactionService = tokenTransactionService;
  }

  //get all transactions (optional parameter by userId)
  @GetMapping()
  public List<TokenTransactionDTO> getAllTokenTransactions(
      @RequestParam(required = false) UUID userId) {
    List<TokenTransactionDTO> transactions = new ArrayList<>();
    if (userId == null) {
      transactions = tokenTransactionService.getAllTokenTransactions().stream().map(transaction ->
          TokenTransactionMapper.mapTokenTransactionDTO(transaction.getUser().getUserId(),
              transaction)
      ).collect(Collectors.toList());
    } else {
      transactions = tokenTransactionService.getAllTokenTransactions(userId).stream()
          .map(transaction ->
              TokenTransactionMapper.mapTokenTransactionDTO(userId, transaction)
          ).collect(Collectors.toList());
    }
    return transactions;
  }

  //get transaction by id
  @GetMapping("/{transactionId}")
  public TokenTransactionDTO getTokenTransactionById(
      @PathVariable UUID transactionId) {
    TokenTransaction transaction = tokenTransactionService.getTokenTransactionById(transactionId);

    return TokenTransactionMapper.mapTokenTransactionDTO(
        transaction.getUser().getUserId(), transaction);
  }

  //create a transaction
  @PostMapping("/users/{userId}")
  public ResponseEntity<TokenTransactionResponse> createTokenTransaction(@PathVariable UUID userId,
      @RequestBody TokenTransactionDTO tokenTransactionRequestDTO) {
    try {
      TokenTransactionDTO transactionDTO = TokenTransactionMapper.mapTokenTransactionDTO(userId,
          tokenTransactionService.createTokenTransaction(userId, tokenTransactionRequestDTO));

      if (transactionDTO == null) {
        return new ResponseEntity<>(
            new TokenTransactionResponse(null, "User with id: " + userId + " not found."),
            HttpStatus.NOT_FOUND);
      }

      return new ResponseEntity<>(
          new TokenTransactionResponse(transactionDTO, "Transaction created successfully."),
          HttpStatus.CREATED);

    } catch (Exception e) {
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}

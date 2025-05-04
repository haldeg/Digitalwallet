package com.company.digitalwallet.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.digitalwallet.dto.DepositRequestDTO;
import com.company.digitalwallet.dto.TransactionResponseDTO;
import com.company.digitalwallet.dto.WalletRequestDTO;
import com.company.digitalwallet.dto.WithdrawRequestDTO;
import com.company.digitalwallet.model.Transaction;
import com.company.digitalwallet.model.Wallet;
import com.company.digitalwallet.service.TransactionService;
import com.company.digitalwallet.service.WalletService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/wallet")
public class WalletController {
	
    private final WalletService walletService;
    private final TransactionService transactionService;
    
    public WalletController(WalletService walletService, TransactionService transactionService) {
        this.walletService = walletService;
        this.transactionService = transactionService;
    }
    
    @Operation(summary = "Create wallet with given details")
    @PostMapping("/createWallet/{customerId}")
    public ResponseEntity<WalletRequestDTO> createWallet(@PathVariable Long customerId, @Valid @RequestBody WalletRequestDTO walletRequest, @RequestHeader("Authorization") String authentication) {
    	Wallet wallet = walletService.createWallet(walletRequest, customerId, authentication);
    	return new ResponseEntity<>(walletRequest, HttpStatus.OK);
    }
    
    @Operation(summary = "List wallets for a given customer")
    @GetMapping("/listWallets/{customerId}")
    public ResponseEntity<List<Wallet>> listWalletsByCustomerId(@PathVariable Long customerId, @RequestHeader("Authorization") String authentication) {
        List<Wallet> wallets = walletService.getWalletsByCustomerId(customerId, authentication);
        return ResponseEntity.ok(wallets);
    }
    
    @Operation(summary = "Make deposit with given details")
    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@Valid @RequestBody DepositRequestDTO depositRequest) {
    	Transaction depositTransaction = walletService.deposit(depositRequest.getWalletId(), depositRequest.getAmount(), depositRequest.getSource().toString());
        return new ResponseEntity<>(depositTransaction, HttpStatus.OK);
    }

    @Operation(summary = "Make withdraw with given details")
    @PostMapping("/withdrawal")
    public ResponseEntity<Transaction> withdraw(@Valid @RequestBody WithdrawRequestDTO withdrawRequest) {
        Transaction withdrawTransaction = walletService.withdraw(withdrawRequest.getWalletId(), withdrawRequest.getAmount(), withdrawRequest.getDestination().toString());
        return new ResponseEntity<>(withdrawTransaction, HttpStatus.OK);
    }

    @Operation(summary = "List transactions for a given wallet")
    @GetMapping("/{walletId}/listTransactions")
    public ResponseEntity<List<TransactionResponseDTO>> listTransactionsByWalletId(@PathVariable Long walletId) {
        List<Transaction> transactions = transactionService.getTransactionsByWalletId(walletId);
        List<TransactionResponseDTO> transactionDTOs = transactions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transactionDTOs);
    }
    
    private TransactionResponseDTO convertToDto(Transaction transaction) {
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setOppositePartyType(transaction.getOppositePartyType());
        dto.setStatus(transaction.getStatus());
        return dto;
    }
    
    
}


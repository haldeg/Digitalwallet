package com.company.digitalwallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.digitalwallet.dto.enums.TransactionStatus;
import com.company.digitalwallet.model.Transaction;
import com.company.digitalwallet.service.TransactionService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    @Operation(summary = "Approve or Deny a transaction")
    @PutMapping("/{transactionId}/approval")
    public ResponseEntity<Transaction> approveTransaction(@PathVariable Long transactionId, @RequestParam TransactionStatus status) {
        Transaction transaction = transactionService.approveTransaction(transactionId, status);
        return ResponseEntity.ok(transaction);
    }
    
}

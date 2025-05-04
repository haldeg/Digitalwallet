package com.company.digitalwallet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.company.digitalwallet.dto.enums.TransactionStatus;
import com.company.digitalwallet.dto.enums.TransactionType;
import com.company.digitalwallet.model.Transaction;
import com.company.digitalwallet.model.Wallet;
import com.company.digitalwallet.repository.TransactionRepository;

import jakarta.transaction.Transactional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final WalletService walletService;

    public TransactionService(TransactionRepository transactionRepository, WalletService walletService) {
        this.transactionRepository = transactionRepository;
        this.walletService = walletService;
    }

    public List<Transaction> getTransactionsByWalletId(Long walletId) {
        return transactionRepository.findByWalletId(walletId);
    }

    @Transactional
    public Transaction approveTransaction(Long transactionId, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
        
        if (transaction.getStatus() != TransactionStatus.PENDING) {
        	throw new IllegalStateException("Only PENDING transactions can be updated");
        }
        
        if (!isValidTransactionStatus(status.toString())) {
            throw new IllegalArgumentException("Invalid Transaction Status: " + (status.toString()));
        }
        
        Wallet wallet = transaction.getWallet();

        if (status == TransactionStatus.APPROVED) {
            if (transaction.getType() == TransactionType.DEPOSIT) {
                wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                 wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
            }
        } else if (status == TransactionStatus.DENIED) {
        	if (transaction.getType() == TransactionType.DEPOSIT) {
                wallet.setBalance(wallet.getBalance().subtract(transaction.getAmount()));
            } else if (transaction.getType() == TransactionType.WITHDRAW) {
                 wallet.setUsableBalance(wallet.getUsableBalance().add(transaction.getAmount()));
            }
        }
        
        walletService.saveWallet(wallet);
        transaction.setStatus(status);
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(Long transactionId) {
         return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
    
    private boolean isValidTransactionStatus(String status) {
        try {
        	TransactionStatus.valueOf(status);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

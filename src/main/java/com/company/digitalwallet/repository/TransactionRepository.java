package com.company.digitalwallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.company.digitalwallet.model.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{
	List<Transaction> findByWalletId(Long walletId);
}

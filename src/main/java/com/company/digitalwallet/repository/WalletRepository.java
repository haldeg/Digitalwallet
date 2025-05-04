package com.company.digitalwallet.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.company.digitalwallet.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long>{
	List<Wallet> findByCustomerId(Long customerId);
}

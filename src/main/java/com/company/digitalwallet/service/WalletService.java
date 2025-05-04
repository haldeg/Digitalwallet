package com.company.digitalwallet.service;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.List;

import org.springframework.stereotype.Service;

import com.company.digitalwallet.dto.WalletRequestDTO;
import com.company.digitalwallet.dto.enums.Currency;
import com.company.digitalwallet.dto.enums.OppositePartyType;
import com.company.digitalwallet.dto.enums.Role;
import com.company.digitalwallet.dto.enums.TransactionStatus;
import com.company.digitalwallet.dto.enums.TransactionType;
import com.company.digitalwallet.model.Customer;
import com.company.digitalwallet.model.Transaction;
import com.company.digitalwallet.model.Wallet;
import com.company.digitalwallet.repository.TransactionRepository;
import com.company.digitalwallet.repository.WalletRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class WalletService {

	private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerService customerService;
    
    public WalletService(WalletRepository walletRepository, TransactionRepository transactionRepository, CustomerService customerService) {
        this.walletRepository = walletRepository;
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
    }
    
    public Wallet createWallet(WalletRequestDTO walletRequest, Long customerId, String authentication) {
    	
    	Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            throw new EntityNotFoundException("Customer not found with id: " + customerId);
        }

        if (!isValidCurrency(walletRequest.getCurrency().toString())) {
            throw new IllegalArgumentException("Invalid currency: " + walletRequest.getCurrency().toString());
        }
        
        if (!validAuthorization(authentication, customerId)) {
        	throw new RuntimeException("Unauthorized operation");
        }
        
        Wallet wallet = new Wallet();
        wallet.setCustomer(customer);
        wallet.setWalletName(walletRequest.getWalletName());
        wallet.setCurrency(Currency.valueOf(walletRequest.getCurrency().toString()));
        wallet.setActiveForShopping(walletRequest.isActiveForShopping());
        wallet.setActiveForWithdraw(walletRequest.isActiveForWithdraw());
        wallet.setBalance(BigDecimal.ZERO);
        wallet.setUsableBalance(BigDecimal.ZERO);

        walletRepository.save(wallet);
        return wallet;
    }
    
	public List<Wallet> getWalletsByCustomerId(Long customerId, String authentication) {
		
		if (!validAuthorization(authentication, customerId)) {
        	throw new RuntimeException("Unauthorized operation");
        }
		
        return walletRepository.findByCustomerId(customerId);
	 }

    public Wallet getWalletById(Long id) {
         return walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }
    
    @Transactional
    public Transaction deposit(Long walletId, BigDecimal amount, String source) {
    	
    	if (!isValidOppositePartyType(source)) {
            throw new IllegalArgumentException("Invalid source: " + source);
        }
    	
        Wallet wallet = getWalletById(walletId);
        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setOppositePartyType(OppositePartyType.valueOf(source));

        if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
            transaction.setStatus(TransactionStatus.PENDING);
            wallet.setBalance(wallet.getBalance().add(amount));
        } else {
            transaction.setStatus(TransactionStatus.APPROVED);
            wallet.setBalance(wallet.getBalance().add(amount));
            wallet.setUsableBalance(wallet.getUsableBalance().add(amount));
            walletRepository.save(wallet);
        }

        return transactionRepository.save(transaction);
    }

    @Transactional
	public Transaction withdraw(Long walletId, BigDecimal amount, String destination) {
		Wallet wallet = getWalletById(walletId);
		
		if (!wallet.isActiveForWithdraw()) {
		    throw new IllegalStateException("Wallet is not active for withdrawal");
		}
		
		if (!wallet.isActiveForShopping()) {
		    throw new IllegalStateException("Wallet is not active for shopping");
		}
		
		if (wallet.getUsableBalance().compareTo(amount) < 0) {
		    throw new IllegalStateException("Insufficient balance");
		}
		
		if (!isValidOppositePartyType(destination)) {
            throw new IllegalArgumentException("Invalid destination: " + destination);
        }
		
		Transaction transaction = new Transaction();
		transaction.setWallet(wallet);
		transaction.setAmount(amount);
		transaction.setType(TransactionType.WITHDRAW);
		transaction.setOppositePartyType(OppositePartyType.valueOf(destination));
		
		if (amount.compareTo(BigDecimal.valueOf(1000)) > 0) {
		    transaction.setStatus(TransactionStatus.PENDING);
		    wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
		} else {
		    transaction.setStatus(TransactionStatus.APPROVED);
		    wallet.setBalance(wallet.getBalance().subtract(amount));
		    wallet.setUsableBalance(wallet.getUsableBalance().subtract(amount));
		    walletRepository.save(wallet);
		}
		
		return transactionRepository.save(transaction);
	}
    
    private boolean isValidCurrency(String currency) {
        try {
            Currency.valueOf(currency);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
    private boolean isValidOppositePartyType(String oppositePartyType) {
        try {
        	OppositePartyType.valueOf(oppositePartyType);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
    
	public void saveWallet(Wallet wallet) {
		walletRepository.save(wallet);
	}
	
	private boolean validAuthorization(String authentication, Long customerId) {
		String userName = getUserName(authentication);
        if (userName != null) {
        	Customer customerTckn = customerService.getCustomerByTckn(userName);
            if (customerTckn == null) {
                throw new EntityNotFoundException("Customer not found with tckn: " + userName);
            }
            
            if (!(customerTckn.getRole() == Role.EMPLOYEE || (customerTckn.getRole() == Role.CUSTOMER && customerId == customerTckn.getId()))) {
            	return false;
            }
        }
        return true;
	}
	
	private String getUserName(String authentication) {
    	String userName = null;
    	if (authentication != null && !authentication.isEmpty()) {
    		String pair = new String(Base64.getDecoder().decode(authentication.substring(6)));
            userName = pair.split(":")[0];
    	}
    	return userName;
    }
}

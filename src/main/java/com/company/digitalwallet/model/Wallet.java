package com.company.digitalwallet.model;

import java.math.BigDecimal;
import java.util.List;

import com.company.digitalwallet.dto.enums.Currency;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "wallet")
public class Wallet {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Customer customer;
	
	private String walletName;
	
	@Enumerated(EnumType.STRING)
	private Currency currency;
	
	private boolean activeForShopping;
	private boolean activeForWithdraw;
	private BigDecimal balance = BigDecimal.ZERO;
	private BigDecimal usableBalance = BigDecimal.ZERO;
	
	@OneToMany(mappedBy = "wallet")
    private List<Transaction> transactions;
	
	public Wallet() {
	}
	
	public Wallet(Long id, Customer customer, String walletName, Currency currency, boolean activeForShopping,
			boolean activeForWithdraw, BigDecimal balance, BigDecimal usableBalance) {
		super();
		this.id = id;
		this.customer = customer;
		this.walletName = walletName;
		this.currency = currency;
		this.activeForShopping = activeForShopping;
		this.activeForWithdraw = activeForWithdraw;
		this.balance = balance;
		this.usableBalance = usableBalance;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Customer getCustomer() {
		return customer;
	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	public String getWalletName() {
		return walletName;
	}

	public void setWalletName(String walletName) {
		this.walletName = walletName;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public boolean isActiveForShopping() {
		return activeForShopping;
	}

	public void setActiveForShopping(boolean activeForShopping) {
		this.activeForShopping = activeForShopping;
	}

	public boolean isActiveForWithdraw() {
		return activeForWithdraw;
	}

	public void setActiveForWithdraw(boolean activeForWithdraw) {
		this.activeForWithdraw = activeForWithdraw;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getUsableBalance() {
		return usableBalance;
	}

	public void setUsableBalance(BigDecimal usableBalance) {
		this.usableBalance = usableBalance;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	
	
}

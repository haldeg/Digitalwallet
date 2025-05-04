package com.company.digitalwallet.model;

import java.math.BigDecimal;

import com.company.digitalwallet.dto.enums.OppositePartyType;
import com.company.digitalwallet.dto.enums.TransactionStatus;
import com.company.digitalwallet.dto.enums.TransactionType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "transaction")
public class Transaction {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	private Wallet wallet;
	
	private BigDecimal amount;
	
	@Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private OppositePartyType oppositePartyType;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    public Transaction() {
	}
    
	public Transaction(Long id, Wallet wallet, BigDecimal amount, TransactionType type,
			OppositePartyType oppositePartyType, TransactionStatus status) {
		super();
		this.id = id;
		this.wallet = wallet;
		this.amount = amount;
		this.type = type;
		this.oppositePartyType = oppositePartyType;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Wallet getWallet() {
		return wallet;
	}

	public void setWallet(Wallet wallet) {
		this.wallet = wallet;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public TransactionType getType() {
		return type;
	}

	public void setType(TransactionType type) {
		this.type = type;
	}

	public OppositePartyType getOppositePartyType() {
		return oppositePartyType;
	}

	public void setOppositePartyType(OppositePartyType oppositePartyType) {
		this.oppositePartyType = oppositePartyType;
	}

	public TransactionStatus getStatus() {
		return status;
	}

	public void setStatus(TransactionStatus status) {
		this.status = status;
	}
	
	
}

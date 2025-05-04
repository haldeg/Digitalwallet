package com.company.digitalwallet.dto;

import java.math.BigDecimal;

import com.company.digitalwallet.dto.enums.OppositePartyType;
import com.company.digitalwallet.dto.enums.TransactionStatus;
import com.company.digitalwallet.dto.enums.TransactionType;

public class TransactionResponseDTO {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private OppositePartyType oppositePartyType;
    private TransactionStatus status;
    
	public TransactionResponseDTO(Long id, BigDecimal amount, TransactionType type, OppositePartyType oppositePartyType, TransactionStatus status) {
		super();
		this.id = id;
		this.amount = amount;
		this.type = type;
		this.oppositePartyType = oppositePartyType;
		this.status = status;
	}

	public TransactionResponseDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

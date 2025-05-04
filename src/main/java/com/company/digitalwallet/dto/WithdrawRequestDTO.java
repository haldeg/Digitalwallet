package com.company.digitalwallet.dto;

import java.math.BigDecimal;

import com.company.digitalwallet.dto.enums.OppositePartyType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class WithdrawRequestDTO {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Wallet ID is required")
    private Long walletId;

    private OppositePartyType destination;

	public WithdrawRequestDTO(BigDecimal amount, Long walletId, OppositePartyType destination) {
		super();
		this.amount = amount;
		this.walletId = walletId;
		this.destination = destination;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getWalletId() {
		return walletId;
	}

	public void setWalletId(Long walletId) {
		this.walletId = walletId;
	}

	public OppositePartyType getDestination() {
		return destination;
	}

	public void setDestination(OppositePartyType destination) {
		this.destination = destination;
	}

	
    
}

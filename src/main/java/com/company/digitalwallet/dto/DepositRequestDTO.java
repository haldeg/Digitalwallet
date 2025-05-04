package com.company.digitalwallet.dto;

import java.math.BigDecimal;

import com.company.digitalwallet.dto.enums.OppositePartyType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class DepositRequestDTO {
    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    @NotNull(message = "Wallet ID is required")
    private Long walletId;

    private OppositePartyType source;

	public DepositRequestDTO(BigDecimal amount, Long walletId, OppositePartyType source) {
		super();
		this.amount = amount;
		this.walletId = walletId;
		this.source = source;
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

	public OppositePartyType getSource() {
		return source;
	}

	public void setSource(OppositePartyType source) {
		this.source = source;
	}

	
    
}

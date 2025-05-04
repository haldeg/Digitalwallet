package com.company.digitalwallet.dto;

import com.company.digitalwallet.dto.enums.Currency;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WalletRequestDTO {
    @NotBlank(message = "Wallet name is required")
    private String walletName;

    @NotNull(message = "Currency is required")
    private Currency currency;

    private boolean activeForShopping;
    private boolean activeForWithdraw;
    
    public WalletRequestDTO() {
    }
    
	public WalletRequestDTO(String walletName, Currency currency, boolean activeForShopping, boolean activeForWithdraw) {
		super();
		this.walletName = walletName;
		this.currency = currency;
		this.activeForShopping = activeForShopping;
		this.activeForWithdraw = activeForWithdraw;
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

	
    
}

package com.company.digitalwallet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

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
import com.company.digitalwallet.service.CustomerService;
import com.company.digitalwallet.service.WalletService;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
class DigitalWalletApplicationTests {

	@Mock
    private TransactionRepository transactionRepository;
	
    @Mock
    private WalletRepository walletRepository;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private WalletService walletService;

    private Customer testCustomer;
    private Wallet testWallet;
    private WalletRequestDTO testWalletRequestDTO;

    @BeforeEach
    void setUp() {
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("Test");
        testCustomer.setSurname("Customer");
        testCustomer.setTckn("12345678901");
        testCustomer.setRole(Role.EMPLOYEE);

        testWallet = new Wallet();
        testWallet.setId(2L);
        testWallet.setCustomer(testCustomer);
        testWallet.setWalletName("Test Wallet");
        testWallet.setCurrency(Currency.TRY);
        testWallet.setBalance(BigDecimal.ZERO);
        testWallet.setUsableBalance(BigDecimal.ZERO);
        testWallet.setActiveForShopping(true);
        testWallet.setActiveForWithdraw(true);

        testWalletRequestDTO = new WalletRequestDTO();
        testWalletRequestDTO.setWalletName("Test Wallet");
        testWalletRequestDTO.setCurrency(Currency.TRY);
        testWalletRequestDTO.setActiveForShopping(true);
        testWalletRequestDTO.setActiveForWithdraw(true);
    }
    
    @Test
    void createWallet_CustomerNotFound() {
        when(customerService.getCustomerById(1L)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> walletService.createWallet(testWalletRequestDTO, 1L, "12345678901"));
        verify(walletRepository, never()).save(any(Wallet.class));
    }

    @Test
    void getWalletById_Success() {
        when(walletRepository.findById(2L)).thenReturn(Optional.of(testWallet));

        Wallet foundWallet = walletService.getWalletById(2L);

        assertNotNull(foundWallet);
        assertEquals(testWallet.getId(), foundWallet.getId());
        verify(walletRepository, times(1)).findById(2L);
    }

    @Test
    void deposit_AmountLessThan1000_Success() {
        BigDecimal depositAmount = BigDecimal.valueOf(500);
        String source = OppositePartyType.IBAN.toString();
        when(walletRepository.findById(2L)).thenReturn(Optional.of(testWallet));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(i -> i.getArguments()[0]); // Return the saved transaction

        Transaction transaction = walletService.deposit(2L, depositAmount, source);

        assertNotNull(transaction);
        assertEquals(depositAmount, transaction.getAmount());
        assertEquals(TransactionType.DEPOSIT, transaction.getType());
        assertEquals(TransactionStatus.APPROVED, transaction.getStatus());
        assertEquals(depositAmount, testWallet.getBalance());
        assertEquals(depositAmount, testWallet.getUsableBalance());
        verify(walletRepository, times(1)).save(testWallet);
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void withdraw_InsufficientBalance() {
        testWallet.setUsableBalance(BigDecimal.valueOf(100));
        BigDecimal withdrawAmount = BigDecimal.valueOf(500);
        String destination = "Destination";
        when(walletRepository.findById(2L)).thenReturn(Optional.of(testWallet));

        assertThrows(RuntimeException.class, () -> walletService.withdraw(2L, withdrawAmount, destination));
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(walletRepository, never()).save(testWallet);
    }


}

package com.company.digitalwallet.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.company.digitalwallet.model.Customer;
import com.company.digitalwallet.repository.CustomerRepository;

@Service
public class CustomerUserDetailsService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    public CustomerUserDetailsService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String tckn) throws UsernameNotFoundException {
        Optional<Customer> customerOptional = customerRepository.findByTckn(tckn);
        return customerOptional.orElseThrow(() -> new UsernameNotFoundException("Customer not found with TCKN: " + tckn));
    }
    
//    @Override
//    public Customer loadUserByUsername(String tckn) throws UsernameNotFoundException {
//        return customerRepository.findByTckn(tckn)
//                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with TCKN: " + tckn));
//    }
}
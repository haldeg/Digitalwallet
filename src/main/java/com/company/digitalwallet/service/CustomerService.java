package com.company.digitalwallet.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.company.digitalwallet.model.Customer;
import com.company.digitalwallet.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Customer not found"));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
    
    public Customer getCustomerByTckn(String tckn) {
    	return customerRepository.findByTckn(tckn)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
    }
}

package com.company.digitalwallet.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.company.digitalwallet.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long>{
	Optional<Customer> findByTckn(String tckn);
}

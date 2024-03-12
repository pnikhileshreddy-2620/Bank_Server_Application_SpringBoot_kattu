package com.cg.scb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cg.scb.entity.Account;
import com.cg.scb.entity.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	Customer findByName(String name);
	@Query("SELECT a FROM Account a JOIN FETCH a.customer WHERE a.customer.customerId = :customerId")
    Account findAccountByCustomerId(@Param("customerId") int customerId);
	Customer findByEmail(String email);

}

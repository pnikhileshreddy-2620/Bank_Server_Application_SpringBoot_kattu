package com.cg.scb.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cg.scb.entity.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
	
	List<Transaction> findByAccount_AccountNumber(int accountNumber);

}

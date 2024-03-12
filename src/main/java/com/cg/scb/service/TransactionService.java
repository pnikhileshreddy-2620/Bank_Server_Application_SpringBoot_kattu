package com.cg.scb.service;

import java.util.List;

import com.cg.scb.entity.Account;
import com.cg.scb.entity.Transaction;

public interface TransactionService {
	
	void createTransaction(Account account, String transactionType, double amount);

    List<Transaction> getTransactionByType(int accNUm, String type);

    List<Transaction> getAllTransactions(int accountNumber);

    List<Transaction> getLast10Transactions(int accountNumber);

    Transaction getTransactionByTransactionId(int accountNum, int transactionId);

    List<Transaction> getPreviousDaysTransactions(int accountNumber, int days);
    void deleteTransactionById(int transactionId);

}

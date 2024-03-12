package com.cg.scb.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.numbergenerator.NumberGenerator;
import com.cg.scb.entity.Account;
import com.cg.scb.entity.Transaction;
import com.cg.scb.exception.AccountNotFoundException;
import com.cg.scb.exception.TransactionNotFoundException;
import com.cg.scb.repository.AccountRepository;
import com.cg.scb.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {
	
	NumberGenerator numberGenerator = new NumberGenerator();
	@Autowired
	TransactionRepository transactionRepository;
	@Autowired
	AccountRepository accountRepository;
	

	@Override
    public void createTransaction(Account account, String transactionType, double amount) {
        if (account == null) {
            throw new AccountNotFoundException("Account not found with account number");
        }

        Transaction transaction = new Transaction();
        transaction.setTransactionId(numberGenerator.generateNumber());
        transaction.setAccount(account);
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(transactionType);
        transaction.setAmount(amount);

        transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionByType(int accNUm, String type) throws TransactionNotFoundException {
        List<Transaction> transactions1 = new ArrayList<>();
        List<Transaction> transactions = getAllTransactions(accNUm);
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionType().equals(type)) {
                transactions1.add(transaction);
            }
        }
        if(transactions1.size()!=0)
            return transactions1;
        throw new TransactionNotFoundException("No Transactions found with type "+type);
    }

    @Override
    public List<Transaction> getAllTransactions(int accountNumber) throws AccountNotFoundException
    {
        Account account = accountRepository.findById(accountNumber).get();
        if (account == null) {
            throw new AccountNotFoundException("Account not found with account number: " + accountNumber);
        }

        return transactionRepository.findByAccount_AccountNumber(accountNumber);
    }

    @Override
    public List<Transaction> getLast10Transactions(int account_number) throws TransactionNotFoundException {
        List<Transaction> transactions = getAllTransactions(account_number);
        System.out.println(transactions.size());
        if(transactions.size()!=0) {
            int size = transactions.size();
            int startIndex = size > 10 ? size - 10 : 0;
            int endIndex = size;
            return transactions.subList(startIndex, endIndex);
        }
        throw new TransactionNotFoundException("No Transaction found in last 10 days");
    }

    @Override
    public Transaction getTransactionByTransactionId(int accountNum, int transactionId) throws TransactionNotFoundException {
        List<Transaction> transactions = getAllTransactions(accountNum);
        for (Transaction transaction : transactions) {
            if (transaction != null && transaction.getTransactionId() == transactionId) {
                return transaction;
            }
        }
        throw new TransactionNotFoundException("Transaction with " + transactionId + " not found");
    }

    @Override
    public List<Transaction> getPreviousDaysTransactions(int accountNumber, int days) throws TransactionNotFoundException {
        List<Transaction> allTransactions = getAllTransactions(accountNumber);
        List<Transaction> lastNDaysTransactions = new ArrayList<>();
        LocalDate currentDate = LocalDate.now();
        for (Transaction transaction : allTransactions) {
            if (transaction != null && transaction.getTransactionDate().isAfter(currentDate.minusDays(days))) {
                lastNDaysTransactions.add(transaction);
            }
        }
        if (lastNDaysTransactions.size() != 0)
            return lastNDaysTransactions;
        throw new TransactionNotFoundException("No Transactions found in last " + days +" days");
    }
    @Override
    public void deleteTransactionById(int transactionId) throws TransactionNotFoundException{
        if (transactionRepository.existsById(transactionId)) {
            transactionRepository.deleteById(transactionId);
        } else {
            throw new TransactionNotFoundException("Transaction not found with ID: " + transactionId);
        }
    }

}

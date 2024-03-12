package com.cg.scb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.numbergenerator.NumberGenerator;
import com.cg.scb.entity.Account;
import com.cg.scb.entity.Customer;
import com.cg.scb.entity.Transaction;
import com.cg.scb.repository.AccountRepository;

@Service
public class AccountServiceImpl implements AccountService {
	
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	TransactionService transactionService;
	NumberGenerator numberGenerator = new NumberGenerator();
	

	@Override
	public String deposit(int accountNumber, double amount) {
		Account account = accountRepository.findById(accountNumber).get();
		account.setCurrentBalance(account.getCurrentBalance()+amount);
		accountRepository.save(account);
		transactionService.createTransaction(account, "deposit", amount);
		return "Deposited Successfully";
	}

	@Override
	public double checkBalance(int accountNumber) {
		
		return accountRepository.findById(accountNumber).get().getCurrentBalance();
	}

	@Override
	public String withdraw(int accountNumber, double amount) {
		Account account = accountRepository.findById(accountNumber).get();
		if(account.getCurrentBalance()<amount)
		{
			return "Insufficient Amount";
		}
		account.setCurrentBalance(account.getCurrentBalance()-amount);
		accountRepository.save(account);
		transactionService.createTransaction(account, "withdraw", amount);

		return "Withdraw Successfully";
	}

	@Override
	public String transfer(int fromAccountNumber, int toAccountNumber, double amount) {
		Account fromAccount = findAccountByNumber(fromAccountNumber);
		double fromNewBalance = fromAccount.getCurrentBalance()-amount;
		transactionService.createTransaction(fromAccount, "Send", amount);
		Account toAccount = findAccountByNumber(toAccountNumber);
		double toNewBalance = toAccount.getCurrentBalance()+amount;
		transactionService.createTransaction(toAccount, "Received", amount);
		fromAccount.setCurrentBalance(fromNewBalance);
		toAccount.setCurrentBalance(toNewBalance);
		accountRepository.save(fromAccount);
		accountRepository.save(toAccount);
		return "Transfer Successful";
	}

	@Override
	public Account createAccount(Customer customer, double currentBalance, String accountType) {
		Account newAccount = new Account();
        newAccount.setCustomer(customer);
        newAccount.setAccountNumber(numberGenerator.generateNumber());
        newAccount.setCurrentBalance(currentBalance);
        newAccount.setAccountType(accountType);        
        Account account =  accountRepository.save(newAccount);
        transactionService.createTransaction(newAccount, accountType, currentBalance);
        return account;

	}

	@Override
	public String deleteAccount(int accountNumber) {
		
		Account account = accountRepository.findById(accountNumber).get();
            List<Transaction> transactionList = transactionService.getAllTransactions(accountNumber);
            for (Transaction transaction : transactionList) {
                if (transaction != null)
                    transactionService.deleteTransactionById(transaction.getTransactionId());
            }
            accountRepository.delete(account);
            return "Account with account number "+ accountNumber+" Deleted Successfully";
	}

	@Override
	public Account findAccountByNumber(int accountNumber) {
		
		return accountRepository.findById(accountNumber).get(); 
	}

	

}

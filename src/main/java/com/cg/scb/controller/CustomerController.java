package com.cg.scb.controller;

import java.util.List;
import java.util.Optional;

import org.hibernate.query.sqm.mutation.internal.temptable.LocalTemporaryTableInsertStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.cg.scb.dto.Admin;
import com.cg.scb.entity.Account;
import com.cg.scb.entity.Customer;
import com.cg.scb.entity.Transaction;
import com.cg.scb.repository.AccountRepository;
import com.cg.scb.repository.AdminRepository;
import com.cg.scb.repository.CustomerRepository;
import com.cg.scb.service.AccountService;
import com.cg.scb.service.CustomerService;
import com.cg.scb.service.TransactionService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/login")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	@Autowired
	AccountService accountService;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	TransactionService transactionService;
	@Autowired
	AdminRepository adminRepository;
	@Autowired
	CustomerRepository customerRepository;
	
	@GetMapping(value = "/register")
	public ModelAndView register()
	{
		return new ModelAndView("Registration");
	}
	@PostMapping("/registration")
	  public ModelAndView registerUser(
	          @RequestParam String name,
	          @RequestParam String email,
	          @RequestParam String password,
	          Model model) {
	     		Customer customer = customerService.createCustomer(name, email, password);
	     		if(customer!=null)
	     		{
	     			return new ModelAndView("Registration","Message","Customer details saved Successfully");
	     		}
	     		return null;
	  }
	@GetMapping(value = "/operations")
	public ModelAndView Operations(HttpSession session,
            ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		return new ModelAndView("Operations");
	}
	@GetMapping(value="/userloginpage")
	public ModelAndView displayUserLoginPage()
	{
		ModelAndView loginPageView=
				new ModelAndView("UserLogin","fname","Vaishali");
		return loginPageView;
	}
	@PostMapping(value="/validateuser")
	public ModelAndView validateUser(
			@RequestParam String username,@RequestParam String password,
			HttpSession session,
            ModelMap model) 
	{
		List<Customer> customers = customerService.getAllCustomers();
		List<Account> accounts = accountRepository.findAll();
		Customer temp = isCustomerIdPresent(customers, username, password);

		if(temp!=null)
		{
			session.setAttribute("username", temp);
			for(Account account:accounts)
			{
				if(account.getCustomer().getCustomerId()==temp.getCustomerId())
				{
					model.addAttribute("username", account.getAccountNumber());
				}
			}

			return new ModelAndView("Operations");
		}
		else {
	        model.addAttribute("error", "Invalid username or password");
	        return new ModelAndView("UserLogin");
	    }
	}
	
	@GetMapping(value = "/accountdetails")
	public ModelAndView AccountDetails(HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		List<Account> accounts = accountRepository.findAll();
		for(Account account:accounts)
		{
			if(account.getCustomer().getCustomerId()==username.getCustomerId())
			{
				model.addAttribute("username", account.getAccountNumber());
			}
		}
//		model.addAttribute("username", username.getName());
		return new ModelAndView("Account");
	}
	@GetMapping("/checkaccountdetails")
	public ModelAndView checkAccountDetails(@RequestParam Integer accountNumber, HttpSession session, ModelMap model) {
	    Customer username = (Customer) session.getAttribute("username");
	    model.addAttribute("username", username.getName());
	    Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
	    if (optionalAccount.isPresent()) {
	        Account account = optionalAccount.get();
	        if (username.getCustomerId() == account.getCustomer().getCustomerId()) {
	            return new ModelAndView("AccountDetails", "account", account);
	        } else {
	            return new ModelAndView("AccountDetails", "balanceMessage", "Account Number Mismatch");
	        }
	    } else {
	        return new ModelAndView("AccountDetails", "balanceMessage", "Account not found");
	    }
	}
	@GetMapping("/customerdetails")
    public ModelAndView checkCustomerDetails(HttpSession session, ModelMap model) {
        Customer username = (Customer) session.getAttribute("username");
        model.addAttribute("username", username.getName());
        return new ModelAndView("CustomerDetails","customer",username);
    }
	@GetMapping(value = "/balance")
	public ModelAndView balance(HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		return new ModelAndView("Balance");
	}

	@GetMapping("/checkBalance")
	public ModelAndView checkBalance(@RequestParam Integer accountNumber, HttpSession session, ModelMap model) {
	    Customer username = (Customer) session.getAttribute("username");
	    model.addAttribute("username", username.getName());
	    Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
	    if (optionalAccount.isPresent()) {
	        Account account = optionalAccount.get();
	        if (username.getCustomerId() == account.getCustomer().getCustomerId()) {
	            return new ModelAndView("Display", "balanceMessage", account.getCurrentBalance());
	        } else {
	            return new ModelAndView("Display", "balanceMessage", "Account Number Mismatch");
	        }
	    } else {
	        return new ModelAndView("Display", "balanceMessage", "Account not found");
	    }
	}

	@GetMapping(value = "/deposit")
	public ModelAndView deposit(HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		return new ModelAndView("Deposit");
	}
	@GetMapping("/depositmoney")
    public ModelAndView performTransaction(
            @RequestParam Integer accountNumber,
            @RequestParam double amount,HttpSession session, ModelMap model) 
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
	    Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
	    if (optionalAccount.isPresent()) 
	    {
	        Account account = optionalAccount.get();
			if(username.getCustomerId()!=account.getCustomer().getCustomerId())
			{
		    	return new ModelAndView("Display", "balanceMessage", "Account Number Mismatch");
			}
			accountService.deposit(accountNumber, amount);
			return new ModelAndView("Display", "balanceMessage", account.getCurrentBalance());
	    }
			else
			{
				return new ModelAndView("Display", "balanceMessage", "Account not found");
				
			}
    }
	@GetMapping(value = "/withdraw")
	public ModelAndView withdraw(HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		return new ModelAndView("Withdraw");
	}
	@GetMapping("/withdrawmoney")
    public ModelAndView withdraw(
            @RequestParam Integer accountNumber,
            @RequestParam double amount,HttpSession session, ModelMap model) {
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
	    if (optionalAccount.isPresent()) 
	    {
	        Account account = optionalAccount.get();
			if(username.getCustomerId()!=account.getCustomer().getCustomerId())
			{
		    	return new ModelAndView("Display", "balanceMessage", "Account Number Mismatch");
			}
			accountService.withdraw(accountNumber, amount);
			return new ModelAndView("Display", "balanceMessage", account.getCurrentBalance());
	    }
			else
			{
				return new ModelAndView("Display", "balanceMessage", "Account not found");
				
			}
			
    }
	@GetMapping(value = "/transfer")
	public ModelAndView transfer(HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		List<Account> accounts = accountRepository.findAll();
		for(Account account:accounts)
		{
			if(account.getCustomer().getCustomerId()==username.getCustomerId())
			{
				model.addAttribute("username", account.getAccountNumber());
			}
		}
//		model.addAttribute("username", username.getName());
		return new ModelAndView("Transfer");
	}
	@GetMapping("/transfermoney")
    public ModelAndView transfer(
            @RequestParam Integer fromaccountNumber,@RequestParam Integer toaccountNumber,
            @RequestParam double amount,HttpSession session, ModelMap model) {
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		Optional<Account> optionalAccount = accountRepository.findById(fromaccountNumber);
		Optional<Account> optionalAccount1 = accountRepository.findById(toaccountNumber);
	    if (optionalAccount.isPresent() && optionalAccount1.isPresent()) 
	    {
	        Account account = optionalAccount.get();
			if(username.getCustomerId()!=account.getCustomer().getCustomerId())
			{
		    	return new ModelAndView("Display", "balanceMessage", "Account Number Mismatch");
			}
			accountService.transfer(fromaccountNumber, toaccountNumber, amount);
			return new ModelAndView("Display", "balanceMessage", account.getCurrentBalance());
	    }
			else
			{
				return new ModelAndView("Display", "balanceMessage", "Account not found");
				
			}
			
    }
	@GetMapping(value = "/alltransactions")
	public ModelAndView AllTransactions(HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		return new ModelAndView("Alltransactions");
	}
	@GetMapping(value = "/alltransactionsdisplay")
	public ModelAndView AllTransactionsDisplay(@RequestParam Integer accountNumber,HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
		if (optionalAccount.isPresent()) 
	    {
			Account account = optionalAccount.get();
			if(username.getCustomerId()!=account.getCustomer().getCustomerId())
	    	return new ModelAndView("Display", "balanceMessage", "Account Number Mismatch");
		List<Transaction> transactions = transactionService.getAllTransactions(accountNumber);
		return new ModelAndView("Alltransactions","transactionList",transactions);
	    }
		else
		{
			return new ModelAndView("Display", "balanceMessage", "Account not found");

		}
		
	}
	@GetMapping(value = "/last10transactions")
	public ModelAndView last10Transactions(HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		return new ModelAndView("Last10Transactions");
	}
	@GetMapping(value = "/last10transactionsdisplay")
	public ModelAndView last10TransactionsDisplay(@RequestParam Integer accountNumber,HttpSession session, ModelMap model)
	{
		Customer username = (Customer) session.getAttribute("username");
		model.addAttribute("username", username.getName());
		Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
		if (optionalAccount.isPresent()) 
	    {
			Account account = optionalAccount.get();
			if(username.getCustomerId()!=account.getCustomer().getCustomerId())
	    	return new ModelAndView("Display", "balanceMessage", "Account Number Mismatch");
		List<Transaction> transactions = transactionService.getLast10Transactions(accountNumber);
		return new ModelAndView("Last10Transactions","transactionList",transactions);
	    }
		else
		{
			return new ModelAndView("Display", "balanceMessage", "Account not found");

		}
		
	}
	@GetMapping(value = "/alltransactions1")
	public ModelAndView AllTransactionsadmin(HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		return new ModelAndView("Alltransactionsadmin");
	}
	@GetMapping(value = "/alltransactionsdisplayadmin")
	public ModelAndView AllTransactionsDisplayForAdmin(@RequestParam Integer accountNumber,HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
		if (optionalAccount.isPresent()) 
	    {
			Account account = optionalAccount.get();
			List<Transaction> transactions = transactionService.getAllTransactions(accountNumber);
			return new ModelAndView("Alltransactionsadmin","transactionList",transactions);
	    }
		else
		{
			return new ModelAndView("Alltransactionsadmin", "balanceMessage", "Account not found");

		}
		
	}
	@GetMapping(value = "/last10transactions1")
	public ModelAndView last10Transactionsadmin(HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		return new ModelAndView("Last10TransactionsAdmin");
	}
	@GetMapping(value = "/last10transactionsdisplayadmin")
	public ModelAndView last10TransactionsDisplayForAdmin(@RequestParam Integer accountNumber,HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		Optional<Account> optionalAccount = accountRepository.findById(accountNumber);
		if (optionalAccount.isPresent()) 
	    {
			Account account = optionalAccount.get();
			List<Transaction> transactions = transactionService.getLast10Transactions(accountNumber);
			return new ModelAndView("Last10TransactionsAdmin","transactionList",transactions);
	    }
		else
		{
			return new ModelAndView("Last10TransactionsAdmin", "balanceMessage", "Account not found");

		}
		
	}
	private static Customer isCustomerIdPresent(List<Customer> customers, String username, String password) {
        for (Customer customer : customers) {
            if (customer.getEmail().equals(username) && customer.getPassword().equals(password)) {
                return customer; 
            }
        }
        return null;
    }
	
	@GetMapping("/updatedetails")
    public ModelAndView updateDetailsPage(HttpSession session, ModelMap model) 
    {
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		return new ModelAndView("UpdateDetails");
    }
	@PostMapping("/updateDetails")
	public ModelAndView updateDetails(@RequestParam(name = "customerId", required = false) String customerIdStr, HttpSession session, ModelMap model) {
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
	    if (customerIdStr == null || customerIdStr.isEmpty()) {
	        return new ModelAndView("UpdateDetails", "message", "Customer ID is required");
	    }

	    try {
	        int customerId = Integer.parseInt(customerIdStr);
	        System.out.println(customerId);
	        Optional<Customer> customer = customerRepository.findById(customerId);

	        if (customer.isPresent()) {
	            model.addAttribute("customerId", customerId);
	            return new ModelAndView("UpdateOptions");
	        } else {
	            return new ModelAndView("UpdateDetails", "message", "Customer ID not Found");
	        }
	    } catch (NumberFormatException e) {
	        return new ModelAndView("UpdateDetails", "message", "Invalid Customer ID format");
	    }
	}

	
	
	@PostMapping("/update/name")
    public ModelAndView updateName(@RequestParam int customerId, @RequestParam String newName,HttpSession session, ModelMap model ) {
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		customerService.updateCustomerName(customerId, newName);
        model.addAttribute("message", "Name updated successfully: " + newName);
        return new ModelAndView("UpdateOptions","message","Name Updated Successfully");
    }

    @PostMapping("/update/email")
    public ModelAndView updateEmail(@RequestParam int customerId, @RequestParam String newEmail, HttpSession session, ModelMap model) {
    	Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
    	customerService.updateCustomerEmail(customerId, newEmail);        
    	return new ModelAndView("UpdateOptions","message","Email Updated Successfully");

    }
	
	@GetMapping(value="/adminloginpage")
	public ModelAndView displayAdminLoginPage(@ModelAttribute("loginObj")
	Admin user)
	{
		ModelAndView loginPageView=
				new ModelAndView("AdminLogin");
		return loginPageView;
	}
	@GetMapping(value = "adminoperations")
	public ModelAndView adminOperations(HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		return new ModelAndView("AdminOperations");
	}
	@PostMapping(value="/validateadmin")
	public ModelAndView validateAdmin(@RequestParam String username, @RequestParam String password,
	        HttpSession session, ModelMap model) {
	    Admin admin = adminRepository.findByUsername(username);
	    if (admin != null && admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
	    	session.setAttribute("username", admin);
            model.addAttribute("username", admin.getName());
	        return new ModelAndView("AdminOperations");
	    } else {
	        model.addAttribute("error", "Invalid username or password");
	        return new ModelAndView("adminLogin", model);
	    }
	}

	@GetMapping( value="/createaccount")
	public ModelAndView createAccount(HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		return new ModelAndView("AccountCreation");
	}
	@GetMapping(value="/adminregister")
	public ModelAndView createUser()
	{
		return new ModelAndView("AdminRegistration");
	}
	@PostMapping("/adminregistration")
    public ModelAndView registerAdmin(
            @RequestParam String name,
            @RequestParam String email,
            @RequestParam String password) {
        Admin admin = adminRepository.save(new Admin(email, name, password));
        if(admin!=null)
 		{
 			return new ModelAndView("AdminRegistration","Message","Admin details saved Successfully");
 		}
 		return null;
    }
	@GetMapping( value="/accountcreation")
    public ModelAndView createAccount(@RequestParam String name,
            @RequestParam String email,@RequestParam String password,
            @RequestParam double amount,HttpSession session, ModelMap model) {
		Admin admin  = (Admin) session.getAttribute("username");
		        model.addAttribute("username", admin.getName());
		Customer customer = customerService.createCustomer(name, email,password );
		Account account = accountService.createAccount(customer, amount, "Savings");
			return new ModelAndView("AccountDetails","account",account);
			
    }
	@GetMapping(value = "/delete")
	public ModelAndView delete(HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		return new ModelAndView("DeleteAccount");
	}
	@GetMapping(value = "/deleteaccount")
	public ModelAndView deleteAccount(@RequestParam Integer accountNumber,HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
        Optional<Account> account = accountRepository.findById(accountNumber);
        if(account.isPresent())
        {
			accountService.deleteAccount(accountNumber);
			return new ModelAndView("DeleteAccount","balanceMessage", "Account Deleted Successfully");
        }
        return new ModelAndView("DeleteAccount","balanceMessage","Account Not Found");
	}
	@GetMapping(value = "/deletecust")
	public ModelAndView deletecust(HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		return new ModelAndView("DeleteCustomer");
	}
	@GetMapping(value = "/deletecustomer")
	public ModelAndView deleteCustomer(@RequestParam Integer customerId,HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		Optional<Customer> cuOptional = customerRepository.findById(customerId);
		if(cuOptional.isPresent())
		{
			customerRepository.deleteById(customerId);
			return new ModelAndView("DeleteCustomer","balanceMessage", "Customer Deleted Successfully");
		}
		return new ModelAndView("DeleteCustomer","balanceMessage", "No Customer with ID found");

	}
	@GetMapping(value = "/allcustomers")
	public ModelAndView allCustomers(HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		List<Customer> customers = customerRepository.findAll();
		return new ModelAndView("ListofCustomers","mcList",customers);
	}
	@GetMapping(value = "/allaccounts")
	public ModelAndView allAccounts(HttpSession session, ModelMap model)
	{
		Admin admin  = (Admin) session.getAttribute("username");
        model.addAttribute("username", admin.getName());
		List<Account> accounts = accountRepository.findAll();
		return new ModelAndView("AllAccounts","mcList",accounts);
	}
	

}

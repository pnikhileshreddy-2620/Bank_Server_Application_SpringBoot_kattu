package com.cg.scb.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cg.numbergenerator.NumberGenerator;
import com.cg.scb.dto.Admin;
import com.cg.scb.entity.Account;
import com.cg.scb.entity.Customer;
import com.cg.scb.repository.AdminRepository;
import com.cg.scb.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	CustomerRepository customerRepository;

	NumberGenerator numberGenerator = new NumberGenerator();
	@Autowired
	AdminRepository adminRepository;

	@Override
	public Customer createCustomer(String name, String email,String password) {
		Customer newCustomer = new Customer();
        newCustomer.setCustomerId(numberGenerator.generateNumber());
        newCustomer.setName(name);
        newCustomer.setEmail(email);
        newCustomer.setPassword(password);
        return customerRepository.save(newCustomer);
	}

	@Override
	public Customer findCustomerById(int customerId) {
		
		return customerRepository.getById(customerId);
	}

	@Override
	public Customer findCustomerByName(String Name) {
		// TODO Auto-generated method stub
		return customerRepository.findByName(Name);
	}

	@Override
	public Customer updateCustomerName(int customerId, String newName) {
		Customer customer = customerRepository.getById(customerId);
		customer.setName(newName);
		customerRepository.save(customer);
		return customer;
	}

	@Override
	public Customer updateCustomerEmail(int customerId, String newEmail) {
		Customer customer = customerRepository.getById(customerId);
		customer.setEmail(newEmail);
		customerRepository.save(customer);
		return customer;
	}

	@Override
	public void deleteCustomer(int customerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Customer> getAllCustomers() {
		
	return customerRepository.findAll();
	}

	@Override
	public Account findAccountByCustomerId(int customerId) {
		
		return customerRepository.findAccountByCustomerId(customerId);
	}

	@Override
	public Admin saveAdmin(String name, String email, String password) {
		Admin admin = new Admin();
		admin.setName(name);
		admin.setPassword(password);
		admin.setUsername(email);
		adminRepository.save(admin);
		return admin;	
	}

	

}

package com.cg.scb.service;

import java.util.List;

import com.cg.scb.dto.Admin;
import com.cg.scb.entity.Account;
import com.cg.scb.entity.Customer;

public interface CustomerService {
	Customer createCustomer(String name, String email,String password);
    Customer findCustomerById(int customerId);
    Customer findCustomerByName(String Name);
    Customer updateCustomerName(int customerId,String newName);
    Customer updateCustomerEmail(int customerId,String newEmail);
    void deleteCustomer(int customerId);
    List<Customer> getAllCustomers();
    Account findAccountByCustomerId(int customerId);
    Admin saveAdmin(String name,String email,String password);
    

}

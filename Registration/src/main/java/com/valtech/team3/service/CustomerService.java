package com.valtech.team3.service;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.valtech.team3.model.Customer;
import com.valtech.team3.model.Role;
import com.valtech.team3.repository.CustomerRepository;
import com.valtech.team3.repository.RoleRepository;

@Service("customerService")
public class CustomerService {

	private CustomerRepository customerRepository ;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	//Constructor..
	@Autowired
	public CustomerService(CustomerRepository customerRepository,
			RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.customerRepository = customerRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	
	
	//Get the User By Email..
	public Customer findUserByEmail(String email) {
		return customerRepository.findByEmail(email);
	}
	
	
	//Save the New Customer to the DataBase..
	public Customer saveCustomer(Customer customer) {
		customer.setPassword(bCryptPasswordEncoder.encode(customer.getPassword()));
		customer.setActive(1);
		Role customerRole = roleRepository.findByRole("ADMIN");
		customer.setRoles(new HashSet<Role>(Arrays.asList(customerRole)));
		return customerRepository.save(customer);
	}
}

package com.valtech.team3.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.valtech.team3.model.Customer;
import com.valtech.team3.service.CustomerService;

@Controller
public class CustomerController {

	// Inject the Dependency of the Customer Service Implicitly..
	@Autowired
	private CustomerService customerService;

	// Login Mapping..
	@RequestMapping(value = { "/", "/login" }, method = RequestMethod.GET)
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("login");
		return modelAndView;
	}

	// Registration Mapping..
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public ModelAndView registration() {
		ModelAndView modelAndView = new ModelAndView();
		Customer customer = new Customer();
		modelAndView.addObject("user", customer);
		modelAndView.setViewName("registration");
		return modelAndView;
	}

	// Payment Mapping..
	@RequestMapping(value = "/payment", method = RequestMethod.GET)
	public ModelAndView payment() {
		ModelAndView modelAndView = new ModelAndView();
		Customer customer = new Customer();
		modelAndView.addObject("user", customer);
		modelAndView.setViewName("payment");
		return modelAndView;
	}

	// Create A New User..
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public ModelAndView createNewUser(@Valid Customer customer, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		Customer userExists = customerService.findUserByEmail(customer.getEmail());
		if (userExists != null) {
			bindingResult.rejectValue("email", "error.user",
					"There is already a customer registered with the email provided");
		}
		if (bindingResult.hasErrors()) {
			modelAndView.setViewName("registration");
		} else {
			customerService.saveCustomer(customer);
			modelAndView.addObject("successMessage", "Customer has been registered successfully");
			modelAndView.addObject("user", new Customer());
			modelAndView.setViewName("registration");

		}
		return modelAndView;
	}

	// Home Page for User.. After Login Is Successfull..
	@RequestMapping(value = "/customers/index", method = RequestMethod.GET)
	public ModelAndView customerHome() {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Customer customer = customerService.findUserByEmail(auth.getName());
		modelAndView.addObject("userName", "Welcome " + customer.getName() + " " + " (" + customer.getEmail() + ")");
		modelAndView.addObject("userMessage", "Content Available Only for Customers");
		modelAndView.setViewName("customers/index");
		return modelAndView;
	}
}

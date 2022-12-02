package com.valtech.team3.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customer")  //Name of the Table in the DataBase..
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Column(name = "customer_id")
	private int id;   //Declare Id for a Customer.. This will be Auto Generated ;
	@Column(name = "email")
	@Email(message = "*Please provide a valid Email")
	@NotEmpty(message = "*Please provide an email")
	private String email;  // Declare Email for a Customer.. This will be entered by the Customer at the Time of Registration..
	@Column(name = "password")
	@Length(min = 5, message = "*Your password must have at least 5 characters")
	@NotEmpty(message = "*Please provide your password")
	private String password;  // Password for the Customer to authorize his login..
	@Column(name = "name")
	@NotEmpty(message = "*Please provide your name")
	private String name;  //Name of the Customer ..
	@Column(name = "active")
	private int active;  // Set the status of the Customers Account.. 1 indicates Activates.. And 0 Zero indicates Inactive..
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "customer_role", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles;
	
	
	//Getters And Setters For all the Variables Used Above.. 
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getActive() {
		return active;
	}
	public void setActive(int active) {
		this.active = active;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	//Override To String Method
	
	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", password=" + password + ", name=" + name + ", active="
				+ active + ", roles=" + roles + "]";
	}
	
	
}

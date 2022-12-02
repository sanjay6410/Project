
package com.valtech.team3.config;

import javax.sql.DataSource; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	
	
	//To Save the Encrypted Password In the DataBase..
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	//To Establish The Connection To the DataBase..
	@Autowired
	private DataSource dataSource;
	
	
	//To Declare The Queries for the Customer Actions..
	@Value("${spring.queries.customers-query}")
	private String customersQuery;
	
	
	//To Declare The Queries that are Role-Specific..
	@Value("${spring.queries.roles-query}")
	private String rolesQuery;
	
	
	//To Configure The Authentication Procedure and manage the Authentication Procedure..
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.
		jdbcAuthentication()
		.usersByUsernameQuery(customersQuery)
		.authoritiesByUsernameQuery(rolesQuery)
		.dataSource(dataSource)
		.passwordEncoder(bCryptPasswordEncoder);
	}
	
	
	//Configure the Request Sent By the Customer Or the Admin and Configure Which Requests are to be Permitted..
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.
		authorizeRequests()
		.antMatchers("/").permitAll()
		.antMatchers("/login").permitAll()
		.antMatchers("/registration").permitAll()
		.antMatchers("/payment").permitAll()
		.antMatchers("customers/**").hasAuthority("USER").anyRequest()
		.authenticated().and().csrf().disable().formLogin()
		.loginPage("/login").failureUrl("/login?error=true")
		.defaultSuccessUrl("/customers/index")
		.usernameParameter("email")
		.passwordParameter("password")
		.and().logout()
		.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
		.logoutSuccessUrl("/").and().exceptionHandling()
		.accessDeniedPage("/access-denied");
	}
	
	
	//Configure the External Folders in the project like CSS, JAVASCRIPT,ETC...
	@Override
	public void configure(WebSecurity web) throws Exception {
		web
		.ignoring()
		.antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}
}

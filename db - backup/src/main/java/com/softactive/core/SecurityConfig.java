package com.softactive.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	  protected void configure(HttpSecurity http) throws Exception {
	    // require all requests to be authenticated except for the resources
	    http.authorizeRequests().antMatchers("/javax.faces.resource/**")
	        .permitAll().anyRequest().authenticated();
	    // login
	    http.formLogin().loginPage("/login.xhtml").permitAll()
	        .failureUrl("/login.xhtml?error=true");
	    // logout
	    http.logout().logoutSuccessUrl("/login.xhtml");
	    // not needed as JSF 2.2 is implicitly protected against CSRF
	    http.csrf().disable();
	  }

	  @Autowired
	  public void configureGlobal(AuthenticationManagerBuilder auth)
	      throws Exception {
	    auth.inMemoryAuthentication().withUser("john.doe")
	        .password("1234").roles("USER").and().withUser("jane.doe")
	        .password("5678").roles("ADMIN");
	  }
}

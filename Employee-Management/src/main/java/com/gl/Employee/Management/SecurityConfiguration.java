package com.gl.Employee.Management;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import com.gl.Employee.Management.model.AppUserService;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		//  /** -->means any url starting with /

		http.csrf().disable().authorizeRequests().requestMatchers("/**").authenticated().and().formLogin();
		return http.build();
	}

	/*

	// in memory
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user=User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		UserDetails admin=User.withDefaultPasswordEncoder()
				.username("admin")
				.password("password")
				.roles("ADMIN")
				.build();

		UserDetails user2=User.withDefaultPasswordEncoder()
				.username("nimal")
				.password("password")
				.roles("USER","ADMIN")
				.build();

		return new InMemoryUserDetailsManager(user,admin,user2);
	}
	 */


	//for Database
	//here after instead of userdetailservice we have to appuserservice
	@Bean
	public UserDetailsService userDetailsService() {
		return new AppUserService();
	}

	//we are going to using bycrypt method
	@Bean
	public PasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	//definition how the authentication must happen
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider provider= new DaoAuthenticationProvider();

		provider.setPasswordEncoder(encoder());
		provider.setUserDetailsService(userDetailsService());

		return provider;
	}

}

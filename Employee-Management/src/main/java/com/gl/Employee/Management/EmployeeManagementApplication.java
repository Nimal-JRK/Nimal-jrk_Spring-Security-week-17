package com.gl.Employee.Management;

import java.util.HashSet;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gl.Employee.Management.model.AppUser;
import com.gl.Employee.Management.model.AppUserService;
import com.gl.Employee.Management.model.EmployeeService;


@SpringBootApplication
public class EmployeeManagementApplication implements CommandLineRunner{

	@Autowired
	AppUserService appUserService;

	@Autowired
	EmployeeService employeeService;
	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementApplication.class, args);
	}



	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		Set<String> authAdmin= new HashSet<>();
		authAdmin.add("Admin");


		//create encode object
		PasswordEncoder en= new BCryptPasswordEncoder();
		AppUser appAdmin= new AppUser(1, "admin", "admin", en.encode("adminPassword"), authAdmin);
		appUserService.add(appAdmin);

	}


}

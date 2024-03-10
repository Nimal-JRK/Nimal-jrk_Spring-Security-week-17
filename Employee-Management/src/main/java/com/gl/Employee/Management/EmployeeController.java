package com.gl.Employee.Management;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gl.Employee.Management.model.AppUser;
import com.gl.Employee.Management.model.AppUserService;
import com.gl.Employee.Management.model.Employee;
import com.gl.Employee.Management.model.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;


@RestController
public class EmployeeController {

	@Autowired
	EmployeeService service;

	@Autowired
	AppUserService appUserService;

   
	@GetMapping("/emp/showall")
	public List<Employee> showAll(Model data) {
		List<Employee>teachers=service.getAll();
		data.addAttribute("teacher",teachers);
		return teachers;

	}
	@GetMapping("/emp/getEmployeeById")
	public Employee getById(@RequestParam int id) {
		Employee e1= new Employee(id, null, null, null);
		return service.getById(id);
	}
	@Operation(summary = "Only admins are allowed to add")
	@PostMapping("/emp/addUser")
	public String addUser(@RequestParam int id, @RequestParam String userName, @RequestParam String password, Authentication authentication, SecurityContextHolder auth) {
		String acceptedRole = "admin"; // for db
		boolean roleFound = false;

		//who is currently logging in
		System.out.println("Current login by: " + authentication.getName());

		//find the role of the person who have logged
		@SuppressWarnings("static-access")
		Collection<?extends GrantedAuthority> grantedRoles = auth.getContext().getAuthentication().getAuthorities();
		//above - getting the role(s) mapped with the user

		for (int i=0; i<grantedRoles.size(); i++) {
			String role = grantedRoles.toArray()[i].toString();
			System.out.println("My Role: " + role);

			if (role.equalsIgnoreCase(acceptedRole)) {
				roleFound = true;
			}
		}
		if (roleFound) {
			Set<String> authUser = new HashSet<>();
			authUser.add("User");

			//create encode object
			PasswordEncoder en = new BCryptPasswordEncoder();

			AppUser appUser = new AppUser(id, userName, userName, en.encode(password), authUser);
			appUserService.add(appUser);
			return "Sucessfully registered the user";
		}
		return "Only admin is allowed to add and make changes";
	}
	@Operation(summary = "Only admins are allowed to add")
	@PostMapping("/emp/addform")
	public String addForm(Authentication authentication,SecurityContextHolder auth,@RequestParam int id,@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email,Model data) {
		String acceptedROle="admin";//for in db

		boolean roleFound=false;

		//who is currently loggin in
		System.out.println("Current login by :"+authentication.getName());

		//find the role of the person who have logged
		Collection<?extends GrantedAuthority> grantedRoles=auth.getContext().getAuthentication().getAuthorities();//get the roles mapped with user

		for(int i=0;i<grantedRoles.size();i++) {
			String role=grantedRoles.toArray()[i].toString();
			System.out.println("my role :"+role);
			if(role.equalsIgnoreCase(acceptedROle)) {
				roleFound=true;
			}

		}
		if(roleFound) {
			Employee e1 = new  Employee(id, firstName, lastName, email) ;

			if(service.getById(id)==null) {
				service.add(e1);
				return "Book Added";
			}
			else {

				return "Duplicate entry";

			}
		}
		return"Only admin is allowed to add and make changes";

	}

	@Operation(summary = "Only admins are allowed to delete")
	@DeleteMapping("/emp/delete-Employee")
	public String deleteEmployee(Model data,@RequestParam int id,Authentication authentication,SecurityContextHolder auth) {
		String acceptedROle="admin";//for in db

		boolean roleFound=false;

		//who is currently loggin in
		System.out.println("Current login by :"+authentication.getName());

		//find the role of the person who have logged
		Collection<?extends GrantedAuthority> grantedRoles=auth.getContext().getAuthentication().getAuthorities();//get the roles mapped with user

		for(int i=0;i<grantedRoles.size();i++) {
			String role=grantedRoles.toArray()[i].toString();
			System.out.println("my role :"+role);
			if(role.equalsIgnoreCase(acceptedROle)) {
				roleFound=true;
			}

		}
		if(roleFound) {

			Employee e1= new Employee(id, null, null, null);
			service.delete(e1);



			return "Deleted Employee -"+id;

		}
		else {

			return "Only admin is allowed to add and make changes";
		}

	}


	@Operation(summary = "Only admins are allowed to update")
	@PutMapping("/emp/update")
	public Employee update(Authentication authentication,SecurityContextHolder auth,@RequestParam int id,@RequestParam String firstName,@RequestParam String lastName,@RequestParam String email) {

		String acceptedROle="admin";//for in db

		boolean roleFound=false;

		//who is currently loggin in
		System.out.println("Current login by :"+authentication.getName());

		//find the role of the person who have logged
		Collection<?extends GrantedAuthority> grantedRoles=auth.getContext().getAuthentication().getAuthorities();//get the roles mapped with user

		for(int i=0;i<grantedRoles.size();i++) {
			String role=grantedRoles.toArray()[i].toString();
			System.out.println("my role :"+role);
			if(role.equalsIgnoreCase(acceptedROle)) {
				roleFound=true;
			}

		}
		if(roleFound) {


			Employee e1 = new Employee(id, firstName, lastName, email);

			service.update(e1);



			return e1;



		}
		return null;


	}
	@Operation(summary = "Send Direction as 1 for ascending and others for descending.")
	@GetMapping("/emp/sort")
	public List<Employee> getBySort(@RequestParam int direction,@RequestParam String columnName) {

		if (direction == 1) {
			List<Employee> lib =service.getBySortOnly(Direction.ASC, columnName);
			return lib;
		}
		else {
			List<Employee> lib = service.getBySortOnly(Direction.DESC, columnName);
			return lib;
		}

	}

	@Operation(summary = "getting by firstname by giving exact name")
	@GetMapping("/emp/getByFirstName")
	public List<Employee> getByFirstName(@RequestParam (defaultValue = "first_name") String columnName,@RequestParam String searchKey) {
		return service.filterByFirstName(columnName, searchKey);
	}



}

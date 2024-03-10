package com.gl.Employee.Management.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepo extends JpaRepository<AppUser, Integer> {

	//creating custom method using JPA 
	Optional<AppUser> findByName(String name);

	//must always start from findBy -select query with where condition
	//Column name -'name'
	//findByName(parameter -column name)



}

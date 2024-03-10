package com.gl.Employee.Management.model;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;


@Service
public class EmployeeService {

	@Autowired
	EmployeeRepo repo;

	public void add(Employee emp) {
		repo.save(emp);
	}
	public void update(Employee emp) {
		repo.save(emp);
	}
	public void delete(Employee emp) {
		repo.delete(emp);
	}
	public List<Employee> getAll(){
		return repo.findAll();
	}
	public Employee getById(int id) {
		Optional<Employee>optRep=repo.findById(id);
		Employee rep=null;
		if(!optRep.isEmpty()) {
			rep=optRep.get();
		}
		return rep;

	}
	public List<Employee> getBySortOnly(Direction direction, String columnName) {
		return repo.findAll(Sort.by(direction, columnName));
	}

	public List<Employee> filterByFirstName(String columnName, String searchKey) {
		//1. Create a dummy object based on the searchKey
		Employee dummy = new Employee();
		dummy.setFirstName(searchKey);


		//2. Create Example JPA - where
		ExampleMatcher exMatcher = ExampleMatcher.matching().withMatcher(columnName, ExampleMatcher.GenericPropertyMatchers.exact()).withIgnorePaths("id", "last_name", "email");

		//3. Combining Dummy with Where
		Example<Employee> example = Example.of(dummy, exMatcher);

		return repo.findAll(example);
	}



}

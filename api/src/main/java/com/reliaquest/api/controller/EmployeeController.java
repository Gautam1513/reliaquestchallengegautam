package com.reliaquest.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reliaquest.api.pojo.Employee;
import com.reliaquest.api.pojo.EmployeeInput;
import com.reliaquest.api.service.EmployeeService;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController implements IEmployeeController<Employee, EmployeeInput> {

	private final EmployeeService employeeService;

	// using constructor based DI, which is preferred over field and setter based
	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}

	/**
	 * This API is used to fetch list of all employees
	 */
	@GetMapping
	public ResponseEntity<List<Employee>> getAllEmployees() {
		List<Employee> employees = employeeService.getAllEmployees();
		return employees != null ? ResponseEntity.ok(employees) : ResponseEntity.noContent().build();
	}

	/**
	 * This API is used to fetch all employees matching the search criteria
	 */
	public ResponseEntity<List<Employee>> getEmployeesByNameSearch(
			@PathVariable(value = "searchString") String searchString) {
		List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);
		return employees != null ? ResponseEntity.ok(employees) : ResponseEntity.noContent().build();
	}

	/**
	 * This API is used to fetch the details of particular employee
	 */
	public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
		Employee employee = employeeService.getEmployeeById(id);
		return employee != null ? ResponseEntity.ok(employee) : ResponseEntity.notFound().build();
	}

	/**
	 * This API is used to fetch the highest salary
	 */
	public ResponseEntity<Integer> getHighestSalaryOfEmployees() {
		Integer highestSalary = employeeService.getHighestSalaryOfEmployees();
		return highestSalary != null ? ResponseEntity.ok(highestSalary) : ResponseEntity.noContent().build();
	}

	/**
	 * This API is used to fetch the names of the employees earning top 10 salaries
	 */
	public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
		List<String> topTenNames = employeeService.getTopTenHighestEarningEmployeeNames();
		return topTenNames != null ? ResponseEntity.ok(topTenNames) : ResponseEntity.noContent().build();
	}

	/**
	 * This API is used to create a new employee
	 */
	@PostMapping
	public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeInput employeeInput) {
		Employee employee = employeeService.createEmployee(employeeInput);
		return employee != null ? ResponseEntity.status(201).body(employee) : ResponseEntity.badRequest().build();
	}

	/**
	 * This API is used to delete the employee with given id
	 */
	public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") String id) {
		employeeService.deleteEmployeeById(id);
		return ResponseEntity.ok("Employee with ID " + id + " deleted successfully.");
	}
}

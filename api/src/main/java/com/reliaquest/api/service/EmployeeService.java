package com.reliaquest.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.reliaquest.api.exception.EmployeeNotFoundException;
import com.reliaquest.api.pojo.Employee;
import com.reliaquest.api.pojo.EmployeeInput;
import com.reliaquest.api.pojo.EmployeeListResponse;
import com.reliaquest.api.pojo.EmployeeResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmployeeService {
	private static final String API_URL = "http://localhost:8112/api/v1/employee";
	private final RestTemplate restTemplate;

	public EmployeeService(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	/**
	 * This method is used to fetch list of all employees from the downstream
	 * application using rest template
	 * 
	 * @return
	 */
	public List<Employee> getAllEmployees() {
		log.info("Fetching all employees from downstream API");
		EmployeeListResponse response = restTemplate.getForObject(API_URL, EmployeeListResponse.class);
		if (response == null || response.getData() == null) {
			log.warn("No employees found.");
			return new ArrayList<>();
		}
		log.info("Successfully fetched {} employee.", response.getData().size());
		return response.getData();
	}

	/**
	 * This method is used to fetch list of all employees matching with search
	 * criteria from the downstream application using rest template
	 * 
	 * @param searchString
	 * @return
	 */
	public List<Employee> getEmployeesByNameSearch(String searchString) {
		log.info("Searching employees with name containing '{}'.", searchString);
		String url = API_URL + "/search/" + searchString;
		EmployeeListResponse response = restTemplate.getForObject(url, EmployeeListResponse.class);
		if (response == null || response.getData() == null) {
			log.warn("No employees found for search '{}'.", searchString);
			return new ArrayList<>();
		}
		log.info("Found {} employees for search '{}'.", response.getData().size(), searchString);
		return response.getData();
	}

	/**
	 * This method is used to fetch the employee with given id from the downstream
	 * application using rest template
	 * 
	 * @param id
	 * @return
	 */
	public Employee getEmployeeById(String id) {
		EmployeeResponse response = null;
		log.info("Fetching employee by ID: {}", id);
		String url = API_URL + "/" + id;
		try {
			response = restTemplate.getForObject(url, EmployeeResponse.class);
			if (response != null && response.getData() != null) {
				log.info("Successfully fetched employee with ID {}.", id);
			}
		} catch (Exception e) {
			throw new EmployeeNotFoundException("Employee with ID " + id + " not found");
		}
		return response.getData();
	}

	/**
	 * This method is used to fetch the highest salary from the downstream
	 * application using rest template
	 * 
	 * @return
	 */
	public Integer getHighestSalaryOfEmployees() {
		log.info("Fetching highest salary of employees.");
		EmployeeListResponse response = restTemplate.getForObject(API_URL, EmployeeListResponse.class);
		if (response != null && response.getData() != null) {
			int highestSalary = response.getData().stream().mapToInt(Employee::getEmployeeSalary).max().orElse(0);
			log.info("Highest salary fetched: {}", highestSalary);
			return highestSalary;
		}
		log.warn("No employees found to calculate highest salary.");
		return null;
	}

	/**
	 * This method is used to fetch the names of employees with top ten salaries
	 * from the downstream application using rest template
	 * 
	 * @return
	 */
	public List<String> getTopTenHighestEarningEmployeeNames() {
		log.info("Fetching top 10 highest-earning employee names.");
		EmployeeListResponse response = restTemplate.getForObject(API_URL, EmployeeListResponse.class);
		if (response != null && response.getData() != null) {
			List<String> topTenNames = response.getData().stream()
					.sorted((e1, e2) -> Integer.compare(e2.getEmployeeSalary(), e1.getEmployeeSalary())).limit(10)
					.map(Employee::getEmployeeName).toList();
			log.info("Top 10 employee names fetched successfully.");
			return topTenNames;
		}
		log.warn("No employees found to fetch top 10 names.");
		return new ArrayList<>();
	}

	/**
	 * This method is used to create a new employee in the downstream application
	 * using rest template
	 * 
	 * @param employeeInput
	 * @return
	 */
	public Employee createEmployee(EmployeeInput employeeInput) {
		log.info("Creating new employee with name '{}'.", employeeInput.getName());
		EmployeeResponse response = restTemplate.postForObject(API_URL, employeeInput, EmployeeResponse.class);
		if (response == null || response.getData() == null) {
			log.error("Failed to create employee with name '{}'.", employeeInput.getName());
			return null;
		}
		log.info("Employee created successfully with ID {}.", response.getData().getId());
		return response.getData();
	}

	/**
	 * This method is used to create a delete given employee in the downstream
	 * application using rest template
	 * 
	 * @param id
	 */
	public void deleteEmployeeById(String id) {
		log.info("Deleting employee by ID: {}", id);
		String url = API_URL + "/" + id;
		restTemplate.delete(url);
		log.info("Employee with ID {} deleted successfully.", id);
	}
}

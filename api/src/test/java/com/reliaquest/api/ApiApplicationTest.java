package com.reliaquest.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import com.reliaquest.api.pojo.Employee;
import com.reliaquest.api.pojo.EmployeeInput;
import com.reliaquest.api.pojo.EmployeeListResponse;
import com.reliaquest.api.pojo.EmployeeResponse;
import com.reliaquest.api.service.EmployeeService;

@ExtendWith(MockitoExtension.class)
public class ApiApplicationTest {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private EmployeeService employeeService;

	public ApiApplicationTest() {
		MockitoAnnotations.openMocks(this);
	}

	@BeforeEach
	public void setUp() {
		employeeService = new EmployeeService(restTemplate);
	}

	@Test
	void testGetAllEmployees() {
		EmployeeListResponse mockResponse = new EmployeeListResponse();
		mockResponse.setData(Arrays.asList(new Employee("1", "Alice", 50000), new Employee("2", "Bob", 60000)));

		when(restTemplate.getForObject("http://localhost:8112/api/v1/employee", EmployeeListResponse.class))
				.thenReturn(mockResponse);

		List<Employee> employees = employeeService.getAllEmployees();

		assertNotNull(employees);
		assertEquals(2, employees.size());
		assertEquals("Alice", employees.get(0).getEmployeeName());
	}

	@Test
	void testGetEmployeesByNameSearch() {
		EmployeeListResponse mockResponse = new EmployeeListResponse();
		mockResponse.setData(Arrays.asList(new Employee("1", "Alice", 50000)));

		String searchString = "Alice";
		when(restTemplate.getForObject("http://localhost:8112/api/v1/employee/search/" + searchString,
				EmployeeListResponse.class)).thenReturn(mockResponse);

		List<Employee> employees = employeeService.getEmployeesByNameSearch(searchString);

		assertNotNull(employees);
		assertEquals(1, employees.size());
		assertEquals("Alice", employees.get(0).getEmployeeName());
	}

	@Test
	void testGetEmployeeById() {
		EmployeeResponse mockResponse = new EmployeeResponse();
		mockResponse.setData(new Employee("1", "Alice", 50000));

		String employeeId = "1";
		when(restTemplate.getForObject("http://localhost:8112/api/v1/employee/" + employeeId, EmployeeResponse.class))
				.thenReturn(mockResponse);

		Employee employee = employeeService.getEmployeeById(employeeId);

		assertNotNull(employee);
		assertEquals("Alice", employee.getEmployeeName());
		assertEquals(50000, employee.getEmployeeSalary());
	}

	@Test
	void testGetHighestSalaryOfEmployees() {
		EmployeeListResponse mockResponse = new EmployeeListResponse();
		mockResponse.setData(Arrays.asList(new Employee("1", "Alice", 50000), new Employee("2", "Bob", 70000)));

		when(restTemplate.getForObject("http://localhost:8112/api/v1/employee", EmployeeListResponse.class))
				.thenReturn(mockResponse);

		Integer highestSalary = employeeService.getHighestSalaryOfEmployees();

		assertNotNull(highestSalary);
		assertEquals(70000, highestSalary);
	}

	@Test
	void testGetTopTenHighestEarningEmployeeNames() {
		EmployeeListResponse mockResponse = new EmployeeListResponse();
		mockResponse.setData(Arrays.asList(new Employee("1", "Alice", 50000), new Employee("2", "Bob", 70000),
				new Employee("3", "Charlie", 60000), new Employee("4", "Dave", 80000)));

		when(restTemplate.getForObject("http://localhost:8112/api/v1/employee", EmployeeListResponse.class))
				.thenReturn(mockResponse);

		List<String> topTenNames = employeeService.getTopTenHighestEarningEmployeeNames();

		assertNotNull(topTenNames);
		assertEquals(4, topTenNames.size());
		assertEquals("Dave", topTenNames.get(0));
	}

	@Test
	void testCreateEmployee() {
		EmployeeInput employeeInput = new EmployeeInput("Alice", 50000, 35, "Technical Lead", "alice.hayes@gmail.com");
		EmployeeResponse mockResponse = new EmployeeResponse();
		mockResponse.setData(new Employee("1", "Alice", 50000));

		when(restTemplate.postForObject("http://localhost:8112/api/v1/employee", employeeInput, EmployeeResponse.class))
				.thenReturn(mockResponse);

		Employee createdEmployee = employeeService.createEmployee(employeeInput);

		assertNotNull(createdEmployee);
		assertEquals("Alice", createdEmployee.getEmployeeName());
		assertEquals(50000, createdEmployee.getEmployeeSalary());
	}

	// NOTE: the downstream application running at port 8112 does not support DELETE
	// endpoint
//	@Test
//	void testDeleteEmployeeById() {
//		String employeeId = "1";
//		doNothing().when(restTemplate).delete("http://localhost:8112/api/v1/employee/" + employeeId);
//
//		assertDoesNotThrow(() -> employeeService.deleteEmployeeById(employeeId));
//
//		verify(restTemplate, times(1)).delete("http://localhost:8112/api/v1/employee/" + employeeId);
//	}
}

package com.reliaquest.api.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Employee {
	private String id;
	@JsonProperty(value = "employee_name")
	private String employeeName;
	@JsonProperty(value = "employee_salary")
	private Integer employeeSalary;
	@JsonProperty(value = "employee_age")
	private Integer employeeAge;
	@JsonProperty(value = "employee_title")
	private String employeeTitle;
	@JsonProperty(value = "employee_email")
	private String employeeEmail;

	public Employee() {

	}

	// Constructor
	public Employee(String id, String employeeName, int employeeSalary) {
		this.id = id;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
	}

	// Constructor
	public Employee(String id, String employeeName, int employeeSalary, int employeeAge, String employeeTitle,
			String employeeEmail) {
		this.id = id;
		this.employeeName = employeeName;
		this.employeeSalary = employeeSalary;
		this.employeeAge = employeeAge;
		this.employeeTitle = employeeTitle;
		this.employeeEmail = employeeEmail;
	}
}

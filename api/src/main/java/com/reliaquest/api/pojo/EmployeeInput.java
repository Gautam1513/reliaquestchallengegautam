package com.reliaquest.api.pojo;

import lombok.Data;

@Data
public class EmployeeInput {
	private String name;
	private Integer salary;
	private Integer age;
	private String title;
	private String email;

	public EmployeeInput() {

	}

	public EmployeeInput(String name, Integer salary, Integer age, String title, String email) {
		this.name = name;
		this.salary = salary;
		this.age = age;
		this.title = title;
		this.email = email;
		validate();
	}

	private void validate() {
		validateName();
		validateSalary();
		validateAge();
		validateTitle();
	}

	private void validateName() {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Name must not be blank");
		}
	}

	private void validateSalary() {
		if (salary == null || salary <= 0) {
			throw new IllegalArgumentException("Salary must be greater than zero");
		}
	}

	private void validateAge() {
		if (age == null || age < 16 || age > 75) {
			throw new IllegalArgumentException("Age must be between 16 and 75");
		}
	}

	private void validateTitle() {
		if (title == null || title.trim().isEmpty()) {
			throw new IllegalArgumentException("Title must not be blank");
		}
	}
}

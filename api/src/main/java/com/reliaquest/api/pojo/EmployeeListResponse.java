package com.reliaquest.api.pojo;

import java.util.List;

import lombok.Data;

@Data
public class EmployeeListResponse {
	private List<Employee> data;
    private String status;
}

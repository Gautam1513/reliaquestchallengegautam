package com.reliaquest.api.pojo;

import lombok.Data;

@Data
public class ErrorResponse {
	private String message;
	private int status;
}

package com.sbi.example.model;

import lombok.Data;

@Data
public class UserRequest {
	private String firstName;
	private String lastName;
	private String email;
	private String phoneNumber;
	private String password;
	private String confirmPassword;
}

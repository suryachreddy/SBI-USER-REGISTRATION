package com.sbi.example.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class UserRequest {
	@NotBlank(message = "First Name must not be empty")
	private String firstName;
	private String lastName;
	@NotBlank(message = "Email must not be empty")
	@Pattern(regexp = "^[a-zA-Z0-9._%+-]+@gmail\\.com$", message = "Email must be a valid Gmail address")
	private String email;
	@NotBlank(message = "Phone number must not be empty")
	@Pattern(regexp = "^\\d{10}$", message = "Phone number must be exactly 10 digits")
	private String phoneNumber;
	@NotBlank(message = "Password must not be empty")
	private String password;
	@NotBlank(message = "Confirm Password must not be empty")
	private String confirmPassword;
	//private long accountNumber;
}

package com.sbi.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sbi.example.model.UserRequest;
import com.sbi.example.service.UserService;

@RestController
public class UserController {
	
	@Autowired
	UserService userService;

	@PostMapping("/register")
	public String register(@RequestBody UserRequest userRequest) {
		String response = userService.saveUser(userRequest);
		return response;
	}
}

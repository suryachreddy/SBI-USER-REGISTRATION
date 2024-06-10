package com.sbi.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbi.example.model.UserEnitity;
import com.sbi.example.model.UserRequest;
import com.sbi.example.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	public String saveUser(UserRequest userRequest) {
		
		UserEnitity userEnitity = new UserEnitity();
		userEnitity.setFirstName(userRequest.getFirstName());
		userEnitity.setLastName(userRequest.getLastName());
		userEnitity.setEmail(userRequest.getEmail());
		userEnitity.setPhoneNumber(userRequest.getPhoneNumber());
		userEnitity.setPassword(userRequest.getPassword());
		userEnitity.setConfirmPassword(userRequest.getConfirmPassword());
		
		userRepository.save(userEnitity);
		
		return "User successfully registered into the system, Please login";
		
	}
}

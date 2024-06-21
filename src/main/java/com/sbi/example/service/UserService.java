package com.sbi.example.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbi.example.constants.ApiConstants;
import com.sbi.example.model.UserEnitity;
import com.sbi.example.model.UserRequest;
import com.sbi.example.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	ModelMapper mapper = new ModelMapper();
 
	public String saveUser(UserRequest userRequest) {
		
		/*
		 * UserEnitity userEnitity = new UserEnitity();
		 * 
		 * userEnitity.setFirstName(userRequest.getFirstName());
		 * userEnitity.setLastName(userRequest.getLastName());
		 * userEnitity.setEmail(userRequest.getEmail());
		 * userEnitity.setPhoneNumber(userRequest.getPhoneNumber());
		 * userEnitity.setPassword(userRequest.getPassword());
		 * userEnitity.setConfirmPassword(userRequest.getConfirmPassword());
		 */		 
		
		UserEnitity savedEnitity = userRepository.findByEmail(userRequest.getEmail());
		if(null != savedEnitity) {
			return ApiConstants.REGISTER_FAILED;
		}
		
		UserEnitity userEnitity = mapper.map(userRequest, UserEnitity.class);		
		
		userRepository.save(userEnitity);
		
		return ApiConstants.SUCCESSFULLY_REGISTER;
		
	}
}

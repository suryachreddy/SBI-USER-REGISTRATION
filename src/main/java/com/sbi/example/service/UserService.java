package com.sbi.example.service;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbi.example.constants.ApiConstants;
import com.sbi.example.model.UserEntity;
import com.sbi.example.model.UserRequest;
import com.sbi.example.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	ModelMapper mapper = new ModelMapper();
 
	public String saveUser(UserRequest userRequest) {
		UserEntity savedEntity = userRepository.findByEmail(userRequest.getEmail());
		if(null != savedEntity) {
			return ApiConstants.REGISTER_FAILED;
		}
		UserEntity userEntity = mapper.map(userRequest, UserEntity.class);
		userRepository.save(userEntity);
		return ApiConstants.SUCCESSFULLY_REGISTER;
	}

	public UserRequest getUserByMailId(String emailId) {
		UserEntity userEntity = userRepository.findByEmail(emailId);
		UserRequest user = mapper.map(userEntity, UserRequest.class);
		log.info("user details based on emailId: {}", user);
		return user;
	}

	@Transactional
	public boolean deleteUser(String emailId) {
		boolean isUserDeleted = false;
		UserEntity userEntity = userRepository.findByEmail(emailId);
		if (Objects.nonNull(userEntity)) {
			userRepository.deleteByEmail(emailId);
			isUserDeleted = true;
			log.info("user deleted successfully");
		}
		log.info("user not found by the given email-id: {}", emailId);
		return isUserDeleted;
	}
}

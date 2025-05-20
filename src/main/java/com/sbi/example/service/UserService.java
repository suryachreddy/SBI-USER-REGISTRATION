package com.sbi.example.service;

import com.sbi.example.model.BalanceEnquiryEntity;
import com.sbi.example.repository.BalanceEnquiryRepository;
import com.sbi.example.util.RandomNumberGenerator;
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
import java.util.Optional;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	BalanceEnquiryRepository balanceEnquiryRepository;
	
	ModelMapper mapper = new ModelMapper();
 
	public String saveUser(UserRequest userRequest) {
		if (!(userRequest.getPassword().equals(userRequest.getConfirmPassword()))) {
			return ApiConstants.PASSWORD_VALIDATION;
		}
		UserEntity savedEntity = userRepository.findByEmail(userRequest.getEmail());
		if(null != savedEntity) {
			return ApiConstants.REGISTER_FAILED;
		}
		UserEntity userEntity = mapper.map(userRequest, UserEntity.class);
		long accountNumber = RandomNumberGenerator.generate12DigitNumber();
		log.info("Generated account number for user: {}, accountNumber: {}", userEntity.getEmail(), accountNumber);
		userEntity.setAccountNumber(accountNumber);
		userRepository.save(userEntity);
		// saving bank account info into the BalanceEnquiry table
		BalanceEnquiryEntity balanceEnquiry = new BalanceEnquiryEntity();
		balanceEnquiry.setAccountNumber(accountNumber);
		balanceEnquiryRepository.save(balanceEnquiry);
		return ApiConstants.SUCCESSFULLY_REGISTER + accountNumber;
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
		log.info("user not found for the given email-id: {}", emailId);
		return isUserDeleted;
	}

	public String updateUser(int id, UserRequest userRequest) {
		String userUpdated;
		if (!(userRequest.getPassword().equals(userRequest.getConfirmPassword()))) {
			return ApiConstants.PASSWORD_VALIDATION;
		}
		Optional<UserEntity> userEntity = userRepository.findById(id);
		if (userEntity.isPresent()) {
			UserEntity userPresent = userEntity.get();
			userPresent.setFirstName(userRequest.getFirstName());
			userPresent.setLastName(userRequest.getLastName());
			userPresent.setPassword(userRequest.getPassword());
			userPresent.setConfirmPassword(userRequest.getConfirmPassword());
			userRepository.save(userPresent);
			log.info("user update successfully for id : {}", id);
			userUpdated = ApiConstants.USER_UPDATED_SUCCESSFULLY;
		} else {
			userUpdated = ApiConstants.USER_NOT_FOUND_BY_ID;
			log.info("user not found by id : {}", id);
		}
		return userUpdated;
	}
}

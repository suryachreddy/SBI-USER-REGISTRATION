package com.sbi.example.service;

import com.sbi.example.model.*;
import com.sbi.example.util.RandomNumberGenerator;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sbi.example.constants.ApiConstants;
import com.sbi.example.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
@Slf4j
public class UserService {
	
	@Autowired
	UserRepository userRepository;

	@Autowired
	RestTemplate restTemplate;

	@Value("${transactionurl}")
    String transactionServiceBaseUrl;

	ModelMapper mapper = new ModelMapper();
 
	public String saveUser(UserRequest userRequest) {
		if (!(userRequest.getPassword().equals(userRequest.getConfirmPassword()))) {
			return ApiConstants.PASSWORD_VALIDATION;
		}
		Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(userRequest.getEmail());
		if(optionalUserEntity.isPresent()) {
			return ApiConstants.REGISTER_FAILED;
		}
		UserEntity userEntity = mapper.map(userRequest, UserEntity.class);
		long accountNumber = RandomNumberGenerator.generate12DigitNumber();
		log.info("Generated account number for user: {}, accountNumber: {}", userEntity.getEmail(), accountNumber);
		userRepository.save(userEntity);
		// calling next service to save the account details
		AccountDetails accountDetails = new AccountDetails();
		accountDetails.setAccountNumber(accountNumber);
		accountDetails.setEmail(userRequest.getEmail());
		ResponseEntity<String> response = postAccountDetails(accountDetails);
		log.info("receiving response from the transaction service : {}", response.getBody());
		return ApiConstants.SUCCESSFULLY_REGISTER + accountNumber;
	}

	public UserResponse getUserByMailId(String emailId) {
		UserResponse user = null;
		Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(emailId);
		if (optionalUserEntity.isPresent()) {
			UserEntity existingUser = optionalUserEntity.get();
			user = mapper.map(optionalUserEntity.get(), UserResponse.class);
			log.info("user details based on emailId: {}", user);
			//calling account enquiry endpoint from the transaction service
			ResponseEntity<BalanceEnquiryResponse> accountDetails = getAccountDetails(emailId);
			if (!ObjectUtils.isEmpty(accountDetails)) {
				BalanceEnquiryResponse balanceEnquiryResponse = accountDetails.getBody();
				log.info("account details from the Transaction service :{}", balanceEnquiryResponse);
				user.setAccountNumber(balanceEnquiryResponse.getAccountNumber());
				user.setAvailableBalance(balanceEnquiryResponse.getAvailableBalance());
				user.setLastCreditedAmount(balanceEnquiryResponse.getLastCreditedAmount());
			}
		}
		return user;
	}

	@Transactional
	public boolean deleteUser(String emailId) {
		boolean isUserDeleted = false;
		Optional<UserEntity> optionalUserEntity = userRepository.findByEmail(emailId);
		if (optionalUserEntity.isPresent()) {
			userRepository.deleteByEmail(emailId);
			isUserDeleted = true;
			log.info("user deleted successfully");
		}
		log.info("user not found for the given email-id: {}", emailId);
		return isUserDeleted;
	}

	public String updateUser(Long id, UserRequest userRequest) {
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

	private ResponseEntity<String> postAccountDetails(AccountDetails accountDetails) {
		String baseUrl = transactionServiceBaseUrl;
		log.info("transaction service url : {}, request body : {}", baseUrl, accountDetails);
		return restTemplate.postForEntity(baseUrl, accountDetails, String.class);
	}

	private ResponseEntity<BalanceEnquiryResponse> getAccountDetails(String email) {
		String baseUrl = transactionServiceBaseUrl+"/"+email;
		log.info("transaction service get account details url : {}", baseUrl);
		return restTemplate.getForEntity(baseUrl, BalanceEnquiryResponse.class);
	}
}

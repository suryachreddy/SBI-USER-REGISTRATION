package com.sbi.example.controller;

import com.sbi.example.constants.ApiConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.sbi.example.model.UserRequest;
import com.sbi.example.service.UserService;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@Slf4j
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/register")
	public ResponseEntity<Object> register(@Valid @RequestBody UserRequest userRequest) {
        log.info("user register input payload: {}", userRequest);
		String response = userService.saveUser(userRequest);
		if (response.equals(ApiConstants.PASSWORD_VALIDATION) || response.equals(ApiConstants.REGISTER_FAILED)) {
			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/user/{emailId}")
	public ResponseEntity<Object> userDetails(@PathVariable String emailId) {
		log.info("input emailId for user details: {}", emailId);
		UserRequest user = userService.getUserByMailId(emailId);
		if (Objects.nonNull(user)) {
			return new ResponseEntity<>(userService.getUserByMailId(emailId), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@DeleteMapping("/user/{emailId}")
	public ResponseEntity<Object> deleteUser(@PathVariable String emailId) {
		log.info("input emailId for user deletion: {}", emailId);
	    boolean isUserDeleted = userService.deleteUser(emailId);
		if (isUserDeleted) {
			return new ResponseEntity<>(ApiConstants.USER_DELETED_SUCCESSFULLY, HttpStatus.OK);
		}
		return new ResponseEntity<>(ApiConstants.USER_NOT_FOUND, HttpStatus.BAD_REQUEST);
	}

	@PatchMapping("/user/{id}")
	public ResponseEntity<Object> updateUser(@PathVariable Long id, @RequestBody UserRequest userRequest) {
		log.info("input - userId: {}, userRequest: {}", id, userRequest);
		String response = userService.updateUser(id, userRequest);
		if (response.equals(ApiConstants.USER_UPDATED_SUCCESSFULLY)) {
			return new ResponseEntity<>(ApiConstants.USER_UPDATED_SUCCESSFULLY, HttpStatus.OK);
		} else if(response.equals(ApiConstants.PASSWORD_VALIDATION)) {
			return new ResponseEntity<>(ApiConstants.PASSWORD_VALIDATION, HttpStatus.BAD_REQUEST);
		} else {
			return new ResponseEntity<>(ApiConstants.USER_NOT_FOUND_BY_ID, HttpStatus.NOT_FOUND);
		}
	}
}

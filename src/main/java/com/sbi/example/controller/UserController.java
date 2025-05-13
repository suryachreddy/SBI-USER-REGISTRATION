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

}

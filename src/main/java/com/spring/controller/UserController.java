package com.spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spring.exception.AuthenticationFailedException;
import com.spring.exception.InvalidCredentialsException;
import com.spring.exception.UserAlreadyExistsException;
import com.spring.model.User;
import com.spring.service.UserService;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
		logger.info("Registering user: {}", user.getUsername());
		try {
			User registeredUser = userService.registerUser(user);
			logger.info("User registered successfully: {}", registeredUser.getUsername());
			return new ResponseEntity<>(registeredUser, HttpStatus.OK);
		} catch (UserAlreadyExistsException e) {
			logger.error("User registration failed: {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (Exception e) {
			logger.error("Unexpected error during registration", e);
			return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody User user) {
		logger.info("Attempting login for user: {}", user.getUsername());
		try {
			String response = userService.verifyUser(user);
			logger.info("Login successful for user: {}", user.getUsername());
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (AuthenticationFailedException  e) {
			logger.warn("Invalid login attempt: {}", e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			logger.error("Unexpected error during login", e);
			return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

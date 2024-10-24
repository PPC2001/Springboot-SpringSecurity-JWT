package com.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.spring.exception.AuthenticationFailedException;
import com.spring.exception.UserAlreadyExistsException;
import com.spring.model.User;
import com.spring.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

	private static final Logger logger = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JWTService jwtService;

	public User registerUser(User user) {
		logger.info("Attempting to register user: {}", user.getUsername());
		// Check if the user already exists
		if (userRepository.existsByUsername(user.getUsername())) {
			logger.warn("User registration failed: User already exists with username {}", user.getUsername());
			throw new UserAlreadyExistsException("User with username " + user.getUsername() + " already exists.");
		}

		// Encode the password and save the user
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User registeredUser = userRepository.save(user);
		logger.info("User registered successfully: {}", user.getUsername());

		return registeredUser;
	}

	public String verifyUser(User user) {
		logger.info("Attempting to authenticate user: {}", user.getUsername());

		try {
			// Authenticate the user
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

			if (authentication.isAuthenticated()) {
				String token = jwtService.generateToken(user.getUsername());
				logger.info("Authentication successful for user: {}. Token generated.", token);
				return token;
			} else {
				logger.warn("Authentication failed for user: {}", user.getUsername());
				throw new AuthenticationFailedException("Authentication failed for user: " + user.getUsername());
			}

		} catch (BadCredentialsException e) {
			logger.warn("Invalid credentials provided for user: {}", user.getUsername());
			throw new AuthenticationFailedException("Invalid username or password.");
		} catch (Exception e) {
			logger.error("Unexpected error during authentication for user: {}", user.getUsername(), e);
			throw new RuntimeException("An unexpected error occurred during authentication. Please try again later.");
		}
	}
}

package com.apps.quantitymeasurement.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.apps.quantitymeasurement.exception.DatabaseException;
import com.apps.quantitymeasurement.dto.dtoRequest.LoginRequest;
import com.apps.quantitymeasurement.dto.dtoRequest.RegisterRequest;
import com.apps.quantitymeasurement.dto.dtoResponse.AuthResponse;
import com.apps.quantitymeasurement.dto.dto_conversion.UserDTO;
import com.apps.quantitymeasurement.entity.User;
import com.apps.quantitymeasurement.exception.UnauthorizedException;
import com.apps.quantitymeasurement.repository.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;
	private PasswordEncoder passwordEncoder;
	private JwtService jwtService;
	
	public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
	}
	
	public AuthResponse registerUser(RegisterRequest registerRequest) {
		User userdb = userRepository.findByEmail(registerRequest.getEmail()).orElse(null);
		
		if(userdb!=null) {
			// Map business failure to a typed exception so the API returns a clean 4xx response.
			throw new DatabaseException("user already registered with this email");
		}
		
		registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
		userRepository.save(new UserDTO().toUser(registerRequest));
		String token = jwtService.generateToken(registerRequest.getEmail());
		return new AuthResponse(token, "User Register success");
	}
	
	
	public AuthResponse loginUser(LoginRequest loginRequest) {
		User userdb = userRepository.findByEmail(loginRequest.getEmail())
				.orElseThrow(() -> new UnauthorizedException("User not found!"));
		
		if(!passwordEncoder.matches(loginRequest.getPassword(), userdb.getPassword())) {
			throw new UnauthorizedException("Invalid Password!");
		}
		String token = jwtService.generateToken(loginRequest.getEmail());
		return new AuthResponse(token, "Login Success");
	}
}

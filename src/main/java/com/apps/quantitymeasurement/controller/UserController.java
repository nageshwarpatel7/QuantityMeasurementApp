package com.apps.quantitymeasurement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.apps.quantitymeasurement.dto.dtoRequest.LoginRequest;
import com.apps.quantitymeasurement.dto.dtoRequest.RegisterRequest;
import com.apps.quantitymeasurement.dto.dtoResponse.AuthResponse;
import com.apps.quantitymeasurement.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth/user")
@Tag(name = "User Authetication", description = "User must have to register or login for using our service")
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService= userService;
	}
	
	private static final String RegisterUser1 = """
			{
				"username":"himesh", "email":"himesh@gmail.com", "password":"himesh1234", "role":"USER"
			}
			""";
	private static final String RegisterUser2 = """
			{
				"username":"lucky", "email":"luckyu@gmail.com", "password":"lucky1234", "role":"USER"
			}
			""";
	
	private static final String LoginUser1= """
			{
				"email":"himesh@gmail.com", "password":"himesh1234"
			}
			""";
	private static final String LoginUser2= """
			{
				"email":"lucky@gmail.com", "password":"lucky1234"
			}
			""";
	
	@GetMapping("/welcome")
	public String welcome() {
		return "welcome! It is working.";
	}
	
	@PostMapping("/register")
	@Operation(summary = "Register",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				content = @Content(
						schema = @Schema(implementation = RegisterRequest.class),
						examples = {
								@ExampleObject(name = "User 1", value = RegisterUser1),
								@ExampleObject(name = "User 2", value = RegisterUser2),
						}
				)
			)
	)
	public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
		return ResponseEntity.ok(userService.registerUser(registerRequest));
	}
	
	@PostMapping("/login")
	@Operation(summary = "Login",
		requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
				content = @Content(
						schema = @Schema(implementation = LoginRequest.class),
						examples = {
								@ExampleObject(name = "User 1", value = LoginUser1),
								@ExampleObject(name = "User 2", value = LoginUser2),
						}
				)
			)
	)
	public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest){
		return ResponseEntity.ok(userService.loginUser(loginRequest));
	}
}

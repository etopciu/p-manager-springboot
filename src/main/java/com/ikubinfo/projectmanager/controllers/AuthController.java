package com.ikubinfo.projectmanager.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.ikubinfo.projectmanager.models.ERole;
import com.ikubinfo.projectmanager.models.Role;
import com.ikubinfo.projectmanager.models.User;
import com.ikubinfo.projectmanager.service.UserService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ikubinfo.projectmanager.payload.request.LoginRequest;
import com.ikubinfo.projectmanager.payload.request.SignupRequest;
import com.ikubinfo.projectmanager.payload.response.JwtResponse;
import com.ikubinfo.projectmanager.payload.response.MessageResponse;
import com.ikubinfo.projectmanager.repository.RoleRepository;
import com.ikubinfo.projectmanager.repository.UserRepository;
import com.ikubinfo.projectmanager.security.jwt.JwtUtils;
import com.ikubinfo.projectmanager.security.services.UserDetailsImpl;

@Data
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserService userService;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/signin")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
		JwtResponse jwtResponse = userService.authenticateUser(loginRequest);
		return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
		userService.registerUser(signUpRequest);
		return new ResponseEntity<>("User registered successfully!",HttpStatus.OK);
	}
}

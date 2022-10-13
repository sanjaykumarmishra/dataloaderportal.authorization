package com.dataloaderportal.authorization.controller;

import com.dataloaderportal.authorization.model.User;
import com.dataloaderportal.authorization.repository.UserRepo;
import com.dataloaderportal.authorization.payload.AuthenticationResponse;
import com.dataloaderportal.authorization.service.UserDetailService;
import org.springframework.http.MediaType;

import javax.security.auth.login.LoginException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.dataloaderportal.authorization.payload.TokenValidationResponse;
import com.dataloaderportal.authorization.payload.AuthenticationRequest;
import com.dataloaderportal.authorization.security.jwt.JwtUtil;

@RestController
@CrossOrigin
public class AuthorizationController {
	

	@Autowired
	private UserDetailService userDetailsService;

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;


	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder encoder;
	
	@PostMapping(value="/login")
	public ResponseEntity<Object> createAuthorizationToken(@RequestBody AuthenticationRequest authenticationRequest) {
		System.out.println("Inside Login");

		//Since password is encoded, we require AuthenticationManager for authentication
		//-----------------------------------------------------------
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final UserDetails userDetails= userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		return new ResponseEntity<>(new AuthenticationResponse(authenticationRequest.getUsername(),
				jwtUtil.generateToken(userDetails),jwtUtil.getCurrentTime(),jwtUtil.getExpirationTime()),HttpStatus.OK);
	}


	@GetMapping(path = "/validate",produces= MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TokenValidationResponse> validatingAuthorizationToken(@RequestHeader(name = "Authorization")String tokenDup){
		String token = tokenDup.substring(7);
		TokenValidationResponse tokenValidationResponse = new TokenValidationResponse();
		try {
			UserDetails user=userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
			if(Boolean.TRUE.equals(jwtUtil.validateToken(token, user))) {
				tokenValidationResponse.setValidStatus(true);
				return new ResponseEntity<>(tokenValidationResponse,HttpStatus.OK);
			}
			else {
				throw new LoginException("Invalid Token");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			tokenValidationResponse.setValidStatus(false);
			return new ResponseEntity<>(tokenValidationResponse,HttpStatus.BAD_REQUEST);
		}
	
	}	
	
	@GetMapping(path = "/health-check")
	public ResponseEntity<String> healthcheck(){
		return new ResponseEntity<>("ok",HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody AuthenticationRequest signUpRequest) {
		if (userRepo.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body("Error: Username is already taken!");
		}

		User user = new User(signUpRequest.getUsername(),
				encoder.encode(signUpRequest.getPassword()));

		userRepo.save(user);

		return ResponseEntity.ok("User registered successfully!");
	}


}
	



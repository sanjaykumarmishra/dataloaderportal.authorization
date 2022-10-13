package com.authorizationservice.authorization.Controller;

import com.authorizationservice.authorization.repository.AuthRequestRepo;
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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.authorizationservice.authorization.dto.AuthenticationRequestDTO;
import com.authorizationservice.authorization.dto.ValidatingDTO;
import com.authorizationservice.authorization.model.AuthenticationRequest;
import com.authorizationservice.authorization.model.AuthenticationResponse;
import com.authorizationservice.authorization.service.AuthUserDetailService;
import com.authorizationservice.authorization.util.JwtUtil.JwtUtil;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@RestController
@CrossOrigin
public class AuthorizationController {
	

	@Autowired
	private AuthUserDetailService userDetailsService;

	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private ValidatingDTO validatingDTO;

	@Autowired
	private AuthRequestRepo authRequestRepo;

	@Autowired
	private PasswordEncoder encoder;
	
	@PostMapping(value="/login")
	public ResponseEntity<Object> createAuthorizationToken(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) throws LoginException{
		System.out.println("Inside Login");
		AuthenticationRequest authenticationRequest=new AuthenticationRequest();
		authenticationRequest.setUsername(authenticationRequestDTO.getUsername());
		authenticationRequest.setPassword(authenticationRequestDTO.getPassword());

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
	public ResponseEntity<ValidatingDTO> validatingAuthorizationToken(@RequestHeader(name = "Authorization")String tokenDup){
		String token = tokenDup.substring(7);
		try {
			UserDetails user=userDetailsService.loadUserByUsername(jwtUtil.extractUsername(token));
			if(Boolean.TRUE.equals(jwtUtil.validateToken(token, user))) {
				
				validatingDTO.setValidStatus(true);
				return new ResponseEntity<>(validatingDTO,HttpStatus.OK);
			}
			else {
				throw new LoginException("Invalid Token");
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			validatingDTO.setValidStatus(false);
			return new ResponseEntity<>(validatingDTO,HttpStatus.BAD_REQUEST);
		}
	
	}	
	
	@GetMapping(path = "/health-check")
	public ResponseEntity<String> healthcheck(){
		return new ResponseEntity<>("ok",HttpStatus.OK);
	}

	@PostMapping("/signup")
	public ResponseEntity<?> registerUser(@Valid @RequestBody AuthenticationRequestDTO signUpRequest) {
		if (authRequestRepo.existsByUsername(signUpRequest.getUsername())) {
			return ResponseEntity.badRequest().body("Error: Username is already taken!");
		}

		AuthenticationRequest user = new AuthenticationRequest(signUpRequest.getUsername(),
				encoder.encode(signUpRequest.getPassword()));

		authRequestRepo.save(user);

		return ResponseEntity.ok("User registered successfully!");
	}


}
	



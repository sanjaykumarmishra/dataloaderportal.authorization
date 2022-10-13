package com.dataloaderportal.authorization.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import com.dataloaderportal.authorization.payload.AuthenticationRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import com.dataloaderportal.authorization.AuthorizationApplication;
import com.dataloaderportal.authorization.controller.AuthorizationController;
import com.dataloaderportal.authorization.repository.UserRepo;
import com.dataloaderportal.authorization.service.UserDetailService;
import com.dataloaderportal.authorization.security.jwt.JwtUtil;

@SpringBootTest
@ContextConfiguration(classes =AuthorizationApplication.class )
public class AuthorizationControllerTest {
	
	@Mock
	private JwtUtil jwtUtil;
	
	
	@Mock
	private UserDetailService userDetailService;
	
	@Mock
	private UserRepo userRepo;
	
	@InjectMocks
	private AuthorizationController authorizationController;
	
	@Test
	void testValidLogin() throws LoginException{
		AuthenticationRequest authenticationRequest=new AuthenticationRequest("xxxxxx@gmail.com","Springboot@123");
		UserDetails userDetails=new User(authenticationRequest.getUsername(),authenticationRequest.getPassword(),
				new ArrayList<>());
		when(userDetailService.loadUserByUsername("xxxxxx@gmail.com")).thenReturn(userDetails);
		when(jwtUtil.generateToken(userDetails)).thenReturn("dummy-token");
		ResponseEntity<Object> authenticationResponse=authorizationController.createAuthorizationToken(authenticationRequest);
	    assertEquals(HttpStatus.OK,authenticationResponse.getStatusCode());	
	}
	
	@Test
	void testinvalidlogin() {
		AuthenticationRequest authenticationRequest=new AuthenticationRequest("xxxxxx@gmail.com","Springboot@1234");
		UserDetails userDetails=new User(authenticationRequest.getUsername(),"Springboot@123",
				new ArrayList<>());
		when(userDetailService.loadUserByUsername("xxxxxx@gmail.com")).thenReturn(userDetails);
		Exception exception=assertThrows(LoginException.class,
				()->authorizationController.createAuthorizationToken(authenticationRequest));
		assertEquals("Invalid username or password", exception.getMessage());
		
	}
	
	@Test
	void testValidToken() {
		AuthenticationRequest authenticationRequest=new AuthenticationRequest("xxxxxx@gmail.com","Springboot@123");
		UserDetails userDetails=new User(authenticationRequest.getUsername(),authenticationRequest.getPassword(),
				new ArrayList<>());
		when(jwtUtil.validateToken("token", userDetails)).thenReturn(true);
		when(jwtUtil.extractUsername("token")).thenReturn("xxxxxx@gmail.com");
		when(userDetailService.loadUserByUsername("xxxxxx@gmail.com")).thenReturn(userDetails);
		ResponseEntity<?> validityEntity=authorizationController.validatingAuthorizationToken("Bearer token");
		assertTrue(validityEntity.getBody().toString().contains("true"));
	}
	
	@Test
	void testinvalidtoken() {
		AuthenticationRequest authenticationRequest=new AuthenticationRequest("xxxxxx@gmail.com","Springboot@1234");
		UserDetails userDetails=new User(authenticationRequest.getUsername(),authenticationRequest.getPassword(),
				new ArrayList<>());
		when(jwtUtil.validateToken("token", userDetails)).thenReturn(false);
		when(jwtUtil.extractUsername("token")).thenReturn("xxxxxx@gmail.com");
		when(userDetailService.loadUserByUsername("xxxxxx@gmail.com")).thenReturn(userDetails);
		ResponseEntity<?> validityEntity=authorizationController.validatingAuthorizationToken("Bearer token");
		assertEquals(true,validityEntity.getBody().toString().contains("false"));
		
	}
	

}
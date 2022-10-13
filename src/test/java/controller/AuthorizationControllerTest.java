package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.ArrayList;

import javax.security.auth.login.LoginException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;

import com.authorizationservice.authorization.AuthorizationApplication;
import com.authorizationservice.authorization.Controller.AuthorizationController;
import com.authorizationservice.authorization.dto.AuthenticationRequestDTO;
import com.authorizationservice.authorization.repository.AuthRequestRepo;
import com.authorizationservice.authorization.service.AuthUserDetailService;
import com.authorizationservice.authorization.util.JwtUtil.JwtUtil;

@SpringBootTest
@ContextConfiguration(classes =AuthorizationApplication.class )
public class AuthorizationControllerTest {
	
	@Mock
	private JwtUtil jwtUtil;
	
	
	@Mock
	private AuthUserDetailService authUserDetailService;
	
	@Mock
	private AuthRequestRepo authRequestRepo;
	
	@InjectMocks
	private AuthorizationController authorizationController;
	
	@Test
	void testValidLogin() throws LoginException{
		AuthenticationRequestDTO authenticationRequestDTO=new AuthenticationRequestDTO("xxxxxx@gmail.com","Springboot@123");
		UserDetails userDetails=new User(authenticationRequestDTO.getUsername(),authenticationRequestDTO.getPassword(),
				new ArrayList<>());
		when(authUserDetailService.loadUserByUsername("xxxxxx@gmail.com")).thenReturn(userDetails);
		when(jwtUtil.generateToken(userDetails)).thenReturn("dummy-token");
		ResponseEntity<Object> authenticationResponse=authorizationController.createAuthorizationToken(authenticationRequestDTO);
	    assertEquals(HttpStatus.OK,authenticationResponse.getStatusCode());	
	}
	
	@Test
	void testinvalidlogin() {
		AuthenticationRequestDTO authenticationRequestDTO=new AuthenticationRequestDTO("xxxxxx@gmail.com","Springboot@1234");
		UserDetails userDetails=new User(authenticationRequestDTO.getUsername(),"Springboot@123",
				new ArrayList<>());
		when(authUserDetailService.loadUserByUsername("xxxxxx@gmail.com")).thenReturn(userDetails);
		Exception exception=assertThrows(LoginException.class,
				()->authorizationController.createAuthorizationToken(authenticationRequestDTO));
		assertEquals("Invalid username or password", exception.getMessage());
		
	}
	
	@Test
	void testValidToken() {
		AuthenticationRequestDTO authenticationRequestDTO=new AuthenticationRequestDTO("xxxxxx@gmail.com","Springboot@123");
		UserDetails userDetails=new User(authenticationRequestDTO.getUsername(),authenticationRequestDTO.getPassword(),
				new ArrayList<>());
		when(jwtUtil.validateToken("token", userDetails)).thenReturn(true);
		when(jwtUtil.extractUsername("token")).thenReturn("xxxxxx@gmail.com");
		when(authUserDetailService.loadUserByUsername("xxxxxx@gmail.com")).thenReturn(userDetails);
		ResponseEntity<?> validityEntity=authorizationController.validatingAuthorizationToken("Bearer token");
		assertTrue(validityEntity.getBody().toString().contains("true"));
	}
	
	@Test
	void testinvalidtoken() {
		AuthenticationRequestDTO authenticationRequestDTO=new AuthenticationRequestDTO("xxxxxx@gmail.com","Springboot@1234");
		UserDetails userDetails=new User(authenticationRequestDTO.getUsername(),authenticationRequestDTO.getPassword(),
				new ArrayList<>());
		when(jwtUtil.validateToken("token", userDetails)).thenReturn(false);
		when(jwtUtil.extractUsername("token")).thenReturn("xxxxxx@gmail.com");
		when(authUserDetailService.loadUserByUsername("xxxxxx@gmail.com")).thenReturn(userDetails);
		ResponseEntity<?> validityEntity=authorizationController.validatingAuthorizationToken("Bearer token");
		assertEquals(true,validityEntity.getBody().toString().contains("false"));
		
	}
	

}
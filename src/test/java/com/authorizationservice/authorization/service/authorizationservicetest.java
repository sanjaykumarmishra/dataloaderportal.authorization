package com.authorizationservice.authorization.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import com.authorizationservice.authorization.model.AuthenticationRequest;
import com.authorizationservice.authorization.repository.AuthRequestRepo;

@SpringBootTest(classes =AuthUserDetailService.class)
public class authorizationservicetest {
	
	@MockBean
	private AuthRequestRepo authRequestRepo;
	
	@Autowired
	private AuthUserDetailService authUserDetailService;
	
	@Test
	void testloadUserByName() {
		AuthenticationRequest user = new AuthenticationRequest("xxxxxx@gmail.com","Springboot@123");
		when(authRequestRepo.findById("xxxxxx@gmail.com")).thenReturn(Optional.of(user));
		UserDetails userDetails=new User("xxxxxx@gmail.com","Springboot@123",new ArrayList<>());
		assertEquals(userDetails,authUserDetailService.loadUserByUsername("xxxxxx@gmail.com"));
		
	}
	
	

}

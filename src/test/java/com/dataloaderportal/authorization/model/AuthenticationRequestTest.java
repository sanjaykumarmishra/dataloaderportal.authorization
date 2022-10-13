package com.dataloaderportal.authorization.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dataloaderportal.authorization.payload.AuthenticationRequest;

@SpringBootTest(classes = AuthenticationRequest.class)
public class AuthenticationRequestTest {
	
	@Autowired
	@Mock
	private AuthenticationRequest authenticationRequest;
	
	@Test
	void authenticationrequestnotnulltest() {
		assertThat(authenticationRequest).isNotNull();
	}
	
	@Test
	void testuserLoginallargs() {
		AuthenticationRequest authenticationRequest=new AuthenticationRequest("xxxxxx@gmail.com","Springboot@123");
		assertEquals("xxxxxx@gmail.com", authenticationRequest.getUsername());
		assertEquals("Springboot@123", authenticationRequest.getPassword());
		
	}

}

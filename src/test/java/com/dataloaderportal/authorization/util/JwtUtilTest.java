package com.dataloaderportal.authorization.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;

import com.dataloaderportal.authorization.security.jwt.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest(classes = JwtUtil.class)
public class JwtUtilTest {
	
	@Autowired
	JwtUtil jwtUtil;
	
	UserDetails userDetails;
	
	@Test
	void generatetokentest() {
		userDetails=new User("xxxxxx@gmail.com","Springboot@123",new ArrayList<>());
		String generatetoke = jwtUtil.generateToken(userDetails);
		assertNotNull(generatetoke);
	}
	
	@ParameterizedTest
	@ValueSource(strings = {"xxxxxx@gmail.com","root","user"})
	void validatetokentest(String arg) {
		userDetails=new User(arg,"Springboot@123",new ArrayList<>());
		String generatetokenString=jwtUtil.generateToken(userDetails);
		Boolean validatetokenBoolean=jwtUtil.validateToken(generatetokenString, userDetails);
		assertEquals(true,validatetokenBoolean);
		
	}

}

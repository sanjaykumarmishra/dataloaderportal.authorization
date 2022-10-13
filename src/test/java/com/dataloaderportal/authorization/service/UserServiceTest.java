package com.dataloaderportal.authorization.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import com.dataloaderportal.authorization.repository.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

@SpringBootTest(classes = UserDetailService.class)
public class UserServiceTest {
	
	@MockBean
	private UserRepo userRepo;
	
	@Autowired
	private UserDetailService userDetailService;
	
	@Test
	void testloadUserByName() {
		com.dataloaderportal.authorization.model.User user = new com.dataloaderportal.authorization.model.User("xxxxxx@gmail.com","Springboot@123");
		when(userRepo.findByUsername("xxxxxx@gmail.com")).thenReturn(user);
		UserDetails userDetails=new User("xxxxxx@gmail.com","Springboot@123",new ArrayList<>());
		assertEquals(userDetails, userDetailService.loadUserByUsername("xxxxxx@gmail.com"));
		
	}
	
	

}

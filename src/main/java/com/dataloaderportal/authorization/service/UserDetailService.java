package com.dataloaderportal.authorization.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dataloaderportal.authorization.payload.AuthenticationRequest;
import com.dataloaderportal.authorization.repository.UserRepo;

@Service
public class UserDetailService implements UserDetailsService{
	
	@Autowired
    UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException{
		
		com.dataloaderportal.authorization.model.User user;
		
		if(userRepo.findByUsername(userName)!=null) {
			user = userRepo.findByUsername(userName);
			UserDetails userDetails = new User(user.getUsername(), user.getPassword(),
					new ArrayList<>());
			return userDetails;

		
			
		}
		throw new UsernameNotFoundException("User not found");
	}
	
	public com.dataloaderportal.authorization.model.User getUser(String userName) {
		return userRepo.findByUsername(userName);
	}


}

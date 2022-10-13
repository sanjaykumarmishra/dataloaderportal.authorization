package com.dataloaderportal.authorization.repository;

import com.dataloaderportal.authorization.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dataloaderportal.authorization.payload.AuthenticationRequest;



@Repository
public 	interface UserRepo extends JpaRepository<User,String>{
    Boolean existsByUsername(String username);
    User findByUsername(String username);
}

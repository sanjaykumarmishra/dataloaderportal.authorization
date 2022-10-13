package com.dataloaderportal.authorization.payload;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.*;
import org.springframework.stereotype.Component;



@Getter
@Setter
@ToString
public class AuthenticationRequest {
	public String username;
	public String password;
	public AuthenticationRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}
}

package com.authorizationservice.authorization.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;


@Entity
@Data
@Table(name="person")
public class AuthenticationRequest {
	
	@Id
	@Column(name="username")
	public String username;
	
	@Column(name="password")
	public String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public AuthenticationRequest(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	public AuthenticationRequest() {
		
	}
	

}

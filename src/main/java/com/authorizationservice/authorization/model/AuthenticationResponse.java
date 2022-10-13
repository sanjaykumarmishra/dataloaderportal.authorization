package com.authorizationservice.authorization.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class AuthenticationResponse {
	
	public AuthenticationResponse(String username, String jwtAuthTokenString, long serverCurrentTime,
			long tokenExpirationTime) {
		super();
		this.username = username;
		this.jwtAuthTokenString = jwtAuthTokenString;
		this.serverCurrentTime = serverCurrentTime;
		this.tokenExpirationTime = tokenExpirationTime;
	}

	private String username;
	
	private String jwtAuthTokenString;
	
	private long serverCurrentTime;
	
	private long tokenExpirationTime;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getJwtAuthTokenString() {
		return jwtAuthTokenString;
	}

	public void setJwtAuthTokenString(String jwtAuthTokenString) {
		this.jwtAuthTokenString = jwtAuthTokenString;
	}

	public long getServerCurrentTime() {
		return serverCurrentTime;
	}

	public void setServerCurrentTime(long serverCurrentTime) {
		this.serverCurrentTime = serverCurrentTime;
	}

	public long getTokenExpirationTime() {
		return tokenExpirationTime;
	}

	public void setTokenExpirationTime(long tokenExpirationTime) {
		this.tokenExpirationTime = tokenExpirationTime;
	}

}

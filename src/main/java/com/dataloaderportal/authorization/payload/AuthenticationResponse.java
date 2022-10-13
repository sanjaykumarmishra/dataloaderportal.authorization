package com.dataloaderportal.authorization.payload;

import lombok.*;


@Getter
@Setter
@ToString
public class AuthenticationResponse {

	private String username;
	
	private String jwtAuthTokenString;
	
	private long serverCurrentTime;
	
	private long tokenExpirationTime;

	public AuthenticationResponse(String username, String jwtAuthTokenString, long serverCurrentTime,
								  long tokenExpirationTime) {
		super();
		this.username = username;
		this.jwtAuthTokenString = jwtAuthTokenString;
		this.serverCurrentTime = serverCurrentTime;
		this.tokenExpirationTime = tokenExpirationTime;
	}

}

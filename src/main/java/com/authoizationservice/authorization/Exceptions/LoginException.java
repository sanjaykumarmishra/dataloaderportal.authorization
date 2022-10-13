package com.authoizationservice.authorization.Exceptions;

public class LoginException extends Exception{
	
	public static final long serialVersionUID=1L;
	
	public LoginException(String exceptionMessage) {
		super(exceptionMessage);
	}

}

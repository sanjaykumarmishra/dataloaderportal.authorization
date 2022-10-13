package com.authorizationservice.authorization.util.JwtUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
	private static final String SECRET_KEY = "abcABC09";

	private long currentTime;

	private long expirationTime;

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	}

	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claimsMap = new HashMap<>();

		return createToken(claimsMap, userDetails.getUsername());
	}

	public String createToken(Map<String, Object> claims, String subject) {

		setCurrentTime(System.currentTimeMillis());
		setExpirationTime(getCurrentTime() + 1000 * 60 * 30);

		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(getExpirationTime())).signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();

	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String usernameString = extractUsername(token);
		return (usernameString.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}

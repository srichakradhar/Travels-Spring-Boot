package com.fresco.tournament.config;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	@Value("${jwt.secret}")
	private String jwtSecret;
	@Value("${jwt.token.validity}")
	private long tokenValidity;

	public User getUser(final String token) {
		try {
			Claims body = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
			User user = new User(body.getSubject(), "", true, true, true, true, Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("USER") }));
			return user;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public String generateToken(String name) {
		Claims claims = Jwts.claims().setSubject(name);
		long nowMillis = System.currentTimeMillis();
		long expMillis = nowMillis + tokenValidity;
		Date exp = new Date(expMillis);
		return Jwts.builder().setClaims(claims).setIssuedAt(new Date(nowMillis)).setExpiration(exp)
				.signWith(SignatureAlgorithm.HS512, jwtSecret).compact();
	}

	public void validateToken(final String token) {
		Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
	}
}

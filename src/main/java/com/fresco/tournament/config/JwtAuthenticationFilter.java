package com.fresco.tournament.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fresco.tournament.service.UserAuthService;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserAuthService userAuthService;

	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader("JWT");
		if (token == null) {
			JSONObject json = new JSONObject();
			json.put("error", "Please authenticate");
			response.setStatus(400);
			response.getWriter().write(json.toJSONString());
			return;
		}
		UserDetails userDetails;
		try {
			jwtUtil.validateToken(token);
			User user = jwtUtil.getUser(token);
			System.out.println(user);
			userDetails = userAuthService.loadUserByUsername(user.getUsername());
		} catch (UsernameNotFoundException e) {
			JSONObject json = new JSONObject();
			json.put("error", "Please authenticate");
			response.setStatus(400);
			response.getWriter().write(json.toJSONString());
			return;
		}
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		if (SecurityContextHolder.getContext().getAuthentication() == null)
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		filterChain.doFilter(request, response);
	}
}

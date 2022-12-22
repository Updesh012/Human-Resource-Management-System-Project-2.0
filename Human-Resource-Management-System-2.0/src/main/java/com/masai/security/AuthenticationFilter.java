package com.masai.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.masai.service.CustomUserDetailsService;



@Component
public class AuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Autowired
	private TokenGenerator generator;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String token = getTokenFromRequest(request);
		
		if(StringUtils.hasText(token) && generator.validateToken(token)) {
			
			String userName = generator.getUserName(token);
			
			UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
			
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			
			
		}
		
		filterChain.doFilter(request, response);
		
	}

	
	public String getTokenFromRequest(HttpServletRequest request) {
		
		String bearerToken = request.getHeader("Authorization");
		
		try {
			
			if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
				
				String token = bearerToken.substring(7);
				
				return token;
			}
			
		} catch (Exception e) {
			throw new RuntimeException("Invalid Token");
		}
		
		return null;
		
	}
	
	
	
}

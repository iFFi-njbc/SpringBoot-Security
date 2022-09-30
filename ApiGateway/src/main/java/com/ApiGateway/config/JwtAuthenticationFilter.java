package com.ApiGateway.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ApiGateway.helper.JwtUtil;
import com.ApiGateway.services.CustomUserDetailsService;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException
	{
		
	//get jwt
	//bearer
	//validate
	
	String authorization = request.getHeader("Authorization");
	String username = null;
	String jwtToken = null;
	
	
	//null and format
	if(authorization != null && authorization.startsWith("Bearer "))
	{
		
		jwtToken = authorization.substring(7);
		
		try {
			
			username = this.jwtUtil.extractUsername(jwtToken);
			
		}catch (Exception ex)
		{
			ex.printStackTrace();
		}
		
		
		UserDetails userDetails = this.customUserDetailsService.loadUserByUsername(username);
		
		//security
		if(username != null && SecurityContextHolder.getContext().getAuthentication()==null)
		{
			UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			
			usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			
			SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
		}else
		{
			System.out.println("token not validated");
		}
	}
	
	filterChain.doFilter(request, response);
	
	}
}

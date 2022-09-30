package com.ApiGateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ApiGateway.helper.JwtUtil;
import com.ApiGateway.model.JwtRequest;
import com.ApiGateway.model.jwtResponse;
import com.ApiGateway.services.CustomUserDetailsService;

@RestController
public class JwtController {
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	
	@RequestMapping(value = "/token", method = RequestMethod.POST)
	public ResponseEntity<jwtResponse> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception
	{
		System.out.println(jwtRequest);
		try {
			
			this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(), jwtRequest.getPassword()));
			
		}catch (UsernameNotFoundException ex)
		{
			ex.printStackTrace();
			throw new Exception("BAD CREDENTIALS!!!");
		}
		
		
		//fine-area
		UserDetails userDetails =  customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
		String token =  this.jwtUtil.generateToken(userDetails);
		
		System.out.println("JWT TOKEN --------> " + token);
		
		return ResponseEntity.ok(new jwtResponse(token));
		
	}


}

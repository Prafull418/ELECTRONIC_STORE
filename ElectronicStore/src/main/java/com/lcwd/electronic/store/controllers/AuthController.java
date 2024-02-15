package com.lcwd.electronic.store.controllers;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lcwd.electronic.store.dtos.JwtRequest;
import com.lcwd.electronic.store.dtos.JwtResponse;
import com.lcwd.electronic.store.dtos.UserDto;
import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.security.JwtHelper;
import com.lcwd.electronic.store.services.UserService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/auth")
@Api(value = "AuthController",description = "APIs for authentication") // for swagger-ui
public class AuthController { // this class only will use when we user will login first time to create token and then user will login through token until token is expired

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager manager;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Autowired
	private ModelMapper mapper;
	
	
	@PostMapping("/login")  // when we user will login first time to create token and then user will login through token until token is expired
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest jwtRequest){
		this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwtRequest.getEmail());
		String token = this.jwtHelper.generateToken(userDetails);
		
		UserDto userDto = this.mapper.map(userDetails, UserDto.class);
		
		JwtResponse response = JwtResponse.builder()
		                                  .jwtToken(token)
		                                  .user(userDto).build();
		return new ResponseEntity<JwtResponse>(response, HttpStatus.OK);
	}

    // 
	private void doAuthenticate(String email, String password) {
		
		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			manager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new BadApiRequestException("Invalid Username or password !!");
		}
	}
	
	@GetMapping("/current")  // to get the user name or user details who is login already, here we have to use token to get the user name
	public ResponseEntity<UserDto> getCurrentUser(Principal principal){
		String name = principal.getName();
		return new ResponseEntity<UserDto>(mapper.map(userDetailsService.loadUserByUsername(name), UserDto.class), HttpStatus.OK);
	}
}

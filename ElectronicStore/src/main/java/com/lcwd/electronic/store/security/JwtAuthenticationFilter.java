package com.lcwd.electronic.store.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	Logger logger = LoggerFactory.getLogger(OncePerRequestFilter.class);
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtHelper jwtHelper;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		//Authorization :- we will send the value in key:-Authorization and its value will be token
		String requestHeader = request.getHeader("Authorization");
		//Bearer 2324ioknntitn4t4tjdfnj84u48ru4rjb48rh4bfjbjbr4uf
        logger.info("header:  "+requestHeader);		
        String username=null;
        String token = null;
        if (requestHeader != null && requestHeader.startsWith("Bearer")) {
			//looking good..
        	  token = requestHeader.substring(7);
        	  try {
        		  username = this.jwtHelper.getUsernameFromToken(token);
				
			} catch (IllegalArgumentException e) {
				logger.info("Illegal Argument fetching the username !!");
				e.printStackTrace();
			}
        	  catch (ExpiredJwtException e) {
				logger.info("GIven jwt token is expired !!");
				e.printStackTrace();
			}
        	  catch (MalformedJwtException e) {
				logger.info("Some changed has done in token !! Invalid Token");
				e.printStackTrace();
			}
        	  catch (Exception e) {
				e.printStackTrace();
			}
		}
        else {
			logger.info("invalid header value");
		}
        //if we dont get any exception then something is there username and previously authentication set should not be there for anyone
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
		     	
        	//fetch the user details through username from database
        	UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
        	Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);// it checks the username of token and token expired or not
        	if (validateToken) {
				//set the authentication for that user which going to be login
        		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,userDetails.getAuthorities());
        		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        		SecurityContextHolder.getContext().setAuthentication(authentication);
        		// this is main line to set authentication
			}
        	else {
				logger.info("Validation fails !!");
			}	
		}
        filterChain.doFilter(request, response); // whatever we set in request we have to forword with response then user can login
	}

	
}

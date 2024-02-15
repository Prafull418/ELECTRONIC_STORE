package com.lcwd.electronic.store.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lcwd.electronic.store.entities.User;
import com.lcwd.electronic.store.security.JwtAuthenticationEntryPoint;
import com.lcwd.electronic.store.security.JwtAuthenticationFilter;


@Configuration   // because we are creating beans here so spring boot create objects automatically to them
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig  {

	
	// this is the way from temporary memory
	/*
	 * @Bean //first bean its a interface, InMemoryUserDetailsManager provides
	 * implementation of it public UserDetailsService userDetailsService() {
	 * 
	 * //here we create the users for security // userDetaiService interface to
	 * create users UserDetails normal = User.builder().name("prafull")
	 * .password(passwordEncoder().encode("prafull@123")).build(); //latest version
	 * we use here password encoded method for encrypting the password
	 * 
	 * 
	 * UserDetails admin = User.builder().name("ankit")
	 * .password(passwordEncoder().encode("ankit@123")).build();
	 * 
	 * return new InMemoryUserDetailsManager(normal,admin); // here we have
	 * constructor with muliplte UserDetails objects to create users
	 * 
	 * }
	 */
	 
	
	
	// this is the way from database
	
	  @Autowired 
	  private UserDetailsService userDetailsService;
	  
	  @Autowired
	  private JwtAuthenticationEntryPoint authenticationEntryPoint;
	  
	  @Autowired
	  private JwtAuthenticationFilter authenticationFilter;
	  
	  
	  private final String[] PUBLIC_URLS = {"/swagger-ui/**","/webjars/**","/swagger-resources/**","/v3/api-docs","/v2/api-docs"};
	  
	  
	  //it has responsibility for authenticating the user
	  
		/*
		 * @Bean //this method will call when we click on sign in button to check both
		 * username and password. public DaoAuthenticationProvider
		 * authenticationProvider() { DaoAuthenticationProvider authenticationProvider =
		 * new DaoAuthenticationProvider();
		 * authenticationProvider.setUserDetailsService(userDetailsService); //here
		 * username come from database user and username input on browser both are
		 * compared if its same then authenticate
		 * authenticationProvider.setPasswordEncoder(passwordEncoder()); //here password
		 * come from database user and password input on browser(which is not encrypted)
		 * so we used passwordEncode both are compared if its same then authenticate
		 * return authenticationProvider; }
		 */
	 
	 
	  //basic authentication : since spring boot 2.7.6 but we will change this into 3
		/*
		 * @Bean public SecurityFilterChain securityFilterChain(HttpSecurity http)
		 * throws Exception { http. csrf().disable() .cors().disable()
		 * .authorizeRequests() // we want all url authorized .anyRequest() //any url
		 * .authenticated() .and() .httpBasic(); // this indicates we are using basic
		 * authentication
		 * 
		 * return http.build(); }
		 */
	
	  //jwt authentication 
	  @Bean
	  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		  http.
		  csrf().disable()
		  .cors().disable()
		  .authorizeRequests() // we want all url authorized
		  .antMatchers("/auth/login")   // we want login api request as public due to generate token then we can access any api through token
		  .permitAll()
		  .antMatchers(HttpMethod.POST,"/users") // we want users post request only as public to giving role to user
		  .permitAll()
		  .antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
		  .antMatchers(PUBLIC_URLS)
		  .permitAll()
		  .anyRequest()  //any url
		  .authenticated()
		  .and()
		  .exceptionHandling()
		  .authenticationEntryPoint(authenticationEntryPoint)
		  .and()
		  .sessionManagement()
		  .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		  
		  http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
		  return http.build();
	  }
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {  // we have this interface in spring boot security to encrypt the password
		return new BCryptPasswordEncoder();
	}
	
	@Bean // to create object of AuthenticationManager interface implementing AuthenticationConfiguration class to authenticate email and password when user will login to genarate token
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}

package com.lcwd.electronic.store.dtos;

import com.lcwd.electronic.store.entities.Role;
import com.lcwd.electronic.store.entities.User;

import io.swagger.annotations.ApiModelProperty;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {

		private String userId;
		
		@ApiModelProperty(value = "user_name") // in swagger-ui inside model of userDto 
		@Size(min = 3, max = 15, message = "Invaid Name !!")
		private String name;
		
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid User Email !!")
        //@Email :- in case of @Email, we can't set regular expression
        @NotBlank(message =  "Email is required")
		private String email;
		
		@NotBlank(message = "Password is required !!")
		private String password;
		
		//according to male and female
		@Size(min = 4,max=6,message = "Invalid gender !!" )
		private String gender;
		
		@NotBlank(message = "Write something abput yourself !!")
		private String about;
		
		//@Pattern and custom validator
		
		
		private String imageName;
		
		private Set<RoleDto> roles = new HashSet<>();
		
}
package com.prorent.carrental.controller.vm;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginVM {
	
	@Email(message="Please provide a valid email")
	@NotNull(message="Please provide your email")
	@Size(min=5,max=100,message="Email '${validatedValue}' must be between {min} and {max} chracters long")
	private String email;
	
	
	@NotNull(message="Please provide your password")
	@NotBlank(message="Please provide your password")
	@Size(min=4,max=60,message="Password '${validatedValue}' must be between {min} and {max} chracters long")
	private String password;
	
	
	@Override
	public String toString() {
	return "LoginVM{"+
			"email='"+email+
			'}';
	}
	
}
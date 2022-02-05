package com.prorent.carrental.controller;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prorent.carrental.controller.vm.LoginVM;
import com.prorent.carrental.domain.User;
import com.prorent.carrental.security.JwtUtils;
import com.prorent.carrental.service.UserService;

@RestController
@RequestMapping
public class UserJWTController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@PostMapping("/register")
	public ResponseEntity<Map<String,Boolean>> registerUser(@Valid @RequestBody User user){
		userService.register(user);
		Map <String,Boolean> map=new HashMap<>();
		map.put("Register is Ok", true);
		return new ResponseEntity<>(map,HttpStatus.CREATED);			
	}
	

	@PostMapping("/login")
	public ResponseEntity<JWTToken> login(@Valid @RequestBody LoginVM loginVM){
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginVM.getEmail(), loginVM.getPassword()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt=jwtUtils.generateToken(authentication);
		
		HttpHeaders httpHeaders=new HttpHeaders();
		httpHeaders.add("Authorization","Bearer "+jwt);
		return new ResponseEntity<>(new JWTToken(jwt),httpHeaders,HttpStatus.OK);
	}
	
	static class JWTToken{
		private String token;
		
		public JWTToken(String token) {
			this.token=token;
		}
		
		@JsonProperty("jwt_token")
		String getToken() {
			return token;
		}
		
		void setToken(String token) {
			this.token=token;
		}
	}
	
}

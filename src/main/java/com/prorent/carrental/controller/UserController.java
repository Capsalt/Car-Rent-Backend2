package com.prorent.carrental.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.prorent.carrental.domain.User;
import com.prorent.carrental.service.UserService;
import com.prorent.carrental.service.dto.UserDTO;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	private ModelMapper modelMapper;
	
	
	@GetMapping("/auth/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<UserDTO>> getAllUsers(){
		List<User> userList = userService.fetchAllUsers();
		List<UserDTO> userDTOList = userList.stream().map(this::convertToDTO).collect(Collectors.toList());
		return new ResponseEntity<>(userDTOList, HttpStatus.OK);
	}

	private UserDTO convertToDTO(User user) {
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		return userDTO;
	}
	
}

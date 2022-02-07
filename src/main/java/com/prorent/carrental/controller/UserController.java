package com.prorent.carrental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prorent.carrental.domain.User;
import com.prorent.carrental.service.UserService;
import com.prorent.carrental.service.dto.AdminDTO;
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
	
	@GetMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<UserDTO> getUserByIdAdmin(@PathVariable Long id){
		User user = userService.findById(id);
		UserDTO userDTO = convertToDTO(user);
		return new ResponseEntity<>(userDTO,HttpStatus.OK);
	}
	
	@GetMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<UserDTO> getUserById(HttpServletRequest request){
		Long id = (Long)request.getAttribute("id");
		User user = userService.findById(id);
		UserDTO userDTO = convertToDTO(user);
		return new ResponseEntity<>(userDTO, HttpStatus.OK);
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String,Boolean>> addUser(@Valid @RequestBody AdminDTO adminDTO){
		userService.addUserAuth(adminDTO);
		Map<String, Boolean> map = new HashMap<>();
		map.put("User added successfully", true);
		return new ResponseEntity<>(map, HttpStatus.CREATED);
	}
	
	
	@PutMapping
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<Map<String, Boolean>> updateUser(HttpServletRequest request,
			@Valid @RequestBody UserDTO userDTO){
		Long id=(Long)request.getAttribute("id");  
		userService.updateUser(id,userDTO);
		Map<String, Boolean> map = new HashMap<>();
		map.put("User updated successfully", true);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@PutMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Boolean>> updateUserAuth(@PathVariable Long id,
			@Valid @RequestBody AdminDTO adminDTO){
		userService.updateUserAuth(id, adminDTO);
		Map<String, Boolean> map = new HashMap<>();
		map.put("User updated successfully", true);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	
	
	@PatchMapping("/auth")
	@PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
	public ResponseEntity<Map<String, Boolean>> updatePassword(HttpServletRequest request,
			@RequestBody Map<String,String> userMap){
		Long id=(Long)request.getAttribute("id");
		String newPassword = userMap.get("newPassword");
		String oldPassword = userMap.get("oldPassword");
		userService.updatePassword(id,newPassword,oldPassword);
		Map<String, Boolean> map = new HashMap<>();
		map.put("Password changed successfully", true);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	@DeleteMapping("/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String, Boolean>>deleteUser(@PathVariable Long id){
		userService.removeById(id);
		
		Map<String, Boolean> map = new HashMap<>();
		map.put("User deleted successfully", true);
		return new ResponseEntity<>(map,HttpStatus.OK);
	}
	
	
		@GetMapping("/search")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<List<UserDTO>> searchUserByLastName(
				@RequestParam("lastname") String lastName){
			List<User> userList = userService.searchUserByLastName(lastName);
			List<UserDTO> userDTOList = userList.stream().map(this::convertToDTO).collect(Collectors.toList());
			return new ResponseEntity<>(userDTOList, HttpStatus.OK);
		}
		
		@GetMapping("/search/contain")
		@PreAuthorize("hasRole('ADMIN')")
		public ResponseEntity<List<UserDTO>> searchUserByLastNameContain(
				@RequestParam("lastname") String lastName){
			List<User> userList = userService.searchUserByLastNameContain(lastName);
			List<UserDTO> userDTOList = userList.stream().map(this::convertToDTO).collect(Collectors.toList());
			return new ResponseEntity<>(userDTOList, HttpStatus.OK);
		}

	private UserDTO convertToDTO(User user) {
		UserDTO userDTO = modelMapper.map(user, UserDTO.class);
		return userDTO;
	}
	

}

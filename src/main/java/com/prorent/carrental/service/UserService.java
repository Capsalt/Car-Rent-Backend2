package com.prorent.carrental.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prorent.carrental.domain.Role;
import com.prorent.carrental.domain.User;
import com.prorent.carrental.domain.enumeration.UserRole;
import com.prorent.carrental.exception.BadRequestException;
import com.prorent.carrental.exception.ConflictException;
import com.prorent.carrental.exception.ResourceNotFoundException;
import com.prorent.carrental.repository.RoleRepository;
import com.prorent.carrental.repository.UserRepository;

@Transactional
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public void register(User user) throws BadRequestException{
		
		if(userRepository.existsByEmail(user.getEmail())){
			throw new ConflictException("Error: Email is already in use");	
		}
		
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		
		user.setPassword(encodedPassword);
		user.setBuiltIn(false);
		
		Set<Role> roles=new HashSet<>();
		Role role=roleRepository.findByName(UserRole.ROLE_CUSTOMER).
				orElseThrow(()->new ResourceNotFoundException("Role Not Found"));
		
		roles.add(role);
		
		user.setRoles(roles);
		userRepository.save(user);
		
	}
	
	public List<User> fetchAllUsers(){
		return userRepository.findAll();
	}

}
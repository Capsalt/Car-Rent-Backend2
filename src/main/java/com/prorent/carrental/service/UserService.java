package com.prorent.carrental.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
import com.prorent.carrental.service.dto.AdminDTO;
import com.prorent.carrental.service.dto.UserDTO;

@Transactional
@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final static String USER_NOT_FOUND_MSG="user with id %d not found";
	
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
	
	
	public User findById(Long id) throws ResourceNotFoundException{
		User user = userRepository.findById(id).
				orElseThrow(()->new ResourceNotFoundException(String.format(USER_NOT_FOUND_MSG, id)));
		return user;
	}
	
	public void addUserAuth(AdminDTO adminDTO) throws BadRequestException{
		Boolean emailExists = userRepository.existsByEmail(adminDTO.getEmail());
		
		if(emailExists) {
			throw new ConflictException("Error: Email is already in use");
		}
		
		String encodedPassword= passwordEncoder.encode(adminDTO.getPassword());
		adminDTO.setPassword(encodedPassword);
		adminDTO.setBuiltIn(false);
		
		Set<String> userRoles = adminDTO.getRoles();
		Set<Role> roles = addRoles(userRoles);
		
		User user=new User(adminDTO.getFirstName(),adminDTO.getLastName(),
				 adminDTO.getPassword(),adminDTO.getPhoneNumber(),adminDTO.getEmail(),
				adminDTO.getAddress(),adminDTO.getZipCode(),roles,adminDTO.getBuiltIn());
				
		userRepository.save(user);	
	}
	
	
	public void updateUser(Long id,UserDTO userDTO) throws BadRequestException{
		boolean emailExists = userRepository.existsByEmail(userDTO.getEmail());
		Optional<User> foundUser = userRepository.findById(id);
		
		if(foundUser.get().getBuiltIn()) {
			throw new ResourceNotFoundException("You don't have permission to change this user info");
		}
		
		if(emailExists && !userDTO.getEmail().equals(foundUser.get().getEmail())) {
			throw new ConflictException("Error: Email is in use");
		}
		
		userRepository.update(id,userDTO.getFirstName(),userDTO.getLastName(),
				userDTO.getPhoneNumber(),userDTO.getEmail(),userDTO.getAddress(),userDTO.getZipCode());
		
	}
	
	public void updateUserAuth(Long id,AdminDTO adminDTO) throws BadRequestException{
		Boolean emailExists = userRepository.existsByEmail(adminDTO.getEmail());
		Optional<User> foundUser = userRepository.findById(id);
		
		if(foundUser.get().getBuiltIn()) {
			throw new ResourceNotFoundException("You don't have permission to change this user info");
		}
		
		adminDTO.setBuiltIn(false);
		
		if(emailExists && !adminDTO.getEmail().equals(foundUser.get().getEmail())) {
			throw new ConflictException("Error: Email is in use");
		}
		
		if(adminDTO.getPassword()==null) {
			adminDTO.setPassword(foundUser.get().getPassword());
		}else {
			String encodedPassword=passwordEncoder.encode(adminDTO.getPassword());
			adminDTO.setPassword(encodedPassword);
		}
		
		Set<String> userRoles=adminDTO.getRoles();
		Set<Role> roles=addRoles(userRoles);
		
		User user=new User(id,adminDTO.getFirstName(),adminDTO.getLastName(),
				adminDTO.getEmail()
				,adminDTO.getPassword(),adminDTO.getPhoneNumber(),
				adminDTO.getAddress(),adminDTO.getZipCode(),adminDTO.getBuiltIn(),roles);
		
		userRepository.save(user);
		
	}
	
	public Set<Role> addRoles(Set<String> userRoles){
		Set<Role> roles = new HashSet<>();
		if(userRoles == null) {
			Role userRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
					.orElseThrow(()->new RuntimeException("Error: Role not found"));
			roles.add(userRole);
		}else {
			userRoles.forEach(role->{
				switch (role) {
				case "Administrator":
					Role adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
							.orElseThrow(() -> new RuntimeException("Error: Role not found"));
					roles.add(adminRole);

					break;

				case "Manager":
					Role managerRole = roleRepository.findByName(UserRole.ROLE_MANAGER)
							.orElseThrow(() -> new RuntimeException("Error: Role not found"));
					roles.add(managerRole);
					break;

				default:
					Role customerRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
							.orElseThrow(() -> new RuntimeException("Error: Role not found"));
					roles.add(customerRole);
				}
			});
		}
		return roles;
	}
	
	

}
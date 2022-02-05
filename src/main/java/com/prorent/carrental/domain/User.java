package com.prorent.carrental.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.prorent.carrental.domain.enumeration.UserRole;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tbl_user")
@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Please provide not blank firstName")
	@NotNull(message = "Please provide your firstName")
	@Size(min = 1, max = 15, message = "FirstName '${validatedValue}' must be between {min} and {max} chracters long")
	@Column(length = 15, nullable = false)
	private String firstName;

	@NotBlank(message = "Please provide not blank lastName")
	@NotNull(message = "Please provide your lastName")
	@Size(min = 1, max = 15, message = "lastName '${validatedValue}' must be between {min} and {max} chracters long")
	@Column(length = 15, nullable = false)
	private String lastName;

	@Email(message = "Please provide a valid email")
	@NotNull(message = "Please provide your email")
	@Size(min = 5, max = 100, message = "Email '${validatedValue}' must be between {min} and {max} chracters long")
	@Column(length = 100, unique = true, nullable = false)
	private String email;

	@NotNull(message = "Please provide your password")
	@NotBlank(message = "Please provide your password")
	@Size(min = 4, max = 60, message = "Password '${validatedValue}' must be between {min} and {max} chracters long")
	@Column(nullable = false, length = 255)
	private String password;

	// (555) 555 5555
	// 555-555-5555
	// 555.555.5555
	@Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", message = "Please provide valid phone number")
	@Column(length = 14, nullable = false)
	private String phoneNumber;

	@NotBlank(message = "Please provide not blank Adress")
	@NotNull(message = "Please provide your Address")
	@Size(min = 10, max = 250, message = "Adress '${validatedValue}' must be between {min} and {max} chracters long")
	@Column(length = 250, nullable = false)
	private String address;

	@NotBlank(message = "Please provide not blank ZipCode")
	@NotNull(message = "Please provide your ZipCode")
	@Size(min = 4, max = 15, message = "ZipCode '${validatedValue}' must be between {min} and {max} chracters long")
	@Column(length = 15, nullable = false)
	private String zipCode;

	@Column(nullable = false)
	private Boolean builtIn;

	/*
	 * Bir user birden fazla role sahip olabilir. Bu nedenle manytomany yapıldı ve
	 * rolleri tutacak hashset tipinde bir alan eklendi.
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tbl_user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();
	
	public Set<Role> getRole(){
		return roles;
	}
	
	public Set<String> getRoles(){
		Set<String> roleStr = new HashSet<>();
		Role[] role = roles.toArray(new Role[roles.size()]);
		
		for (int i = 0; i < roles.size(); i++) {
			if(role[i].getName().equals(UserRole.ROLE_ADMIN))
				roleStr.add("Administrator");
			else
				roleStr.add("Customer");
		}
		return roleStr;
	}

	public User(String firstName, String lastName, String password, 
			String phoneNumber, String email, String address,
			String zipCode) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.zipCode = zipCode;
	}

	public User(String firstName, String lastName, String password, 
			String phoneNumber, String email, String address,
			String zipCode, Set<Role> roles, Boolean builtIn) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.zipCode = zipCode;
		this.roles = roles;
		this.builtIn = builtIn;
	}
	

}

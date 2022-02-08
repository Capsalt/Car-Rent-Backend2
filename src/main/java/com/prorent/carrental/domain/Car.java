package com.prorent.carrental.domain;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@Table(name="tbl_car")
@Entity
public class Car implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	@Size(min=1,max=20,message="Model '${validatedValue}' must be between {min} and {max} chracters long")
	@NotNull(message="Please provide model")
	@Column(length=20,nullable=false)
	private String model;
	
	@NotNull(message="Please provide car doors")
	@Column(nullable=false)
	private Integer doors;
	
	@NotNull(message="Please provide car seats")
	@Column(nullable=false)
	private Integer seats;
	
	@NotNull(message="Please provide car luggage")
	@Column(nullable=false)
	private Integer luggage;
	
	@Size(min=1,max=20,message="Transmission '${validatedValue}' must be between {min} and {max} chracters long")
	@NotNull(message="Please provide transmission")
	@Column(length=20,nullable=false)
	private String transmission;
	
	@NotNull(message="Please provide car airConditioning status ")
	@Column(nullable=false)
	private Boolean airConditioning;
	
	@NotNull(message="Please provide car age")
	@Column(nullable=false)
	private Integer age;
	
	@ManyToMany(fetch=FetchType.LAZY)
	@JoinTable(name="tbl_car_image", joinColumns=@JoinColumn(name="car_id"),
	inverseJoinColumns=@JoinColumn(name="file_id"))
	private Set<ImageFile> image;

	@NotNull(message="Please provide car price per hour")
	@Column(nullable=false)
	private Double pricePerHour;
	
	@Size(min=1,max=20,message="FuelType '${validatedValue}' must be between {min} and {max} chracters long")
	@NotNull(message="Please provide car fuel type")
	@Column(length=20,nullable=false)
	private String fuelType;
	
	
	private Boolean builtIn;
	
	public Car(String model, Integer doors, Integer seats, Integer luggage, 
			String transmission,Boolean airConditioning, Integer age, 
			Set<ImageFile> image, Double pricePerHour,String fuelType) {
		this.model=model;
		this.doors=doors;
		this.seats=seats;
		this.luggage=luggage;
		this.transmission=transmission;
		this.airConditioning=airConditioning;
		this.age=age;
		this.image=image;
		this.pricePerHour=pricePerHour;
		this.fuelType=fuelType;
	}
}

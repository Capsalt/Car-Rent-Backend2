package com.prorent.carrental.service.dto;

import java.util.HashSet;
import java.util.Set;

import com.prorent.carrental.domain.Car;
import com.prorent.carrental.domain.ImageFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class CarDTO{

	private Long id;
	

	private String model;
	

	private Integer doors;
	

	private Integer seats;
	

	private Integer luggage;
	

	private String transmission;
	

	private Boolean airConditioning;
	

	private Integer age;
	
	private Set<String> image;
	

	private Double pricePerHour;
	

	private String fuelType;
	
	
	private Boolean builtIn;
	
	public CarDTO(Car car) {
		this.id=car.getId();
		this.model=car.getModel();
		this.doors=car.getDoors();
		this.seats=car.getSeats();
		this.luggage=car.getLuggage();
		this.transmission=car.getTransmission();
		this.airConditioning=car.getAirConditioning();
		this.age=car.getAge();
		this.pricePerHour=car.getPricePerHour();
		this.fuelType=car.getFuelType();
		this.image=getImageId(car.getImage());
		this.builtIn=car.getBuiltIn();
	}
	
	public CarDTO(Long id,Car car) {
		this.id=car.getId();
		this.model=car.getModel();
		this.doors=car.getDoors();
		this.seats=car.getSeats();
		this.luggage=car.getLuggage();
		this.transmission=car.getTransmission();
		this.airConditioning=car.getAirConditioning();
		this.age=car.getAge();
		this.pricePerHour=car.getPricePerHour();
		this.fuelType=car.getFuelType();
		this.image=getImageId(car.getImage());
		this.builtIn=car.getBuiltIn();
	}
	

	
	public Set<String> getImageId(Set<ImageFile> images){
		Set<String> imgs=new HashSet<>();
		ImageFile[] imageFiles=images.toArray(new ImageFile[images.size()]);
		for (int i = 0; i < images.size(); i++) {
			imgs.add(imageFiles[i].getId());
		}
		return imgs;
	}
	

}

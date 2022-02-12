package com.prorent.carrental.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prorent.carrental.domain.Car;
import com.prorent.carrental.domain.ImageFile;
import com.prorent.carrental.exception.BadRequestException;
import com.prorent.carrental.exception.ResourceNotFoundException;
import com.prorent.carrental.repository.CarRepository;
import com.prorent.carrental.repository.ImageFileRepository;
import com.prorent.carrental.repository.ReservationRepository;
import com.prorent.carrental.service.dto.CarDTO;

@Transactional
@Service
public class CarService {
	
	@Autowired
	ImageFileRepository imageFileRepository;
	
	@Autowired
	CarRepository carRepository;
	
	@Autowired
	ReservationRepository reservationRepository;
	
	public void addCar(Car car, String imageId) throws BadRequestException{
		
		ImageFile imageFile = imageFileRepository.findById(imageId).orElseThrow(()->
		new ResourceNotFoundException("Car Not Found id:"+imageId));
		Set<ImageFile> imageFiles=new HashSet<>();
		imageFiles.add(imageFile);
		car.setImage(imageFiles);
		
		car.setBuiltIn(false);
		
		carRepository.save(car);
	}
	
	public List<CarDTO> getAllCars(){
		return carRepository.findAllCar();
	}
	
	public CarDTO findById(Long id) throws ResourceNotFoundException{
		return carRepository.findCarByCId(id).orElseThrow(()->
			new ResourceNotFoundException("Car not found by id:"+id));
	}
	
	public void updateCar(Long id,Car car, String imageId) throws BadRequestException{
		car.setId(id);

		ImageFile imageFile=imageFileRepository.findById(imageId).
				orElseThrow(()->new ResourceNotFoundException("Image not found:"+imageId));
		
		Car existCar = carRepository.findById(id).orElseThrow(()->
			new ResourceNotFoundException("Car not found with id:"+id));
		
		if(existCar.getBuiltIn()) {
			throw new BadRequestException("Yo dont have permission to update this car:"+id);
		}
		
		car.setBuiltIn(false);
		
		Set<ImageFile> imageFiles=new HashSet<>();
		imageFiles.add(imageFile);
		car.setImage(imageFiles);
		 
		carRepository.save(car);
	}
	
	public void removeById(Long id) throws ResourceNotFoundException{
		Car car = carRepository.findById(id).orElseThrow(()->
			new ResourceNotFoundException("Car not found id:"+id));
		
		if(car.getBuiltIn()) {
			throw new BadRequestException("Yo dont have permission to update this car:"+id);
		}
		
		boolean exists = reservationRepository.existsByCarId(car);
		
		if(exists) {
			throw new BadRequestException("Car is used by a reservation");
		}
		
		carRepository.deleteById(id);
	}
	
	
	public Page<CarDTO> getCarPage(Pageable pageable){
		return carRepository.findCarPage(pageable);
	}
	
	

	
}

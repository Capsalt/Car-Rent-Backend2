package com.prorent.carrental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.prorent.carrental.domain.Car;
import com.prorent.carrental.helper.PaginationUtil;
import com.prorent.carrental.service.CarService;
import com.prorent.carrental.service.dto.CarDTO;

@RestController
@RequestMapping("/car")
public class CarController {

	@Autowired
	private CarService carService;
	
	@PostMapping("/admin/{id}/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String,Boolean>> addCar(@PathVariable String id,@Valid @RequestBody Car car){
		carService.addCar(car,id);
		Map<String,Boolean> map=new HashMap<>();
		map.put("Car saved OK", true);
		return new ResponseEntity<>(map,HttpStatus.CREATED);
	}
	
	
	@GetMapping("/visitors/all")
	public ResponseEntity<List<CarDTO>> getAllCars(){
		List<CarDTO> allCars = carService.getAllCars();
		return ResponseEntity.ok(allCars);
	}
	
	@GetMapping("/visitors/{id}")
	public ResponseEntity<CarDTO> getCarById(@PathVariable Long id){
		CarDTO car = carService.findById(id);
		return ResponseEntity.ok(car);
	}
	
	
	@PutMapping("/admin/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String,Boolean>> updateCar(@RequestParam("id") Long id,
			@RequestParam("imageId") String imageId,@Valid @RequestBody Car car){
		carService.updateCar(id, car, imageId);
		Map<String,Boolean> map=new HashMap<>();
		map.put("Car Update successful", true);
		return ResponseEntity.status(HttpStatus.OK).body(map);
	}
	
	@DeleteMapping("/admin/{id}/auth")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Map<String,Boolean>> removeCar(@PathVariable Long id){
		carService.removeById(id);
		Map<String,Boolean> map=new HashMap<>();
		map.put("Car Remove successful", true);
		return ResponseEntity.status(HttpStatus.OK).body(map);
	}
	
 
	@GetMapping("/visitors/carpage")
	public ResponseEntity<List<CarDTO>> getCarPage(Pageable pageable){
		Page<CarDTO> page = carService.getCarPage(pageable);
		HttpHeaders headers=PaginationUtil.
				generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
		return ResponseEntity.ok().headers(headers).body(page.getContent());
	}
	
	
}

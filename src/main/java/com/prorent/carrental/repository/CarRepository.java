package com.prorent.carrental.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prorent.carrental.domain.Car;
import com.prorent.carrental.service.dto.CarDTO;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

	//Select c.id, c.model from tbl_car c;
	@Query("SELECT new com.prorent.carrental.service.dto.CarDTO(c) FROM Car c")
	List<CarDTO> findAllCar();
	
	
	@Query("SELECT new com.prorent.carrental.service.dto.CarDTO(c) FROM Car c WHERE c.id=:id")
	Optional<CarDTO> findCarByCId(@Param("id") Long id );
	
	
	@Query("SELECT new com.prorent.carrental.service.dto.CarDTO(c.id,c) FROM Car c")
	Page findCarPage(Pageable pageable);
	
}

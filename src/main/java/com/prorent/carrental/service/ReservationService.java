package com.prorent.carrental.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prorent.carrental.domain.Car;
import com.prorent.carrental.domain.Reservation;
import com.prorent.carrental.domain.User;
import com.prorent.carrental.domain.enumeration.ReservationStatus;
import com.prorent.carrental.exception.BadRequestException;
import com.prorent.carrental.exception.ReservationTimeException;
import com.prorent.carrental.exception.ResourceNotFoundException;
import com.prorent.carrental.repository.CarRepository;
import com.prorent.carrental.repository.ReservationRepository;
import com.prorent.carrental.repository.UserRepository;
import com.prorent.carrental.service.dto.ReservationDTO;

@Transactional
@Service
public class ReservationService {

	@Autowired
	ReservationRepository reservationRepository;
	
	@Autowired
	UserRepository userRepository;
	
	
	@Autowired CarRepository carRepository;
	
	
	public List<ReservationDTO> getAllReservations(){
		return reservationRepository.getAllReservations();
	}
	
	public List<ReservationDTO> getAllUserReservations(Long id, Long userId){
		return reservationRepository.getFindAllUserReservation(id, userId);
	}
	
	public ReservationDTO findById(Long id) throws ResourceNotFoundException{
		return reservationRepository.findReservationById(id).
		orElseThrow(()-> new ResourceNotFoundException("Reservation not found id:"+id));
	}
	
	public ReservationDTO findByUserId(Long id,Long userId) throws ResourceNotFoundException {
		return reservationRepository.findReservationByUserId(id, userId).
			orElseThrow(()->new ResourceNotFoundException("Reservation not found id:"+id));
	}
	
	public List<ReservationDTO> findAllByUserId(Long userId) throws ResourceNotFoundException {
			return reservationRepository.findReservationsByUserId(userId);
	
	}
	
	public void addReservation(Reservation reservation, Long id, Car carId) throws BadRequestException {
		
		LocalDateTime now = LocalDateTime.now();
		if(reservation.getPickUpTime().isBefore(now)) {
			throw new ReservationTimeException("Pickup Time or DropOff Time is not correct");
		}
		
		boolean isEqual=reservation.getPickUpTime().isEqual(reservation.getDropOffTime())?true:false;
		boolean isBefore=reservation.getPickUpTime().isBefore(reservation.getDropOffTime())?true:false;
		
		if(isEqual|| !isBefore) {
			throw new ReservationTimeException("Pickup Time or DropOff Time is not correct");
		}
		
		boolean checkCarIsAvailable = checkCarIsAvailable(carId.getId(), reservation.getPickUpTime(), reservation.getDropOffTime());
		
		User user = userRepository.findById(id).orElseThrow(()->new 
				ResourceNotFoundException("User not found id:"+id));
		
		if(!checkCarIsAvailable) {
			reservation.setStatus(ReservationStatus.CREATED);
		}else {
			throw new BadRequestException("Car is already reserved");
		}
		
		reservation.setCarId(carId);
		reservation.setUserId(user);
		
		Double totalPrice=calcTotalPrice(reservation.getPickUpTime(),reservation.getDropOffTime(),carId.getId());
		reservation.setTotalPrice(totalPrice);
		
		reservationRepository.save(reservation);
	}
	
	public Double calcTotalPrice(LocalDateTime pickUpTime, LocalDateTime dropOffTime, Long carId ) {
		Car car = carRepository.findById(carId).
				orElseThrow(()->new ResourceNotFoundException("Car not found id:"+carId));
		
		Long totalHours = (new Reservation()).getTotalHours(pickUpTime, dropOffTime);
		return car.getPricePerHour()*totalHours;
	}
	
	
	
	public boolean checkCarIsAvailable(Long carId, 
			LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
		List<Reservation> reservations = reservationRepository.checkStatus(carId, pickUpTime, dropOffTime, ReservationStatus.DONE, 
				ReservationStatus.CANCELLED);
		return reservations.size()>0;
	}
}

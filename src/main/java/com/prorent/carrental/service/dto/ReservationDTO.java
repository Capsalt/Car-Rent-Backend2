package com.prorent.carrental.service.dto;

import java.time.LocalDateTime;

import com.prorent.carrental.domain.Reservation;
import com.prorent.carrental.domain.enumeration.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDTO{

	private Long id;
	
	private CarDTO car;
	
	private Long userId;
	
	private LocalDateTime pickUpTime;
	
	private LocalDateTime dropOffTime;
	
	private String pickUpLocation;
	
	private String dropOffLocation;
	
	private ReservationStatus status;
	
	private Double totalPrice;
	
	public ReservationDTO(Reservation reservation) {
		this.id=reservation.getId();
		this.car=new CarDTO(reservation.getCarId());
		this.userId=reservation.getUserId().getId();
		this.pickUpTime=reservation.getPickUpTime();
		this.dropOffTime=reservation.getDropOffTime();
		this.pickUpLocation=reservation.getPickUpLocation();
		this.dropOffLocation=reservation.getDropOffLocation();
		this.status=reservation.getStatus();
		this.totalPrice=reservation.getTotalPrice();
	}
}

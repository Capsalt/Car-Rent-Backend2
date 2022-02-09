package com.prorent.carrental.domain;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
import com.prorent.carrental.domain.enumeration.ReservationStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="tbl_reservation")
@Entity
public class Reservation implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@OneToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="car_id", referencedColumnName = "id")
	private Car carId;
	
	@OneToOne(cascade=CascadeType.PERSIST)
	@JoinColumn(name="user_id", referencedColumnName = "id")
	private User userId;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss", timezone = "Turkey")
	@NotNull(message="Please Provide pick up time for the reservation")
	@Column(nullable=false)
	private LocalDateTime pickUpTime;
	
	@JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm:ss", timezone = "Turkey")	
	@NotNull(message="Please Provide drop off time for the reservation")
	@Column(nullable=false)
	private LocalDateTime dropOffTime;
	
	@NotNull(message="Please Provide pick up location for the reservation")
	@Column(length=100, nullable=false)
	private String pickUpLocation;
	
	@NotNull(message="Please Provide drop off location for the reservation")
	@Column(length=100, nullable=false)
	private String dropOffLocation;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 20, nullable = false)
	private ReservationStatus status;
	
	@Column(nullable = false)
	private Double totalPrice;
	
	public Long getTotalHours(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
		
		Long hours = ChronoUnit.HOURS.between(pickUpTime, dropOffTime);
				return hours;
	}
}

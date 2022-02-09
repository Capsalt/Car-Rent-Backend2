package com.prorent.carrental.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prorent.carrental.domain.Car;
import com.prorent.carrental.domain.Reservation;
import com.prorent.carrental.service.ReservationService;
import com.prorent.carrental.service.dto.ReservationDTO;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
	
	@Autowired
	private ReservationService reservationService;

	@PostMapping("/add")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<Map<String,Boolean>> makeReservation(HttpServletRequest request,
			@RequestParam(value="carId") Car carId, @Valid @RequestBody Reservation reservation){
		Long userId = (Long)request.getAttribute("id");
		reservationService.addReservation(reservation, userId, carId);
		Map<String, Boolean> map = new HashMap<>();
		map.put("Reservation is Success", true);
		return new ResponseEntity<>(map,HttpStatus.CREATED);
		
		
	}
	
	@GetMapping("/admin/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getAllReservations() {
		List<ReservationDTO> reservartions = reservationService.getAllReservations();
		return ResponseEntity.ok(reservartions);
	}

	@GetMapping("/admin/auth/all")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getAllUserReservations(@RequestParam(value = "id") Long id,
			@RequestParam(value = "userId") Long userId) {
		List<ReservationDTO> reservations = reservationService.getAllUserReservations(id, userId);
		return ResponseEntity.ok(reservations);
	}

	@GetMapping("/{id}/admin")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
		ReservationDTO reservation = reservationService.findById(id);
		return ResponseEntity.ok(reservation);
	}

	@GetMapping("/{id}/auth")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<ReservationDTO> getUserReservationById
	(@PathVariable Long id, HttpServletRequest request) {
		Long userId=(Long)request.getAttribute("id");
		ReservationDTO reservation= reservationService.findByUserId(id,userId);
		return ResponseEntity.ok(reservation);
	}

	@GetMapping("/auth/all")
	@PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
	public ResponseEntity<List<ReservationDTO>> getUserReservationsById
	(HttpServletRequest request) {
		Long userId=(Long)request.getAttribute("id");
		List<ReservationDTO> reservation= reservationService.findAllByUserId(userId);
		return ResponseEntity.ok(reservation);
	}
}

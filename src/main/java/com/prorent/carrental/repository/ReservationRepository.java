package com.prorent.carrental.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.prorent.carrental.domain.Reservation;
import com.prorent.carrental.domain.enumeration.ReservationStatus;
import com.prorent.carrental.service.dto.ReservationDTO;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	@Query("SELECT new com.prorent.carrental.service.dto.ReservationDTO (r) FROM Reservation r")
	List<ReservationDTO> getAllReservations();
	
	@Query("SELECT new com.prorent.carrental.service.dto.ReservationDTO (r)"
			+ " FROM Reservation r WHERE r.id=:id and r.userId.id=:userId")
	List<ReservationDTO> getFindAllUserReservation(@Param("id") Long id, @Param("userId") Long userId);
	
	
	@Query("SELECT new com.prorent.carrental.service.dto.ReservationDTO (r) "
			+ "FROM Reservation r WHERE r.id=:id")
	Optional<ReservationDTO> findReservationById(@Param("id") Long id);
	
	
	@Query("SELECT new com.prorent.carrental.service.dto.ReservationDTO (r)"
			+ "FROM Reservation r WHERE r.id=:id and r.userId.id=:userId")
	Optional<ReservationDTO> findReservationByUserId(@Param("id") Long id, @Param("userId") Long userId);
	
	@Query("SELECT new com.prorent.carrental.service.dto.ReservationDTO (r)"
			+ "FROM Reservation r WHERE r.userId.id=:userId")
	List<ReservationDTO> findReservationsByUserId(@Param("userId")Long userId);
	
	
	@Query("SELECT r FROM Reservation r "+
         "INNER JOIN Car cd on r.carId.id=cd.id WHERE "+
			"(cd.id=:carId and r.status<>:done and r.status<>:cancel and :pickUpTime BETWEEN"+ 
         " r.pickUpTime and r.dropOffTime) or "
         + "(cd.id=:carId and r.status<>:done and r.status<>:cancel and :dropOffTime BETWEEN"+ 
         " r.pickUpTime and r.dropOffTime)"
			
			)
	
	List<Reservation> checkStatus(@Param("carId") Long carId,@Param("pickUpTime") LocalDateTime pickUpTime, 
			@Param("dropOffTime") LocalDateTime dropOffTime,
			@Param ("done") ReservationStatus done, @Param ("cancel")ReservationStatus cancelled);
}

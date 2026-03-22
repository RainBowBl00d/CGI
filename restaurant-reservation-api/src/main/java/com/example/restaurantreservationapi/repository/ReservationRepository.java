package com.example.restaurantreservationapi.repository;

import com.example.restaurantreservationapi.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	@Query("SELECT r FROM Reservation r WHERE :startTime < r.reservationTimeEnd AND :endTime > r.reservationTimeStart")
	List<Reservation> findActiveReservationsAt(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);}

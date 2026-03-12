package com.example.restaurantreservationapi.repository;

import com.example.restaurantreservationapi.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByReservationTimeBetween(LocalDateTime start, LocalDateTime end);
}

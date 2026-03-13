package com.example.restaurantreservationapi.repository;

import com.example.restaurantreservationapi.entity.Reservation;
import com.example.restaurantreservationapi.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TableRepository  extends JpaRepository<RestaurantTable, Long> {
	@Query("SELECT r FROM Reservation r WHERE :time >= r.reservationTimeStart AND :time < r.reservationTimeEnd")
	List<Reservation> findActiveReservationsAt(LocalDateTime time);
}

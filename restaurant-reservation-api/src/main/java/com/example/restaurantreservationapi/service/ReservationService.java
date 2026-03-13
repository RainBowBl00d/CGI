package com.example.restaurantreservationapi.service;

import com.example.restaurantreservationapi.entity.Reservation;
import com.example.restaurantreservationapi.entity.RestaurantTable;
import com.example.restaurantreservationapi.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;

	public ReservationService(ReservationRepository reservationRepository) {
		this.reservationRepository = reservationRepository;
	}

	public List<Reservation> getAllReservations(){
		return reservationRepository.findAll();
	}

	public Reservation saveNewReservation(int groupSize, LocalDateTime reservationTimeStart, LocalDateTime reservationTimeEnd){
		return reservationRepository.save(
				Reservation.builder()
						.groupSize(groupSize)
						.reservationTimeStart(reservationTimeStart)
						.reservationTimeEnd(reservationTimeEnd)
						.build()
		);
	}
}

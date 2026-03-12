package com.example.restaurantreservationapi.service;

import com.example.restaurantreservationapi.entity.Reservation;
import com.example.restaurantreservationapi.repository.ReservationRepository;
import org.springframework.stereotype.Service;

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
}

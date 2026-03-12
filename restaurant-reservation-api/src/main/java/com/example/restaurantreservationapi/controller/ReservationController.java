package com.example.restaurantreservationapi.controller;

import com.example.restaurantreservationapi.entity.Reservation;
import com.example.restaurantreservationapi.service.ReservationService;
import com.example.restaurantreservationapi.service.TableService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {
	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService, TableService tableService) {
		this.reservationService = reservationService;
	}

	@GetMapping
	public List<Reservation> getReservations(){
		return reservationService.getAllReservations();
	}
}

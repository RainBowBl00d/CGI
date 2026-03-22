package com.example.restaurantreservationapi.controller;

import com.example.restaurantreservationapi.entity.Reservation;
import com.example.restaurantreservationapi.service.ReservationService;
import com.example.restaurantreservationapi.service.TableService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {
	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService, TableService tableService) {
		this.reservationService = reservationService;
	}
//region Get
	@GetMapping
	public List<Reservation> getReservations(){
		return reservationService.getAllReservations();
	}

	@GetMapping("/active")
	public List<Reservation> getActiveReservations() {
		LocalDateTime now = LocalDateTime.now();
		return reservationService.getActiveReservationsAt(now);
	}
//endregion
//region Post
	@PostMapping
	public Reservation createReservation(
			@RequestParam int groupSize,
			@RequestParam LocalDateTime reservationTimeStart,
			@RequestParam LocalDateTime reservationTimeEnd,
			@RequestParam(required = false) List<Long> tableIds
	) {
		return reservationService.saveNewReservation(groupSize, reservationTimeStart, reservationTimeEnd, tableIds);
	}
	//endregion
}

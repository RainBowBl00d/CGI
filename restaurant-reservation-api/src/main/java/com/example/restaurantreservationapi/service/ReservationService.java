package com.example.restaurantreservationapi.service;

import com.example.restaurantreservationapi.entity.Reservation;
import com.example.restaurantreservationapi.entity.RestaurantTable;
import com.example.restaurantreservationapi.repository.ReservationRepository;
import com.example.restaurantreservationapi.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
	private final ReservationRepository reservationRepository;
	private final TableRepository tableRepository;

	public ReservationService(ReservationRepository reservationRepository, TableRepository tableRepository) {
		this.reservationRepository = reservationRepository;
		this.tableRepository = tableRepository;
	}

	public List<Reservation> getAllReservations(){
		return reservationRepository.findAll();
	}

	public List<Reservation> getActiveReservationsAt(LocalDateTime time) {
		LocalDateTime endTime = time.plusHours(3);
		return reservationRepository.findActiveReservationsAt(time, endTime);
	}

	public Reservation saveNewReservation(int groupSize, LocalDateTime reservationTimeStart, LocalDateTime reservationTimeEnd, List<Long> tableIds){
		// Find tables by IDs if provided
		List<RestaurantTable> tables = null;
		if (tableIds != null && !tableIds.isEmpty()) {
			tables = tableRepository.findAllById(tableIds);
		}

		return reservationRepository.save(
				Reservation.builder()
						.groupSize(groupSize)
						.reservationTimeStart(reservationTimeStart)
						.reservationTimeEnd(reservationTimeEnd)
						.tables(tables)
						.build()
		);
	}
}

package com.example.restaurantreservationapi.service;

import com.example.restaurantreservationapi.entity.Reservation;
import com.example.restaurantreservationapi.entity.RestaurantTable;
import com.example.restaurantreservationapi.repository.ReservationRepository;
import com.example.restaurantreservationapi.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TableService {
	private final TableRepository tableRepository;
	private final ReservationRepository reservationRepository;

	public TableService(TableRepository tableRepository, ReservationRepository reservationRepository) {
		this.tableRepository = tableRepository;
		this.reservationRepository = reservationRepository;
	}

	public List<RestaurantTable> getAllTables(){
		return tableRepository.findAll();
	}

	public RestaurantTable saveNewTable(int capacity, int x, int y, boolean windowSeat, boolean quietArea) {
		return tableRepository.save(
				RestaurantTable.builder()
						.capacity(capacity)
						.x(x)
						.y(y)
						.zone("Main Hall")
						.windowSeat(windowSeat)
						.quietArea(quietArea)
						.build()
		);
	}

	public RestaurantTable recommendBestTable(int groupSize, LocalDateTime requestedTime, boolean prefersWindow) {
		// 1. Võta kõik lauad
		List<RestaurantTable> allTables = tableRepository.findAll();

		// 2. Võta broneeringud, mis sel ajal toimuvad
		List<Reservation> activeReservations = reservationRepository.findActiveReservationsAt(requestedTime);

		// 3. Kogu kokku kõik laudade ID-d, mis on broneeritud
		Set<Long> occupiedTableIds = activeReservations.stream()
				.flatMap(res -> res.getTables().stream())
				.map(RestaurantTable::getId)
				.collect(Collectors.toSet());

		// 4. Filtreeri vabad lauad, mis sobivad seltskonnale
		return allTables.stream()
				.filter(t -> !occupiedTableIds.contains(t.getId())) // Laud ei tohi olla hõivatud
				.filter(t -> t.getCapacity() >= groupSize)          // Mahutavus peab olema piisav
				.min(Comparator.comparingInt((RestaurantTable t) -> calculateScore(t, groupSize, prefersWindow)))
				.orElse(null);
	}

	private int calculateScore(RestaurantTable table, int groupSize, boolean prefersWindow) {
		int score = 0;

		score += (table.getCapacity() - groupSize) * 10;

		if (prefersWindow && !table.isWindowSeat()) {
			score += 50;
		}

		return score;
	}
}

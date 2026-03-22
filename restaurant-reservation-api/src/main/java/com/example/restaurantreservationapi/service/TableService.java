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

	public List<List<RestaurantTable>> recommendTableOptions(int groupSize, LocalDateTime requestedTime, boolean prefersWindow, boolean prefersQuiet, String zone) {
		// 1. Get all tables
		List<RestaurantTable> allTables = tableRepository.findAll();

		// 2. Get active reservations at requested time (with 3-hour duration)
		LocalDateTime endTime = requestedTime.plusHours(3);
		List<Reservation> activeReservations = reservationRepository.findActiveReservationsAt(requestedTime, endTime);

		// 3. Collect occupied table IDs
		Set<Long> occupiedTableIds = activeReservations.stream()
				.flatMap(res -> res.getTables().stream())
				.map(RestaurantTable::getId)
				.collect(Collectors.toSet());

		// 4. Filter available tables
		List<RestaurantTable> availableTables = allTables.stream()
				.filter(t -> !occupiedTableIds.contains(t.getId()))
				.filter(t -> zone == null || zone.isEmpty() || zone.equals(t.getZone()))
				.sorted(Comparator.comparingInt((RestaurantTable t) -> calculateScore(t, groupSize, prefersWindow, prefersQuiet)))
				.collect(Collectors.toList());

		// 5. Generate table combinations
		List<List<RestaurantTable>> options = new java.util.ArrayList<>();
		Set<Set<Long>> usedCombinations = new java.util.HashSet<>();

		// Option 1: Single table that fits the group
		availableTables.stream()
				.filter(t -> t.getCapacity() >= groupSize)
				.limit(3) // Top 3 single table options
				.forEach(t -> {
					Set<Long> combo = Set.of(t.getId());
					if (usedCombinations.add(combo)) {
						options.add(List.of(t));
					}
				});

		// Option 2: Combination of 2 tables for larger groups (or if no single table found)
		if (options.isEmpty() || groupSize > 6) {
			for (int i = 0; i < availableTables.size() && options.size() < 5; i++) {
				for (int j = i + 1; j < availableTables.size() && options.size() < 5; j++) {
					RestaurantTable t1 = availableTables.get(i);
					RestaurantTable t2 = availableTables.get(j);
					if (t1.getCapacity() + t2.getCapacity() >= groupSize) {
						Set<Long> combo = Set.of(t1.getId(), t2.getId());
						if (usedCombinations.add(combo)) {
							options.add(List.of(t1, t2));
						}
					}
				}
			}
		}

		return options;
	}

	private int calculateScore(RestaurantTable table, int groupSize, boolean prefersWindow, boolean prefersQuiet) {
		int score = 0;

		// Lower score is better - prefer tables closer to group size
		int capacityDiff = table.getCapacity() - groupSize;
		if (capacityDiff < 0) {
			// Table too small - heavily penalize
			score += 1000;
		} else {
			// Prefer tables with minimal excess capacity
			score += capacityDiff * 10;
		}

		// Penalize if preferences don't match
		if (prefersWindow && !table.isWindowSeat()) {
			score += 50;
		}

		if (prefersQuiet && !table.isQuietArea()) {
			score += 50;
		}

		return score;
	}
}

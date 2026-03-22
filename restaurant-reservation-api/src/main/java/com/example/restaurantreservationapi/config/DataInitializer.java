package com.example.restaurantreservationapi.config;

import com.example.restaurantreservationapi.entity.Reservation;
import com.example.restaurantreservationapi.entity.RestaurantTable;
import com.example.restaurantreservationapi.entity.TableShape;
import com.example.restaurantreservationapi.repository.ReservationRepository;
import com.example.restaurantreservationapi.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
	private final TableRepository tableRepository;
	private final ReservationRepository reservationRepository;

	@Override
	public void run(String... args) {
		// Initialize tables if they don't exist
		List<RestaurantTable> savedTables;

		if (tableRepository.count() == 0) {
			// Create tables first
			List<RestaurantTable> initialTables = new ArrayList<>();

			// Main Hall (Sisesaal) - main dining area
			initialTables.add(createTable(2, 50, 50, "Main Hall", true, false, TableShape.CIRCLE));
			initialTables.add(createTable(2, 50, 180, "Main Hall", true, false, TableShape.CIRCLE));
			initialTables.add(createTable(4, 200, 80, "Main Hall", false, false, TableShape.SQUARE));
			initialTables.add(createTable(4, 200, 250, "Main Hall", false, false, TableShape.SQUARE));
			initialTables.add(createTable(4, 200, 420, "Main Hall", false, false, TableShape.SQUARE));
			initialTables.add(createTable(6, 350, 150, "Main Hall", false, false, TableShape.RECTANGLE));
			initialTables.add(createTable(8, 350, 350, "Main Hall", false, false, TableShape.RECTANGLE));
			initialTables.add(createTable(4, 500, 80, "Main Hall", false, true, TableShape.SQUARE));
			initialTables.add(createTable(4, 500, 250, "Main Hall", false, true, TableShape.SQUARE));

			// Terrace (Terrass) - outdoor seating
	  		initialTables.add(createTable(2, 50, 50, "Terrace", true, false, TableShape.CIRCLE));
			initialTables.add(createTable(2, 50, 180, "Terrace", true, false, TableShape.CIRCLE));
			initialTables.add(createTable(4, 200, 80, "Terrace", true, false, TableShape.SQUARE));
			initialTables.add(createTable(4, 200, 250, "Terrace", true, false, TableShape.SQUARE));
			initialTables.add(createTable(6, 350, 150, "Terrace", true, false, TableShape.RECTANGLE));

			// Private Room (Privaatruum) - exclusive area
			initialTables.add(createTable(4, 100, 100, "Private Room", false, true, TableShape.SQUARE));
			initialTables.add(createTable(6, 300, 150, "Private Room", false, true, TableShape.RECTANGLE));
			initialTables.add(createTable(8, 300, 350, "Private Room", false, true, TableShape.RECTANGLE));

			savedTables = tableRepository.saveAll(initialTables);
			System.out.println("Andmebaas on algandmetega täidetud: " + savedTables.size() + " lauda loodud.");
		} else {
			savedTables = tableRepository.findAll();
			System.out.println("Lauad juba olemas: " + savedTables.size() + " lauda.");
		}

		// Create diverse reservations for next 3 days (always recreate if empty)
		if (reservationRepository.count() == 0) {
				List<Reservation> sampleReservations = new ArrayList<>();
				LocalDateTime now = LocalDateTime.now();

				// Generate reservations for next 3 days
				for (int day = 0; day < 3; day++) {
					LocalDateTime currentDay = now.plusDays(day);

					// Lunch time (12:00 - 15:00) - quieter, smaller groups
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 12, 2, "Main Hall"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 13, 4, "Terrace"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 14, 2, "Private Room"));

					// Early dinner (17:00 - 19:00) - mix of group sizes
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 17, 4, "Main Hall"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 18, 6, "Main Hall"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 18, 2, "Terrace"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 19, 4, "Private Room"));

					// Peak dinner (19:00 - 21:00) - busiest time, larger groups
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 19, 8, "Main Hall"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 20, 6, "Main Hall"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 20, 4, "Terrace"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 20, 2, "Terrace"));
					sampleReservations.add(createRandomReservation(savedTables, currentDay, 21, 4, "Private Room"));
				}

				List<Reservation> savedReservations = reservationRepository.saveAll(sampleReservations);
				System.out.println("Generated " + savedReservations.size() + " diverse reservations for next 3 days.");
			} else {
				System.out.println("Reservations already exist: " + reservationRepository.count() + " reservations.");
			}
	}

	private RestaurantTable createTable(int capacity, int x, int y, String zone, boolean window, boolean quiet, TableShape shape) {
		return RestaurantTable.builder()
				.capacity(capacity)
				.x(x)
				.y(y)
				.zone(zone)
				.windowSeat(window)
				.quietArea(quiet)
				.shape(shape)
				.build();
	}

	private Reservation createReservation(int groupSize, LocalDateTime start, LocalDateTime end, List<RestaurantTable> tables) {
		Reservation reservation = new Reservation();
		reservation.setGroupSize(groupSize);
		reservation.setReservationTimeStart(start);
		reservation.setReservationTimeEnd(end);
		reservation.setTables(tables);
		return reservation;
	}

	private Reservation createRandomReservation(List<RestaurantTable> allTables, LocalDateTime date, int hour, int groupSize, String preferredZone) {
		// Set time to specified hour
		LocalDateTime startTime = date.withHour(hour).withMinute(0).withSecond(0).withNano(0);
		LocalDateTime endTime = startTime.plusHours(3);

		// Filter tables by zone and capacity
		List<RestaurantTable> zoneTables = allTables.stream()
				.filter(t -> t.getZone().equals(preferredZone))
				.toList();

		// Find suitable table(s)
		List<RestaurantTable> selectedTables = new ArrayList<>();

		// Try to find single table first
		RestaurantTable singleTable = zoneTables.stream()
				.filter(t -> t.getCapacity() >= groupSize)
				.min((t1, t2) -> Integer.compare(t1.getCapacity() - groupSize, t2.getCapacity() - groupSize))
				.orElse(null);

		if (singleTable != null) {
			selectedTables.add(singleTable);
		} else {
			// Try to find combination of 2 tables
			for (int i = 0; i < zoneTables.size(); i++) {
				for (int j = i + 1; j < zoneTables.size(); j++) {
					RestaurantTable t1 = zoneTables.get(i);
					RestaurantTable t2 = zoneTables.get(j);
					if (t1.getCapacity() + t2.getCapacity() >= groupSize) {
						selectedTables.add(t1);
						selectedTables.add(t2);
						break;
					}
				}
				if (!selectedTables.isEmpty()) break;
			}
		}

		// If no suitable tables found in zone, pick any available table
		if (selectedTables.isEmpty() && !allTables.isEmpty()) {
			selectedTables.add(allTables.get(0));
		}

		return createReservation(groupSize, startTime, endTime, selectedTables);
	}
}
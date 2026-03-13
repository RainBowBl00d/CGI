package com.example.restaurantreservationapi.config;

import com.example.restaurantreservationapi.entity.RestaurantTable;
import com.example.restaurantreservationapi.repository.TableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
	private final TableRepository tableRepository;

	@Override
	public void run(String... args) {
		if (tableRepository.count() == 0) {
			List<RestaurantTable> initialTables = List.of(
					// Kasutame uut meetodit, mis täidab ka lisaväljad
					createTable(2, 50, 50, "A", true, false),
					createTable(4, 200, 50, "A", false, false),
					createTable(2, 50, 200, "B", false, true),
					createTable(6, 400, 300, "B", false, false),
					createTable(2, 650, 50, "Window", true, false),
					createTable(4, 650, 450, "Quiet", false, true)
			);
			tableRepository.saveAll(initialTables);
			System.out.println("Andmebaas on algandmetega täidetud.");
		}
	}

	private RestaurantTable createTable(int capacity, int x, int y, String zone, boolean window, boolean quiet) {
		// Kasutame @Builder mustrit, et kood oleks loetavam
		return RestaurantTable.builder()
				.capacity(capacity)
				.x(x)
				.y(y)
				.zone(zone)
				.windowSeat(window)
				.quietArea(quiet)
				.build();
	}
}
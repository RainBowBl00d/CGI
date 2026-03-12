package com.example.restaurantreservationapi.service;

import com.example.restaurantreservationapi.entity.RestaurantTable;
import com.example.restaurantreservationapi.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TableService {
	private final TableRepository tableRepository;

	public TableService(TableRepository tableRepository) {
		this.tableRepository = tableRepository;
	}

	public List<RestaurantTable> getAllTables(){
		return tableRepository.findAll();
	}

	public RestaurantTable recommendBestTable(int groupSize, boolean prefersWindow){
		List<RestaurantTable> freeTableTops = tableRepository.findByIsOccupiedFalse();

		return freeTableTops.stream()
				.filter(t -> t.getCapacity() >= groupSize)
				.min(Comparator.comparingInt(t -> calculateScore(t, groupSize, prefersWindow)))
				.orElse(null);
	}

	private int calculateScore(RestaurantTable tableTop, int groupSize, boolean prefersWindow){
		int score = (tableTop.getCapacity() - groupSize);
		if(prefersWindow && ! tableTop.isWindowSeat()) score += 50;
		return score;
	}
}

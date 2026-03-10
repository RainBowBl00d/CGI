package com.example.restaurantreservationapi.service;

import com.example.restaurantreservationapi.entity.TableTop;
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

	public List<TableTop> getAllTables(){
		return tableRepository.findAll();
	}

	public TableTop recommendBestTable(int groupSize, boolean prefersWindow){
		List<TableTop> freeTableTops = tableRepository.findByIsOccupiedFalse();

		return freeTableTops.stream()
				.filter(t -> t.getCapacity() >= groupSize)
				.min(Comparator.comparingInt(t -> calculateScore(t, groupSize, prefersWindow)))
				.orElse(null);
	}

	private int calculateScore(TableTop tableTop, int groupSize, boolean prefersWindow){
		int score = (tableTop.getCapacity() - groupSize);
		if(prefersWindow && ! tableTop.isWindowSeat()) score += 50;
		return score;
	}
}

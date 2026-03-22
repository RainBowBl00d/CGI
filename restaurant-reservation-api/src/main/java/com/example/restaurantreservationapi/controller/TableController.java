package com.example.restaurantreservationapi.controller;
import com.example.restaurantreservationapi.entity.RestaurantTable;
import com.example.restaurantreservationapi.service.TableService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "http://localhost:5173")
public class TableController {
	private final TableService tableService;

	public TableController(TableService tableService) {
		this.tableService = tableService;
	}

//region Get
	@GetMapping
	public List<RestaurantTable> getTables(){
		return tableService.getAllTables();
	}

	@GetMapping("/recommend")
	public List<List<RestaurantTable>> getRecommendation(
			@RequestParam int groupSize,
			@RequestParam(required = false) boolean prefersWindow,
			@RequestParam(required = false) boolean prefersQuiet,
			@RequestParam(required = false) String zone,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime time
	) {
		// Validate reservation time is within reasonable hours (10:00 - 22:00)
		int hour = time.getHour();
		if (hour < 10 || hour > 22) {
			throw new IllegalArgumentException("Broneeringuid saab teha ainult vahemikus 10:00-22:00");
		}
		return tableService.recommendTableOptions(groupSize, time, prefersWindow, prefersQuiet, zone);
	}
//endregion
//region Post
	@PostMapping("/tables") // Puhas ja selge tee
	public RestaurantTable createTable(
			@RequestParam int capacity,
			@RequestParam int x,
			@RequestParam int y,
			@RequestParam boolean windowSeat,
			@RequestParam boolean quietArea
	) {
		return tableService.saveNewTable(capacity, x, y, windowSeat, quietArea);
	}
//endregion
}

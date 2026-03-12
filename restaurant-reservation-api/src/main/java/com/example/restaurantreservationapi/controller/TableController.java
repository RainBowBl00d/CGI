package com.example.restaurantreservationapi.controller;
import com.example.restaurantreservationapi.entity.RestaurantTable;
import com.example.restaurantreservationapi.service.TableService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tables")
@CrossOrigin(origins = "http://localhost:5173")
public class TableController {
	private final TableService tableService;

	public TableController(TableService tableService) {
		this.tableService = tableService;
	}

	@GetMapping
	public List<RestaurantTable> getTables(){
		return tableService.getAllTables();
	}

	@GetMapping("/recommend")
	public RestaurantTable getRecommendation(
			@RequestParam int groupSize,
			@RequestParam(required = false) boolean prefersWindow){
		return tableService.recommendBestTable(groupSize, prefersWindow);
	}
}

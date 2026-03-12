package com.example.restaurantreservationapi.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity

public class RestaurantTable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int capacity;
	private String zone;

	private int x;
	private int y;

	private boolean isWindowSeat;
	private boolean isQuietArea;

	private boolean isOccupied;

	@ManyToMany(mappedBy = "tables")
	@JsonBackReference
	private List<Reservation> reservations;
}

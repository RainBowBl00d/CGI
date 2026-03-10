package com.example.restaurantreservationapi.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity

public class TableTop {
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
}

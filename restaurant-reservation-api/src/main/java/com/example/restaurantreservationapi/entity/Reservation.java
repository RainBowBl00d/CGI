package com.example.restaurantreservationapi.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int groupSize;
	private LocalDateTime reservationTimeStart;
	private LocalDateTime reservationTimeEnd;

	@ManyToMany
	@JoinTable(
			name = "reservation_tables",
			joinColumns = @JoinColumn(name = "reservation_id"),
			inverseJoinColumns = @JoinColumn(name = "table_id")
            )
	@JsonManagedReference
	private List<RestaurantTable> tables;
}

package com.example.restaurantreservationapi.repository;

import com.example.restaurantreservationapi.entity.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TableRepository  extends JpaRepository<RestaurantTable, Long> {
	List<RestaurantTable> findByIsOccupiedFalse();
}

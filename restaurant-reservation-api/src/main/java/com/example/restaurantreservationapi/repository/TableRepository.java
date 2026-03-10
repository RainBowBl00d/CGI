package com.example.restaurantreservationapi.repository;

import com.example.restaurantreservationapi.entity.TableTop;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TableRepository  extends JpaRepository<TableTop, Long> {
	List<TableTop> findByIsOccupiedFalse();
}

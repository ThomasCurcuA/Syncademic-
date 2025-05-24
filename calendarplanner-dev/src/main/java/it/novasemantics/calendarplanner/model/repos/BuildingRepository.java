package it.novasemantics.calendarplanner.model.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import it.novasemantics.calendarplanner.model.entities.Building;

@Component
public interface BuildingRepository extends JpaRepository<Building, Long>{
	public List<Building> findAllByOrderByName();
	public List<Building> findByNameContainsOrderByName(String txt);
}

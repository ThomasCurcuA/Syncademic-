package it.novasemantics.calendarplanner.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.novasemantics.calendarplanner.models.Building;

@Repository
public interface BuildingRepository extends JpaRepository<Building, Long> {
	public List<Building> findAllByOrderByName();
	public List<Building> findByNameContainsOrderByName(String txt);
}

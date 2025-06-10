package it.novasemantics.calendarplanner.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.novasemantics.calendarplanner.models.Building;
import it.novasemantics.calendarplanner.repos.BuildingRepository;



@Service
public class BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    public List<Building> findAll() {
        return buildingRepository.findAll();
    }

    public List<Building> findAllByOrderByName() {
        return buildingRepository.findAllByOrderByName();
    }

    public List<Building> findByNameContainsOrderByName(String txt) {
        return buildingRepository.findByNameContainsOrderByName(txt);
    }

    public Optional<Building> findById(Long id) {
        return buildingRepository.findById(id);
    }

    public Building save(Building building) {
        return buildingRepository.save(building);
    }

    public void deleteById(Long id) {
        buildingRepository.deleteById(id);
    }

    public boolean existsById(Long id) {
        return buildingRepository.existsById(id);
    }
} 
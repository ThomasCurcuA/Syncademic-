package it.novasemantics.calendarplanner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import it.novasemantics.calendarplanner.models.Building;
import it.novasemantics.calendarplanner.services.BuildingService;

@RestController
@RequestMapping("/api/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @GetMapping
    public ResponseEntity<List<Building>> getAllBuildings() {
        return ResponseEntity.ok(buildingService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Building> getBuildingById(@PathVariable Long id) {
        return buildingService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Building> createBuilding(@RequestBody Building building) {
        return ResponseEntity.ok(buildingService.save(building));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Building> updateBuilding(@PathVariable Long id, @RequestBody Building building) {
        if (!buildingService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        building.setId(id);
        return ResponseEntity.ok(buildingService.save(building));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuilding(@PathVariable Long id) {
        if (!buildingService.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        buildingService.deleteById(id);
        return ResponseEntity.ok().build();
    }
} 
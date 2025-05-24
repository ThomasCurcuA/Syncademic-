package it.novasemantics.calendarplanner.model.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.javaparser.utils.Log;

import it.novasemantics.calendarplanner.model.entities.*;
import it.novasemantics.calendarplanner.model.repos.*;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class RoomService {

	@Autowired
	BuildingRepository bRepo;

	@Autowired
	RoomRepository rRepo;
	
	public Building createBuilding(String name, String desc, boolean fullDay) {
		return bRepo.save(new Building(name, desc, fullDay));
	}
	

	public List<Building> getBuildings(){
		return bRepo.findAllByOrderByName();
	}

	public List<Building> search(String txt){
		return bRepo.findByNameContainsOrderByName(txt);
	}

	public Building getBuilding(long id) {
		return bRepo.findById(id).orElse(null);
	}

	public Room getRoom(long id) {
		return rRepo.findById(id).orElse(null);
	}

	
	public Building save(Building b){
		return bRepo.save(b);
	}
	
	public void delete(Building b) {
		bRepo.delete(b);
	}

	public Room save(Room r){
		return rRepo.save(r);
	}
	
	@Transactional
	public void delete(Room r) {
		log.info("removing {}", r);
//		r.getBuilding().getRooms().remove(r);
		//r.setBuilding(null);
		rRepo.delete(r);
		log.info("removed");
	}

	
}

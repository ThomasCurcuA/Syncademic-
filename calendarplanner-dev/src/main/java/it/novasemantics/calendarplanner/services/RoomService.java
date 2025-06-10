package it.novasemantics.calendarplanner.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.novasemantics.calendarplanner.models.Room;
import it.novasemantics.calendarplanner.repos.RoomRepository;

@Service
public class RoomService {

	@Autowired
	private RoomRepository roomRepository;

	public List<Room> findAll() {
		return roomRepository.findAll();
	}

	public Optional<Room> findById(Long id) {
		return roomRepository.findById(id);
	}

	public Room save(Room room) {
		return roomRepository.save(room);
	}

	public void deleteById(Long id) {
		roomRepository.deleteById(id);
	}

	public boolean existsById(Long id) {
		return roomRepository.existsById(id);
	}
}

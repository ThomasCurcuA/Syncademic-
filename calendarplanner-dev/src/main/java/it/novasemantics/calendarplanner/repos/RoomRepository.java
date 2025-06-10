package it.novasemantics.calendarplanner.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.novasemantics.calendarplanner.models.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    public List<Room> findAllByOrderByName();
    public List<Room> findByNameContainsOrderByName(String txt);
}

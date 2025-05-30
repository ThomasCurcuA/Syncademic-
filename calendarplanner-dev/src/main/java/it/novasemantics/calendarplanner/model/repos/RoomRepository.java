package it.novasemantics.calendarplanner.model.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import it.novasemantics.calendarplanner.model.entities.Room;


@Component
public interface RoomRepository extends JpaRepository<Room, Long> {

}

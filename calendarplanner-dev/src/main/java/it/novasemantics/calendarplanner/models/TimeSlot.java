package it.novasemantics.calendarplanner.model.entities;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeSlot{

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public LocalDateTime getStartDateTime() {
    	return LocalDateTime.of(date, startTime);
    }
    
    public TimeSlot(LocalDate date, LocalTime startTime, int duration) {
    	this(date, startTime, startTime.plusHours(duration));
    }
    
    public TimeSlot(LocalDate date, LocalTime startTime) {
        this(date, startTime, 4);
    }
    
	public int compareTo(Object o) {
		TimeSlot t = (TimeSlot)o;
    	if(getDate().isBefore(t.getDate()))
    		return -1;
    	if(getDate().isAfter(t.getDate()))
    		return 1;
    	return getStartTime().compareTo(t.getStartTime());
	}
	
	public boolean isBeforeOf(Object o) {
		return compareTo(o)<0;
	}

	public boolean isAfterOf(Object o) {
		return compareTo(o)>0;
	}

}

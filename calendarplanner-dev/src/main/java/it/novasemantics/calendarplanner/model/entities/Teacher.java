package it.novasemantics.calendarplanner.model.entities;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class Teacher {
	@NonNull
    private String name,surname;
    
    private Set<LocalDate> available = new HashSet<>();
	private Set<LocalDate> unavailable = new HashSet<>();
}

package it.novasemantics.calendarplanner.model.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Course {
	@NonNull
    private String title;
	@NonNull
    private StudentGroup group;
    private List<Course> prec= new ArrayList<>();
    private Map<Building, MinMax> lessonsPerBuilding = new HashMap<>();
    
    
    public Course(String title, StudentGroup g, Course... prec) {
    	this(title, g);
    	setPrec(List.of(prec));
    }
}

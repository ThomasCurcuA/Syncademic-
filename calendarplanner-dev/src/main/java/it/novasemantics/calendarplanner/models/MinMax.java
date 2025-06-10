package it.novasemantics.calendarplanner.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MinMax{
	private int min,max;
	
	public boolean contains(int x) {
		return min<=x && max>=x;
	}
}

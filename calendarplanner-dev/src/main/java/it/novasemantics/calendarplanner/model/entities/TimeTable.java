package it.novasemantics.calendarplanner.model.entities;

import java.util.ArrayList;
import java.util.List;

import ai.timefold.solver.core.api.domain.solution.PlanningEntityCollectionProperty;
import ai.timefold.solver.core.api.domain.solution.PlanningScore;
import ai.timefold.solver.core.api.domain.solution.PlanningSolution;
import ai.timefold.solver.core.api.domain.solution.ProblemFactCollectionProperty;
import ai.timefold.solver.core.api.domain.valuerange.ValueRangeProvider;
import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@PlanningSolution
public class TimeTable {

	@ProblemFactCollectionProperty
	@ValueRangeProvider
	private List<TimeSlot> timeslotList;
	@ProblemFactCollectionProperty
	@ValueRangeProvider
	private List<Room> roomList;
	@PlanningEntityCollectionProperty
	private List<Lesson> lessonList;

	@PlanningScore
	private HardSoftScore score;

	@ProblemFactCollectionProperty
	private List<Course> courses = new ArrayList<>();

	@ProblemFactCollectionProperty
	private List<Building> buildings = new ArrayList<>();

	public TimeTable(List<TimeSlot> timeslotList, List<Room> roomList, List<Lesson> lessonList) {
		this.timeslotList = timeslotList;
		this.roomList = roomList;
		this.lessonList = lessonList;

		for (Lesson l : lessonList)
			if (!courses.contains(l.getCourse()))
				courses.add(l.getCourse());
		for (Room r : roomList)
			if (!buildings.contains(r.getBuilding()))
				buildings.add(r.getBuilding());

	}
}

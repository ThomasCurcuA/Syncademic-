package it.novasemantics.calendarplanner.model.entities;

import ai.timefold.solver.core.api.domain.entity.PlanningEntity;
import ai.timefold.solver.core.api.domain.lookup.PlanningId;
import ai.timefold.solver.core.api.domain.variable.PlanningVariable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@PlanningEntity
@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class Lesson {

    @PlanningId
    @NonNull
    private Long id;
    @NonNull
    private int number;
    @NonNull
    private Course course;
    @NonNull
    private Teacher teacher;

    @PlanningVariable
    private TimeSlot timeSlot;
    @PlanningVariable
    private Room room;

}

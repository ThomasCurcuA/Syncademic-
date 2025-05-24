package it.novasemantics.calendarplanner;

import org.apache.commons.math3.util.Pair;

import ai.timefold.solver.core.api.score.buildin.hardsoft.HardSoftScore;
import ai.timefold.solver.core.api.score.stream.Constraint;
import ai.timefold.solver.core.api.score.stream.ConstraintCollectors;
import ai.timefold.solver.core.api.score.stream.ConstraintFactory;
import ai.timefold.solver.core.api.score.stream.ConstraintProvider;
import ai.timefold.solver.core.api.score.stream.Joiners;
import it.novasemantics.calendarplanner.model.entities.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeTableConstraintProvider implements ConstraintProvider {

	@Override
	public Constraint[] defineConstraints(ConstraintFactory cf) {
		return new Constraint[] {
				// Hard constraints
				roomConflict(cf), teacherConflict(cf), studentGroupConflict(cf), lessonOrder(cf),
				teacherUnavailable(cf),
				// Soft constraints
				teacherRoomStability(cf), studentRoomStability(cf), coursePrecedence(cf), courseVariety(cf),
				fulldayBuildingStudent(cf), fulldayBuildingTeacher(cf), noFreeDays(cf), buildingBalance(cf),
				buildingBalance2(cf) };
	}

	// A room can accommodate at most one lesson at the same time.
	Constraint roomConflict(ConstraintFactory constraintFactory) {
		return constraintFactory
				.forEachUniquePair(Lesson.class, Joiners.equal(Lesson::getTimeSlot), Joiners.equal(Lesson::getRoom))
				.penalize(HardSoftScore.ONE_HARD).asConstraint("Room conflict");
	}

	// A teacher can teach at most one lesson at the same time.
	Constraint teacherConflict(ConstraintFactory constraintFactory) {
		return constraintFactory
				.forEachUniquePair(Lesson.class, Joiners.equal(Lesson::getTimeSlot), Joiners.equal(Lesson::getTeacher))
				.penalize(HardSoftScore.ONE_HARD).asConstraint("Teacher conflict");
	}

	// A student can attend at most one lesson at the same time.
	Constraint studentGroupConflict(ConstraintFactory constraintFactory) {
		return constraintFactory
				.forEachUniquePair(Lesson.class, Joiners.equal(Lesson::getTimeSlot),
						Joiners.equal(l -> l.getCourse().getGroup()))
				.penalize(HardSoftScore.ONE_HARD).asConstraint("Student group conflict");
	}

	// Course precedence
	Constraint coursePrecedence(ConstraintFactory constraintFactory) {

		return constraintFactory.forEach(Lesson.class)
				.join(Lesson.class, Joiners.lessThan(l -> l.getTimeSlot().getStartDateTime()),
						Joiners.filtering((l1, l2) -> l1.getCourse().getPrec().contains(l2.getCourse())))
				.penalize(HardSoftScore.ONE_HARD).asConstraint("course precedence");
	}

	// respect orders of lessons in the same course
	Constraint lessonOrder(ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(Lesson.class)
				.join(Lesson.class, Joiners.equal(l -> l.getCourse()), Joiners.filtering(
						(l1, l2) -> l1.getTimeSlot().isBeforeOf(l2.getTimeSlot()) && l1.getNumber() > l2.getNumber()))
				.penalize(HardSoftScore.ONE_HARD).asConstraint("Lesson order");
	}

	Constraint teacherUnavailable(ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(Lesson.class)
				.filter(l -> l.getTeacher().getUnavailable().contains(l.getTimeSlot().getDate())
						|| !l.getTeacher().getAvailable().isEmpty()
								&& !l.getTeacher().getAvailable().contains(l.getTimeSlot().getDate()))
				.penalize(HardSoftScore.ONE_HARD).asConstraint("tracher availability");
	}

	/////////////////////////////////// SOFT

	// A teacher prefers to teach in a single room in a given day.
	Constraint teacherRoomStability(ConstraintFactory constraintFactory) {
		return constraintFactory
				.forEachUniquePair(Lesson.class, Joiners.equal(Lesson::getTeacher),
						Joiners.equal(l -> l.getTimeSlot().getDate()))
				.filter((lesson1, lesson2) -> lesson1.getRoom() != lesson2.getRoom()).penalize(HardSoftScore.ONE_SOFT)
				.asConstraint("Teacher room stability");
	}

	// Students prefer to be in a single room in a given day.
	Constraint studentRoomStability(ConstraintFactory constraintFactory) {
		return constraintFactory
				.forEachUniquePair(Lesson.class, Joiners.equal(l -> l.getCourse().getGroup()),
						Joiners.equal(l -> l.getTimeSlot().getDate()))
				.filter((lesson1, lesson2) -> lesson1.getRoom() != lesson2.getRoom()).penalize(HardSoftScore.ONE_SOFT)
				.asConstraint("Student room stability");
	}

	// Avoid two slots of the same course in the same day
	Constraint courseVariety(ConstraintFactory constraintFactory) {
		return constraintFactory
				.forEachUniquePair(Lesson.class, Joiners.equal(l -> l.getCourse()),
						Joiners.equal(l -> l.getTimeSlot().getDate()))
				.penalize(HardSoftScore.ONE_SOFT).asConstraint("Course variety");
	}

	// if the building is "fullday" students prefer to move for the full day
	Constraint fulldayBuildingStudent(ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(Lesson.class).filter(l -> l.getRoom().getBuilding().isFullDay())
				.join(TimeSlot.class, Joiners.equal(l -> l.getTimeSlot().getDate(), s -> s.getDate()))
				.ifNotExists(Lesson.class, Joiners.equal((l, slot) -> slot, l -> l.getTimeSlot()),
						Joiners.equal((l, slot) -> l.getCourse().getGroup(), l -> l.getCourse().getGroup()),
						Joiners.equal((l, slot) -> l.getRoom().getBuilding(), l -> l.getRoom().getBuilding()))
				.penalize(HardSoftScore.ONE_SOFT).asConstraint("full day building for students");
	}

	// if the building is "fullday" techers prefer to move for the full day
	Constraint fulldayBuildingTeacher(ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(Lesson.class).filter(l -> l.getRoom().getBuilding().isFullDay())
				.join(TimeSlot.class, Joiners.equal(l -> l.getTimeSlot().getDate(), s -> s.getDate()))
				.ifNotExists(Lesson.class, Joiners.equal((l, slot) -> slot, l -> l.getTimeSlot()),
						Joiners.equal((l, slot) -> l.getTeacher(), l -> l.getTeacher()),
						Joiners.equal((l, slot) -> l.getRoom().getBuilding(), l -> l.getRoom().getBuilding()))
				.penalize(HardSoftScore.ONE_SOFT).asConstraint("full day building for teachers");
	}

	// a student group should have no free days between lessons
	Constraint noFreeDays(ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(Lesson.class)
				.join(Lesson.class, Joiners.equal(l -> l.getCourse().getGroup()),
						Joiners.lessThan(l -> l.getTimeSlot().getDate()))
				.ifExists(TimeSlot.class, Joiners.lessThan((l1, l2) -> l1.getTimeSlot().getDate(), s -> s.getDate()),
						Joiners.greaterThan((l1, l2) -> l2.getTimeSlot().getDate(), s -> s.getDate()))
				.ifNotExists(Lesson.class,
						Joiners.lessThan((l1, l2) -> l1.getTimeSlot().getDate(), l -> l.getTimeSlot().getDate()),
						Joiners.greaterThan((l1, l2) -> l2.getTimeSlot().getDate(), l -> l.getTimeSlot().getDate()),
						Joiners.equal((l1, l2) -> l1.getCourse().getGroup(), l -> l.getCourse().getGroup()))
				.penalize(HardSoftScore.ONE_SOFT).asConstraint("no free days");
	}

	Constraint buildingBalance(ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(Lesson.class)
				.groupBy(l -> new Pair<>(l.getCourse(), l.getRoom().getBuilding()), ConstraintCollectors.count())
				.filter((p, n) -> {
					MinMax target = p.getFirst().getLessonsPerBuilding().get(p.getSecond());
					return target != null && !target.contains(n);
				}).penalize(HardSoftScore.ONE_HARD).asConstraint("building balance");
	}

	Constraint buildingBalance2(ConstraintFactory constraintFactory) {
		return constraintFactory.forEach(Course.class)
				.join(Building.class, Joiners.filtering((c, b) -> c.getLessonsPerBuilding().containsKey(c)))
				.ifNotExists(Lesson.class, Joiners.equal((c, b) -> c, Lesson::getCourse),
						Joiners.equal((c, b) -> b, l -> l.getRoom().getBuilding()))
				.penalize(HardSoftScore.ONE_HARD).asConstraint("building balance 2");
	}

	/*
	 * Constraint teacherTimeEfficiency(ConstraintFactory constraintFactory) { //
	 * Ateacher prefers to teach sequential lessons and dislikes gaps between
	 * lessons. return constraintFactory.forEach(Lesson.class).join(Lesson.class,
	 * Joiners.equal(Lesson::getTeacher), Joiners.equal(l ->
	 * ln.getTimeSlot().getDate())).filter((lesson1, lesson2) -> { Duration between
	 * = Duration.between(lesson1.getTimeslot().getEndTime(),
	 * lesson2.getTimeslot().getStartTime()); return !between.isNegative() &&
	 * between.compareTo(Duration.ofMinutes(30)) <= 0;
	 * }).reward(HardSoftScore.ONE_SOFT).asConstraint("Teacher time efficiency"); }
	 */

	/*
	 * Constraint studentGroupSubjectVariety(ConstraintFactory constraintFactory) {
	 * // A student group dislikes sequential lessons on the same subject. return
	 * constraintFactory.forEach(Lesson.class).join(Lesson.class,
	 * Joiners.equal(Lesson::getSubject), Joiners.equal(Lesson::getStudentGroup),
	 * Joiners.equal((lesson) -> lesson.getTimeslot().getDayOfWeek()))
	 * .filter((lesson1, lesson2) -> { Duration between =
	 * Duration.between(lesson1.getTimeslot().getEndTime(),
	 * lesson2.getTimeslot().getStartTime()); return !between.isNegative() &&
	 * between.compareTo(Duration.ofMinutes(30)) <= 0;
	 * }).penalize(HardSoftScore.ONE_SOFT).
	 * asConstraint("Student group subject variety"); }
	 */

}

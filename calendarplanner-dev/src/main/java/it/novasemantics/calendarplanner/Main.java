package it.novasemantics.calendarplanner;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ai.timefold.solver.core.api.solver.SolverFactory;
import ai.timefold.solver.core.config.solver.SolverConfig;
import it.novasemantics.calendarplanner.model.entities.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class Main implements CommandLineRunner {

	@Override
	public void run(String... args) throws Exception {
		SolverFactory<TimeTable> solverFactory = SolverFactory
				.create(new SolverConfig().withSolutionClass(TimeTable.class).withEntityClasses(Lesson.class)
						.withConstraintProviderClass(TimeTableConstraintProvider.class)
						.withTerminationSpentLimit(Duration.ofSeconds(10)));
		printTimetable(solverFactory.buildSolver().solve(generateDemoData()));
	}

	public static TimeTable generateDemoData() {
		List<TimeSlot> timeslotList = new ArrayList<>(10);
		for (int day = 0; day < 6; day++) {
			timeslotList.add(new TimeSlot(LocalDate.now().plusDays(day), LocalTime.of(9, 0)));
			timeslotList.add(new TimeSlot(LocalDate.now().plusDays(day), LocalTime.of(13, 0)));
		}
		
		Building fam = new Building("Famagosta", "",false);
		Building mind = new Building("MIND", "", true);
		List<Room> roomList = new ArrayList<>();
		roomList.add(new Room(null,"F1", 100, fam));
		roomList.add(new Room(null,"M1", 100, mind));
		roomList.add(new Room(null,"M2", 100, mind));

		List<Lesson> lessonList = new ArrayList<>();
		Teacher man = new Teacher("Mario", "Arrigoni Neri", Set.of(), Set.of(LocalDate.of(2024, Month.MAY, 9)));
		Teacher fri = new Teacher("Luca", "Frigerio");

		StudentGroup fs1 = new StudentGroup("FS1");
		StudentGroup fs2 = new StudentGroup("FS2");
		Course fond = new Course("FOND", fs1);
		fond.getLessonsPerBuilding().put(mind, new MinMax(1,2));
		Course imp = new Course("IMP", fs1, fond);
		Course be2 = new Course("BE2", fs2);

		long id = 0;
		for (int i = 1; i <= 5; i++) {
			lessonList.add(new Lesson(id++, i, fond, man));
			lessonList.add(new Lesson(id++, i, imp, man));
			lessonList.add(new Lesson(id++, i, be2, fri));
		}
		return new TimeTable(timeslotList, roomList, lessonList);
	}

	private static void printTimetable(TimeTable timeTable) {
		log.info("");
		List<Room> roomList = timeTable.getRoomList();
		List<Lesson> lessonList = timeTable.getLessonList();
		Map<TimeSlot, Map<Room, List<Lesson>>> lessonMap = lessonList.stream()
				.filter(lesson -> lesson.getTimeSlot() != null && lesson.getRoom() != null)
				.collect(Collectors.groupingBy(Lesson::getTimeSlot, Collectors.groupingBy(Lesson::getRoom)));
		log.info("|                 | " + roomList.stream().map(room -> String.format("%-15s", room.getName()))
				.collect(Collectors.joining(" | ")) + " |");
		log.info("|" + "-----------------|".repeat(roomList.size() + 1));
		for (TimeSlot timeslot : timeTable.getTimeslotList()) {
			List<List<Lesson>> cellList = roomList.stream().map(room -> {
				Map<Room, List<Lesson>> byRoomMap = lessonMap.get(timeslot);
				if (byRoomMap == null)
					return Collections.<Lesson>emptyList();
				List<Lesson> cellLessonList = byRoomMap.get(room);
				return Objects.requireNonNullElse(cellLessonList, Collections.<Lesson>emptyList());
			}).toList();

			log.info("| " + String.format("%-15s", timeslot.getDate().toString()) + " | "
					+ cellList.stream()
							.map(cellLessonList -> String.format("%-15s",
									cellLessonList.stream().map(l -> l.getCourse().getTitle() + " (" + l.getNumber() + ")")
											.collect(Collectors.joining(", "))))
							.collect(Collectors.joining(" | "))
					+ " |");
			log.info("| " + String.format("%-15s", timeslot.getStartTime()) + " | " + cellList.stream()
					.map(ll -> String.format("%-15s",
							ll.stream().map(l -> l.getTeacher().getSurname()).collect(Collectors.joining(", "))))
					.collect(Collectors.joining(" | ")) + " |");
			log.info("|                 | "
					+ cellList.stream()
							.map(ll -> String.format("%-15s",
									ll.stream().map(l -> l.getCourse().getGroup().getName())
											.collect(Collectors.joining(", "))))
							.collect(Collectors.joining(" | "))
					+ " |");
			log.info("|" + (timeslot.getStartTime().getHour()<12?"- - - - - - - - -|":"-----------------|").repeat(roomList.size() + 1));
		}
		List<Lesson> unassignedLessons = lessonList.stream()
				.filter(lesson -> lesson.getTimeSlot() == null || lesson.getRoom() == null).toList();
		if (!unassignedLessons.isEmpty()) {
			log.info("Unassigned lessons");
			for (Lesson lesson : unassignedLessons)
				log.info("  " + lesson.getCourse().getTitle() + " - " + lesson.getTeacher() + " - "
						+ lesson.getCourse().getGroup().getName());
		}
	}

}

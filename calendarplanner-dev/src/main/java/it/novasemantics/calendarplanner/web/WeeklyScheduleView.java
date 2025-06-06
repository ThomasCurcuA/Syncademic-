package it.novasemantics.calendarplanner.web;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Route("weekly-schedule")
public class WeeklyScheduleView extends VerticalLayout {

    private LocalDate currentMonday = LocalDate.now().with(DayOfWeek.MONDAY);
    private final Map<LocalDate, List<Row>> weeklyRows = new HashMap<>();

    private final Grid<Row> grid = new Grid<>();
    private final H1 title = new H1("Orario Settimanale");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");

    public WeeklyScheduleView() {
        addClassName("schedule-view");
        setSizeFull();

        title.addClassName("schedule-title");

        configureGrid();

        Button prevWeek = new Button("← Settimana precedente", e -> updateWeek(currentMonday.minusWeeks(1)));
        Button nextWeek = new Button("Settimana successiva →", e -> updateWeek(currentMonday.plusWeeks(1)));

        HorizontalLayout navigation = new HorizontalLayout(prevWeek, nextWeek);
        navigation.setSpacing(true);

        add(title, navigation, grid);
        updateGridHeaders();
        grid.setItems(getOrCreateRows(currentMonday));
    }

    private List<Row> getOrCreateRows(LocalDate monday) {
        return weeklyRows.computeIfAbsent(monday, m -> {
            List<Row> newRows = new ArrayList<>();
            for (int hour = 9; hour < 18; hour++) {
                newRows.add(new Row(String.format("%02d:00 - %02d:00", hour, hour + 1)));
            }
            return newRows;
        });
    }

    private void configureGrid() {
        grid.setHeight("600px");
        grid.setWidthFull();
        grid.addClassName("schedule-grid");

        // Colonna orari
        grid.addColumn(Row::getTime).setHeader("Orario").setAutoWidth(true);

        Editor<Row> editor = grid.getEditor();
        editor.setBuffered(false); // Salvataggio automatico
        Binder<Row> binder = new Binder<>(Row.class);
        editor.setBinder(binder);

        // 5 giorni (lun-ven)
        for (int i = 0; i < 5; i++) {
            final int index = i;

            Grid.Column<Row> column = grid.addColumn(row -> row.getEntries().get(index))
                    .setAutoWidth(true)
                    .setSortable(false);

            TextField field = new TextField();
            binder.forField(field).bind(
                    row -> row.getEntries().get(index),
                    (row, value) -> {
                        row.getEntries().set(index, value);
                        grid.getDataProvider().refreshItem(row);
                        // 🔽 Qui potresti anche salvare su DB: saveToDatabase(currentMonday, row, index, value);
                    }
            );

            column.setEditorComponent(field);
            column.setHeader("Giorno " + (index + 1));
        }

        grid.addItemDoubleClickListener(event -> editor.editItem(event.getItem()));
    }

    private void updateWeek(LocalDate newMonday) {
        currentMonday = newMonday;
        updateGridHeaders();
        grid.setItems(getOrCreateRows(currentMonday));
    }

    private void updateGridHeaders() {
        for (int i = 1; i <= 5; i++) {
            LocalDate date = currentMonday.plusDays(i - 1);
            String header = capitalize(date.getDayOfWeek().toString()) + " " + date.format(dateFormatter);
            grid.getColumns().get(i).setHeader(header);
        }
        title.setText("Orario Settimanale: " + currentMonday.format(dateFormatter) + " - " + currentMonday.plusDays(4).format(dateFormatter));
    }

    private String capitalize(String word) {
        return word.charAt(0) + word.substring(1).toLowerCase();
    }

    // === Classe riga della griglia ===
    public static class Row {
        private final String time;
        private final List<String> entries = new ArrayList<>(Collections.nCopies(5, ""));

        public Row(String time) {
            this.time = time;
        }

        public String getTime() {
            return time;
        }

        public List<String> getEntries() {
            return entries;
        }
    }
}

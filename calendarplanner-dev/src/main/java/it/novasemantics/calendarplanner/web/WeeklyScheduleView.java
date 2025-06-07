package it.novasemantics.calendarplanner.web;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@UIScope
@Route("weekly-schedule")
public class WeeklyScheduleView extends VerticalLayout {

    private static final long serialVersionUID = 7526471155622776147L;

    private LocalDate currentMonday = LocalDate.now().with(DayOfWeek.MONDAY);
    private final Map<LocalDate, List<Row>> weeklyRows = new HashMap<>();
    private final Grid<Row> grid = new Grid<>();
    private final H1 title = new H1("Orario Settimanale");
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM");
    private DatePicker datePicker;

    @PostConstruct
    public void init() {
        setSizeFull();
        setSpacing(false);
        setPadding(true);
        addClassName("schedule-view");

        configureGrid();

        // Pulsanti per navigare tra le settimane
        Button prevWeek = new Button("← Settimana precedente", e -> updateWeek(currentMonday.minusWeeks(1)));
        Button nextWeek = new Button("Settimana successiva →", e -> updateWeek(currentMonday.plusWeeks(1)));

        // DatePicker per selezionare una data qualsiasi nella settimana
        datePicker = new DatePicker();
        datePicker.setValue(LocalDate.now());
        datePicker.addValueChangeListener(e -> {
            LocalDate selected = e.getValue();
            if (selected != null) {
                LocalDate monday = selected.with(DayOfWeek.MONDAY);
                updateWeek(monday);
            }
        });

        HorizontalLayout navigation = new HorizontalLayout(prevWeek, datePicker, nextWeek);
        navigation.setSpacing(true);
        navigation.setWidthFull();
        navigation.getStyle().setMarginBottom("20px");

        title.addClassName("schedule-title");

        add(title, navigation, grid);

        updateGridHeaders();
        grid.setItems(getOrCreateRows(currentMonday));
    }

    private void configureGrid() {
        grid.setHeight("600px");
        grid.setWidthFull();
        grid.addClassName("schedule-grid");

        grid.addColumn(Row::getTime).setHeader("Orario").setAutoWidth(true);

        Editor<Row> editor = grid.getEditor();
        editor.setBuffered(false); // salvataggio automatico
        Binder<Row> binder = new Binder<>(Row.class);
        editor.setBinder(binder);

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
                    }
            );

            column.setEditorComponent(field);

            // Chiudi editor con Invio o Escape
            field.addKeyDownListener(Key.ENTER, e -> editor.save());
            field.addKeyDownListener(Key.ESCAPE, e -> editor.cancel());

            column.setHeader("Giorno " + (index + 1));
        }

        // Apri editor al doppio click sulla cella
        grid.addItemDoubleClickListener(event -> grid.getEditor().editItem(event.getItem()));
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

    private void updateWeek(LocalDate newMonday) {
        currentMonday = newMonday;
        datePicker.setValue(LocalDate.now().isBefore(newMonday) || LocalDate.now().isAfter(newMonday.plusDays(6))
                ? newMonday : LocalDate.now());
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

    public static class Row {

        private static final long serialVersionUID = 8150323952244061049L;

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

package it.novasemantics.calendarplanner.web;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UIScope
@Route("")
public class HomeView extends VerticalLayout {

    private static final long serialVersionUID = 2462031014228754921L;

    private HorizontalLayout menuRow;

    @PostConstruct
    public void init() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        H2 title = new H2("Calendar Planner");
        menuRow = new HorizontalLayout();
        menuRow.setSpacing(true);
        menuRow.setWidthFull();

        menuRow.add(
            new MenuCard("Gestione Edificio", "Vai alla gestione edificio singolo", "building", VaadinIcon.BUILDING),
            new MenuCard("Lista Edifici", "Visualizza tutti gli edifici", "buildings", VaadinIcon.LIST),
            new MenuCard("Gestione Aula", "Vai alla gestione delle aule", "room", VaadinIcon.NOTEBOOK),
            new MenuCard("Visualizzazione orario", "Vai alla visualizzazione dell'orario", "weekly-schedule", VaadinIcon.ALARM)
        );

        add(title, menuRow);
    }

    public static class MenuCard extends Div {

        private static final long serialVersionUID = 5283498842211354789L;

        public MenuCard(String title, String description, String route, VaadinIcon iconType) {
            setClassName("card");
            addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(route)));

            Icon icon = iconType.create();
            icon.setSize("40px");
            icon.addClassName("card-icon");

            H2 header = new H2(title);
            header.addClassName("card-title");

            Paragraph desc = new Paragraph(description);
            desc.addClassName("card-description");

            add(icon, header, desc);
        }
    }
}

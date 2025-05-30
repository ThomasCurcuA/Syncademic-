package it.novasemantics.calendarplanner.web;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class HomeView extends VerticalLayout {

    public HomeView() {
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(new H2("Calendar Planner"));

        HorizontalLayout row = new HorizontalLayout(
            createCard("Gestione Edificio", "Vai alla gestione edificio singolo", "building", VaadinIcon.BUILDING),
            createCard("Lista Edifici", "Visualizza tutti gli edifici", "buildings", VaadinIcon.LIST),
            createCard("Gestione Aula", "Vai alla gestione delle aule", "room", VaadinIcon.NOTEBOOK)
        );
        row.setSpacing(true);
        row.setWidthFull();

        add(row);
    }

    private Div createCard(String title, String description, String route, VaadinIcon iconType) {
        Div card = new Div();
        card.addClassName("card");
        card.addClickListener(e -> card.getUI().ifPresent(ui -> ui.navigate(route)));

        Icon icon = iconType.create();
        icon.setSize("40px");
        icon.addClassName("card-icon");

        H2 header = new H2(title);
        header.addClassName("card-title");

        Paragraph desc = new Paragraph(description);
        desc.addClassName("card-description");

        card.add(icon, header, desc);
        return card;
    }
}

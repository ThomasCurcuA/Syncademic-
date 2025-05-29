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

        H2 title = new H2("Calendar Planner");
        add(title);

        HorizontalLayout row = new HorizontalLayout();
        row.setSpacing(true);
        row.setWidthFull();

        row.add(
            createCard("Gestione Edificio", "Vai alla gestione edificio singolo", "building", VaadinIcon.BUILDING),
            createCard("Lista Edifici", "Visualizza tutti gli edifici", "buildings", VaadinIcon.LIST),
            createCard("Gestione Aula", "Vai alla gestione delle aule", "room", VaadinIcon.ARCHIVES)
        );

        add(row);
    }

    private Div createCard(String title, String description, String route, VaadinIcon iconType) {
        Div card = new Div();
        card.getStyle()
            .set("border", "1px solid #ccc")
            .set("border-radius", "12px")
            .set("padding", "1rem")
            .set("min-width", "200px")
            .set("max-width", "300px")
            .set("cursor", "pointer")
            .set("box-shadow", "0 2px 5px rgba(0, 0, 0, 0.1)")
            .set("background-color", "#fafafa")
            .set("margin-right", "1rem")
            .set("text-align", "center");

        card.addClickListener(e -> card.getUI().ifPresent(ui -> ui.navigate(route)));

        Icon icon = iconType.create();
        icon.setSize("40px");
        icon.getStyle().set("color", "#1676f3").set("margin-bottom", "0.5rem");

        H2 header = new H2(title);
        header.getStyle().set("font-size", "1.25rem").set("margin", "0");

        Paragraph desc = new Paragraph(description);
        desc.getStyle().set("font-size", "0.9rem").set("color", "#666");

        card.add(icon, header, desc);
        return card;
    }
}

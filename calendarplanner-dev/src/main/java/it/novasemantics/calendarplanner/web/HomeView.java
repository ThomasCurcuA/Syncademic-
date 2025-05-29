package it.novasemantics.calendarplanner.web;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("") // Home page (root)
public class HomeView extends VerticalLayout {
	
    public HomeView() {
        setSpacing(true);
        setPadding(true);

        H1 title = new H1("Benvenuto in Calendar Planner");

        Anchor buildingLink = new Anchor("building", "Gestione Edificio");
        Anchor buildingsLink = new Anchor("buildings", "Lista Edifici");
        Anchor roomLink = new Anchor("room", "Gestione Aula");

        add(title, buildingLink, buildingsLink, roomLink);
    }
}

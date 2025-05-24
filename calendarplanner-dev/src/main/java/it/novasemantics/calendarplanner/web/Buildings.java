package it.novasemantics.calendarplanner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.firitin.components.orderedlayout.VScroller;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller.ScrollDirection;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import it.novasemantics.calendarplanner.model.entities.Building;
import it.novasemantics.calendarplanner.model.services.RoomService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UIScope
@Route("buildings")
public class Buildings extends VerticalLayout {

	private static final long serialVersionUID = -426789154082102965L;

	private FlexLayout buildings = new FlexLayout();

	@Autowired
	private RoomService rs;

	private TextField text = new TextField(e -> search());

	@PostConstruct
	public void init() {
/*		VerticalLayout layout = new VerticalLayout();
        for(int i = 0; i< 100; i++)
            layout.add(new Paragraph("This is a paragraph "+i+" in scroller."));*/
        VScroller s = new VScroller(buildings, ScrollDirection.VERTICAL);
        s.setSizeFull();
//        s.addScrollToEndListener(e->layout.add(new Paragraph("added " + new Date().toLocaleString())));
        
		setSizeFull();
		rs.getBuildings().forEach(b -> buildings.add(new BuildingBox(b)));
		buildings.add(new AddBuildingBox());
		buildings.setFlexWrap(FlexWrap.WRAP);
		buildings.setWidthFull();
//		buildings.getStyle().setOverflow(Overflow.AUTO);
		text.setPlaceholder("search");
		text.setPrefixComponent(VaadinIcon.SEARCH.create());
		Div space = new Div();

		HorizontalLayout h;
		add(h = new HorizontalLayout(text), s);
		h.setSpacing(true);
		h.getStyle().setPadding("20px");
		h.setWidthFull();
		h.setFlexGrow(10, space);
		h.setFlexGrow(10, text);
		getStyle().setPadding("20px");
		setSpacing(false);
		h.getStyle().setBorderBottom("2px solid black");
		h.getStyle().setMarginBottom("30px");
	}

	private void search() {
		buildings.removeAll();
		rs.search(text.getValue()).forEach(b -> buildings.add(new BuildingBox(b)));
		buildings.add(new AddBuildingBox());
	}

	public static class AddBuildingBox extends VerticalLayout {

		private static final long serialVersionUID = 1L;

		public AddBuildingBox() {
			addClickListener(e -> getUI().get().navigate(BuildingView.class));
			setSizeUndefined();
			setClassName("add-building-box");
			Div name = new Div("+");
			add(name);
			setJustifyContentMode(JustifyContentMode.CENTER);
			setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		}
	}

	public static class BuildingBox extends VerticalLayout {
		private static final long serialVersionUID = -1779625989759079936L;

		public BuildingBox(Building b) {
			addClickListener(
					e -> getUI().get().navigate(BuildingView.class, QueryParameters.of("id", b.getId().toString())));
			setSizeUndefined();
			setClassName("building-box");
			Div name = new Div(b.getName());
			name.setClassName("building-name");
			setJustifyContentMode(JustifyContentMode.CENTER);
			HorizontalLayout rooms = new HorizontalLayout(new Icon(VaadinIcon.WORKPLACE),
					new Div("" + b.getRooms().size()));
			rooms.setSpacing(false);
			HorizontalLayout sits = new HorizontalLayout(new Icon(VaadinIcon.USER),
					new Div("" + (int) (1000 * Math.random())));
			sits.setSpacing(false);
			add(name, new HorizontalLayout(rooms, sits,
					new Icon(b.isFullDay() ? VaadinIcon.CALENDAR_CLOCK : VaadinIcon.CLOCK)));
			setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		}
	}

}

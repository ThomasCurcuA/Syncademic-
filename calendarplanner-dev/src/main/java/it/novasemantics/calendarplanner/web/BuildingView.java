package it.novasemantics.calendarplanner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.dom.Style.Overflow;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import it.novasemantics.calendarplanner.model.entities.Building;
import it.novasemantics.calendarplanner.model.entities.Room;
import it.novasemantics.calendarplanner.model.services.RoomService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScope
@Route("building")
public class BuildingView extends VerticalLayout implements BeforeEnterObserver {

	private TextField name = new TextField("name");
	private TextArea description = new TextArea("description");
	private Checkbox fullDay = new Checkbox("full day");
	private Button save = new Button("save", new Icon(VaadinIcon.CHECK), e -> save()),
			cancel = new Button("cancel", new Icon(VaadinIcon.STEP_BACKWARD), e -> back()),
			delete = new Button("delete", new Icon(VaadinIcon.TRASH), e -> delete());

	private Building building = new Building();
	private boolean creating;
	private FlexLayout rooms = new FlexLayout();

	@Autowired
	private RoomService rs;

//	@PostConstruct
	public void init() {
		setWidthFull();
		HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		buttons.setWidthFull();
		buttons.setJustifyContentMode(JustifyContentMode.END);
		add(createForm(), buttons);
		setFlexGrow(1, rooms);
		name.setValue(building.getName());
		description.setValue(building.getDescription());
		fullDay.setValue(building.isFullDay());

		Binder<Building> binder = new Binder<>(Building.class);
		binder.bindInstanceFields(this);
		binder.setBean(building);
		delete.setEnabled(!creating);

		////////////////////////////////////////////////
		rooms.setFlexWrap(FlexWrap.WRAP);
		rooms.setWidthFull();
		rooms.getStyle().setOverflow(Overflow.AUTO);
		building.getRooms().forEach(r->rooms.add(new RoomBox(r)));
		rooms.add(new AddRoomBox());
		add(rooms);
		getStyle().setPadding("20px");
		setSpacing(false);
	}

	private void save() {
		rs.save(building);
		Notification.show("building '" + name.getValue() + "' " + (creating ? "CREATED" : "UPDATED"))
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		back();
	}

	private void delete() {
		rs.delete(building);
		Notification.show("building '" + name.getValue() + "' DELETED")
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		back();
	}

	private void back() {
		getUI().get().navigate(Buildings.class);
	}

	private FormLayout createForm() {
		FormLayout f = new FormLayout(name, fullDay, description);
		description.setHeight("300px");
		f.setColspan(description, 2);
		return f;
	}


	public static class RoomBox extends VerticalLayout {
		public RoomBox(Room r) {
			addClickListener(e -> getUI().get().navigate(RoomView.class, QueryParameters.of("r",r.getId().toString())));
			setSizeUndefined();
			setClassName("room-box");
			Div name = new Div(r.getName());
			name.setClassName("room-name");
			setJustifyContentMode(JustifyContentMode.CENTER);
			HorizontalLayout sits = new HorizontalLayout(new Icon(VaadinIcon.USER), new Div("" + r.getSits()));
			sits.setSpacing(false);
			add(name, new HorizontalLayout(sits));
			setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		}
	}
	
	public class AddRoomBox extends VerticalLayout {

		private static final long serialVersionUID = 1L;

		public AddRoomBox() {
			addClickListener(e -> getUI().get().navigate(RoomView.class,QueryParameters.of("b", building.getId().toString())));
			setSizeUndefined();
			setClassName("add-room-box");
			Div name = new Div("+");
			add(name);
			setJustifyContentMode(JustifyContentMode.CENTER);
			setDefaultHorizontalComponentAlignment(Alignment.CENTER);
		}
	}


	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		String id = event.getLocation().getQueryParameters().getSingleParameter("id").orElse(null);
		creating = id == null;
		building = creating ? new Building("new building", "", false) : rs.getBuilding(Long.parseLong(id));
		init();

	}

}

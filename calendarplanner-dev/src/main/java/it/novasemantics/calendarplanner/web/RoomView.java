package it.novasemantics.calendarplanner.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.annotation.RequestScope;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import it.novasemantics.calendarplanner.model.entities.Building;
import it.novasemantics.calendarplanner.model.entities.Room;
import it.novasemantics.calendarplanner.model.services.RoomService;
import jakarta.transaction.Transactional;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestScope
@Route("room")
public class RoomView extends VerticalLayout implements BeforeEnterObserver {

	@Data
	public static class Pars {
		private Long roomId, builderId;
	}

	private TextField name = new TextField("name");
	private IntegerField sits = new IntegerField("sits");
	private Button save = new Button("save", new Icon(VaadinIcon.CHECK), e -> save()),
			cancel = new Button("cancel", new Icon(VaadinIcon.STEP_BACKWARD), e -> back()),
			delete = new Button("delete", new Icon(VaadinIcon.TRASH), e -> delete());

	private Room room;
	private Building building;
	private boolean creating;

	@Autowired
	private RoomService rs;

	public void init() {
		setWidthFull();
		HorizontalLayout buttons = new HorizontalLayout(save, cancel, delete);
		save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
		delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
		cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
		buttons.setWidthFull();
		buttons.setJustifyContentMode(JustifyContentMode.END);
		add(createForm(), buttons);

		Binder<Room> binder = new Binder<>(Room.class);
		binder.bindInstanceFields(this);
		binder.setBean(room);
		delete.setEnabled(!creating);
	}

	private void save() {
		rs.save(room);
		Notification.show("room '" + name.getValue() + "' " + (creating ? "CREATED" : "UPDATED"))
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		back();
	}

	@Transactional
	private void delete() {
		rs.delete(room);
		Notification.show("room '" + name.getValue() + "' DELETED")
				.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
		back();
	}

	private void back() {
		getUI().get().navigate(BuildingView.class, QueryParameters.of("id", building.getId().toString()));
	}

	private FormLayout createForm() {
		sits.setMin(1);
		sits.setMax(1000);
		sits.setStep(1);
		sits.setStepButtonsVisible(true);
		return new FormLayout(name, sits);
	}

	@Override
	public void beforeEnter(BeforeEnterEvent event) {
		QueryParameters pars = event.getLocation().getQueryParameters();
		String r = pars.getSingleParameter("r").orElse(null);
		String b = pars.getSingleParameter("b").orElse(null);
		creating = r == null;
		room = !creating ? rs.getRoom(Long.parseLong(r))
				: new Room(null, "nuova aula", 10, rs.getBuilding(Long.parseLong(b)));
		building = room.getBuilding();
		init();
	}

}

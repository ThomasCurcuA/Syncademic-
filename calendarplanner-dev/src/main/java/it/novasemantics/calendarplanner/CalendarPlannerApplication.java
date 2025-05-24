package it.novasemantics.calendarplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@Theme("nova-theme")
public class CalendarPlannerApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(CalendarPlannerApplication.class, args);
	}

}

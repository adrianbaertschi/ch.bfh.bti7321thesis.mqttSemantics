package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.ArrayList;
import java.util.List;

public class EventDescription {
	

	
	private final List<Event> events = new ArrayList<Event>();

	public List<Event> getEvents() {
		return events;
	}
	
	public boolean addEvent(Event event) {
		return this.events.add(event);
	}

}
package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EventDescription {
	
	private final List<Event> events = new ArrayList<Event>();
	private final Set<ComplexType> types = new HashSet<ComplexType>();


	public List<Event> getEvents() {
		return events;
	}
	
	public boolean addEvent(Event event) {
		
		if(event.getComplexType() != null) {
			types.add(event.getComplexType());
		}
		
		return this.events.add(event);
	}

	Set<ComplexType> getTypes() {
		return types;
	}

}
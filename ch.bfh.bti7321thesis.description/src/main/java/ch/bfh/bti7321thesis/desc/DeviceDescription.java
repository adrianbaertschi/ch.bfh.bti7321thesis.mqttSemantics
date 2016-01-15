package ch.bfh.bti7321thesis.desc;

import java.util.HashSet;
import java.util.Set;

import ch.bfh.bti7321thesis.desc.cmd.CommandDescription;
import ch.bfh.bti7321thesis.desc.event.EventDescription;
import ch.bfh.bti7321thesis.desc.state.StateDescription;

public class DeviceDescription {
	
	private String id;
	private String version;
	private String description;
	
	private StateDescription stateDescription;
	private EventDescription eventDescription;
	private CommandDescription commandDescription;
	private final Set<ComplexType> types = new HashSet<ComplexType>();
	
	public DeviceDescription() {
		
	}
	
	public DeviceDescription(String id, String version) {
		this.id = id;
		this.version = version;
	}

	public StateDescription getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(StateDescription stateDescription) {
		this.stateDescription = stateDescription;
		this.types.addAll(stateDescription.getTypes());
	}
	
	public EventDescription getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(EventDescription eventDescription) {
		this.eventDescription = eventDescription;
		this.types.addAll(eventDescription.getTypes());
	}

	public CommandDescription getCommandDescription() {
		return commandDescription;
	}

	public void setCommandDescription(CommandDescription commandDescription) {
		this.commandDescription = commandDescription;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Set<ComplexType> getComplexTypes() {
		return types;
	}

}

package ch.bfh.bti7321thesis.tinkerforge.desc;

public class DeviceDescription {
	
	private String id;
	private String version;
	private String description;
	
	private String topic;
	
	private StateDescription stateDescription;
	private EventDescription eventDescription;
	private CommandDescription commandDescription;
	
	public DeviceDescription(String id, String version) {
		this.id = id;
		this.version = version;
	}

	public StateDescription getStateDescription() {
		return stateDescription;
	}

	public void setStateDescription(StateDescription stateDescription) {
		this.stateDescription = stateDescription;
	}

	public EventDescription getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(EventDescription eventDescription) {
		this.eventDescription = eventDescription;
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

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}

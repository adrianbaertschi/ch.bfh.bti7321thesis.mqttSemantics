package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.HashMap;
import java.util.Map;


public class Command {
	private String name;
	private final Map<String, Object> params = new HashMap<String, Object>();
	private String linkedState; 

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getParameter() {
		return params;
	}

	public void setParam(String name, Range<?> range) {
		this.params.put(name, range);
	}

	public void setParam(String name, PresetValues<?> presetValues) {
		params.put(name, presetValues);
	}
	
	public void setParam(String name, ComplexType type) {
		params.put(name, type.getName());
	}

	public String getLinkedState() {
		return linkedState;
	}

	public void setLinkedState(String linkedState) {
		this.linkedState = linkedState;
	}
}
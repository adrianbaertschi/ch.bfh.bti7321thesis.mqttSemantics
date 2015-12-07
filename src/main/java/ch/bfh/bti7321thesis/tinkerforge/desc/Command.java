package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.HashMap;
import java.util.Map;

public class Command {
	private String name;
	private final Map<String, Object> params = new HashMap<String, Object>();
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, Object> getParams() {
		return params;
	}
	public void addParam(String name, Object value) {
		this.params.put(name, value);
	}
}
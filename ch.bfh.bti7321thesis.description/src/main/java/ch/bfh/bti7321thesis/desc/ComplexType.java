package ch.bfh.bti7321thesis.desc;

import java.util.ArrayList;
import java.util.List;

public class ComplexType {

	private String name;
	private final List<Property> properties = new ArrayList<Property>();
	private String summary;
	
	public ComplexType() {
		
	}

	public ComplexType(String name) {
		this.setName(name);
	}

	public void addNumberProperty(String name, Class<? extends Number> clazz, String desc) {
		this.properties.add(new Property(name, clazz, desc));
	}

	public void addStringProperty(String name, String desc) {
		this.properties.add(new Property(name, String.class, desc));
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}


}

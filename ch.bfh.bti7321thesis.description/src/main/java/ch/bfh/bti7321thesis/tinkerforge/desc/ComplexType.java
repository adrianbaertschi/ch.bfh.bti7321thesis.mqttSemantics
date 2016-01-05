package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.ArrayList;
import java.util.List;

public class ComplexType {
	
	private String name;
	private final List<Property> properties = new ArrayList<Property>();
	private String summary;
//	private final Map<String, Object> properties = new HashMap<String, Object>();
	
	public ComplexType(String name) {
		this.setName(name);
	}
	
	public void addNumberProperty(String name, Class<? extends Number> clazz) {
		this.properties.add(new Property(name, clazz, null));
//		this.properties.put(name, clazz.getSimpleName());
	}
	
	public void addNumberProperty(String name, Class<? extends Number> clazz, String desc) {
		this.properties.add(new Property(name, clazz, desc));
	}
	
	public void addStringProperty(String name) {
		this.properties.add(new Property(name, String.class, null));
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
//	
//	public Map<String, Object> getProperties() {
//	return properties;
//}

	public List<Property> getProperties() {
		return properties;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	class Property {

		private String name;
		private Class<?> clazz;
		private String description;

//		public Property(String name, Class<?> clazz) {
//			this.name= name;
//			this.clazz = clazz;
//		}
		
		public Property(String name, Class<?> clazz, String desc) {
			this.name= name;
			this.clazz = clazz;
			this.description = desc;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return clazz.getSimpleName();
		}

		public String getDescription() {
			return description;
		}

	}
}

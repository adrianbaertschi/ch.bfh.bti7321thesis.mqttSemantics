package ch.bfh.bti7321thesis.tinkerforge.desc;

public class Property {

	private String name;
	private Class<?> clazz;
	private String description;
	private String type;
	
	public Property() {
		
	}

	public Property(String name, Class<?> clazz, String desc) {
		this.name = name;
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



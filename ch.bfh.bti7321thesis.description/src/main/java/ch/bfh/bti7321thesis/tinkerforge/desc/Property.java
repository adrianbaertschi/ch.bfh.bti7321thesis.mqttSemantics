package ch.bfh.bti7321thesis.tinkerforge.desc;

public class Property {

	private String name;
	private String description;
	private String type;
	
	public Property() {
		
	}

	public Property(String name, Class<?> clazz, String desc) {
		this.name = name;
		this.type = clazz.getSimpleName();
		this.description = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}

	public void setType(String type) {
		this.type = type;
	}
}



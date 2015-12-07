package ch.bfh.bti7321thesis.tinkerforge.desc;

public class Event {
	private String name;
//	private Class<?> type;
	private Range<?> range;
	
	public Event(String name, Range<?> range) {
		this.name = name;
		this.setRange(range);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
//	public Class<?> getType() {
//		return type;
//	}
//	public void setType(Class<?> clazz) {
//		this.type = clazz;
//	}

	public Range<?> getRange() {
		return range;
	}

	public void setRange(Range<?> range) {
		this.range = range;
	}
	
}
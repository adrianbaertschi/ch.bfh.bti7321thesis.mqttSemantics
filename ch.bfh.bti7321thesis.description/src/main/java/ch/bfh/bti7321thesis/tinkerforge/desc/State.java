package ch.bfh.bti7321thesis.tinkerforge.desc;

public class State {
	private String name;
	private Range<?> range;
	private Options<?> options;
	private String complexTypeRef;
	private String desc;

	public State(String name,  Range<?> range, String desc) {
		this.name = name;
		this.range = range;
		this.desc = desc;
	}
	
	public State(String name, Options<?> options, String desc) {
		this.name = name;
		this.options =  options;
		this.desc = desc;
	}

	public State(String name, String complexTypeRef,  String desc) {
		this.name = name;
		this.complexTypeRef = complexTypeRef;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return desc;
	}

	public void getDescription(String desc) {
		this.desc = desc;
	}

	public Range<?> getRange() {
		return range;
	}

	public void setRange(Range<?> range) {
		this.range = range;
	}



	public String getComplexTypeRef() {
		return complexTypeRef;
	}

	public void setComplexTypeRef(String complexTypeRef) {
		this.complexTypeRef = complexTypeRef;
	}

	public Options<?> getOptions() {
		return options;
	}

	public void setOptions(Options<?> options) {
		this.options = options;
	}


}

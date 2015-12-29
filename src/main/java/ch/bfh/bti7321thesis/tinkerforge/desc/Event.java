package ch.bfh.bti7321thesis.tinkerforge.desc;

public class Event {
	private String name;
	// private Class<?> type;
	private Range<?> range;
	private String desc;
	private PresetValues<?> presetValues;

	public Event(String name, Range<?> range, String desc) {
		this.name = name;
		this.range = range;
		this.desc = desc;
	}
	public Event(String name, PresetValues<?> presetValues, String desc) {
		this.name = name;
		this.presetValues = presetValues;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Range<?> getRange() {
		return range;
	}

	public void setRange(Range<?> range) {
		this.range = range;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
	public PresetValues<?> getPresetValues() {
		return presetValues;
	}
	public void setPresetValues(PresetValues<?> presetValues) {
		this.presetValues = presetValues;
	}

}
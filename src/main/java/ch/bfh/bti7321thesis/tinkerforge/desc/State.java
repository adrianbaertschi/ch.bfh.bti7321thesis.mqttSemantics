package ch.bfh.bti7321thesis.tinkerforge.desc;

public class State {
	private String topic;
	private Object value;
	private Range<?> range;
	private PresetValues<?> presetValues;
	private String desc;

	public State(String topic, Object value, Range<?> range, String desc) {
		this.topic = topic;
		this.value = value;
		this.range = range;
		this.desc = desc;
	}
	
	public State(String topic, Object value, PresetValues<?> presetValues, String desc) {
		this.topic = topic;
		this.value = value;
		this.presetValues = presetValues;
		this.desc = desc;
	}

	public State(String topic, Object value,  String desc) {
		this.topic = topic;
		this.value = value;
		this.desc = desc;
	}

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Range<?> getRange() {
		return range;
	}

	public void setRange(Range<?> range) {
		this.range = range;
	}

	public PresetValues<?> getPresetValues() {
		return presetValues;
	}

	public void setPresetValues(PresetValues<?> presetValues) {
		this.presetValues = presetValues;
	}

}

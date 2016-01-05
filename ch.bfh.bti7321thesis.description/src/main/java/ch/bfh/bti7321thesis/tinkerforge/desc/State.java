package ch.bfh.bti7321thesis.tinkerforge.desc;

public class State {
	private String topic;
	private Range<?> range;
	private PresetValues<?> presetValues;
	private String complexTypeRef;
	private String desc;

	public State(String topic,  Range<?> range, String desc) {
		this.topic = topic;
		this.range = range;
		this.desc = desc;
	}
	
	public State(String topic, PresetValues<?> presetValues, String desc) {
		this.topic = topic;
		this.presetValues = presetValues;
		this.desc = desc;
	}

	public State(String topic, String complexTypeRef,  String desc) {
		this.topic = topic;
		this.setComplexTypeRef(complexTypeRef);
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

	public String getComplexTypeRef() {
		return complexTypeRef;
	}

	public void setComplexTypeRef(String complexTypeRef) {
		this.complexTypeRef = complexTypeRef;
	}


}

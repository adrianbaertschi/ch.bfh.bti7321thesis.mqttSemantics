package ch.bfh.bti7321thesis.tinkerforge.desc;

public class State {
	private String topic;
	private Object value;
	private String desc;
	
	
	public State(String topic, Object value, String desc) {
		this.topic = topic;
		this.value = value;
		this.desc = desc;
	}
	
	public State(String key, Object value) {
		this.topic = key;
		this.setValue(value);
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
	

}

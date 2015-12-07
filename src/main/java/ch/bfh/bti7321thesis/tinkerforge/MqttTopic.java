package ch.bfh.bti7321thesis.tinkerforge;

import java.util.ArrayList;
import java.util.List;


public class MqttTopic {
	private List<String> topics = new ArrayList<String>();

	public MqttTopic() {
//		this.topics = builder.topics;
	}

	public MqttTopic(String encodedTopicString) {
		this.topics = new ArrayList<String>();
		for (String topic : encodedTopicString.split("/")) {
			topics.add(topic);
		}
	}

	public String getTopic(int level) {
		return topics.get(level);
	}

	public String getLast() {
		return topics.get(topics.size() - 1);
	}
	
	public void append(String entry) {
		this.topics.add(entry);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String s : topics) {
			sb.append(s).append("/");
		}
		sb.setLength(sb.length() - 1);

		return sb.toString();
	}
	
	public int getSize() {
		return topics.size();
	}

//	public static class MqttTopicBuilder {
//		private List<String> topics = new ArrayList<String>();
//
//		public MqttTopicBuilder append(String topic) {
//			this.topics.add(topic);
//			return this;
//		}
//
//		public MqttTopic build() {
//			return new MqttTopic(this);
//		}
//	}

}

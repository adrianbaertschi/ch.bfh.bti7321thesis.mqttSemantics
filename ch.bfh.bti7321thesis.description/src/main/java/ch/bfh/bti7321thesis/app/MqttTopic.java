package ch.bfh.bti7321thesis.app;

import java.util.ArrayList;
import java.util.List;

public class MqttTopic {
	private List<String> topics = new ArrayList<String>();

	public MqttTopic() {
	}

	public MqttTopic(String encodedTopicString) {
		
		if(encodedTopicString == null) {
			throw new IllegalArgumentException("encodedTopicString is null");
		}
		
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

}

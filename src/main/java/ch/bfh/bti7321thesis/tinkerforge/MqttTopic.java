package ch.bfh.bti7321thesis.tinkerforge;

import java.util.ArrayList;
import java.util.List;

public class MqttTopic {
	
	private List<String> topics;
	
	public MqttTopic(MqttTopicBuilder builder) {
		this.topics = builder.topics;
		
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(String s : topics) {
			sb.append(s).append("/");
		}
		sb.setLength(sb.length()-1);
		
		return sb.toString();
	}

	
	public static class MqttTopicBuilder {
		private List<String> topics = new ArrayList<String>();
		
		public MqttTopicBuilder append(String topic) {
			this.topics.add(topic);
			return this;
		}
		
		public MqttTopic build() {
			return new MqttTopic(this);
		}
	}
	

}

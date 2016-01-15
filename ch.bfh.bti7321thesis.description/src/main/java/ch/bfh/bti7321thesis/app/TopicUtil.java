package ch.bfh.bti7321thesis.app;

import ch.bfh.bti7321thesis.tinkerforge.devices.MqttDevice;

public class TopicUtil {
	
	public String getBaseTopic(String appId, MqttDevice mqttThing) {
		MqttTopic topic = new MqttTopic();
		topic.append(appId);
		topic.append(mqttThing.getGroup());
		topic.append(mqttThing.getSubGroup());
		topic.append(mqttThing.getDeviceType());
		topic.append(mqttThing.getDeviceInstance());
		return topic.toString();
	}
}

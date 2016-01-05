package ch.bfh.bti7321thesis.tinkerforge;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import ch.bfh.bti7321thesis.tinkerforge.devices.MqttThing;

// TODO: rename ThingToTopic??
// TODO: all in one method
public class BrickletToMqttConverter {
	
	public String getBaseTopic(String appId, MqttThing mqttThing) {
		MqttTopic topic = getBase(appId, mqttThing);
		topic.append(mqttThing.getDeviceInstance());
		return topic.toString();
	}
	
	private MqttTopic getBase(String appId, MqttThing mqttThing) {
		String hostName = "Host";
		try {
			hostName = Inet4Address.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			hostName = "ERROR";
		}
		MqttTopic topic =  null;
			topic =  new MqttTopic();
			topic.append(appId);
			topic.append(hostName);
			topic.append(mqttThing.getStackName());
			topic.append(mqttThing.getDeviceType());
			return topic;
	}

}

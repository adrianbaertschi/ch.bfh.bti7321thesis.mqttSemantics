package ch.bfh.bti7321thesis.tinkerforge;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.devices.MqttThing;
import ch.bfh.bti7321thesis.tinkerforge.util.TinkerforgeBrickletDB;

public class BrickletToMqttConverter {
	
	private final String TOPIC_MASTER = "ch.bfh.barta3"; 
	
	public String getBaseTopic(MqttThing mqttThing) {
		MqttTopic topic=  getBase(mqttThing);
		topic.append(mqttThing.getUid());
		return topic.toString();
	}
	
	private MqttTopic getBase(MqttThing mqttThing) {
		String hostName = "Host";
		try {
			hostName = Inet4Address.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			hostName = "ERROR";
		}
		MqttTopic topic =  null;
			topic =  new MqttTopic();
			topic.append(TOPIC_MASTER);
			topic.append(hostName);
			topic.append(mqttThing.getStackName());
			try {
				topic.append(TinkerforgeBrickletDB.getDisplayName(mqttThing.getDevice().getIdentity().deviceIdentifier));
			} catch (TimeoutException | NotConnectedException e) {
				e.printStackTrace();
			}
			return topic;
	}

}

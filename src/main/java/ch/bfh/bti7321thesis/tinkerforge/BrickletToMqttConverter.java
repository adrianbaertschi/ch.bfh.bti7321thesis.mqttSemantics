package ch.bfh.bti7321thesis.tinkerforge;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.devices.MqttThing;
import ch.bfh.bti7321thesis.tinkerforge.util.TinkerforgeBrickletDB;

public class BrickletToMqttConverter {
	
	private final String TOPIC_MASTER = "ch.bfh.barta3"; 
	
	// TODO: refactor, so this can be replaced wth getBaseTopic(MqttThing mqttThing)
	@Deprecated
	public String getBaseTopic(Device device, String stackName) {
		
		String hostName = "Host";
		try {
			hostName = Inet4Address.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hostName = "ERROR";
		}
		MqttTopic topic =  null;
		try {
			topic =  new MqttTopic();
			topic.append(TOPIC_MASTER);
			topic.append(hostName);
			topic.append(stackName);
			topic.append(TinkerforgeBrickletDB.getDisplayName(device.getIdentity().deviceIdentifier));
			topic.append(device.getIdentity().uid); // TODO uid or connectedUid?? 
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topic.toString();
	}
	
	public String getBaseTopic(MqttThing mqttThing) {
		String hostName = "Host";
		try {
			hostName = Inet4Address.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			hostName = "ERROR";
		}
		MqttTopic topic=  getBase(mqttThing);
		topic.append(mqttThing.getUid());
		return topic.toString();
	}
	
	public String getTopicToThingtype(MqttThing mqttThing, int level) {
		MqttTopic base = getBase(mqttThing);
		
		MqttTopic newTopic = new MqttTopic();
		for(int i = 0; i < base.getSize(); i++) {
			newTopic.append(base.getTopic(i));
		}
		
		return newTopic.toString();
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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return topic;
	}

}

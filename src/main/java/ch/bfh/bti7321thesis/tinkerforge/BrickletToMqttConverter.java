package ch.bfh.bti7321thesis.tinkerforge;

import java.net.Inet4Address;
import java.net.UnknownHostException;

import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.util.TinkerforgeBrickletDB;

public class BrickletToMqttConverter {
	
	private final String TOPIC_MASTER = "ch.bfh.barta3"; 
	
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
			topic =  new MqttTopic.MqttTopicBuilder()
			.append(TOPIC_MASTER)
//			.append("IOTGW")
			.append(hostName)
			.append(stackName)
			.append(TinkerforgeBrickletDB.getDisplayName(device.getIdentity().deviceIdentifier))
			.append(device.getIdentity().uid) // TODO uid or connectedUid?? 
			.build();
			;
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return topic.toString();
	}

}

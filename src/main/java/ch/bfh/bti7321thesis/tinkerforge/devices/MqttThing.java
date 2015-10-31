package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.Map;

import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public abstract class MqttThing {
	
	String stackName;

	public abstract Device getDevice();
	
	public String getUid() {
		try {
			return getDevice().getIdentity().uid;
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public abstract boolean handleAction(String action, byte[] payload);
	
	public abstract Map<String, Object> getState();

	public String getStackName() {
		return stackName;
	}
	
}

package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.Map;

import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;

public abstract class MqttThing<T extends Device> {
	
	T bricklet;
	String stackName;
	
	// TODO Setupt, init ein eigene Methode gliedern, ähnlich wie TinkerForgeBaseSensor

	public T getDevice() {
		return bricklet;
	}
	
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
	
	public abstract DeviceDescription getDescription();
	
}

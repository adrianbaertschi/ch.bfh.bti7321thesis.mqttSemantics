package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.Map;

import com.tinkerforge.Device;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.MqttPublisher;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;
import ch.bfh.bti7321thesis.tinkerforge.util.TinkerforgeBrickletDB;

public abstract class MqttDevice<T extends Device> {
	
	T bricklet;
	String stackName;
	String uid;
	IPConnection ipcon;
	
	public MqttDevice(String uid, IPConnection ipcon, String stackName) {
		
		this.stackName = stackName;
		this.uid = uid;
		this.ipcon = ipcon;
		setUpDevice();
		
		MqttPublisher.getInstance().publishDeviceState(this.toThing());
		MqttPublisher.getInstance().publishDesc(this.toThing());
	}
	
	
	// TODO Setupt, init ein eigene Methode gliedern, ähnlich wie TinkerForgeBaseSensor
	
	public abstract void setUpDevice();

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

	public abstract boolean handleCommand(String commandName, byte[] payload);
	
	public abstract Map<String, Object> getState();

	public String getStackName() {
		return stackName;
	}
	
	public abstract DeviceDescription getDescription();
	
	public MqttThing toThing() {
		MqttThing mqttThing = new MqttThing();
		mqttThing.setStackName(this.getStackName());
		try {
			mqttThing.setDeviceType(TinkerforgeBrickletDB.getDisplayName(this.getDevice().getIdentity().deviceIdentifier));
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
		mqttThing.setDeviceInstance(this.getUid());
		
		mqttThing.setState(this.getState());
		
		mqttThing.setDeviceDescription(this.getDescription());
		
		return mqttThing;
		
	}
		
}

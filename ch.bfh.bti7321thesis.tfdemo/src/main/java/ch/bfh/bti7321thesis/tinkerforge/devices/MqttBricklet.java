package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Map;

import com.tinkerforge.Device;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.app.MqttDevice;
import ch.bfh.bti7321thesis.app.MqttPublisher;
import ch.bfh.bti7321thesis.desc.DeviceDescription;
import ch.bfh.bti7321thesis.tinkerforge.util.TinkerforgeBrickletDB;

public abstract class MqttBricklet<T extends Device> {
	
	T bricklet;
	String stackName;
	String uid;
	IPConnection ipcon;
	
	public MqttBricklet(String uid, IPConnection ipcon, String stackName) {
		
		this.stackName = stackName;
		this.uid = uid;
		this.ipcon = ipcon;
		setUpBricklet();
		
		MqttPublisher.getInstance().publishState(this.toDevice());
		MqttPublisher.getInstance().publishDesc(this.toDevice());
	}
	
	
	public abstract void setUpBricklet();

//	public T getDevice() {
//		return bricklet;
//	}
	
	public String getUid() {
		try {
			return bricklet.getIdentity().uid;
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
	
	public MqttDevice toDevice() {
		MqttDevice mqttThing = new MqttDevice();
		
		String hostName = "Host";
		try {
			hostName = Inet4Address.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			hostName = "ERROR";
		}
		mqttThing.setGroup(hostName);
		
		mqttThing.setSubGroup(this.getStackName());
		try {
			mqttThing.setDeviceType(TinkerforgeBrickletDB.getDisplayName(this.bricklet.getIdentity().deviceIdentifier));
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
		mqttThing.setDeviceInstance(this.getUid());
		
		mqttThing.setState(this.getState());
		
		mqttThing.setDeviceDescription(this.getDescription());
		
		return mqttThing;
		
	}
		
}

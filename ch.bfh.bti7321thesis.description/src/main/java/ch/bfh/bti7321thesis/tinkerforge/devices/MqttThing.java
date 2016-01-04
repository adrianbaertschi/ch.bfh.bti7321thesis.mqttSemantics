package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.Map;

import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;

public class MqttThing {
	
	// TODO: remove?
	private String stackName;
	private String deviceType;
	private String deviceInstance;
	
	private Map<String, Object> state;
	
	private DeviceDescription deviceDescription;
	
//	// TODO: Rename to handleCommand
//	public abstract boolean handleAction(String action, byte[] payload);
//	
//	public abstract Map<String, Object> getState();


	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceInstance() {
		return deviceInstance;
	}

	public void setDeviceInstance(String deviceInstance) {
		this.deviceInstance = deviceInstance;
	}

	public String getStackName() {
		return stackName;
	}

	public void setStackName(String stackName) {
		this.stackName = stackName;
	}

	public DeviceDescription getDeviceDescription() {
		return deviceDescription;
	}

	public void setDeviceDescription(DeviceDescription deviceDescription) {
		this.deviceDescription = deviceDescription;
	}

	public Map<String, Object> getState() {
		return state;
	}

	public void setState(Map<String, Object> state) {
		this.state = state;
	}

	
}

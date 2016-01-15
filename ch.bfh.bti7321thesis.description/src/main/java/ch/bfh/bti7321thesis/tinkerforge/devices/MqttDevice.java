package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.Map;

import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;

public class MqttDevice {
	
	private String group;
	private String subGroup;
	private String deviceType;
	private String deviceInstance;
	
	private Map<String, Object> state;
	
	private DeviceDescription deviceDescription;
	
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

	public String getSubGroup() {
		return subGroup;
	}

	public void setSubGroup(String subGroup) {
		this.subGroup = subGroup;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	
}

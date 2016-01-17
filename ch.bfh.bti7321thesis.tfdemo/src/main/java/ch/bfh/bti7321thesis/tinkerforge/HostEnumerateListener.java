package ch.bfh.bti7321thesis.tinkerforge;

import java.util.logging.Logger;

import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.IPConnection;
import com.tinkerforge.IPConnection.EnumerateListener;
import com.tinkerforge.IPConnectionBase;

import ch.bfh.bti7321thesis.tinkerforge.devices.DualButtonMqttBricklet;
import ch.bfh.bti7321thesis.tinkerforge.devices.HumidityMqttBricklet;
import ch.bfh.bti7321thesis.tinkerforge.devices.JoyStickDevice;
import ch.bfh.bti7321thesis.tinkerforge.devices.TempIrMqttBricklet;

public class HostEnumerateListener implements EnumerateListener {
	
	private Logger LOG = Logger.getLogger(this.getClass().getName());

	private String hostname;
	private IPConnection ipcon;

	public HostEnumerateListener(String hostname, IPConnection ipcon) {
		this.setHostname(hostname);
		this.ipcon = ipcon;
		
//		MockDevice mockDevice = new MockDevice(hostname, ipcon, hostname);
//		TinkerforgeDeviceRegistry.getInstance().add(mockDevice);
	}

	@Override
	public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
			short[] firmwareVersion, int deviceIdentifier, short enumerationType) {
		String enumInfo = String.format("uid %s, connectedUid %s, position %c, deviceIdentifier %d, enumerationType %d",
				uid, connectedUid, position, deviceIdentifier, enumerationType);
		LOG.info(enumInfo);

		if (enumerationType != IPConnectionBase.ENUMERATION_TYPE_AVAILABLE) {
			return;
		}
		

		switch (deviceIdentifier) {
		case BrickletTemperatureIR.DEVICE_IDENTIFIER:
			LOG.info(this.getHostname() + " " + BrickletTemperatureIR.DEVICE_DISPLAY_NAME + " found");
			
			TempIrMqttBricklet tempIrDevice = new TempIrMqttBricklet(uid, ipcon, hostname);
			TinkerforgeDeviceRegistry.getInstance().add(tempIrDevice);
			
			break;
		case BrickletDualButton.DEVICE_IDENTIFIER:
			LOG.info(hostname + " " + BrickletDualButton.DEVICE_DISPLAY_NAME + " found");
			
			DualButtonMqttBricklet dualButtonDevice = new DualButtonMqttBricklet(uid, ipcon, hostname);
			TinkerforgeDeviceRegistry.getInstance().add(dualButtonDevice);
			
			break;
		case BrickletJoystick.DEVICE_IDENTIFIER:
			LOG.info(hostname + " " + BrickletJoystick.DEVICE_DISPLAY_NAME + " found");
			
			JoyStickDevice joyStickDevice = new JoyStickDevice(uid, ipcon, hostname);
			TinkerforgeDeviceRegistry.getInstance().add(joyStickDevice);
			
			break;
//		case BrickletMotionDetector.DEVICE_IDENTIFIER:
//			LOG.info(hostname + " " + BrickletMotionDetector.DEVICE_DISPLAY_NAME + " found");
//			
//			MotionDetectorDevice motionDetectorDevice = new MotionDetectorDevice(uid, ipcon, hostname);
//			TinkerforgeDeviceRegistry.getInstance().add(motionDetectorDevice);
//			
//			break;
		case BrickletHumidity.DEVICE_IDENTIFIER:
			LOG.info(hostname + " " + BrickletHumidity.DEVICE_DISPLAY_NAME + " found");
			HumidityMqttBricklet humidityDevice = new HumidityMqttBricklet(uid, ipcon, hostname);
			TinkerforgeDeviceRegistry.getInstance().add(humidityDevice);
		default:
			LOG.warning("Unknown Device: " + enumInfo);
			break;
		}
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}

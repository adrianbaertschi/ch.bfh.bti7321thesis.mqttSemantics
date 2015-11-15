package ch.bfh.bti7321thesis.tinkerforge;

import java.util.logging.Logger;

import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.IPConnection;
import com.tinkerforge.IPConnection.EnumerateListener;
import com.tinkerforge.IPConnectionBase;

import ch.bfh.bti7321thesis.tinkerforge.devices.DualButtonDevice;
import ch.bfh.bti7321thesis.tinkerforge.devices.TempIrDevice;

public class HostEnumerateListener implements EnumerateListener {
	
	private Logger LOG = Logger.getLogger(this.getClass().getName());

	private String hostname;
	private IPConnection ipcon;

	public HostEnumerateListener(String hostname, IPConnection ipcon) {
		this.setHostname(hostname);
		this.ipcon = ipcon;
	}

	@Override
	public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
			short[] firmwareVersion, int deviceIdentifier, short enumerationType) {
		String enumInfo = String.format("uid %s, connectedUid %s, position %c, deviceIdentifier %d, enumerationType %d",
				uid, connectedUid, position, deviceIdentifier, enumerationType);
		LOG.fine(enumInfo);

		if (enumerationType != IPConnectionBase.ENUMERATION_TYPE_AVAILABLE) {
			return;
		}

		switch (deviceIdentifier) {
		case BrickletTemperatureIR.DEVICE_IDENTIFIER:
			LOG.info(this.getHostname() + " " + BrickletTemperatureIR.DEVICE_DISPLAY_NAME + " found");
			
			// TODO move BrickletTemperatureIR constr to Device
			TempIrDevice tempIrDevice = new TempIrDevice(uid, ipcon, hostname);
			TinkerforgeDeviceRegistry.getInstance().add(tempIrDevice);
			
//			BrickletSetup.setUpTempIR(brickletTemperatureIR, hostname);
			break;
		case BrickletDualButton.DEVICE_IDENTIFIER:
			LOG.info(hostname + " " + BrickletDualButton.DEVICE_DISPLAY_NAME + " found");
//			BrickletDualButton brickletDualButton = new BrickletDualButton(uid, ipcon);
//			BrickletSetup.setUpDualButton(uid, ipcon, hostname);
			
			DualButtonDevice device = new DualButtonDevice(uid, ipcon, hostname);
			TinkerforgeDeviceRegistry.getInstance().add(device);
			MqttPublisher.getInstance().publishDeviceState(device);

			break;
		case BrickletJoystick.DEVICE_IDENTIFIER:
			LOG.info(hostname + " " + BrickletJoystick.DEVICE_DISPLAY_NAME + " found");
			BrickletJoystick brickletJoystick = new BrickletJoystick(uid, ipcon);
			BrickletSetup.setUpJoystick(brickletJoystick, hostname);
			// MqttPublisher.getInstance().publishDevice(host,
			// brickletJoystick);
			break;
		case BrickletMotionDetector.DEVICE_IDENTIFIER:
			LOG.info(hostname + " " + BrickletMotionDetector.DEVICE_DISPLAY_NAME + " found");
			BrickletMotionDetector brickletMotionDetector = new BrickletMotionDetector(uid, ipcon);
			BrickletSetup.setUpMotionDetetor(brickletMotionDetector, hostname);
			break;
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

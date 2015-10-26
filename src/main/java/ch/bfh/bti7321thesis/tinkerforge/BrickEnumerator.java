package ch.bfh.bti7321thesis.tinkerforge;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.IPConnection;
import com.tinkerforge.IPConnectionBase;
import com.tinkerforge.NotConnectedException;

public class BrickEnumerator extends Thread {

	private static Logger LOG = Logger.getLogger(BrickEnumerator.class.getName());
	
	private static final int PORT = 4223;

	private String host;

	private IPConnection ipcon;

	public BrickEnumerator(String host) {
		this.host = host;
	}
	
	public void closeConnection() {
		try {
			this.ipcon.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		LOG.info("Enumerating for host " + host);

		ipcon = new IPConnection();

		ipcon.addEnumerateListener(new HostEnumerateListener(host) {

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
					BrickletTemperatureIR brickletTemperatureIR = new BrickletTemperatureIR(uid, ipcon);
					BrickletSetup.setUpTempIR(brickletTemperatureIR, host);
					break;
				case BrickletDualButton.DEVICE_IDENTIFIER:
					LOG.info(host + " " + BrickletDualButton.DEVICE_DISPLAY_NAME + " found");
					BrickletDualButton brickletDualButton = new BrickletDualButton(uid, ipcon);
					BrickletSetup.setUpDualButton(brickletDualButton, host);
					// MqttPublisher.getInstance().publishDevice(host,
					// brickletDualButton);
					break;
				case BrickletJoystick.DEVICE_IDENTIFIER:
					LOG.info(host + " " + BrickletJoystick.DEVICE_DISPLAY_NAME + " found");
					BrickletJoystick brickletJoystick = new BrickletJoystick(uid, ipcon);
					BrickletSetup.setUpJoystick(brickletJoystick, host);
					// MqttPublisher.getInstance().publishDevice(host,
					// brickletJoystick);
					break;
				case BrickletMotionDetector.DEVICE_IDENTIFIER:
					LOG.info(host + " " + BrickletMotionDetector.DEVICE_DISPLAY_NAME + " found");
					BrickletMotionDetector brickletMotionDetector = new BrickletMotionDetector(uid, ipcon);
					BrickletSetup.setUpMotionDetetor(brickletMotionDetector, host);
					break;
				default:
					LOG.warning("Unknown Device: " + enumInfo);
					break;
				}

			}
		});
		try {
			ipcon.connect(host, PORT);
			ipcon.enumerate();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AlreadyConnectedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

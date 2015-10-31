package ch.bfh.bti7321thesis.tinkerforge;

import java.util.logging.Logger;

import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletDualButton.StateChangedListener;
import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletJoystick.PositionListener;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletMotionDetector.DetectionCycleEndedListener;
import com.tinkerforge.BrickletMotionDetector.MotionDetectedListener;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.BrickletTemperatureIR.AmbientTemperatureListener;
import com.tinkerforge.BrickletTemperatureIR.ObjectTemperatureListener;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.devices.DualButtonDevice;
import ch.bfh.bti7321thesis.tinkerforge.devices.JoyStickDevice;
import ch.bfh.bti7321thesis.tinkerforge.devices.TempIrDevice;

public class BrickletSetup {

	private static Logger LOG = Logger.getLogger(BrickletSetup.class.getName());

	public static void setUpTempIR(BrickletTemperatureIR brickletTemperatureIR, String stackHost) {

		TempIrDevice device = new TempIrDevice(brickletTemperatureIR, stackHost);
		TinkerforgeDeviceRegistry.getInstance().add(device);

		MqttPublisher.getInstance().publishDeviceState(device);

		try {
			brickletTemperatureIR.setObjectTemperatureCallbackPeriod(1000);
			brickletTemperatureIR.setAmbientTemperatureCallbackPeriod(1000);
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		brickletTemperatureIR.addObjectTemperatureListener(new ObjectTemperatureListener() {

			@Override
			public void objectTemperature(short temperature) {
				Double temp = temperature / 10.0;
				LOG.fine("Object Temp: " + temp);
				MqttPublisher.getInstance().pubEvent(stackHost, brickletTemperatureIR, "ObjectTemp", temp);
			}
		});

		brickletTemperatureIR.addAmbientTemperatureListener(new AmbientTemperatureListener() {

			@Override
			public void ambientTemperature(short temperature) {
				Double temp = temperature / 10.0;
				LOG.fine("Ambient Temp: " + temp);
				MqttPublisher.getInstance().pubEvent(stackHost, brickletTemperatureIR, "AmbientTemp", temp);
			}
		});


	}


	public static void setUpDualButton(final BrickletDualButton brickletDualButton, String stackHost) {
		
		DualButtonDevice device = new DualButtonDevice(brickletDualButton, stackHost);
		TinkerforgeDeviceRegistry.getInstance().add(device);

		MqttPublisher.getInstance().publishDeviceState(device);
		
		brickletDualButton.addStateChangedListener(new StateChangedListener() {

			@Override
			public void stateChanged(short buttonL, short buttonR, short ledL, short ledR) {
				 String state = "btl: " + buttonL + " btr: " + buttonR + " ledL: " + ledL + " ledR: " + ledR;
				
				LOG.info(state);
//				MqttPublisher.getInstance().publishDeviceState(device);

				if (buttonL == BrickletDualButton.BUTTON_STATE_PRESSED) {
					MqttPublisher.getInstance().pubEvent(stackHost, brickletDualButton, "L", "pressed");
				}

				if (buttonR == BrickletDualButton.BUTTON_STATE_PRESSED) {
					MqttPublisher.getInstance().pubEvent(stackHost, brickletDualButton, "R", "pressed");
				}
			}
		});
	}

	public static void setUpJoystick(BrickletJoystick brickletJoystick, String stackHost) {
		TinkerforgeDeviceRegistry.getInstance().add(new JoyStickDevice(brickletJoystick));
		try {
			LOG.fine(brickletJoystick.getPosition().toString());
			brickletJoystick.setPositionCallbackPeriod(100);
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MqttPublisher.getInstance().publishJoystickState(stackHost, brickletJoystick);

		brickletJoystick.addPositionListener(new PositionListener() {

			@Override
			public void position(short x, short y) {
				LOG.fine("X:" + x + " Y: " + y);
				MqttPublisher.getInstance().pubEvent(stackHost, brickletJoystick, "X", x);
				MqttPublisher.getInstance().pubEvent(stackHost, brickletJoystick, "Y", y);
			}
		});
	}

	public static void setUpMotionDetetor(BrickletMotionDetector brickletMotionDetector, String stackHost) {

		brickletMotionDetector.addMotionDetectedListener(new MotionDetectedListener() {

			@Override
			public void motionDetected() {
				MqttPublisher.getInstance().pubEvent(stackHost, brickletMotionDetector, "MotionDetected", "yes");
			}
		});

		brickletMotionDetector.addDetectionCycleEndedListener(new DetectionCycleEndedListener() {

			@Override
			public void detectionCycleEnded() {
				MqttPublisher.getInstance().pubEvent(stackHost, brickletMotionDetector, "MotionDetected", "No");
			}
		});
	}

}

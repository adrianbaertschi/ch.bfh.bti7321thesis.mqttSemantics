package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletDualButton.StateChangedListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.MqttPublisher;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;

public class DualButtonDevice extends MqttThing<BrickletDualButton> {
	
	private int stateButtonL = -1;
	private int stateButtonR = -1;

	private Logger LOG = Logger.getLogger(this.getClass().getName());

	public DualButtonDevice(String uid, IPConnection ipcon, String stackName) {
		this.stackName = stackName;
		
		bricklet = new BrickletDualButton(uid, ipcon);
		
		bricklet.addStateChangedListener(new StateChangedListener() {

			@Override
			public void stateChanged(short buttonL, short buttonR, short ledL, short ledR) {
				 String state = "btl: " + buttonL + " btr: " + buttonR + " ledL: " + ledL + " ledR: " + ledR;
				
				LOG.info(state);
				

//				// Pressed
//				if (buttonL == BrickletDualButton.BUTTON_STATE_PRESSED) {
//					MqttPublisher.getInstance().pubEvent(stackName, getDevice(), "L", "pressed");
//				}
//
//				if (buttonR == BrickletDualButton.BUTTON_STATE_PRESSED) {
//					MqttPublisher.getInstance().pubEvent(stackName, getDevice(), "R", "pressed");
//				}
				
				// n
				if (buttonL  != stateButtonL && buttonL == BrickletDualButton.BUTTON_STATE_PRESSED) {
					MqttPublisher.getInstance().pubEvent(stackName, getDevice(), "ButtonL", "press");
				}
				if (buttonR  != stateButtonR && buttonR == BrickletDualButton.BUTTON_STATE_PRESSED) {
					MqttPublisher.getInstance().pubEvent(stackName, getDevice(), "ButtonR", "press");
				}
				

				stateButtonL = buttonL;
				stateButtonR = buttonR;
				
				MqttPublisher.getInstance().publishDeviceState(DualButtonDevice.this);
			}
		});

	}


	@Override
	public boolean handleAction(String action, byte[] payload) {
		LOG.info("Action: " + action);
		
		short state = Short.parseShort(new String(payload));

		try {
			switch (action) {
			case "setLedL":
				bricklet.setSelectedLEDState(BrickletDualButton.LED_LEFT, state);
				return true;
			case "setLedR":
				bricklet.setSelectedLEDState(BrickletDualButton.LED_RIGHT, state);
				return true;
			default:
				LOG.warning("Unexpected action");
				break;
			}
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Map<String, Object> getState() {
		Map<String, Object> stateEntries = new HashMap<String, Object>();

		try {
			stateEntries.put("ButtonL", bricklet.getButtonState().buttonL);
			stateEntries.put("ButtonR", bricklet.getButtonState().buttonR);
			stateEntries.put("LedL", bricklet.getLEDState().ledL);
			stateEntries.put("LedR", bricklet.getLEDState().ledR);
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stateEntries;
	}


	@Override
	public DeviceDescription getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

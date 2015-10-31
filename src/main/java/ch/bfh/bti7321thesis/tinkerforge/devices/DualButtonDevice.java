package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class DualButtonDevice extends MqttThing {

	private BrickletDualButton brickletDualButton;
	private Logger LOG = Logger.getLogger(this.getClass().getName());

	public DualButtonDevice(BrickletDualButton brickletDualButton, String stackName) {
		this.brickletDualButton = brickletDualButton;
		this.stackName = stackName;
	}

	@Override
	public Device getDevice() {
		return brickletDualButton;
	}

	@Override
	public boolean handleAction(String action, byte[] payload) {
		LOG.info("Action: " + action);

		short state = Short.parseShort(new String(payload));

		try {
			switch (action) {
			case "setLedL":
				brickletDualButton.setSelectedLEDState(BrickletDualButton.LED_LEFT, state);
				return true;
			case "setLedR":
				brickletDualButton.setSelectedLEDState(BrickletDualButton.LED_RIGHT, state);
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
			stateEntries.put("ButtonL", brickletDualButton.getButtonState().buttonL);
			stateEntries.put("ButtonR", brickletDualButton.getButtonState().buttonR);
			stateEntries.put("LedL", brickletDualButton.getLEDState().ledL);
			stateEntries.put("LedR", brickletDualButton.getLEDState().ledR);
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return stateEntries;
	}

}

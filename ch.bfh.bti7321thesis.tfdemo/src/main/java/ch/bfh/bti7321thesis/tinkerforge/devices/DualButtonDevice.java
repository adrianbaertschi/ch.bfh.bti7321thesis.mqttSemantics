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
import ch.bfh.bti7321thesis.tinkerforge.desc.BooleanPresetValues;
import ch.bfh.bti7321thesis.tinkerforge.desc.Command;
import ch.bfh.bti7321thesis.tinkerforge.desc.CommandDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.Event;
import ch.bfh.bti7321thesis.tinkerforge.desc.EventDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.PresetValues;
import ch.bfh.bti7321thesis.tinkerforge.desc.StateDescription;

public class DualButtonDevice extends MqttDevice<BrickletDualButton> {
	
	private int stateButtonL = -1;
	private int stateButtonR = -1;

	private Logger LOG = Logger.getLogger(this.getClass().getName());

	public DualButtonDevice(String uid, IPConnection ipcon, String stackName) {
		this.stackName = stackName;
		
		bricklet = new BrickletDualButton(uid, ipcon);
		
		try {
			bricklet.setLEDState(BrickletDualButton.LED_STATE_AUTO_TOGGLE_OFF, BrickletDualButton.LED_STATE_AUTO_TOGGLE_OFF);
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
		
		bricklet.addStateChangedListener(new StateChangedListener() {

			@Override
			public void stateChanged(short buttonL, short buttonR, short ledL, short ledR) {
				 String state = "btl: " + buttonL + " btr: " + buttonR + " ledL: " + ledL + " ledR: " + ledR;
				
				LOG.info(state);
				

				if (buttonL  != stateButtonL && buttonL == BrickletDualButton.BUTTON_STATE_PRESSED) {
					MqttPublisher.getInstance().pubEvent(DualButtonDevice.this.toThing(), "ButtonL", "pressed");
				}
				if (buttonR  != stateButtonR && buttonR == BrickletDualButton.BUTTON_STATE_PRESSED) {
					MqttPublisher.getInstance().pubEvent(DualButtonDevice.this.toThing(), "ButtonR", "pressed");
				}
				
				if (buttonL  != stateButtonL && buttonL == BrickletDualButton.BUTTON_STATE_RELEASED) {
					MqttPublisher.getInstance().pubEvent(DualButtonDevice.this.toThing(), "ButtonL", "released");
				}
				if (buttonR  != stateButtonR && buttonR == BrickletDualButton.BUTTON_STATE_RELEASED) {
					MqttPublisher.getInstance().pubEvent(DualButtonDevice.this.toThing(), "ButtonR", "released");
				}
				

				stateButtonL = buttonL;
				stateButtonR = buttonR;
				
				MqttPublisher.getInstance().publishDeviceState(DualButtonDevice.this.toThing());
			}
		});
		
		MqttPublisher.getInstance().publishDesc(this.toThing());
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
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public Map<String, Object> getState() {
		Map<String, Object> stateEntries = new HashMap<String, Object>();
		
		try {
			stateEntries.put("ButonLeftPressed", bricklet.getButtonState().buttonL == BrickletDualButton.BUTTON_STATE_PRESSED);
			stateEntries.put("ButonRightPressed", bricklet.getButtonState().buttonR == BrickletDualButton.BUTTON_STATE_PRESSED);
			stateEntries.put("LedLeft", bricklet.getLEDState().ledL == BrickletDualButton.LED_STATE_ON);
			stateEntries.put("LedRight", bricklet.getLEDState().ledR == BrickletDualButton.LED_STATE_ON);

		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

		return stateEntries;
	}
	

	@Override
	public DeviceDescription getDescription() {
		DeviceDescription deviceDescription = new DeviceDescription(bricklet.DEVICE_DISPLAY_NAME, "0.0.1");
		
		StateDescription stateDescription = new StateDescription();
		stateDescription.add("ButonLeftPressed", new BooleanPresetValues(), "Left Button presses (true) or releases (false)");
		stateDescription.add("ButonRightPressed", new BooleanPresetValues(), "Right Button presses (true) or releases (false)");
		stateDescription.add("LedLeft", new BooleanPresetValues(), "Left LED on (true) or off (false)");
		stateDescription.add("LedRight", new BooleanPresetValues(), "Right LED on (true) or off (false)");
		
		deviceDescription.setStateDescription(stateDescription);
		
		EventDescription eventDescription = new EventDescription();
		Event eventL = new Event("ButtonL", new PresetValues<String>("Pressed", "Released"), "TODO");
		Event eventR = new Event("ButtonR", new PresetValues<String>("Pressed", "Released"), "TODO");
		eventDescription.addEvent(eventL);
		eventDescription.addEvent(eventR);
		deviceDescription.setEventDescription(eventDescription);
		
		CommandDescription commandDescription = new CommandDescription();
		Command cmd1 = new Command();
		cmd1.setName("setLeds");
		cmd1.addParam("setLedL", new PresetValues<Short>((short)2, (short)3));
		cmd1.addParam("setLedR", new PresetValues<Short>((short)2, (short)3));
		
		commandDescription.addCommand(cmd1);
		deviceDescription.setCommandDescription(commandDescription);
		
		return deviceDescription;
		
	}

}

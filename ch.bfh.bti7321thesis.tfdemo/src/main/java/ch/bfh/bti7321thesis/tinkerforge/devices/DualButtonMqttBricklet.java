package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletDualButton.StateChangedListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.app.MqttPublisher;
import ch.bfh.bti7321thesis.desc.BooleanOptions;
import ch.bfh.bti7321thesis.desc.DeviceDescription;
import ch.bfh.bti7321thesis.desc.Options;
import ch.bfh.bti7321thesis.desc.cmd.Command;
import ch.bfh.bti7321thesis.desc.cmd.CommandDescription;
import ch.bfh.bti7321thesis.desc.event.Event;
import ch.bfh.bti7321thesis.desc.event.EventDescription;
import ch.bfh.bti7321thesis.desc.state.StateDescription;

public class DualButtonMqttBricklet extends MqttBricklet<BrickletDualButton> {
	
	private int stateButtonL = -1;
	private int stateButtonR = -1;

	private Logger LOG = Logger.getLogger(this.getClass().getName());

	public DualButtonMqttBricklet(String uid, IPConnection ipcon, String stackName) {
		super(uid, ipcon, stackName);
	}


	@Override
	public boolean handleCommand(String commandName, byte[] payload) {
		LOG.info("Command: " + commandName);
		
		short state = Short.parseShort(new String(payload));

		try {
			switch (commandName) {
			case "setLedL":
				bricklet.setSelectedLEDState(BrickletDualButton.LED_LEFT, state);
				return true;
			case "setLedR":
				bricklet.setSelectedLEDState(BrickletDualButton.LED_RIGHT, state);
				return true;
			default:
				LOG.warning("Unexpected command: " + commandName );
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
			stateEntries.put("ButtonLeftPressed", bricklet.getButtonState().buttonL == BrickletDualButton.BUTTON_STATE_PRESSED);
			stateEntries.put("ButtonRightPressed", bricklet.getButtonState().buttonR == BrickletDualButton.BUTTON_STATE_PRESSED);
			stateEntries.put("LedLeft", bricklet.getLEDState().ledL == BrickletDualButton.LED_STATE_ON);
			stateEntries.put("LedRight", bricklet.getLEDState().ledR == BrickletDualButton.LED_STATE_ON);

		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

		return stateEntries;
	}
	

	@Override
	public DeviceDescription getDescription() {
		DeviceDescription deviceDescription = new DeviceDescription(BrickletDualButton.DEVICE_DISPLAY_NAME, "0.0.1");
		
		StateDescription stateDescription = new StateDescription();
		stateDescription.add("ButtonLeftPressed", new BooleanOptions(), "Left Button presses (true) or releases (false)");
		stateDescription.add("ButtonRightPressed", new BooleanOptions(), "Right Button presses (true) or releases (false)");
		stateDescription.add("LedLeft", new BooleanOptions(), "Left LED on (true) or off (false)");
		stateDescription.add("LedRight", new BooleanOptions(), "Right LED on (true) or off (false)");
		
		deviceDescription.setStateDescription(stateDescription);
		
		EventDescription eventDescription = new EventDescription();
		Event eventL = new Event("ButtonL", new Options<String>("Pressed", "Released"), "TODO");
		Event eventR = new Event("ButtonR", new Options<String>("Pressed", "Released"), "TODO");
		eventDescription.addEvent(eventL);
		eventDescription.addEvent(eventR);
		deviceDescription.setEventDescription(eventDescription);
		
		CommandDescription commandDescription = new CommandDescription();
		Command cmd1 = new Command("setLedL");
		cmd1.setParam("state", new Options<Short>((short)2, (short)3));
		commandDescription.addCommand(cmd1);
		
		Command cmd2 = new Command("setLedR");
		cmd2.setParam("state", new Options<Short>((short)2, (short)3));
		commandDescription.addCommand(cmd2);
		
		
		deviceDescription.setCommandDescription(commandDescription);
		
		return deviceDescription;
		
	}


	@Override
	public void setUpBricklet() {
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
					MqttPublisher.getInstance().pubEvent(DualButtonMqttBricklet.this.toDevice(), "ButtonL", "pressed");
				}
				if (buttonR  != stateButtonR && buttonR == BrickletDualButton.BUTTON_STATE_PRESSED) {
					MqttPublisher.getInstance().pubEvent(DualButtonMqttBricklet.this.toDevice(), "ButtonR", "pressed");
				}
				
				if (buttonL  != stateButtonL && buttonL == BrickletDualButton.BUTTON_STATE_RELEASED) {
					MqttPublisher.getInstance().pubEvent(DualButtonMqttBricklet.this.toDevice(), "ButtonL", "released");
				}
				if (buttonR  != stateButtonR && buttonR == BrickletDualButton.BUTTON_STATE_RELEASED) {
					MqttPublisher.getInstance().pubEvent(DualButtonMqttBricklet.this.toDevice(), "ButtonR", "released");
				}
				

				stateButtonL = buttonL;
				stateButtonR = buttonR;
				
				MqttPublisher.getInstance().publishState(DualButtonMqttBricklet.this.toDevice());
			}
		});
		
	}

}

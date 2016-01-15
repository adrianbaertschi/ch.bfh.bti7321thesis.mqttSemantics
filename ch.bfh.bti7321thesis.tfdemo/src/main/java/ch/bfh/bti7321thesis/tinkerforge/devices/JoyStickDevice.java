package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletJoystick.PositionListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.app.MqttPublisher;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.Range;
import ch.bfh.bti7321thesis.tinkerforge.desc.cmd.Command;
import ch.bfh.bti7321thesis.tinkerforge.desc.cmd.CommandDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.event.Event;
import ch.bfh.bti7321thesis.tinkerforge.desc.event.EventDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.state.StateDescription;

public class JoyStickDevice extends MqttBricklet<BrickletJoystick> {

	private Logger LOG = Logger.getLogger(this.getClass().getName());

	public JoyStickDevice(String uid, IPConnection ipcon, String stackName) {
		super(uid, ipcon, stackName);
	}

	@Override
	public boolean handleCommand(String command, byte[] payload) {
		
		LOG.info("Command " + command);
		
		try {

			long period = Long.parseLong(new String(payload));
			
			switch (command) {
			case "setAnalogValueCallbackPeriod":
				bricklet.setAnalogValueCallbackPeriod(period);
				return true;
			case "setPositionCallbackPeriod":
				bricklet.setPositionCallbackPeriod(period);
				return true;
			default:
				LOG.warning("Unexpected command: " + command);
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
			stateEntries.put("AmbientTemperatureInterval", bricklet.getPositionCallbackPeriod());
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

		return stateEntries;
	}
	
	@Override
	public DeviceDescription getDescription() {
		DeviceDescription description = new DeviceDescription(BrickletJoystick.DEVICE_DISPLAY_NAME, "0.0.1");
		
		StateDescription stateDescription = new StateDescription();
		stateDescription.add("AmbientTemperatureInterval", new Range<Long>(0L, Long.MAX_VALUE), "Time in ms");
		description.setStateDescription(stateDescription);
		
		EventDescription eventDescription = new EventDescription();
		Event eventx = new Event("PosX", new Range<Integer>(-100, 100), "X axis");
		Event eventy = new Event("PosY", new Range<Integer>(-100, 100), "X axis");
		eventDescription.addEvent(eventx);
		eventDescription.addEvent(eventy);
		description.setEventDescription(eventDescription);
		
		CommandDescription commandDescription = new CommandDescription();
		Command command = new Command("setPositionCallbackPeriod");
		command.setParam("CallbackPeriod", new Range<Long>(0L, Long.MAX_VALUE));
		commandDescription.addCommand(command);
		description.setCommandDescription(commandDescription);
		
	
		return description;
	}

	@Override
	public void setUpDevice() {
		bricklet = new BrickletJoystick(uid, ipcon);
		
		try {
			bricklet.setPositionCallbackPeriod(100);
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bricklet.addPositionListener(new PositionListener() {

			@Override
			public void position(short x, short y) {
				LOG.fine("X:" + x + " Y: " + y);
				MqttPublisher.getInstance().pubEvent(JoyStickDevice.this.toThing(), "PosX", x);
				MqttPublisher.getInstance().pubEvent(JoyStickDevice.this.toThing(), "PosY", y);
			}
		});
		
	}

}

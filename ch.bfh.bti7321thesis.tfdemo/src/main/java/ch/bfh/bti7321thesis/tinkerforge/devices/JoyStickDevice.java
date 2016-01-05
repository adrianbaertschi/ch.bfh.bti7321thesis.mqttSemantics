package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletJoystick.PositionListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.MqttPublisher;
import ch.bfh.bti7321thesis.tinkerforge.desc.Command;
import ch.bfh.bti7321thesis.tinkerforge.desc.CommandDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.Event;
import ch.bfh.bti7321thesis.tinkerforge.desc.EventDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.Range;
import ch.bfh.bti7321thesis.tinkerforge.desc.StateDescription;

public class JoyStickDevice extends MqttDevice<BrickletJoystick> {

	private Logger LOG = Logger.getLogger(this.getClass().getName());

	public JoyStickDevice(String uid, IPConnection ipcon, String stackName) {
		this.stackName = stackName;
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
		MqttPublisher.getInstance().publishDesc(this.toThing());
		MqttPublisher.getInstance().publishDeviceState(this.toThing());
	}

	public List<String> getActions() {
		Method[] declaredMethods = BrickletJoystick.class.getDeclaredMethods();

		Stream<String> filter = Arrays.asList(declaredMethods).stream().filter(m -> m.getName().startsWith("set"))
				.map(action -> action.getName());

		return filter.collect(Collectors.toList());
	}
	

	@Override
	public boolean handleAction(String action, byte[] payload) {
		
		LOG.info("Action " + action);
		
		try {

			long period = Long.parseLong(new String(payload));
			
			switch (action) {
			case "setAnalogValueCallbackPeriod":
				bricklet.setAnalogValueCallbackPeriod(period);
				return true;
			case "setPositionCallbackPeriod":
				bricklet.setPositionCallbackPeriod(period);
				return true;
			default:
				throw new IllegalArgumentException("Action " + action + " not supported");
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
		DeviceDescription description = new DeviceDescription(bricklet.DEVICE_DISPLAY_NAME, "0.0.1");
		
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
		Command command = new Command();
		command.setName("setPositionCallbackPeriod");
		command.setParam("CallbackPeriod", new Range<Long>(0L, Long.MAX_VALUE));
		commandDescription.addCommand(command);
		description.setCommandDescription(commandDescription);
		
	
		return description;
	}

}

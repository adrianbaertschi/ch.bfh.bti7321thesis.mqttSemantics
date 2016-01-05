package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.BrickletTemperatureIR.AmbientTemperatureListener;
import com.tinkerforge.BrickletTemperatureIR.ObjectTemperatureListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.MqttPublisher;
import ch.bfh.bti7321thesis.tinkerforge.desc.BooleanPresetValues;
import ch.bfh.bti7321thesis.tinkerforge.desc.Command;
import ch.bfh.bti7321thesis.tinkerforge.desc.CommandDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.ComplexType;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.Event;
import ch.bfh.bti7321thesis.tinkerforge.desc.EventDescription;
import ch.bfh.bti7321thesis.tinkerforge.desc.PresetValues;
import ch.bfh.bti7321thesis.tinkerforge.desc.Range;
import ch.bfh.bti7321thesis.tinkerforge.desc.StateDescription;

public class TempIrDevice extends MqttDevice<BrickletTemperatureIR> {

	private Logger LOG = Logger.getLogger(this.getClass().getName());


	public TempIrDevice(String uid, IPConnection ipcon, String stackName) {
		super.stackName = stackName;
		
		bricklet = new BrickletTemperatureIR(uid, ipcon);
		
		// setup
		try {
			bricklet.setObjectTemperatureCallbackPeriod(1000);
			bricklet.setAmbientTemperatureCallbackPeriod(1000);
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		bricklet.addObjectTemperatureListener(new ObjectTemperatureListener() {

			@Override
			public void objectTemperature(short temperature) {
				Double temp = temperature / 10.0;
				LOG.fine("Object Temp: " + temp);
				MqttPublisher.getInstance().pubEvent(TempIrDevice.this.toThing(), "ObjectTemp", temp);
			}
		});

		bricklet.addAmbientTemperatureListener(new AmbientTemperatureListener() {

			@Override
			public void ambientTemperature(short temperature) {
				Double temp = temperature / 10.0;
				LOG.fine("Ambient Temp: " + temp);
				MqttPublisher.getInstance().pubEvent(TempIrDevice.this.toThing(), "AmbientTemp", temp);
			}
		});
		
		MqttPublisher.getInstance().publishDeviceState(this.toThing());
		MqttPublisher.getInstance().publishDesc(this.toThing());
	}
	
	
	@Override
	public boolean handleAction(String action, byte[] payload) {

		LOG.info("Action: " + action);
		Long value = null;
		try {
			value = Long.parseLong(new String(payload));
		} catch (NumberFormatException nfe) {
			LOG.log(Level.SEVERE, nfe.getMessage(), nfe);
			return false;
		}

		try {
			switch (action) {
			case "setAmbientTemperatureCallbackPeriod":
				bricklet.setAmbientTemperatureCallbackPeriod(value);
				LOG.info("setAmbientTemperatureCallbackPeriod to " + value);
				return true;
			case "setObjectTemperatureCallbackPeriod":
				bricklet.setObjectTemperatureCallbackPeriod(value);
				LOG.info("setObjectTemperatureCallbackPeriod to " + value);
				return true;
			case "setEmissivity":
				bricklet.setEmissivity(value.intValue());
				LOG.info("Emissivity set to " + value);
				break;
			default:
				LOG.warning("Unexpected action");
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
			stateEntries.put("AmbientTemperatureInterval", bricklet.getAmbientTemperatureCallbackPeriod());
			stateEntries.put("ObjectTemperatureInterval", bricklet.getObjectTemperatureCallbackPeriod());
			stateEntries.put("DebouncePeriod", bricklet.getDebouncePeriod());
			stateEntries.put("Emissivity", bricklet.getEmissivity());
			stateEntries.put("AmbientTemperatureCallbackThreshold", bricklet.getAmbientTemperatureCallbackThreshold());
			stateEntries.put("ObjectTemperatureCallbackThreshold", bricklet.getObjectTemperatureCallbackThreshold());

		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

		return stateEntries;
	}

	@Override
	public DeviceDescription getDescription() {
		
		// TODO: timestamp Last_onl
		
		DeviceDescription description = new DeviceDescription(bricklet.DEVICE_DISPLAY_NAME, "0.0.1");
		
		// State
		StateDescription stateDescription = new StateDescription();

		stateDescription.add("AmbientTemperatureInterval", new Range<Long>(0L, Long.MAX_VALUE), "Time in ms");
		stateDescription.add("ObjectTemperatureInterval", new Range<Long>(0L, Long.MAX_VALUE), "TODO");
		stateDescription.add("DebouncePeriod", new Range<Long>(0L, Long.MAX_VALUE), "TODO");
		stateDescription.add("Emissivity", new Range<Integer>(6553, 65535), "TODO");
		
		ComplexType callbackThreshold = new ComplexType("TemperatureCallbackThreshold");
		callbackThreshold.addStringProperty("option");
		callbackThreshold.addNumberProperty("min", Short.class);
		callbackThreshold.addNumberProperty("max", Short.class);
		
		stateDescription.add("AmbientTemperatureCallbackThreshold", callbackThreshold, "TODO"); // TODO
		stateDescription.add("ObjectTemperatureCallbackThreshold",  callbackThreshold, "TODO"); // TODO
		description.setStateDescription(stateDescription);

		// Events
		EventDescription eventDescription = new EventDescription();
		Event event1 = new Event("ObjectTemp", new Range<Double>(-70.0, 380.0), "Measured with IR sensor in Celsius");
		eventDescription.addEvent(event1);

		Event event2 = new Event("AmbientTemp", new Range<Double>(-40.0, 125.0), "Ambient temperature in Celsius");
		eventDescription.addEvent(event2);
		
		description.setEventDescription(eventDescription);
		
		// Commands
		CommandDescription commandDescription = new CommandDescription();
		Command cmd1 = new Command();
		cmd1.setName("setAmbientTemperatureCallbackPeriod");
		cmd1.setLinkedState("AmbientTemperatureCallbackPeriod");
		cmd1.addParam("CallbackPeriod", new Range<Long>(0L, Long.MAX_VALUE));
		commandDescription.addCommand(cmd1);
		
		Command cmd2 = new Command();
		cmd2.setName("setObjectTemperatureCallbackPeriod");
		cmd2.setLinkedState("ObjectTemperatureCallbackPeriod");
		cmd2.addParam("CallbackPeriod", new Range<Long>(0L, Long.MAX_VALUE));
		commandDescription.addCommand(cmd2);
		
		Command cmd3 = new Command();
		cmd3.setName("setEmissivity");
		cmd3.setLinkedState("Emissivity");
		cmd3.addParam("Emissity", new Range<Integer>(6553, 65535));
		commandDescription.addCommand(cmd3);
		
		Command cmd4 = new Command();
		cmd4.setName("EnableAmbientTemperature");
		cmd4.addParam("enabled", new BooleanPresetValues());
		cmd4.addParam("Reallyenabled", new PresetValues<Boolean>(true, false));
		commandDescription.addCommand(cmd4);
		
		// TODO: ENUM Command
		
		
		description.setCommandDescription(commandDescription);
		
		return description;
	}


}

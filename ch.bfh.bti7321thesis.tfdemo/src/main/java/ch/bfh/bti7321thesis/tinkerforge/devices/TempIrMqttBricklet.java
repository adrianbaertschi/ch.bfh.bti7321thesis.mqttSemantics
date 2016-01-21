package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.BrickletTemperatureIR.AmbientTemperatureListener;
import com.tinkerforge.BrickletTemperatureIR.ObjectTemperatureListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.app.MqttPublisher;
import ch.bfh.bti7321thesis.desc.ComplexType;
import ch.bfh.bti7321thesis.desc.DeviceDescription;
import ch.bfh.bti7321thesis.desc.Range;
import ch.bfh.bti7321thesis.desc.cmd.Command;
import ch.bfh.bti7321thesis.desc.cmd.CommandDescription;
import ch.bfh.bti7321thesis.desc.event.Event;
import ch.bfh.bti7321thesis.desc.event.EventDescription;
import ch.bfh.bti7321thesis.desc.state.StateDescription;

public class TempIrMqttBricklet extends MqttBricklet<BrickletTemperatureIR> {

	private Logger LOG = Logger.getLogger(this.getClass().getName());

//	ObjectMapper mapper = new ObjectMapper(); // create once, reuse
	private ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

	
	public TempIrMqttBricklet(String uid, IPConnection ipcon, String stackName) {
		super(uid, ipcon, stackName);

	}
	
	
	@Override
	public boolean handleCommand(String commandName, byte[] payload) {
		
		LOG.info("Command: " + commandName);
//		Long value = null;
//		try {
//			value = Long.parseLong(new String(payload));
//		} catch (NumberFormatException nfe) {
//			LOG.log(Level.SEVERE, nfe.getMessage(), nfe);
////			return false;
//		}
		Long value = 0L;

		try {
			switch (commandName) {
			case "setAmbientTemperatureCallbackPeriod":
				value = yamlMapper.readValue(new String(payload), Long.class);
				bricklet.setAmbientTemperatureCallbackPeriod(value);
				LOG.info("setAmbientTemperatureCallbackPeriod to " + value);
				return true;
				
			case "setObjectTemperatureCallbackPeriod":
				value = yamlMapper.readValue(new String(payload), Long.class);
				bricklet.setObjectTemperatureCallbackPeriod(value);
				LOG.info("setObjectTemperatureCallbackPeriod to " + value);
				return true;
				
			case "setEmissivity":
				value = yamlMapper.readValue(new String(payload), Long.class);
				bricklet.setEmissivity(value.intValue());
				LOG.info("Emissivity set to " + value);
				return true;
				
			case"setAmbientTemperatureCallbackThreshold":
				TemperatureCallbackThreshold threshold = yamlMapper.readValue(new String(payload), TemperatureCallbackThreshold.class);
				bricklet.setAmbientTemperatureCallbackThreshold(threshold.getOption(), threshold.getMin(), threshold.getMax());
				return true;
			default:
				LOG.warning("Unexpected command:" + commandName);
				return false;
			}
		} catch (TimeoutException | NotConnectedException | IOException e) {
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
		
//		ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
//		DeviceDescription desc = null;
//		try {
//			URL url = this.getClass().getResource("/TempIr.yaml");
//			
//			desc = yamlMapper.readValue(url, DeviceDescription.class);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return desc;
		
	
		DeviceDescription description = new DeviceDescription("IoT - " + BrickletTemperatureIR.DEVICE_DISPLAY_NAME, "0.0.1");
		
		// State
		StateDescription stateDescription = new StateDescription();
		
		stateDescription.add("AmbientTemperatureInterval", new Range<Long>(0L, Long.MAX_VALUE), "Time in ms");
		stateDescription.add("ObjectTemperatureInterval", new Range<Long>(0L, Long.MAX_VALUE), "Interval of the object measurements");
		stateDescription.add("DebouncePeriod", new Range<Long>(0L, Long.MAX_VALUE), "Debounce Period");
		stateDescription.add("Emissivity", new Range<Integer>(6553, 65535), "Emissivity");
		
		ComplexType tempThreshold = new ComplexType("TemperatureCallbackThreshold");
		
		String optionDesc = "'x' 	Listener is turned off \n "
				+ "o' 	Listener is triggered when the ambient temperature is outside the min and max values \n"
				+ "'i' 	Listener is triggered when the ambient temperature is inside the min and max values \n"
				+ "'<' 	Listener is triggered when the ambient temperature is smaller than the min value (max is ignored) \n"
				+ "'>' 	Listener is triggered when the ambient temperature is greater than the min value (max is ignored)";
		
		tempThreshold.addStringProperty("option", optionDesc);
		tempThreshold.addNumberProperty("min", Short.class, "Minimal Value");
		tempThreshold.addNumberProperty("max", Short.class, "Maximal Value");
		
		stateDescription.add("AmbientTemperatureCallbackThreshold", tempThreshold, "Theshold object for the ambient temperature");
		stateDescription.add("ObjectTemperatureCallbackThreshold",  tempThreshold, "Theshold object for the object temperature");
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
		Command cmd1 = new Command("setAmbientTemperatureCallbackPeriod");
		cmd1.setLinkedState("AmbientTemperatureCallbackPeriod");
		cmd1.setParam("CallbackPeriod", new Range<Long>(0L, Long.MAX_VALUE));
		commandDescription.addCommand(cmd1);
		
		Command cmd2 = new Command("setObjectTemperatureCallbackPeriod");
		cmd2.setLinkedState("ObjectTemperatureCallbackPeriod");
		cmd2.setParam("CallbackPeriod", new Range<Long>(0L, Long.MAX_VALUE));
		commandDescription.addCommand(cmd2);
		
		Command cmd3 = new Command("setEmissivity");
		cmd3.setLinkedState("Emissivity");
		cmd3.setParam("Emissity", new Range<Integer>(6553, 65535));
		commandDescription.addCommand(cmd3);
		
		Command cmd4 = new Command("setAmbientTemperatureCallbackThreshold");
		cmd4.setParam("Threshold", tempThreshold);
		commandDescription.addCommand(cmd4);
		
		description.setCommandDescription(commandDescription);
		
		return description;
	}


	@Override
	public void setUpBricklet() {
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
				MqttPublisher.getInstance().pubEvent(TempIrMqttBricklet.this.toDevice(), "ObjectTemp", temp);
			}
		});

		bricklet.addAmbientTemperatureListener(new AmbientTemperatureListener() {

			@Override
			public void ambientTemperature(short temperature) {
				Double temp = temperature / 10.0;
				LOG.fine("Ambient Temp: " + temp);
				MqttPublisher.getInstance().pubEvent(TempIrMqttBricklet.this.toDevice(), "AmbientTemp", temp);
			}
		});
	}


}

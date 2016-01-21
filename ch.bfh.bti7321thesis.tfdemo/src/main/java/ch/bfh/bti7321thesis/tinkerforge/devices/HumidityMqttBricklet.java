package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletHumidity.HumidityListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.app.MqttPublisher;
import ch.bfh.bti7321thesis.desc.DeviceDescription;
import ch.bfh.bti7321thesis.desc.Range;
import ch.bfh.bti7321thesis.desc.cmd.Command;
import ch.bfh.bti7321thesis.desc.cmd.CommandDescription;
import ch.bfh.bti7321thesis.desc.event.Event;
import ch.bfh.bti7321thesis.desc.event.EventDescription;
import ch.bfh.bti7321thesis.desc.state.StateDescription;

public class HumidityMqttBricklet extends MqttBricklet<BrickletHumidity> {

	public HumidityMqttBricklet(String uid, IPConnection ipcon, String stackName) {
		super(uid, ipcon, stackName);
	}
	
	@Override
	public void setUpBricklet() {
		bricklet = new BrickletHumidity(uid, ipcon);

		try {
			bricklet.setHumidityCallbackPeriod(5000);
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

		bricklet.addHumidityListener(new HumidityListener() {

			@Override
			public void humidity(int humidity) {
				MqttPublisher.getInstance().pubEvent(HumidityMqttBricklet.this.toDevice(), "Humidity", humidity / 10.0);
			}
		});
	}

	@Override
	public boolean handleCommand(String commandName, byte[] payload) {
		
		if("SetInterval".equals(commandName)) {
			
			Long value = Long.parseLong(new String(payload));
			try {
				bricklet.setHumidityCallbackPeriod(value);
			} catch (TimeoutException | NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public Map<String, Object> getState() {
		Map<String, Object> state = new HashMap<String, Object>();
		try {
			state.put("HumidityInterval", bricklet.getHumidityCallbackPeriod());
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}
		
		return state;
	}

	@Override
	public DeviceDescription getDescription() {
		
//		return readFromFile();
		
		DeviceDescription deviceDescription = new DeviceDescription("IoT-" + BrickletHumidity.DEVICE_DISPLAY_NAME, "0.0.1");
		deviceDescription.setDescription("The Humidity Bricklet can be used to measure relative humidity. The measured humidity can be read out directly in percent, no conversions are necessary, with configurable interval");
		
		StateDescription state = new StateDescription();
		state.add("HumidityInterval", new Range<Long>(0L, Long.MAX_VALUE), "Interval of the measurements in ms.");
		deviceDescription.setStateDescription(state);
		
		EventDescription eventDescription = new EventDescription();
		Event event = new Event("Humidity", new Range<Double>(0.0, 100.0), "Relative Humidity in percent");
		eventDescription.addEvent(event);
		deviceDescription.setEventDescription(eventDescription);

		CommandDescription commandDescription = new CommandDescription();
		Command command = new Command("SetInterval");
		command.setDescription("Set the measurement interval of the sensor, 0 disables the measurements");
		command.setLinkedState("HumidityInterval");
		command.setParam("Interval", new Range<Long>(0L, Long.MAX_VALUE));
		commandDescription.addCommand(command);
		deviceDescription.setCommandDescription(commandDescription);

		return deviceDescription;

	}
	
	private DeviceDescription readFromFile() {
		ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
		DeviceDescription desc = null;
		try {
			URL url = this.getClass().getResource("/humidity.yaml");
			
			desc = yamlMapper.readValue(url, DeviceDescription.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return desc;
	}



}

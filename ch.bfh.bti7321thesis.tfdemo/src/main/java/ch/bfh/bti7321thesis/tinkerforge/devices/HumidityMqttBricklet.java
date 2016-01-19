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
		return false;
	}

	@Override
	public Map<String, Object> getState() {
		return new HashMap<String, Object>();
	}

	@Override
	public DeviceDescription getDescription() {
		
//		return readFromFile();
		
		DeviceDescription deviceDescription = new DeviceDescription("IoT-" + BrickletHumidity.DEVICE_DISPLAY_NAME, "0.0.1");
		deviceDescription.setStateDescription(new StateDescription());
		
		EventDescription eventDescription = new EventDescription();
		Event event = new Event("Humidity", new Range<Double>(0.0, 100.0), "Relative Humidity in percent");
		eventDescription.addEvent(event);
		deviceDescription.setEventDescription(eventDescription);

		CommandDescription commandDescription = new CommandDescription();
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

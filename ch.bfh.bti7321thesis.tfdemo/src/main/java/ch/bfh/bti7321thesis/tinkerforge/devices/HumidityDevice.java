package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.io.File;
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

import ch.bfh.bti7321thesis.tinkerforge.MqttPublisher;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;

public class HumidityDevice extends MqttDevice<BrickletHumidity> {

	public HumidityDevice(String uid, IPConnection ipcon, String stackName) {
		super(uid, ipcon, stackName);
	}
	
	@Override
	public void setUpDevice() {
		bricklet = new BrickletHumidity(uid, ipcon);

		try {
			bricklet.setHumidityCallbackPeriod(5000);
		} catch (TimeoutException | NotConnectedException e) {
			e.printStackTrace();
		}

		bricklet.addHumidityListener(new HumidityListener() {

			@Override
			public void humidity(int humidity) {
				MqttPublisher.getInstance().pubEvent(HumidityDevice.this.toThing(), "Humidity", humidity / 10.0);
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
		
		ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
		DeviceDescription desc = null;
		try {
			URL url = this.getClass().getResource("asdf.yaml");
//			yamlMapper.setSerializationInclusion(Include.NON_NULL);
			desc = yamlMapper.readValue(new File("/home/adrian/workspace_tinker/ch.bfh.bti7321thesis/ch.bfh.bti7321thesis.tfdemo/src/main/java/ch/bfh/bti7321thesis/tinkerforge/devices/humidity.yaml"), DeviceDescription.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return desc;
		
//		DeviceDescription deviceDescription = new DeviceDescription("IoT-" + BrickletHumidity.DEVICE_DISPLAY_NAME,
//				"0.0.1");
//		deviceDescription.setStateDescription(new StateDescription());
//		
//		EventDescription eventDescription = new EventDescription();
//		Event event = new Event("Humidity", new Range<Double>(0.0, 100.0), "Relative Humidity in percent");
//		eventDescription.addEvent(event);
//		deviceDescription.setEventDescription(eventDescription);
//
//		CommandDescription commandDescription = new CommandDescription();
//		deviceDescription.setCommandDescription(commandDescription);
//
//		return deviceDescription;

	}



}

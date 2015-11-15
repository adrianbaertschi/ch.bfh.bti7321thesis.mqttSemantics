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

public class TempIrDevice extends MqttThing<BrickletTemperatureIR> {

	private Logger LOG = Logger.getLogger(this.getClass().getName());


	public TempIrDevice(String uid, IPConnection ipcon, String stackName) {
//		this.brickletTemperatureIR = brickletTemperatureIR;
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
				MqttPublisher.getInstance().pubEvent(stackName, bricklet, "ObjectTemp", temp);
			}
		});

		bricklet.addAmbientTemperatureListener(new AmbientTemperatureListener() {

			@Override
			public void ambientTemperature(short temperature) {
				Double temp = temperature / 10.0;
				LOG.fine("Ambient Temp: " + temp);
				MqttPublisher.getInstance().pubEvent(stackName, bricklet, "AmbientTemp", temp);
			}
		});
		
		MqttPublisher.getInstance().publishDeviceState(this);
	}



	@Override
	public boolean handleAction(String action, byte[] payload) {

		LOG.info("Action: " + action);
		Long period = null;
		try {
			period = Long.parseLong(new String(payload));
		} catch (NumberFormatException nfe) {
			LOG.log(Level.SEVERE, nfe.getMessage(), nfe);
			return false;
		}

		try {
			switch (action) {
			case "setAmbientTemperatureCallbackPeriod":
				bricklet.setAmbientTemperatureCallbackPeriod(period);
				LOG.info("setAmbientTemperatureCallbackPeriod to " + period);
				return true;
			case "setObjectTemperatureCallbackPeriod":
				bricklet.setObjectTemperatureCallbackPeriod(period);
				LOG.info("setObjectTemperatureCallbackPeriod to " + period);
				return true;
			default:
				LOG.warning("Unexpected action");
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
		// TODO: better exception handling, only skip those who fail
		try {
			stateEntries.put("AmbientTemperatureCallbackPeriod", Long.toString(bricklet.getAmbientTemperatureCallbackPeriod()));
			stateEntries.put("ObjectTemperatureCallbackPeriod", Long.toString(bricklet.getObjectTemperatureCallbackPeriod()));
			stateEntries.put("DebouncePeriod", Long.toString(bricklet.getDebouncePeriod()));
			stateEntries.put("Emissivity", Long.toString(bricklet.getEmissivity()));
			stateEntries.put("AmbientTemperatureCallbackThreshold", bricklet.getAmbientTemperatureCallbackThreshold().toString());
			stateEntries.put("ObjectTemperatureCallbackThreshold", bricklet.getObjectTemperatureCallbackThreshold().toString());
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info(stateEntries.size() + " states");
		
		return stateEntries;
	}


}

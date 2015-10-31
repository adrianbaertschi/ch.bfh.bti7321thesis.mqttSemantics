package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class TempIrDevice extends MqttThing {

	private Logger LOG = Logger.getLogger(this.getClass().getName());
	private BrickletTemperatureIR brickletTemperatureIR;


	public TempIrDevice(BrickletTemperatureIR brickletTemperatureIR, String stackName) {
		this.brickletTemperatureIR = brickletTemperatureIR;
		super.stackName = stackName;
	}

	@Override
	public Device getDevice() {
		return brickletTemperatureIR;
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
				brickletTemperatureIR.setAmbientTemperatureCallbackPeriod(period);
				LOG.info("setAmbientTemperatureCallbackPeriod to " + period);
				return true;
			case "setObjectTemperatureCallbackPeriod":
				brickletTemperatureIR.setObjectTemperatureCallbackPeriod(period);
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
			stateEntries.put("AmbientTemperatureCallbackPeriod", Long.toString(brickletTemperatureIR.getAmbientTemperatureCallbackPeriod()));
			stateEntries.put("ObjectTemperatureCallbackPeriod", Long.toString(brickletTemperatureIR.getObjectTemperatureCallbackPeriod()));
			stateEntries.put("DebouncePeriod", Long.toString(brickletTemperatureIR.getDebouncePeriod()));
			stateEntries.put("Emissivity", Long.toString(brickletTemperatureIR.getEmissivity()));
			stateEntries.put("AmbientTemperatureCallbackThreshold", brickletTemperatureIR.getAmbientTemperatureCallbackThreshold().toString());
			stateEntries.put("ObjectTemperatureCallbackThreshold", brickletTemperatureIR.getObjectTemperatureCallbackThreshold().toString());
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LOG.info(stateEntries.size() + " states");
		
		return stateEntries;
	}


}

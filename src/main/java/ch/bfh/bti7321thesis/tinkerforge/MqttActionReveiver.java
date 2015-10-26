package ch.bfh.bti7321thesis.tinkerforge;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.Device;

public class MqttActionReveiver implements MqttCallback {

	// TODO: restructure for TempIR only

	private Logger LOG = Logger.getLogger(MqttActionReveiver.class.getName());

	@Override
	public void connectionLost(Throwable arg0) {
		// TODO Auto-generated method stub
		LOG.log(Level.SEVERE, "MqttActionReveiver connectionLost", arg0);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		LOG.fine("MSG ID: " + arg0.getMessageId());
	}

	@Override
	public void messageArrived(String topicString, MqttMessage message) throws Exception {
		
		LOG.info("Topic: " + topicString + " - Msg payload: " + new String(message.getPayload()));
		
		if(message.getPayload() == null || message.getPayload().length == 0) {
			return;
		}

		MqttTopic topic = new MqttTopic(topicString);

		Device device = TinkerforgeDeviceRegistry.getInstance().find(topic);
		
		if(device == null) {
			LOG.severe("No Device found");
			return;
		}
		String action = topic.getLast();
		String stackName = topic.getTopic(2);
		
		if(device instanceof BrickletTemperatureIR) {
			BrickletTemperatureIR brickletTemperatureIR = (BrickletTemperatureIR) device;
			LOG.info("Action: " + action);
			switch (action) {
			case "setAmbientTemperatureCallbackPeriod":
				Long period = null;
				try {
					period = Long.parseLong(new String(message.getPayload()));
				} catch (NumberFormatException nfe) {
					LOG.log(Level.SEVERE, nfe.getMessage(), nfe);
					return;
				}
				brickletTemperatureIR.setAmbientTemperatureCallbackPeriod(period);
				LOG.info("setAmbientTemperatureCallbackPeriod to " + period);
				break;
			case "setObjectTemperatureCallbackPeriod":
				try {
					period = Long.parseLong(new String(message.getPayload()));
				} catch (NumberFormatException nfe) {
					LOG.log(Level.SEVERE, nfe.getMessage(), nfe);
					return;
				}
				brickletTemperatureIR.setObjectTemperatureCallbackPeriod(period);
				LOG.info("setObjectTemperatureCallbackPeriod to " + period);
				break;
			default:
				LOG.warning("Unexpected action");
			}
			MqttPublisher.getInstance().publishTempIrState(stackName, brickletTemperatureIR);
		} else {
			LOG.warning("Unhandled Device Type");
		}

	}

}

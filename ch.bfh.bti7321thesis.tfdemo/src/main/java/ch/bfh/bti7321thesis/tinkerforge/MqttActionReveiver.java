package ch.bfh.bti7321thesis.tinkerforge;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import ch.bfh.bti7321thesis.tinkerforge.devices.MqttDevice;

// TODO: rename to Command...
public class MqttActionReveiver implements MqttCallback {
	
	private Logger LOG = Logger.getLogger(this.getClass().getName());

	@Override
	public void connectionLost(Throwable arg0) {
		LOG.log(Level.SEVERE, "MqttActionReveiver connectionLost", arg0);
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		LOG.fine("MSG ID: " + arg0.getMessageId());
	}

	@Override
	public void messageArrived(String topicString, MqttMessage message) throws Exception {

		LOG.info("Topic: " + topicString + " - Msg payload: " + new String(message.getPayload()));

		if (message.getPayload() == null || message.getPayload().length == 0) {
			return;
		}

		MqttTopic topic = new MqttTopic(topicString);

		MqttDevice<?> mqttDevice = TinkerforgeDeviceRegistry.getInstance().find(topic);
		LOG.info(mqttDevice.toString());
		
		String action = topic.getLast();
		LOG.info(action);
//		
		if(mqttDevice.handleAction(action, message.getPayload())) {
//			// pub state
//			MqttPublisher.getInstance().publishDeviceState(mqttDevice.toThing());
		} else {
			// TODO: error?
			LOG.severe("unhandled action");
		}

	}

}

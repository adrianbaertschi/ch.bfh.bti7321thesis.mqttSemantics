package ch.bfh.bti7321thesis.tinkerforge;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import ch.bfh.bti7321thesis.app.MqttPublisher;
import ch.bfh.bti7321thesis.app.MqttTopic;
import ch.bfh.bti7321thesis.tinkerforge.devices.MqttBricklet;

public class MqttCommandReceiver implements MqttCallback {
	
	private Logger LOG = Logger.getLogger(this.getClass().getName());

	@Override
	public void connectionLost(Throwable arg0) {
		LOG.log(Level.SEVERE, "MqttCommandReceiver connectionLost", arg0);
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

		MqttBricklet<?> mqttDevice = TinkerforgeDeviceRegistry.getInstance().find(topic);
		LOG.info(mqttDevice.toString());
		
		String commandName = topic.getLast();
		LOG.info(commandName);

		if(mqttDevice.handleCommand(commandName, message.getPayload())) {
			// Re-publish State
			MqttPublisher.getInstance().publishState(mqttDevice.toDevice());
		}
	}

}
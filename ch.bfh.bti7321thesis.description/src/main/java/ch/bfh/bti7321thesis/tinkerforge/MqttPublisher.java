package ch.bfh.bti7321thesis.tinkerforge;

import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import ch.bfh.bti7321thesis.tinkerforge.Options.SchemaFormat;
import ch.bfh.bti7321thesis.tinkerforge.devices.MqttThing;

public class MqttPublisher {
	
	// TODO: Subscribe in own class / connection
	private Logger LOG = Logger.getLogger(this.getClass().getName());
	
	
	private IMqttAsyncClient mqttClient;
	
	private static Options options;
	
	private ObjectMapper objectmapper;

	private boolean addRandomtoEvents = false;
	

	private MqttPublisher() {
		validateSetting();
		setUpMqtt();
		setUpJackson();
	}
	
    private void validateSetting() {
		if(options.getMqttCallback() == null) {
			throw new IllegalStateException("MQTT callback handler is not defined, set with setCallback(...)");
		}
		
		if(options.getAppId() == null) {
			throw new IllegalStateException("AppId not set");
		}
		if(options.getMqttBrokerUri() == null) {
			throw new IllegalStateException("Broker URL not set");
		}
		if(options.getMqttClientId()== null) {
			throw new IllegalStateException("MQTT ClientId not set");
		}
		
		
	}

	private static class Holder {
    	private static MqttPublisher instance = new MqttPublisher();
    }
	
	public static MqttPublisher getInstance() {
		return Holder.instance;
	}
	
	public static void setOptions(Options options) {
		MqttPublisher.options = options;
		
	}
	
	private void setUpMqtt() {
		LOG.info("Connecting to broker: " + options.getMqttBrokerUri());
		try {
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			
			MemoryPersistence persistence = new MemoryPersistence();
			mqttClient = new MqttAsyncClient(options.getMqttBrokerUri(), options.getMqttClientId(), persistence);
			mqttClient.connect(connOpts).waitForCompletion();
			
			mqttClient.setCallback(options.getMqttCallback());
			mqttClient.subscribe(options.getAppId() +"/+/+/+/+/commands/#", 2);
		} catch (MqttException e) {
			e.printStackTrace();
		}
		LOG.info("MQTT Connected");
	}
	
	private void setUpJackson() {
		if(options.getSchemaFormat() == SchemaFormat.YAML) {
			objectmapper = new ObjectMapper(new YAMLFactory());
			
			// Attributes with null values should be skipped
			objectmapper.setSerializationInclusion(Include.NON_NULL);
			
		} else if(options.getSchemaFormat() == SchemaFormat.JSON) {
			objectmapper = new ObjectMapper();
			
			// Attributes with null values should be skipped
			objectmapper.setSerializationInclusion(Include.NON_NULL);
		}
	}
	
	public void disconnect() {
		LOG.info("Disconnecting MQTT");
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		LOG.info("MQTT disconnected");
	}
	
	public void pubEvent(MqttThing thing, String eventName, Object payload) {

		String baseTopic = new BrickletToMqttConverter().getBaseTopic(options.getAppId(), thing) + "/events/" + eventName;
		String payloadStr = payload.toString();
		if (addRandomtoEvents) {
			payloadStr += " " + Math.random();
		}

		pubEvent(baseTopic, payloadStr);
	}

	
	public void publishDesc(MqttThing thing) {
		String desc = "";
		try {
			desc = objectmapper.writerWithDefaultPrettyPrinter().writeValueAsString(thing.getDeviceDescription());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String baseTopic = new BrickletToMqttConverter().getBaseTopic(options.getAppId(), thing);
		pubRetained(baseTopic + "/schema/"+ options.getSchemaFormat(), desc);
		
	}
	
	// TODO: rename
	public void publishDeviceState(MqttThing thing) {
		String baseTopic = new BrickletToMqttConverter().getBaseTopic(options.getAppId(), thing) + "/state";
		for(Entry<String, Object> state : thing.getState().entrySet()) {
			LOG.info(state.getKey());
			pubState(baseTopic + "/" + state.getKey(), state.getValue());
		}
	}
	
	// TODO: refactor the 3 private pub... Methods to one
	
	private void pubEvent(String topic, String payload) {
		
		LOG.info("Publishing on " + topic + " data: " + payload);
		try {
			MqttMessage message = new MqttMessage(payload == null ? "".getBytes() : payload.getBytes());
			message.setQos(0);
			mqttClient.publish(topic, message);
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}


	private void pubState(String topic, Object payload) {
		
		try {
			String state = objectmapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
			LOG.info("Publishing on " + topic + " data: " + state);
			
			MqttMessage message = new MqttMessage(state.getBytes());
			message.setRetained(true);
			
			IMqttDeliveryToken imMqttDeliveryToken = mqttClient.publish(topic, message);
			imMqttDeliveryToken.waitForCompletion();
		} catch (MqttException | JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
	private void pubRetained(String topic, Object payload) {
		LOG.info("Publishing Retained on " + topic + " data: " + payload);
		try {
			MqttMessage message = new MqttMessage(payload == null ? "".getBytes() : payload.toString().getBytes());
			message.setRetained(true);
			mqttClient.publish(topic, message);
		} catch (MqttException e) {
			e.printStackTrace();
		}		
	}

}

package ch.bfh.bti7321thesis.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
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

import ch.bfh.bti7321thesis.app.Options.SchemaFormat;

public class MqttPublisher {
	
	private Logger LOG = Logger.getLogger(this.getClass().getName());
	
	
	private IMqttAsyncClient mqttClientPub;
	private IMqttAsyncClient mqttClientSub;
	
	private static Options options;
	
	private Map<String, ObjectMapper> objectmappers = new HashMap<String, ObjectMapper>();

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
		if(options.getMqttClientId() == null) {
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
			// PUB
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			
			MemoryPersistence persistence = new MemoryPersistence();
			mqttClientPub = new MqttAsyncClient(options.getMqttBrokerUri(), options.getMqttClientId() + "_pub", persistence);
			mqttClientPub.connect(connOpts).waitForCompletion();
			
			
			// SUB
			MemoryPersistence subPersistence = new MemoryPersistence();
			mqttClientSub = new MqttAsyncClient(options.getMqttBrokerUri(), options.getMqttClientId() + "_sub", subPersistence);
			mqttClientSub.connect(connOpts).waitForCompletion();
			mqttClientSub.setCallback(options.getMqttCallback());
			mqttClientSub.subscribe(options.getAppId() +"/+/+/+/+/commands/#", 2);
			
		} catch (MqttException e) {
			LOG.log(Level.SEVERE, "", e);
		}
		LOG.info("MQTT Connected");
	}
	
	private void setUpJackson() {
		if(options.getSchemaFormat() == SchemaFormat.YAML || options.getSchemaFormat() == SchemaFormat.JSON_AND_YAML) {
			ObjectMapper objectmapperYaml = new ObjectMapper(new YAMLFactory());

			// Attributes with null values should be skipped
			objectmapperYaml.setSerializationInclusion(Include.NON_NULL);

			this.objectmappers.put("YAML", objectmapperYaml);

		} 

		if(options.getSchemaFormat() == SchemaFormat.JSON || options.getSchemaFormat() == SchemaFormat.JSON_AND_YAML) {
			ObjectMapper objectmapperJson = new ObjectMapper();

			// Attributes with null values should be skipped
			objectmapperJson.setSerializationInclusion(Include.NON_NULL);
			this.objectmappers.put("JSON", objectmapperJson);
		}
	}
	
	public void disconnect() {
		LOG.info("Disconnecting MQTT");
		try {
			mqttClientPub.disconnect();
		} catch (MqttException e) {
			LOG.log(Level.SEVERE, "", e);
		}
		LOG.info("MQTT disconnected");
	}
	
	public void pubEvent(MqttDevice thing, String eventName, Object payload) {

		String baseTopic = new TopicUtil().getBaseTopic(options.getAppId(), thing) + "/events/" + eventName;
		String payloadStr = payload.toString();
		if (addRandomtoEvents) {
			payloadStr += " " + Math.random();
		}

		pubEvent(baseTopic, payloadStr);
	}

	
	public void publishDesc(MqttDevice thing) {
		String desc = "";
		for(Entry<String, ObjectMapper> entry : objectmappers.entrySet()) {


			try {
				desc = entry.getValue().writerWithDefaultPrettyPrinter().writeValueAsString(thing.getDeviceDescription());
			} catch (JsonProcessingException e) {
				LOG.log(Level.SEVERE, "", e);
			}
			String baseTopic = new TopicUtil().getBaseTopic(options.getAppId(), thing);
			pubRetained(baseTopic + "/schema/" + entry.getKey(), desc);
		}
	}
	
	public void publishState(MqttDevice thing) {
		String baseTopic = new TopicUtil().getBaseTopic(options.getAppId(), thing) + "/state";
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
			mqttClientPub.publish(topic, message);
		} catch (MqttException e) {
			LOG.log(Level.SEVERE, "", e);
		}
	}


	private void pubState(String topic, Object payload) {

		for(Entry<String, ObjectMapper> objectmapper : objectmappers.entrySet()) {
			try {
				String state = objectmapper.getValue().writerWithDefaultPrettyPrinter().writeValueAsString(payload);
				LOG.info("Publishing on " + topic + " data: " + state);
				
				MqttMessage message = new MqttMessage(state.getBytes());
				message.setRetained(true);
				
				IMqttDeliveryToken imMqttDeliveryToken = mqttClientPub.publish(topic, message);
				imMqttDeliveryToken.waitForCompletion();
			} catch (MqttException | JsonProcessingException e) {
				LOG.log(Level.SEVERE, "", e);
			}
		}
	}
	
	private void pubRetained(String topic, Object payload) {
		if(options.isLogPublishMessages()) {
			LOG.info("Publishing Retained on " + topic + " data: " + payload);
		}
		
		try {
			MqttMessage message = new MqttMessage(payload == null ? "".getBytes() : payload.toString().getBytes());
			message.setRetained(true);
			mqttClientPub.publish(topic, message);
		} catch (MqttException e) {
			LOG.log(Level.SEVERE, "", e);
		}		
	}

}

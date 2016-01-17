package ch.bfh.bti7321thesis.app;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
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

import ch.bfh.bti7321thesis.app.Config.DescriptionFormat;

/**
 * Main Class for Publishing the Messages and Device Descriptions.
 * Realized with Singleton Pattern.
 * 
 * Config with {@link #setConfig(Config)} before usage.
 * 
 * @author adrian
 *
 */
public class MqttPublisher {
	
	private Logger LOG = Logger.getLogger(this.getClass().getName());
	
	
	private IMqttAsyncClient mqttClientPub;
	private IMqttAsyncClient mqttClientSub;
	
	private static Config options;
	
	private Map<DescriptionFormat, ObjectMapper> objectmappers = new HashMap<DescriptionFormat, ObjectMapper>();

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
		
		if(options.getDescriptionQoS() < 0 || options.getDescriptionQoS() > 2) {
			throw new IllegalStateException("descriptionQoS invalid");
		}
		
		if(options.getEventsQoS() < 0 || options.getEventsQoS() > 2) {
			throw new IllegalStateException("eventQoS invalid");
		}
		
		if(options.getStateQos() < 0 || options.getStateQos() > 2) {
			throw new IllegalStateException("stateQoS invalid");
		}
		
		if(options.getMqttClientIdPrefix() == null) {
			String clientprefix = UUID.randomUUID().toString().replaceAll("-", "");
			LOG.info("ClientId Prefix not specfified, generatin Random: " + clientprefix);
		}
	}

	private static class Holder {
    	private static MqttPublisher instance = new MqttPublisher();
    }
	
	/**
	 * Get the only instance of this singleton
	 * @return instance
	 */
	public static MqttPublisher getInstance() {
		return Holder.instance;
	}
	
	/**
	 * Define Configuration before first usage
	 * @param options
	 */
	public static void setConfig(Config options) {
		MqttPublisher.options = options;
		
	}
	
	private void setUpMqtt() {
		LOG.info("Connecting to broker: " + options.getMqttBrokerUri());
		try {
			// PUB
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			
			MemoryPersistence persistence = new MemoryPersistence();
			mqttClientPub = new MqttAsyncClient(options.getMqttBrokerUri(), options.getMqttClientIdPrefix() + "_pub", persistence);
			mqttClientPub.connect(connOpts).waitForCompletion();
			
			
			// SUB
			MemoryPersistence subPersistence = new MemoryPersistence();
			mqttClientSub = new MqttAsyncClient(options.getMqttBrokerUri(), options.getMqttClientIdPrefix() + "_sub", subPersistence);
			mqttClientSub.connect(connOpts).waitForCompletion();
			mqttClientSub.setCallback(options.getMqttCallback());
			mqttClientSub.subscribe(options.getAppId() +"/+/+/+/+/commands/#", 2);
			
		} catch (MqttException e) {
			LOG.log(Level.SEVERE, "", e);
		}
		LOG.info("MQTT Connected");
	}
	
	private void setUpJackson() {
		ObjectMapper objectmapperYaml = new ObjectMapper(new YAMLFactory());

		// Attributes with null values should be skipped
		objectmapperYaml.setSerializationInclusion(Include.NON_NULL);

		this.objectmappers.put(DescriptionFormat.YAML, objectmapperYaml);

		if (options.getDescriptionFormat() == DescriptionFormat.JSON) {
			ObjectMapper objectmapperJson = new ObjectMapper();

			// Attributes with null values should be skipped
			objectmapperJson.setSerializationInclusion(Include.NON_NULL);
			this.objectmappers.put(DescriptionFormat.JSON, objectmapperJson);
		}
	}
	
	/**
	 * Disconnects all MQTT Connections
	 */
	public void disconnect() {
		LOG.info("Disconnecting MQTT");
		try {
			mqttClientPub.disconnect();
			mqttClientSub.disconnect();
		} catch (MqttException e) {
			LOG.log(Level.SEVERE, "", e);
		}
		LOG.info("MQTT disconnected");
	}
	
	/**
	 * Publish an event.
	 * 
	 * @param device Device from which the event originates
	 * @param eventName Name of the Event. Used as part of the topic
	 * @param payload Payload Data of the event
	 */
	public void pubEvent(MqttDevice device, String eventName, Object payload) {

		String baseTopic = TopicUtil.getBaseTopic(options.getAppId(), device) + "/events/" + eventName;
		String payloadStr = payload.toString();

		pubEvent(baseTopic, payloadStr);
	}

	/**
	 * Publish the Description of a Device
	 * 
	 * @param device Device with the Description attached
	 */
	public void publishDesc(MqttDevice device) {
		String desc = "";
		for (Entry<DescriptionFormat, ObjectMapper> entry : objectmappers.entrySet()) {

			try {
				desc = entry.getValue().writerWithDefaultPrettyPrinter()
						.writeValueAsString(device.getDeviceDescription());
			} catch (JsonProcessingException e) {
				LOG.log(Level.SEVERE, "", e);
			}
			String baseTopic = TopicUtil.getBaseTopic(options.getAppId(), device);
			String topic = baseTopic + "/schema/" + entry.getKey();

			LOG.info("Publishing Description on " + topic + " data: " + desc);

			try {
				MqttMessage message = new MqttMessage(desc == null ? "".getBytes() : desc.toString().getBytes());
				message.setRetained(true);
				message.setQos(options.getDescriptionQoS());
				mqttClientPub.publish(topic, message);
			} catch (MqttException e) {
				LOG.log(Level.SEVERE, "", e);
			}
		}
	}
	
	/**
	 * Publish the State information of the Device
	 * 
	 * @param device Device with the state attributes
	 */
	public void publishState(MqttDevice device) {
		String baseTopic = TopicUtil.getBaseTopic(options.getAppId(), device) + "/state";
		for(Entry<String, Object> state : device.getState().entrySet()) {
			LOG.info(state.getKey());
			pubState(baseTopic + "/" + state.getKey(), state.getValue());
		}
	}
	
	private void pubEvent(String topic, String payload) {
		
		LOG.info("Publishing on " + topic + " data: " + payload);
		try {
			MqttMessage message = new MqttMessage(payload == null ? "".getBytes() : payload.getBytes());
			message.setRetained(options.isEventsRetained());
			message.setQos(options.getEventsQoS());
			mqttClientPub.publish(topic, message);
		} catch (MqttException e) {
			LOG.log(Level.SEVERE, "", e);
		}
	}


	private void pubState(String topic, Object payload) {

		// for(Entry<String, ObjectMapper> objectmapper :
		// objectmappers.entrySet()) {

		try {
			ObjectMapper yamlMapper = objectmappers.get(DescriptionFormat.YAML);
			String state = yamlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
			LOG.info("Publishing on " + topic + " data: " + state);

			MqttMessage message = new MqttMessage(state.getBytes());
			message.setRetained(true);
			message.setQos(options.getStateQos());

			IMqttDeliveryToken imMqttDeliveryToken = mqttClientPub.publish(topic, message);
			imMqttDeliveryToken.waitForCompletion();
		} catch (MqttException | JsonProcessingException e) {
			LOG.log(Level.SEVERE, "", e);
		}
		// }
	}

}

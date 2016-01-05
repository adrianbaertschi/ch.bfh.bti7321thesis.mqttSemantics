package ch.bfh.bti7321thesis.tinkerforge;

import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.IMqttAsyncClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import ch.bfh.bti7321thesis.tinkerforge.devices.MqttThing;

public class MqttPublisher {
	
	private Logger LOG = Logger.getLogger(this.getClass().getName());
	
	private static final String BROKER = "tcp://46.101.165.125:1883";
	private static final String CLIENT_ID = "barta3Tinker";
	
	private MemoryPersistence persistence = new MemoryPersistence();
	private IMqttAsyncClient mqttClient;
	
	// TODO: set somewhere
	private static MqttCallback mqttCallBack;
	
	// TODO: flag prettyprint (true/false)
	
	ObjectMapper mapper = new ObjectMapper(); // create once, reuse
	ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());

	private boolean addRandomtoEvents = false;
	

	private MqttPublisher() {
		setUpMqtt();
		setUpJackson();
	}
	
    private static class Holder {
    	private static MqttPublisher instance = new MqttPublisher();
    }
	
	public static MqttPublisher getInstance() {
		return Holder.instance;
	}
	
//	public static IMqttAsyncClient getclient() {
//		return null;
//	}
	
	public static void setCallback(MqttCallback mqttCallback) {
		mqttCallBack = mqttCallback;
	}
	
	private void setUpMqtt() {
		LOG.info("Connecting to broker: " + BROKER);
		try {
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			
			mqttClient = new MqttAsyncClient(BROKER, CLIENT_ID, persistence);
			mqttClient.connect(connOpts).waitForCompletion();
			
			// TODO
			mqttClient.setCallback(mqttCallBack);
			
			// TODO: read topic base from config
			mqttClient.subscribe("ch.bfh.barta3/+/+/+/+/commands/#", 2);
		} catch (MqttException e) {
			e.printStackTrace();
		}
		LOG.info("MQTT Connected");

	}
	
	private void setUpJackson() {
		// Attributes with null values should be skipped
		mapper.setSerializationInclusion(Include.NON_NULL);
		yamlMapper.setSerializationInclusion(Include.NON_NULL);
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
		
		String baseTopic = new BrickletToMqttConverter().getBaseTopic(thing) + "/events/" + eventName;
		String payloadStr = payload.toString();
		if(addRandomtoEvents ) {
			payloadStr += " " + Math.random();
		}
		
		pubEvent(baseTopic, payloadStr);
	}

	
	public void publishDesc(MqttThing thing) {
		String json = "";
		String yaml = "";
		try {
			json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(thing.getDeviceDescription());
			yaml = yamlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(thing.getDeviceDescription());
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		String baseTopic = new BrickletToMqttConverter().getBaseTopic(thing);
//		pubRetained(baseTopic + "/schema/json", json);
		pubRetained(baseTopic + "/schema/yaml", yaml);
		
	}
	
	public void publishDeviceState(MqttThing thing) {
		String baseTopic = new BrickletToMqttConverter().getBaseTopic(thing) + "/state";
		for(Entry<String, Object> state : thing.getState().entrySet()) {
			LOG.info(state.getKey());
			pubState(baseTopic + "/" + state.getKey(), state.getValue());
		}
	}
	
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
			String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);
			LOG.info("Publishing on " + topic + " data: " + json);
			
			MqttMessage message = new MqttMessage(json.getBytes());
			message.setRetained(true); // TODO: activate if stable
			
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

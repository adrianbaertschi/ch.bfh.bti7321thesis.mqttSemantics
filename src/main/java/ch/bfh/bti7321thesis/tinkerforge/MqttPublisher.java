package ch.bfh.bti7321thesis.tinkerforge;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class MqttPublisher {
	
	// TODO: 
	
//	static final String broker = "tcp://iot.eclipse.org:1883";
//	static final String broker = "tcp://test.mosquitto.org:1883";
	static final String broker = "tcp://46.101.165.125:1883";
	static final String clientId = "barta3Tinker";
	
	private MemoryPersistence persistence = new MemoryPersistence();
	private MqttClient mqttClient;
	

	private MqttPublisher() {
		setUpMqtt();
	}
	
    private static class Holder {
    	private static MqttPublisher instance = new MqttPublisher();
    	
    }
	
	public static MqttPublisher getInstance() {
		return Holder.instance;
	}
	
	 
	
	private void setUpMqtt() {
		System.out.println("Connecting to broker: " + broker);
		try {
			MqttConnectOptions connOpts = new MqttConnectOptions();
			connOpts.setCleanSession(true);
			mqttClient = new MqttClient(broker, clientId, persistence);
			mqttClient.connect(connOpts);
			mqttClient.setCallback(new MqttActionReveiver());
							   //"ch.bfh.barta3/X1-Carbon/tfstack1/Temperature IR Bricklet/qC1/actions/setAmbientTemperatureCallbackPeriod"
			mqttClient.subscribe("ch.bfh.barta3/+/+/+/+/actions/#");
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MQTT Connected");

	}
	
	public void disconnect() {
		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void pubEvent(String stackHost, Device device, String eventName, Object payload) {
		
		String baseTopic = new BrickletToMqttConverter().getBaseTopic(device, stackHost) + "/events/" + eventName;
		
		pubEvent(baseTopic, payload.toString());
	}

	private void pubEvent(String topic, Object payload) {
		
		System.out.println("Publishing on " + topic + " data: " + payload);
		try {
			MqttMessage message = new MqttMessage(payload == null ? "".getBytes() : payload.toString().getBytes());
			mqttClient.publish(topic, message);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void publishTempIrState(String stackName, BrickletTemperatureIR brickletTemperatureIR) {

		String baseTopic = new BrickletToMqttConverter().getBaseTopic(brickletTemperatureIR, stackName) + "/state";

		try {
			pubState(baseTopic + "/AmbientTemperatureCallbackPeriod", Long.toString(brickletTemperatureIR.getAmbientTemperatureCallbackPeriod()));
			pubState(baseTopic + "/ObjectTemperatureCallbackPeriod", Long.toString(brickletTemperatureIR.getObjectTemperatureCallbackPeriod()));
			pubState(baseTopic + "/DebouncePeriod", Long.toString(brickletTemperatureIR.getDebouncePeriod()));
			pubState(baseTopic + "/Emissivity", Long.toString(brickletTemperatureIR.getEmissivity()));
			pubState(baseTopic + "/AmbientTemperatureCallbackThreshold", brickletTemperatureIR.getAmbientTemperatureCallbackThreshold().toString());
			pubState(baseTopic + "/ObjectTemperatureCallbackThreshold", brickletTemperatureIR.getObjectTemperatureCallbackThreshold().toString());
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void publishAction(String stackname, BrickletTemperatureIR brickletTemperatureIR) {
		String baseTopic = new BrickletToMqttConverter().getBaseTopic(brickletTemperatureIR, stackname) + "/actions";
//		pubEvent(baseTopic + "/setAmbientTemperatureCallbackPeriod", null);
//		pubEvent(baseTopic + "/setObjectTemperatureCallbackPeriod", null);
	}
	
	public void publishJoystickState(String stack, BrickletJoystick brickletJoystick) {
		String baseTopic = new BrickletToMqttConverter().getBaseTopic(brickletJoystick, stack) + "/state";
		try {
			pubState(baseTopic + "/AnalogValueCallbackPeriod", brickletJoystick.getAnalogValueCallbackPeriod());
			pubState(baseTopic + "/AnalogValueCallbackThreshold", brickletJoystick.getAnalogValueCallbackThreshold());
//			pubState(baseTopic + "/DebouncePeriod", brickletJoystick.getDebouncePeriod());
			pubState(baseTopic + "/PositionCallbackPeriod", brickletJoystick.getPositionCallbackPeriod());
			pubState(baseTopic + "/PositionCallbackThreshold", brickletJoystick.getPositionCallbackThreshold());
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void pubState(String topic, Object payload) {
		System.out.println("Publishing on " + topic + " data: " + payload);
		try {
			MqttMessage message = new MqttMessage(payload.toString().getBytes());
//			message.setRetained(true); // TODO: activate if stable
			mqttClient.publish(topic, message);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public MqttClient getMqttClient() {
		return mqttClient;
	}

}

package ch.bfh.bti7321thesis.tfdemo.semantic;

import java.io.IOException;
import java.net.UnknownHostException;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletDualButton.StateChangedListener;
import com.tinkerforge.IPConnection;
import com.tinkerforge.IPConnection.EnumerateListener;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class BrickEnumerationEvents {

	private static final String HOST = "localhost";
	private static final int PORT = 4223;

	static String topicMaster = "ch.bfh.barta3/";
	static String broker = "tcp://iot.eclipse.org:1883";
	static String clientId = "JavaSampleBarta3";
	static MemoryPersistence persistence = new MemoryPersistence();
	static MqttClient mqttClient;

	public static void main(String[] args) throws UnknownHostException, AlreadyConnectedException, IOException,
			NotConnectedException, TimeoutException, InterruptedException {

		setUpMqtt();

		final IPConnection ipcon = new IPConnection();

		ipcon.addEnumerateListener(new EnumerateListener() {

			public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
					short[] firmwareVersion, int deviceIdentifier, short enumerationType) {

				System.out.println(
						String.format("uid %s, connectedUid %s, position %c, deviceIdentifier %d, enumerationType %d",
								uid, connectedUid, position, deviceIdentifier, enumerationType));

				switch (deviceIdentifier) {
				case BrickletDualButton.DEVICE_IDENTIFIER:
					System.out.println("OK " + BrickletDualButton.DEVICE_DISPLAY_NAME);
					buttonCallback(new BrickletDualButton(uid, ipcon));
					break;
				default:
					System.out.println("Unknown Device");
					break;
				}

			}
		});
		ipcon.connect(HOST, PORT); // Connect to brickd
		ipcon.enumerate();

		System.out.println("Press key to exit");
		System.in.read();

		try {
			mqttClient.disconnect();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		System.out.println("MQTT Disconnected");
		ipcon.disconnect();
	}

	private static void setUpMqtt() {
		try {
			mqttClient = new MqttClient(broker, clientId, persistence);
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MqttConnectOptions connOpts = new MqttConnectOptions();
		connOpts.setCleanSession(true);
		System.out.println("Connecting to broker: " + broker);
		try {
			mqttClient.connect(connOpts);
			mqttClient.subscribe(topicMaster + "#");
			mqttClient.setCallback(new MqttCallback() {
				
				public void messageArrived(String arg0, MqttMessage arg1) throws Exception {
					// TODO Auto-generated method stub
					System.out.println("Msg arrived " +arg0 + new String(arg1.getPayload()));
					
				}
				
				public void deliveryComplete(IMqttDeliveryToken arg0) {
					// TODO Auto-generated method stub
					
				}
				
				public void connectionLost(Throwable arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		} catch (MqttSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Connected");

	}

	private static void buttonCallback(BrickletDualButton db) {
		db.addStateChangedListener(new StateChangedListener() {

			public void stateChanged(short buttonL, short buttonR, short ledL, short ledR) {
				System.out.println("btl: " + buttonL + " btr: " + buttonR + " ledL: " + ledL + " ledR: " + ledR);
				if (buttonL == BrickletDualButton.BUTTON_STATE_PRESSED) {
					pub("BL", "pressed");
				}

				if (buttonR == BrickletDualButton.BUTTON_STATE_PRESSED) {
					pub("BR", "pressed");
				}

			}
		});
	}

	private static void pub(String topic, String msg) {

		System.out.println("Publishing on topic " + topic + " Msg: " + msg);
		MqttMessage message = new MqttMessage(msg.getBytes());
		message.setQos(2);
		try {
			mqttClient.publish(topicMaster + topic, message);
		} catch (MqttPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MqttException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
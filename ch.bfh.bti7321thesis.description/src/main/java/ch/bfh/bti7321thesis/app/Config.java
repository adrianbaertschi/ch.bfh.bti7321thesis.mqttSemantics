package ch.bfh.bti7321thesis.app;

import org.eclipse.paho.client.mqttv3.MqttCallback;

/**
 * Config data holder for {@link MqttPublisher}
 * 
 * @author adrian
 *
 */
public class Config {
	public enum SchemaFormat {
		JSON, YAML, JSON_AND_YAML
	};

	private SchemaFormat schemaFormat = SchemaFormat.YAML;
	private String mqttBrokerUri;
	private String mqttClientIdPrefix;
	private String appId;
	private MqttCallback mqttCallback;
	
	private boolean eventsRetained = true;
	private int eventsQoS = 1;
	
	private int stateQos = 1;
	private int descriptionQoS = 1;

	public String getMqttBrokerUri() {
		return mqttBrokerUri;
	}

	/**
	 * Broker URL. <br>
	 * Example: tcp://iot.eclipse.org:1883
	 * 
	 * @param mqttBrokerUri
	 */
	public void setMqttBrokerUri(String mqttBrokerUri) {
		this.mqttBrokerUri = mqttBrokerUri;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public MqttCallback getMqttCallback() {
		return mqttCallback;
	}

	/**
	 * Register the Callback Object for the Commands <br>
	 * Standard: YAML
	 * 
	 * @param mqttCallback
	 */
	public void setMqttCallback(MqttCallback mqttCallback) {
		this.mqttCallback = mqttCallback;
	}

	public SchemaFormat getSchemaFormat() {
		return schemaFormat;
	}

	/**
	 * Set the Desired Format for the Device Descriptions
	 * 
	 * @param schemaFormat
	 */
	public void setSchemaFormat(SchemaFormat schemaFormat) {
		this.schemaFormat = schemaFormat;
	}

	public String getMqttClientIdPrefix() {
		return mqttClientIdPrefix;
	}

	/**
	 * Set the Prefix for the MqttClientId.<br>
	 * 
	 * Two connections wille be established. One for Publishing
	 * [mqttClientIdPrefix]_pub and one for the subscruptions
	 * [mqttClientIdPrefix]_sub. <br>
	 * If not Spefified, a randon IDs will be generated.
	 * 
	 * @param mqttClientIdPrefix
	 *            Prefix for the Client IDs
	 */
	public void setMqttClientIdPrefix(String mqttClientIdPrefix) {
		this.mqttClientIdPrefix = mqttClientIdPrefix;
	}

	public boolean isEventsRetained() {
		return eventsRetained;
	}

	/**
	 * Specify if the Event Messages should be publshed as retained <br>
	 * Standard: true
	 * @param eventsRetained
	 */
	public void setEventsRetained(boolean eventsRetained) {
		this.eventsRetained = eventsRetained;
	}

	public int getEventsQoS() {
		return eventsQoS;
	}

	/**
	 * QoS for the Event Messages <br>
	 * Standard: 1
	 * @param eventsQoS
	 */
	public void setEventsQoS(int eventsQoS) {
		this.eventsQoS = eventsQoS;
	}

	public int getStateQos() {
		return stateQos;
	}

	/**
	 * QoS for the State Messages <br>
	 * Standard: 1
	 * @param stateQos
	 */
	public void setStateQos(int stateQos) {
		this.stateQos = stateQos;
	}

	public int getDescriptionQoS() {
		return descriptionQoS;
	}

	/**
	 * QoS for the Description Messages <br>
	 * Standard: 1
	 * @param descriptionQoS
	 */
	public void setDescriptionQoS(int descriptionQoS) {
		this.descriptionQoS = descriptionQoS;
	}
}

package ch.bfh.bti7321thesis.app;

public class TopicUtil {
	
	/**
	 * Get the first topic with the firtst five parts of for Device
	 * 
	 * @param appId AppId
	 * @param device Device
	 * @return Topic String
	 */
	public static String getBaseTopic(String appId, MqttDevice device) {
		MqttTopic topic = new MqttTopic();
		topic.append(appId);
		topic.append(device.getGroup());
		topic.append(device.getSubGroup());
		topic.append(device.getDeviceType());
		topic.append(device.getDeviceInstance());
		return topic.toString();
	}
}

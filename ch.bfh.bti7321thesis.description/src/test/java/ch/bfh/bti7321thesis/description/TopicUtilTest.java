package ch.bfh.bti7321thesis.description;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.bfh.bti7321thesis.app.MqttDevice;
import ch.bfh.bti7321thesis.app.TopicUtil;

public class TopicUtilTest {

	@Test
	public void test() {
		MqttDevice device = new MqttDevice();
		device.setGroup("group");
		device.setSubGroup("sub");
		device.setDeviceType("type");
		device.setDeviceInstance("inst");
		
		
		String topic = TopicUtil.getBaseTopic("app", device);
		assertEquals("app/group/sub/type/inst", topic);
	}

}

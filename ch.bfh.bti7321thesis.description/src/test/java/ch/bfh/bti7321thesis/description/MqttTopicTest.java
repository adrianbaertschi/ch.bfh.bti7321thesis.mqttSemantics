package ch.bfh.bti7321thesis.description;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ch.bfh.bti7321thesis.app.MqttTopic;

public class MqttTopicTest {

	@Test
	public void testMqttTopicString() {
		MqttTopic topic = new MqttTopic("a/b/c/d/");
		assertEquals(4, topic.getNumberofElements());
		
		topic.append("e");
		
		assertEquals("a/b/c/d/e", topic.toString());
	}

}

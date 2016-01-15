package ch.bfh.bti7321thesis.tinkerforge;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import ch.bfh.bti7321thesis.tinkerforge.log.StdoutConsoleHandler;

public class TinkerforgeApp {

	private static Logger LOG = Logger.getLogger(TinkerforgeApp.class.getName());

	public static void main(String[] args) throws IOException {

		LogManager.getLogManager().reset();

		Logger.getLogger("").setLevel(Level.ALL);
		Logger.getLogger("").setUseParentHandlers(false);

		Logger.getLogger("").addHandler(new StdoutConsoleHandler());

		String[] values = loadConfigValue("hosts").split(",");
		Set<String> hosts = new HashSet<String>(Arrays.asList(values));
		// Always include localhost
		hosts.add("localhost");
		
		
		
		Options options = new Options();
		options.setMqttBrokerUri(loadConfigValue("brokerUri"));
		options.setMqttClientId("TfDemoApp");
		options.setAppId(loadConfigValue("appId"));
		options.setMqttCallback(new MqttCommandReceiver());
		options.setLogPublishMessages(true);
		MqttPublisher.setOptions(options);
		
		
		List<BrickEnumerator> enumerators = new ArrayList<BrickEnumerator>();

		for (String host : hosts) {
			BrickEnumerator brickEnumerator = new BrickEnumerator(host);
			enumerators.add(brickEnumerator);
			brickEnumerator.start();
		}

		LOG.info("Press key to exit");
		System.in.read();

		for (BrickEnumerator brickEnumerator : enumerators) {
			brickEnumerator.closeConnection();
		}
		MqttPublisher.getInstance().disconnect();

	}
	
	private static String loadConfigValue(String key) {
		Properties prop = new Properties();
		try {
			InputStream input = new FileInputStream("config.properties");
			prop.load(input);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return prop.getProperty(key);
		
//		List<String> hosts = Arrays.asList(prop.getProperty("hosts").split(","));
//		String appId = prop.getProperty("appId");
		
	}
}
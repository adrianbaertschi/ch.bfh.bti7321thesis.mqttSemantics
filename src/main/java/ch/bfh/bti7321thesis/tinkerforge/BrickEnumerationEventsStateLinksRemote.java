package ch.bfh.bti7321thesis.tinkerforge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class BrickEnumerationEventsStateLinksRemote {

	
	private static Logger LOG = Logger.getLogger(BrickEnumerationEventsStateLinksRemote.class.getName());

	public static void main(String[] args) throws IOException {
		
		
		List<String> hosts = new ArrayList<String>();
		hosts.add("localhost");
		hosts.add("tfstack1");
		hosts.add("tfstack2");
		
		List<BrickEnumerator> enumerators = new ArrayList<BrickEnumerator>();
		
		for(String host : hosts) {
			BrickEnumerator brickEnumerator = new BrickEnumerator(host);
			brickEnumerator.start();
		}
		
		
		System.out.println("Press key to exit");
		System.in.read();
		
		for(BrickEnumerator brickEnumerator : enumerators) {
			brickEnumerator.closeConnection();
		}
		MqttPublisher.getInstance().disconnect();

	}




	

	


}
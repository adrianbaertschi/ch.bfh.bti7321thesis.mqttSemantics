package ch.bfh.bti7321thesis.tinkerforge;

import java.util.ArrayList;
import java.util.List;

import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.app.MqttTopic;
import ch.bfh.bti7321thesis.tinkerforge.devices.MqttBricklet;

// TODO: refactor to HashMap
public class TinkerforgeDeviceRegistry {
	
	private List<MqttBricklet> things = new ArrayList<MqttBricklet>();
//	ListMultimap<String, Device> devices = SynchronizedListMultimap;
	
	
    private static class Holder {
    	private static TinkerforgeDeviceRegistry instance = new TinkerforgeDeviceRegistry();
    	
    }
    
	private TinkerforgeDeviceRegistry() {
		
	}
	
	public static TinkerforgeDeviceRegistry getInstance() {
		return Holder.instance;
	}
	
	
	public void add(MqttBricklet device) {
		this.things.add(device);
	}

	public MqttBricklet find(MqttTopic topic) throws TimeoutException {
		
		String uid = topic.getTopic(4);
		
		return things.stream().filter(t -> t.getUid().equals(uid)).findFirst().get();
	}
	
	

}

package ch.bfh.bti7321thesis.tinkerforge;

import java.util.ArrayList;
import java.util.List;

import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.tinkerforge.devices.MqttThing;

// TODO: refactor to HashMap
public class TinkerforgeDeviceRegistry {
	
	private List<MqttThing> things = new ArrayList<MqttThing>();
//	ListMultimap<String, Device> devices = SynchronizedListMultimap;
	
	
    private static class Holder {
    	private static TinkerforgeDeviceRegistry instance = new TinkerforgeDeviceRegistry();
    	
    }
    
	private TinkerforgeDeviceRegistry() {
		
	}
	
	public static TinkerforgeDeviceRegistry getInstance() {
		return Holder.instance;
	}
	
	
	public void add(MqttThing device) {
		this.things.add(device);
	}

	public MqttThing find(MqttTopic topic) throws TimeoutException {
		
		String uid = topic.getTopic(4);
		
		return things.stream().filter(t -> t.getUid().equals(uid)).findFirst().get();
	}
	
	

}

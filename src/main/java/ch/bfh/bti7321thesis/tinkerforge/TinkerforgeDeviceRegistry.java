package ch.bfh.bti7321thesis.tinkerforge;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class TinkerforgeDeviceRegistry {
	
//	private Map<String, List<Device>> devices = new HashMap<String, List<Device>>();
	ListMultimap<String, Device> devices = ArrayListMultimap.create();
//	ListMultimap<String, Device> devices = SynchronizedListMultimap;
	
	
    private static class Holder {
    	private static TinkerforgeDeviceRegistry instance = new TinkerforgeDeviceRegistry();
    	
    }
    
	private TinkerforgeDeviceRegistry() {
		
	}
	
	public static TinkerforgeDeviceRegistry getInstance() {
		return Holder.instance;
	}
	
	
	public void add(String stackHost, Device device) {
		this.devices.put(stackHost, device);
	}

	public Device find(MqttTopic topic) throws TimeoutException {
		List<Device> devicesOnStack = devices.get(topic.getTopic(2));
		
		// TODO: filter on DeviceType
		
		// Filter on UID
		String uid = topic.getTopic(4);
		
		for(Device device : devicesOnStack) {
			try {
				if(device.getIdentity().uid.equals(uid)) {
					return device;
				}
			} catch (NotConnectedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		return null;
	}

}

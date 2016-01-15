package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.HashMap;
import java.util.Map;

import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletMotionDetector.DetectionCycleEndedListener;
import com.tinkerforge.BrickletMotionDetector.MotionDetectedListener;
import com.tinkerforge.IPConnection;

import ch.bfh.bti7321thesis.tinkerforge.MqttPublisher;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;

public class MotionDetectorDevice extends MqttDevice<BrickletMotionDetector>{
	
	boolean enabled = true;
	
	public MotionDetectorDevice (String uid, IPConnection ipcon, String stackName) {
		super(uid, ipcon, stackName);
	}

	@Override
	public boolean handleCommand(String commandName, byte[] payload) {
		
		
		return false;
	}

	@Override
	public Map<String, Object> getState() {
		Map<String, Object> state = new HashMap<String, Object>();
//		state.put("enabled", bricklet)
		return state;
	}

	@Override
	public DeviceDescription getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUpDevice() {
		bricklet = new BrickletMotionDetector(uid, ipcon);
		
		bricklet.addMotionDetectedListener(new MotionDetectedListener() {

			@Override
			public void motionDetected() {
				MqttPublisher.getInstance().pubEvent(MotionDetectorDevice.this.toThing(), "MotionDetected", "yes");
			}
		});

		bricklet.addDetectionCycleEndedListener(new DetectionCycleEndedListener() {

			@Override
			public void detectionCycleEnded() {
				MqttPublisher.getInstance().pubEvent(MotionDetectorDevice.this.toThing(), "MotionDetected", "No");
				
			}
		});
		
	}

}

package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.Map;

import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletMotionDetector.DetectionCycleEndedListener;
import com.tinkerforge.BrickletMotionDetector.MotionDetectedListener;
import com.tinkerforge.IPConnection;

import ch.bfh.bti7321thesis.tinkerforge.MqttPublisher;
import ch.bfh.bti7321thesis.tinkerforge.desc.DeviceDescription;

public class MotionDetectorDevice extends MqttThing<BrickletMotionDetector>{
	
	public MotionDetectorDevice (String uid, IPConnection ipcon, String stackName) {
		this.stackName = stackName;
		
		bricklet = new BrickletMotionDetector(uid, ipcon);
		
		bricklet.addMotionDetectedListener(new MotionDetectedListener() {

			@Override
			public void motionDetected() {
				MqttPublisher.getInstance().pubEvent(MotionDetectorDevice.this, "MotionDetected", "yes");
			}
		});

		bricklet.addDetectionCycleEndedListener(new DetectionCycleEndedListener() {

			@Override
			public void detectionCycleEnded() {
				MqttPublisher.getInstance().pubEvent(MotionDetectorDevice.this, "MotionDetected", "No");
				
			}
		});
	}

	@Override
	public boolean handleAction(String action, byte[] payload) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map<String, Object> getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceDescription getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}

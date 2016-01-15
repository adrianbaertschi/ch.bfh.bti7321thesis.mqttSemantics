package ch.bfh.bti7321thesis.tinkerforge.util;

import java.util.HashMap;
import java.util.Map;

import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.BrickletHumidity;
import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.BrickletMotionDetector;
import com.tinkerforge.BrickletTemperatureIR;

public class TinkerforgeBrickletDB {

	static Map<Integer, String> bricklets = new HashMap<Integer, String>();

	static {
		bricklets.put(BrickletJoystick.DEVICE_IDENTIFIER, BrickletJoystick.DEVICE_DISPLAY_NAME);
		bricklets.put(BrickletDualButton.DEVICE_IDENTIFIER, BrickletDualButton.DEVICE_DISPLAY_NAME);
		bricklets.put(BrickletTemperatureIR.DEVICE_IDENTIFIER, BrickletTemperatureIR.DEVICE_DISPLAY_NAME);
		bricklets.put(BrickletMotionDetector.DEVICE_IDENTIFIER, BrickletMotionDetector.DEVICE_DISPLAY_NAME);
		bricklets.put(BrickletHumidity.DEVICE_IDENTIFIER, BrickletHumidity.DEVICE_DISPLAY_NAME);
	}

	public static String getDisplayName(int deviceId) {
		String displayName = bricklets.get(deviceId);
		if(displayName == null) {
			return "unknown";
		}
		return displayName;
	}

}

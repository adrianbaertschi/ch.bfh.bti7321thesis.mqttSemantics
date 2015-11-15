package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tinkerforge.BrickletJoystick;
import com.tinkerforge.Device;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class JoyStickDevice extends MqttThing<BrickletJoystick> {

	private Device device;

	public JoyStickDevice(Device device) {
		this.device = device;
	}

	public List<String> getActions() {
		Method[] declaredMethods = BrickletJoystick.class.getDeclaredMethods();

		Stream<String> filter = Arrays.asList(declaredMethods).stream().filter(m -> m.getName().startsWith("set"))
				.map(action -> action.getName());

		return filter.collect(Collectors.toList());
//		 return Arrays.asList(declaredMethods);
	}
	

	public void handleAction(String action, String argument) {
		Method[] declaredMethods = device.getClass().getDeclaredMethods();

		try {
			Optional<Method> method = Arrays.asList(declaredMethods).stream().filter(m -> m.getName() == action)
					.findFirst();

			method.get().invoke(device, argument);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public String getUid() {
		try {
			return this.device.getIdentity().uid;
		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean handleAction(String action, byte[] payload) {

		BrickletJoystick joystick = (BrickletJoystick) device;
		try {

			long period = Long.parseLong(new String(payload));
			
			switch (action) {
			case "setAnalogValueCallbackPeriod":
				joystick.setAnalogValueCallbackPeriod(period);
				return true;
//			case "setAnalogValueCallbackThreshold":
//				// TODO
//				break;
			case "setPositionCallbackPeriod":
				joystick.setPositionCallbackPeriod(period);
				return true;
			default:
				throw new IllegalArgumentException("Action " + action + " not supported");
			}

		} catch (TimeoutException | NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public Map<String, Object> getState() {
		// TODO Auto-generated method stub
		return null;
	}

}

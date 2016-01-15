package ch.bfh.bti7321thesis.tinkerforge.devices;

import java.util.Map;

import com.tinkerforge.Device;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

import ch.bfh.bti7321thesis.desc.ComplexType;
import ch.bfh.bti7321thesis.desc.DeviceDescription;
import ch.bfh.bti7321thesis.desc.Options;
import ch.bfh.bti7321thesis.desc.Range;
import ch.bfh.bti7321thesis.desc.cmd.Command;
import ch.bfh.bti7321thesis.desc.cmd.CommandDescription;
import ch.bfh.bti7321thesis.desc.event.Event;
import ch.bfh.bti7321thesis.desc.event.EventDescription;
import ch.bfh.bti7321thesis.desc.state.StateDescription;

public class MockDevice extends MqttBricklet {
	
	public MockDevice(String uid, IPConnection ipcon, String stackName) {
		super(uid, ipcon, stackName);
		
//		this.stackName = "MockStack";
		

		
//		MqttPublisher.getInstance().publishDesc(this.toThing());

	}
	
	@Override
	public boolean handleCommand(String commandName, byte[] payload) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Map getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DeviceDescription getDescription() {
		DeviceDescription description = new DeviceDescription("IoT - Mock" , "0.0.1");
		
		// State
		StateDescription stateDescription = new StateDescription();

		
		stateDescription.add("StringTest", new Range<String>("", ""), "");
		stateDescription.add("LongTest", new Range<Long>(1000L, 2000L), "");
		stateDescription.add("DoubleTest", new Range<Double>(0.5, 1.2), "");
		stateDescription.add("BoolTest", new Range<Boolean>(true, false), "");
		
		stateDescription.add("Opt1", new Options<String>("S", "M", "L", "XL"), "");
		
		
		ComplexType tempThreshold = new ComplexType("TemperatureCallbackThreshold");
		tempThreshold.addStringProperty("option");
		tempThreshold.addNumberProperty("min", Short.class);
		tempThreshold.addNumberProperty("max", Short.class);
		
		stateDescription.add("AmbientTemperatureCallbackThreshold", tempThreshold, "TODO"); // TODO
		stateDescription.add("ObjectTemperatureCallbackThreshold",  tempThreshold, "TODO"); // TODO
		description.setStateDescription(stateDescription);

		// Events
		Event event1 = new Event("ObjectTemp", new Range<Double>(-70.0, 380.0), "Measured with IR sensor in Celsius");
		Event event2 = new Event("AmbientTemp", new Options<Integer>(1,2,3), "TODO");
		Event event3 = new Event("complex", tempThreshold, "TODO");

		EventDescription eventDescription = new EventDescription();
		eventDescription.addEvent(event1);
		eventDescription.addEvent(event2);
		eventDescription.addEvent(event3);
		description.setEventDescription(eventDescription);
		
		// Commands
		CommandDescription commandDescription = new CommandDescription();
		Command cmd1 = new Command("setAmbientTemperatureCallbackPeriod");
		cmd1.setLinkedState("AmbientTemperatureCallbackPeriod");
		cmd1.setParam("CallbackPeriod", new Range<Long>(0L, Long.MAX_VALUE));
		commandDescription.addCommand(cmd1);
		
		Command cmd2 = new Command("setObjectTemperatureCallbackPeriod");
		cmd2.setLinkedState("ObjectTemperatureCallbackPeriod");
		cmd2.setParam("abc", new Options<Integer>(2,4,6,8,10));
		commandDescription.addCommand(cmd2);
		
		Command cmd3 = new Command("cmd3");
		cmd3.setParam("CmdComplex", tempThreshold);
		commandDescription.addCommand(cmd3);
		
		description.setCommandDescription(commandDescription);
		
		return description;
	}

	@Override
	public void setUpDevice() {
		this.bricklet = new Device("aaa", new IPConnection()) {
			
			@Override
			public Identity getIdentity() throws TimeoutException, NotConnectedException {
				Identity identity = new Identity();
				identity.uid = "mock";
				
				return identity;
			}
		};
		
	}

}

package ch.bfh.bti7321thesis.tfdemo;

import com.tinkerforge.BrickletTemperatureIR;
import com.tinkerforge.BrickletTemperatureIR.AmbientTemperatureListener;
import com.tinkerforge.BrickletTemperatureIR.ObjectTemperatureListener;
import com.tinkerforge.IPConnection;

public class TemperatureIR {

	private static final String HOST = "localhost";
	private static final int PORT = 4223;
	private static final String UID = "qC1"; // Change to your UID

	public static void main(String args[]) throws Exception {
		IPConnection ipcon = new IPConnection(); // Create IP connection
		BrickletTemperatureIR tir = new BrickletTemperatureIR(UID, ipcon); // Create
																			// device
																			// object

		ipcon.connect(HOST, PORT); // Connect to brickd
		
//		tir.addAmbientTemperatureListener(new AmbientTemperatureListener() {
//			
//			public void ambientTemperature(short temperature) {
//				System.out.println("Ambient Temperature: " + temperature / 10.0 + " °C");
//				
//			}
//		});
		
		System.out.println(tir.getAmbientTemperatureCallbackPeriod());
		tir.setAmbientTemperatureCallbackPeriod(1000);
		
		tir.addObjectTemperatureListener(new ObjectTemperatureListener() {
			
			public void objectTemperature(short temperature) {
				System.out.println("Object Temperature: " + temperature / 10.0 + " °C");
				
			}
		});
		
        tir.setObjectTemperatureCallbackPeriod(100);
		
		
//		// Don't use device before ipcon is connected
//
//		// Get current ambient temperature (unit is °C/10)
//		short ambientTemperature = tir.getAmbientTemperature(); // Can throw
//																// com.tinkerforge.TimeoutException
//		System.out.println("Ambient Temperature: " + ambientTemperature / 10.0 + " °C");
//
//		// Get current object temperature (unit is °C/10)
//		short objectTemperature = tir.getObjectTemperature(); // Can throw

		System.out.println("Press key to exit");
		System.in.read();
		ipcon.disconnect();
	}

}

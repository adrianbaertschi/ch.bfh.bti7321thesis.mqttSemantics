package ch.bfh.bti7321thesis.tfdemo;

import java.io.IOException;
import java.net.UnknownHostException;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.BrickletDualButton;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;
import com.tinkerforge.TimeoutException;

public class DualButton {

	private static final String HOST = "localhost";
	private static final int PORT = 4223;
	private static final String UID = "mxg"; // TODO: UID dynamisch auslesen

	public static void main(String[] args) throws UnknownHostException, AlreadyConnectedException, IOException, NotConnectedException, TimeoutException, InterruptedException {

		IPConnection ipcon = new IPConnection(); // Create IP connection
		BrickletDualButton db = new BrickletDualButton(UID, ipcon); // Create
																	// device
																	// object

		ipcon.connect(HOST, PORT); // Connect to brickd
		// Don't use device before ipcon is connected

		// Add and implement state changed listener
		db.addStateChangedListener(new BrickletDualButton.StateChangedListener() {
			public void stateChanged(short buttonL, short buttonR, short ledL, short ledR) {
				if (buttonL == BrickletDualButton.BUTTON_STATE_PRESSED) {
					System.out.println("Left button pressed");
				} else {
					System.out.println("Left button released");
				}

				if (buttonR == BrickletDualButton.BUTTON_STATE_PRESSED) {
					System.out.println("Right button pressed");
				} else {
					System.out.println("Right button released");
				}

				System.out.println("");
			}
		});
		
		blink(db);
		
		

		System.out.println("Press key to exit");
		System.in.read();
		ipcon.disconnect();
	}
	
	private static void blink(BrickletDualButton db) throws TimeoutException, NotConnectedException, InterruptedException {
		int i = 0;
		while(true) {
			Thread.sleep(100);
			if(++i % 2 == 0 ) {
				db.setLEDState(BrickletDualButton.LED_STATE_OFF, BrickletDualButton.LED_STATE_ON);			
			} else {
				db.setLEDState(BrickletDualButton.LED_STATE_ON, BrickletDualButton.LED_STATE_AUTO_TOGGLE_OFF);
			}
		}
	}

}

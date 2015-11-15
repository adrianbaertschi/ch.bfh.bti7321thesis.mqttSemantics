package ch.bfh.bti7321thesis.tinkerforge;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import com.tinkerforge.AlreadyConnectedException;
import com.tinkerforge.IPConnection;
import com.tinkerforge.NotConnectedException;

public class BrickEnumerator extends Thread {

	private static Logger LOG = Logger.getLogger(BrickEnumerator.class.getName());
	
	private static final int PORT = 4223;

	private String host;

	private IPConnection ipcon;

	public BrickEnumerator(String host) {
		this.host = host;
	}
	
	public void closeConnection() {
		try {
			this.ipcon.disconnect();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void run() {
		LOG.info("Enumerating for host " + host);

		ipcon = new IPConnection();

		ipcon.addEnumerateListener(new HostEnumerateListener(host, ipcon)); 
		try {
			ipcon.connect(host, PORT);
			ipcon.enumerate();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (AlreadyConnectedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (NotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

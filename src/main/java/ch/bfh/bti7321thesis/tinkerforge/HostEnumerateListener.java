package ch.bfh.bti7321thesis.tinkerforge;

import com.tinkerforge.IPConnection.EnumerateListener;

public class HostEnumerateListener implements EnumerateListener {
	
	private String hostname;

	public HostEnumerateListener(String hostname) {
		this.setHostname(hostname);
	}

	public void enumerate(String uid, String connectedUid, char position, short[] hardwareVersion,
			short[] firmwareVersion, int deviceIdentifier, short enumerationType) {
		// TODO Auto-generated method stub

	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

}

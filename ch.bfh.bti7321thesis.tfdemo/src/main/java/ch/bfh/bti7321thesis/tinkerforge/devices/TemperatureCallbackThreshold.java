package ch.bfh.bti7321thesis.tinkerforge.devices;

public class TemperatureCallbackThreshold {
	
	private char option;
	private  short min;
	private  short max;
	
	public char getOption() {
		return option;
	}
	public void setOption(char option) {
		this.option = option;
	}
	public short getMin() {
		return min;
	}
	public void setMin(short min) {
		this.min = min;
	}
	public short getMax() {
		return max;
	}
	public void setMax(short max) {
		this.max = max;
	}

}

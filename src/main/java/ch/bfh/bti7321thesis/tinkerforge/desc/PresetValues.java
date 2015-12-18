package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.Arrays;
import java.util.List;

// TODO: better name
public class PresetValues<T> {
	
	private List<T> values;

	public PresetValues(T... values) {
		this.values = Arrays.asList(values);
	}
	
	public List<T> getValues() {
		return this.values;
	}

}

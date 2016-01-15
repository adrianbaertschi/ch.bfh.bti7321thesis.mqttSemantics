package ch.bfh.bti7321thesis.desc;

import java.util.Arrays;
import java.util.List;

public class Options<T> {
	
	private List<T> values;

	public Options(T... values) {
		this.values = Arrays.asList(values);
	}
	
	public List<T> getValues() {
		return this.values;
	}
	
	public String getType() {
		return values.get(0).getClass().getSimpleName();
	}

}

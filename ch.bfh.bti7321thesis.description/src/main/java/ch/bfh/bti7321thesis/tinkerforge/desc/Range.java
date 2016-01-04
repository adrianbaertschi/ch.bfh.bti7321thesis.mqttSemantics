package ch.bfh.bti7321thesis.tinkerforge.desc;

// TODO?? T extends Comparable oder Number
public class Range<T> {
	
//	private Class<?> type;
	private T min;
	private T max;
	
	public Range(T min, T max) {
//		this.setType(min.getClass());
		this.min = min;
		this.max = max;
	}
	
	public String getType() {
		return min.getClass().getSimpleName();
	}
	
//	public void setType(Class<?> type) {
//		this.type = type;
//	}
	public T getMin() {
		return min;
	}
	public void setMin(T min) {
		this.min = min;
	}
	public T getMax() {
		return max;
	}
	public void setMax(T max) {
		this.max = max;
	}


}

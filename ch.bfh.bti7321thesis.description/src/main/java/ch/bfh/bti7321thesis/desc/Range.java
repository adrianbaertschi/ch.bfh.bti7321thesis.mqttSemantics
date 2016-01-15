package ch.bfh.bti7321thesis.desc;


public class Range<T> {
	
	private T min;
	private T max;
	private String type;
	
	public Range() {
	}
	
	public Range(T min, T max) {
		this.min = min;
		this.max = max;
	}
	
	public String getType() {
		return min.getClass().getSimpleName();
	}
	
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

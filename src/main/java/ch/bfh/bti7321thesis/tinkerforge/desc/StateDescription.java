package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.ArrayList;
import java.util.List;

public class StateDescription {
	
	private final List<State> states = new ArrayList<State>();
	
	public List<State> getStates() {
		return states;
	}

//	public boolean add(String key, Class<?> type) {
//		State state = new State(key, type);
//		return this.states.add(state);
//	}
	
	public boolean add(String key, Object value, Range<?> range, String desc) {
		
		State state = new State(key, value, range, desc);
		return this.states.add(state);
	}

}

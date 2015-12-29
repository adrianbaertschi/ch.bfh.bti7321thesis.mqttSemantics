package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.ArrayList;
import java.util.List;

public class StateDescription {
	
	private final List<State> states = new ArrayList<State>();
	
	public List<State> getStates() {
		return states;
	}

	public boolean add(String key, Object value, Range<?> range, PresetValues<?> presetValues, String desc) {
		State state;
		if(range == null) {
			state = new State(key, value, presetValues, desc);
		} else {
			state = new State(key, value, range, desc);
			
		}
		return this.states.add(state);
	}
	
	

}

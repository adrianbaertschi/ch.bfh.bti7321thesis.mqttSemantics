package ch.bfh.bti7321thesis.tinkerforge.desc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StateDescription {

	private final List<State> states = new ArrayList<State>();
	private final Set<ComplexType> types = new HashSet<ComplexType>();

	public List<State> getStates() {
		return states;
	}

	public boolean add(String key, Range<?> range, String desc) {
		State state = new State(key, range, desc);
		return this.states.add(state);
	}

	public boolean add(String key, PresetValues<?> presetValues, String desc) {
		State state = new State(key, presetValues, desc);
		return this.states.add(state);
	}

	public boolean add(String key, ComplexType complextype, String desc) {

		this.types.add(complextype);

		return this.states.add(new State(key, complextype.getName(), desc));
	}
	
	Set<ComplexType> getTypes() {
		return types;
	}



}

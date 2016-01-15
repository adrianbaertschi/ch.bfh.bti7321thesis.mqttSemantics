package ch.bfh.bti7321thesis.desc.state;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.bfh.bti7321thesis.desc.ComplexType;
import ch.bfh.bti7321thesis.desc.Options;
import ch.bfh.bti7321thesis.desc.Range;

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

	public boolean add(String key, Options<?> options, String desc) {
		State state = new State(key, options, desc);
		return this.states.add(state);
	}

	public boolean add(String key, ComplexType complextype, String desc) {

		this.types.add(complextype);

		return this.states.add(new State(key, complextype.getName(), desc));
	}
	
	public Set<ComplexType> getTypes() {
		return types;
	}



}

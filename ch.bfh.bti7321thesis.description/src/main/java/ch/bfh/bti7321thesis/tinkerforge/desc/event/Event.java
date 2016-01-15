package ch.bfh.bti7321thesis.tinkerforge.desc.event;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ch.bfh.bti7321thesis.tinkerforge.desc.ComplexType;
import ch.bfh.bti7321thesis.tinkerforge.desc.Options;
import ch.bfh.bti7321thesis.tinkerforge.desc.Range;

public class Event {
	private String name;
	private Range<?> range;
	private String desc;
	private Options<?> options;
	
	@JsonIgnore
	private ComplexType complexType;
	//TODO: complextyperef
	
	public Event() {
		
	}

	public Event(String name, Range<?> range, String desc) {
		this.name = name;
		this.range = range;
		this.desc = desc;
	}
	public Event(String name, Options<?> options, String desc) {
		this.name = name;
		this.options = options;
		this.desc = desc;
	}
	
	public Event(String name, ComplexType complexType, String desc) {
		this.name = name;
		this.complexType = complexType;
		this.desc = desc;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Range<?> getRange() {
		return range;
	}

	public void setRange(Range<?> range) {
		this.range = range;
	}

	public String getDescription() {
		return desc;
	}

	public void setDescription(String desc) {
		this.desc = desc;
	}
	public Options<?> getOptions() {
		return options;
	}
	public void setOptions(Options<?> options) {
		this.options = options;
	}
	
	public ComplexType getComplexType() {
		return complexType;
	}
	public void setComplexType(ComplexType complexType) {
		this.complexType = complexType;
	}
	public String getComplexTypeRef() {
		return this.complexType == null ? null : this.complexType.getName();
	}

}
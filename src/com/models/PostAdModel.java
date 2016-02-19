package com.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PostAdModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<String> getCapacityValues() {
		return capacityValues;
	}

	public void setCapacityValues(ArrayList<String> capacityValues) {
		this.capacityValues = capacityValues;
	}

	public ArrayList<String> getControlValues() {
		return controlValues;
	}

	public void setControlValues(ArrayList<String> controlValues) {
		this.controlValues = controlValues;
	}

	public ArrayList<String> getTypeValues() {
		return typeValues;
	}

	public void setTypeValues(ArrayList<String> typeValues) {
		this.typeValues = typeValues;
	}

	private ArrayList<String> capacityValues;
	private ArrayList<String> controlValues;
	private ArrayList<String> typeValues;

}

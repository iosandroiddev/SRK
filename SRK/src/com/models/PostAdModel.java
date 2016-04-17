package com.models;

import java.io.Serializable;

import org.json.JSONArray;

public class PostAdModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fieldName;
	private String fieldTitle;
	private String fieldType;
	private JSONArray values;

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public JSONArray getValues() {
		return values;
	}

	public void setValues(JSONArray values) {
		this.values = values;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

}

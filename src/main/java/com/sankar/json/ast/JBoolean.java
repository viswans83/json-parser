package com.sankar.json.ast;

public abstract class JBoolean extends JValue {

	private Boolean value;
	
	JBoolean(Boolean value) {
		this.value = value;
	}
	
	public Boolean value() {
		return value;
	}
	
	@Override
	public String toJson() {
		return value.toString();
	}
	
}

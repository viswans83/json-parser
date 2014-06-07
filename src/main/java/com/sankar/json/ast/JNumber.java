package com.sankar.json.ast;

public class JNumber extends JValue {
	
	private Double value;
	
	public JNumber(Double value) {
		this.value = value;
	}
	
	public Double value() {
		return value;
	}
	
	@Override
	public String toJson() {
		return value.toString();
	}
	
}

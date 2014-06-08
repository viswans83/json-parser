package com.sankar.json.ast;

import com.sankar.json.JValueVisitor;

public abstract class JBoolean extends JValue {

	private Boolean value;
	
	JBoolean(Boolean value) {
		this.value = value;
	}
	
	public Boolean value() {
		return value;
	}
	
	@Override
	public JBoolean getAsBoolean() {
		return this;
	}
	
	@Override
	public void accept(JValueVisitor visitor) {
		visitor.visitPrimitive(this);
	}
	
	@Override
	public String toJson() {
		return value.toString();
	}
	
}

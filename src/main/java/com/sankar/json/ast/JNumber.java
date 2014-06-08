package com.sankar.json.ast;

import com.sankar.json.JValueVisitor;

public class JNumber extends JValue {
	
	private Double value;
	
	public JNumber(Double value) {
		this.value = value;
	}
	
	public Double value() {
		return value;
	}
	
	@Override
	public JNumber getAsNumber() {
		return this;
	}
	
	@Override
	public void accept(JValueVisitor visitor) {
		visitor.visitPrimitive(this);
	}
	
	@Override
	public String toJson() {
		if (value % 1 != 0)
			return value.toString();
		else
			return ((Integer)value.intValue()).toString();
	}
	
	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof JNumber)) return false;
		return value.equals(((JNumber)other).value);
	}
	
}

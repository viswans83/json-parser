package com.sankar.json.ast;

import com.sankar.json.JValueVisitor;

public class JNull extends JValue {
	
	public static JNull INSTANCE = new JNull();
	
	private JNull() {}
	
	@Override
	public void accept(JValueVisitor visitor) {
		visitor.visitPrimitive(this);
	}
	
	@Override
	public String toJson() {
		return "null";
	}
	
}

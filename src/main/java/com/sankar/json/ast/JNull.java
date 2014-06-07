package com.sankar.json.ast;

public class JNull extends JValue {
	
	public static JNull INSTANCE = new JNull();
	
	private JNull() {}
	
	@Override
	public String toJson() {
		return "null";
	}
	
}

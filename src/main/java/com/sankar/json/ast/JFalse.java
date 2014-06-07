package com.sankar.json.ast;

public class JFalse extends JBoolean {
	
	public static JFalse INSTANCE = new JFalse();
	
	private JFalse() {
		super(false);
	}
	
}

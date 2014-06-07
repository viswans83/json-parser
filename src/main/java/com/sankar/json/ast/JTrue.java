package com.sankar.json.ast;

public class JTrue extends JBoolean {
	
	public static JTrue INSTANCE = new JTrue();

	private JTrue() {
		super(true);
	}
	
}

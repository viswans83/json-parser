package com.sankar.json.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JArray extends JValue {
	
	private List<JValue> values = new ArrayList<JValue>();
	
	public void add(JValue value) {
		values.add(value);
	}
	
	public List<JValue> values() {
		return Collections.unmodifiableList(values);
	}
	
	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("[");
		
		int count = values.size();
		for(JValue value : values) {
			sb.append(value.toJson());
			if (--count != 0) {
				sb.append(",");
			}
		}
		
		sb.append("]");
		
		return sb.toString();
	}
	
}

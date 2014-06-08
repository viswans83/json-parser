package com.sankar.json.ast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.sankar.json.JValueVisitor;

public class JArray extends JValue {
	
	private List<JValue> values = new ArrayList<JValue>();
	
	public void add(JValue value) {
		values.add(value == null ? JNull.INSTANCE : value);
	}
	
	public int size() {
		return values.size();
	}
	
	public JValue remove(int indx) {
		return values.remove(indx);
	}
	
	public JValue get(int indx) {
		return values.get(indx);
	}
	
	public List<JValue> values() {
		return Collections.unmodifiableList(values);
	}
	
	@Override
	public JArray getAsArray() {
		return this;
	}
	
	@Override
	public void accept(JValueVisitor visitor) {
		visitor.visitArrayStart(size(), hasOnlyPrimitives());
		
		Iterator<JValue>  iter = values.iterator();
		while(iter.hasNext()) {
			visitor.visitArrayElement(iter.next());
		}
		
		visitor.visitArrayEnd();
	}
	
	private boolean hasOnlyPrimitives() {
		boolean result = true;
		for(JValue item : values)
			if(item.isArray() || item.isObject())
				result = false;
		
		return result;
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

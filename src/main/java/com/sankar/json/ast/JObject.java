package com.sankar.json.ast;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.sankar.json.JValueVisitor;

public class JObject extends JValue {
	
	private Map<JString,JValue> properties = new LinkedHashMap<JString,JValue>();
	
	public int propertyCount() {
		return properties.size();
	}
	
	public boolean containsKey(String key) {
		return properties.containsKey(new JString(key));
	}
	
	public void put(String key, JValue value) {
		properties.put(new JString(key), value);
	}
	
	public JValue remove(String key) {
		return properties.remove(new JString(key));
	}
	
	public JValue get(String key) {
		return properties.get(new JString(key));
	}
	
	@Override
	public JObject getAsObject() {
		return this;
	}
	
	@Override
	public void accept(JValueVisitor visitor) {
		visitor.visitObjectStart(propertyCount());
		
		Iterator<Entry<JString, JValue>> iter = properties.entrySet().iterator();
		while(iter.hasNext()) {
			Entry<JString, JValue> item = iter.next();
			visitor.visitObjectProperty(item.getKey(), item.getValue());
		}
		
		visitor.visitObjectEnd();
	}
	
	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("{");
		
		int count = properties.size();
		for(Entry<JString,JValue> property : properties.entrySet()) {
			sb.append(property.getKey().toJson());
			sb.append(":");
			sb.append(property.getValue().toJson());
			
			if (--count != 0) {
				sb.append(",");
			}
		}
		
		sb.append("}");
		
		return sb.toString();
	}
	
}

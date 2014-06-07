package com.sankar.json.ast;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class JObject extends JValue {
	
	private Map<JString,JValue> properties = new LinkedHashMap<JString,JValue>();
	
	public void addProperty(JString key, JValue value) {
		properties.put(key, value);
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

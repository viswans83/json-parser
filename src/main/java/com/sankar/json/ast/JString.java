package com.sankar.json.ast;

public class JString extends JValue {
	
	private String value;
	
	public JString(String value) {
		this.value = value;
	}
	
	public String value() {
		return value;
	}
	
	@Override
	public String toJson() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\"");
		for(int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			
			if (ch < 0x20) {
				switch(ch) {
				case '\b': sb.append("\\b"); break;
				case '\f': sb.append("\\f"); break;
				case '\n': sb.append("\\n"); break;
				case '\r': sb.append("\\r"); break;
				case '\t': sb.append("\\t"); break;
				default:
					sb.append("\\u00");
					sb.append(Integer.toHexString(ch));
				}
			} 
			else if (ch == '\\') {
				sb.append("\\\\");
			} 
			else if (ch == '"') {
				sb.append("\\\"");
			}
			else
				sb.append(ch);
		}
		sb.append("\"");
		
		return sb.toString();
	}
	
}

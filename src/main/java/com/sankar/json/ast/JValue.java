package com.sankar.json.ast;

import com.sankar.json.JValueVisitor;

public abstract class JValue {
	
	JValue() {}
	
	public boolean isNull() {
		return this instanceof JNull;
	}
	
	public boolean isObject() {
		return this instanceof JObject;
	}
	
	public boolean isArray() {
		return this instanceof JArray;
	}
	
	public boolean isBoolean() {
		return this instanceof JBoolean;
	}
	
	public boolean isNumber() {
		return this instanceof JNumber;
	}
	
	public boolean isString() {
		return this instanceof JString;
	}
	
	public JObject getAsObject() {
		throw new UnsupportedOperationException();
	}
	
	public JArray getAsArray() {
		throw new UnsupportedOperationException();
	}
	
	public JBoolean getAsBoolean() {
		throw new UnsupportedOperationException();
	}
	
	public JNumber getAsNumber() {
		throw new UnsupportedOperationException();
	}
	
	public JString getAsString() {
		throw new UnsupportedOperationException();
	}
	
	public abstract void accept(JValueVisitor visitor);
	
	public abstract String toJson();
	
}

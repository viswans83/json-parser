package com.sankar.json.ast;


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
		return (JObject)this;
	}
	
	public JArray getAsArray() {
		return (JArray)this;
	}
	
	public JBoolean getAsBoolean() {
		return (JBoolean)this;
	}
	
	public JNumber getAsNumber() {
		return (JNumber)this;
	}
	
	public JString getAsString() {
		return (JString)this;
	}
	
	public abstract String toJson();
	
}

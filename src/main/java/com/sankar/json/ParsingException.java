package com.sankar.json;

@SuppressWarnings("serial")
public class ParsingException extends RuntimeException {
	
	private Token found;
	
	public ParsingException(Token found) {
		this.found = found;
	}
	
	@Override
	public String getMessage() {
		return String.format("Unexpected token found: %s", found.toString());
	}
}

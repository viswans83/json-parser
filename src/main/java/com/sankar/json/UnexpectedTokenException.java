package com.sankar.json;

@SuppressWarnings("serial")
public class UnexpectedTokenException extends RuntimeException {
	
	private Token found;
	
	public UnexpectedTokenException(Token found) {
		this.found = found;
	}
	
	@Override
	public String getMessage() {
		return String.format("Unexpected token found: %s", found.toString());
	}
}

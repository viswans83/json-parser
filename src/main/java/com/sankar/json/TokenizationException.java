package com.sankar.json;

@SuppressWarnings("serial")
public class TokenizationException extends RuntimeException {
	public TokenizationException(String msg) {
		super(msg);
	}
	
	public TokenizationException(String msg, Throwable t) {
		super(msg,t);
	}
}

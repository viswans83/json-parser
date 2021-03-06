package com.sankar.json;

public class Token {
	
	public static final Token OBJECT_START = createToken(TokenType.OBJECT_START);
	
	public static final Token OBJECT_END = createToken(TokenType.OBJECT_END);
	
	public static final Token ARRAY_START = createToken(TokenType.ARRAY_START);
	
	public static final Token ARRAY_END = createToken(TokenType.ARRAY_END);
	
	public static final Token TRUE = createToken(TokenType.TRUE, Boolean.TRUE);
	
	public static final Token FALSE = createToken(TokenType.FALSE, Boolean.FALSE);
	
	public static final Token NULL = createToken(TokenType.NULL);
	
	public static final Token COMMA = createToken(TokenType.COMMA);
	
	public static final Token COLON = createToken(TokenType.COLON);
	
	public static final Token EOF = createToken(TokenType.EOF);
	
	
	private TokenType type;
	
	private Object value;
	
	
	private Token(TokenType type) {
		this.type = type;
	}
	
	private Token(TokenType type, Object value) {
		this.type = type;
		this.value = value;
	}
	
	private static Token createToken(TokenType type) {
		return new Token(type);
	}
	
	private static Token createToken(TokenType type, Boolean value) {
		return new Token(type, value);
	}
	
	public static Token createStringToken(String value) {
		return new Token(TokenType.STRING, value);
	}
	
	public static Token createNumberToken(Double value) {
		return new Token(TokenType.NUMBER, value);
	}
	
	public TokenType type() {
		return type;
	}
	
	public boolean isNull() {
		return this == NULL;
	}
	
	public boolean isBoolean() {
		return this == TRUE || this == FALSE;
	}
	
	public boolean isNumber() {
		return type == TokenType.NUMBER;
	}
	
	public boolean isString() {
		return type == TokenType.STRING;
	}
	
	public boolean booleanValue() {
		return (Boolean)value;
	}
	
	public String stringValue() {
		return (String)value;
	}
	
	public Double numericValue() {
		return (Double)value;
	}
	
	@Override
	public String toString() {
		switch(type) {
		case STRING:
		case NUMBER:
			return String.format("%s [value = %s]", type, value);
		default :
			return String.format("%s", type);
		}
	}
	
}

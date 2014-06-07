package com.sankar.json;

import java.io.StringReader;

import com.sankar.json.ast.JArray;
import com.sankar.json.ast.JFalse;
import com.sankar.json.ast.JNull;
import com.sankar.json.ast.JNumber;
import com.sankar.json.ast.JObject;
import com.sankar.json.ast.JString;
import com.sankar.json.ast.JTrue;
import com.sankar.json.ast.JValue;

public class Parser {
	
	private TokenSource tokenSource;
	
	private Token previous, current, peeked;
	
	public Parser(TokenSource tokenSource) {
		this.tokenSource = tokenSource;
	}
	
	public JValue parse() {
		JValue result = expectValue();
		
		assume(TokenType.EOF);
		
		return result;
	}
	
	public static JValue parse(String input) {
		return new Parser(
			new Tokenizer(
				new StringReader(input)
			)
		).parse();
	}
	
	private JValue expectValue() {
		JValue result;
		
		switch(next().type()) {
		case STRING: 
			result = new JString(current().stringValue()); 
			break;
		case NUMBER: 
			result = new JNumber(current().numericValue()); 
			break;
		case NULL:
			result = JNull.INSTANCE;
			break;
		case TRUE: 
			result = JTrue.INSTANCE;
			break;
		case FALSE: 
			result = JFalse.INSTANCE;
			break;
		case OBJECT_START:
			result = expectObject();
			break;
		case ARRAY_START:
			result = expectArray();
			break;
		default:
			result = error();
		}
		
		return result;
	}
	
	private JObject expectObject() {
		JObject object = new JObject();
		
		if (next().type() == TokenType.OBJECT_END) {
			return object;
		}
		
		pushBack();
		while(peek().type() == TokenType.STRING) {
			JString key = new JString(next().stringValue());
			assume(TokenType.COLON);
			JValue value = expectValue();
			
			object.addProperty(key, value);
			
			if (peek().type() == TokenType.OBJECT_END) {
				break;
			}
			assume(TokenType.COMMA);
		}
		consume();
		
		return object;
	}
	
	private JArray expectArray() {
		JArray array = new JArray();
		
		if (next().type() == TokenType.ARRAY_END) {
			return array;
		}
		
		pushBack();
		while(true) {
			array.add(expectValue());
			if (peek().type() == TokenType.ARRAY_END) {
				break;
			}
			assume(TokenType.COMMA);
		}
		consume();
		
		return array;
	}
	
	private void expect(TokenType expected) {
		if (peek().type() != expected) error();
	}
	
	private void assume(TokenType assumed) {
		expect(assumed);
		next();
	}
	
	private <T> T error() {
		throw new UnexpectedTokenException(current());
	}
	
	private Token current() {
		return current;
	}
	
	private Token next() {
		previous = current;
		if (peeked != null) {
			current = peeked;
			peeked = null;
		} else {
			current = tokenSource.nextToken();
		}
			
		return current;
	}
	
	private void consume() {
		next();
	}
	
	private Token peek() {
		if (peeked != null)
			return peeked;
		else
			return (peeked = tokenSource.nextToken());
	}
	
	private void pushBack() {
		if (peeked == null && current != null) {
			peeked = current;
			current = previous;
			previous = null;
		}
		else
			throw new AssertionError();
	}
	
}

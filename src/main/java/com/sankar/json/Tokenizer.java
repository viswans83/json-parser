package com.sankar.json;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Tokenizer implements TokenSource {
	
	private static final String ERR_UNEXPECTED_CLOSE = "Unexpected close error";

	private static final String ERR_UNEXPECTED_READ = "Unexpected read error";
	
	private static final String ERR_UNEXPECTED_UNREAD = "Unexpected unread error";

	private static final String ERR_UNEXPECTED_SYMBOL = "Unexpected symbol";
	
	private static final String ERR_ASSERTION = "Assertion error";
	
	private PushbackReader reader;
	
	public Tokenizer(Reader reader) {
		this.reader = new PushbackReader(reader);
	}
	
	public static List<Token> tokens(String input) {
		ArrayList<Token> result = new ArrayList<Token>();
		
		Reader reader = new StringReader(input);
		Tokenizer tokenizer = new Tokenizer(reader);
		
		Token tok;
		while((tok = tokenizer.nextToken()) != Token.TOK_EOF) {
			result.add(tok);
		}
		
		return Collections.unmodifiableList(result);
	}
	
	public Token nextToken() {
		consumeWhiteSpace();
		
		int ch = next();
		
		switch(ch) {
		case -1 : return Token.TOK_EOF;
		case '[': return Token.TOK_ARRAY_START;
		case ']': return Token.TOK_ARRAY_END;
		case '{': return Token.TOK_OBJECT_START;
		case '}': return Token.TOK_OBJECT_END;
		case ',': return Token.TOK_COMMA;
		case ':': return Token.TOK_COLON;
		case '"': pushBack(ch); return expectString();
		case 't': pushBack(ch); return expectToken("true", Token.TOK_TRUE);
		case 'f': pushBack(ch); return expectToken("false", Token.TOK_FALSE);
		case 'n': pushBack(ch); return expectToken("null", Token.TOK_NULL);
		case '-':
		case '0':
		case '1':
		case '2':
		case '3':
		case '4':
		case '5':
		case '6':
		case '7':
		case '8':
		case '9': pushBack(ch); return expectNumber();
		default: return error(ERR_UNEXPECTED_SYMBOL);
		}
	}
	
	private void consumeWhiteSpace() {
		int ch;
		while(true) {
			ch = next();
			switch(ch) {
			case ' ' :
			case '\t':
			case '\r':
			case '\n': continue;
			default: 
				pushBack(ch); 
				return;
			}
		}
	}
	
	private Token expectString() {
		expect('"');
		
		StringBuilder sb = new StringBuilder();
		
		boolean done = false;
		boolean escaped = false;
		
		int ch;
		while(true) {
			ch = next();
			
			if (ch < 0x20) error(ERR_UNEXPECTED_SYMBOL);
			
			if(escaped) {
				switch(ch) {
				case 'b': ch = '\b'; break;
				case 'f': ch = '\f'; break;
				case 'n': ch = '\n'; break;
				case 'r': ch = '\r'; break;
				case 't': ch = '\t'; break;
				case 'u': ch = expectUnicode(); break;
				case '"':
				case '\\':
				case '/': break;
				default: error(ERR_UNEXPECTED_SYMBOL);
				}
				escaped = false;
			} 
			else if (ch == '\\') {
				escaped = true;
				continue;
			} 
			else if (ch == '"') {
				done = true;
			}
			
			if(done) 
				break;
			else
				sb.append((char)ch);
		}
		
		return Token.createStringToken(sb.toString());
	}
	
	private int expectUnicode() {
		int remaining = 4;
		int value = 0;
		
		int ch;
		while(remaining > 0) {
			value = value * 16;
			
			ch = next();
			if (ch >= '0' && ch <= '9')
				value = value + (ch - '0');
			else if (ch >= 'a' && ch <= 'f')
				value = value + 10 + (ch - 'a');
			else if (ch >= 'A' && ch <= 'F')
				value = value + 10 + (ch - 'A');
			else
				error(ERR_UNEXPECTED_SYMBOL);
			
			remaining = remaining - 1;
		}
		
		return value;
	}
	
	private Token expectNumber() {
		return Token.createNumberToken(new NumberParser().parse());
	}
	
	private Token expectToken(CharSequence seq, Token returnValue) {
		int index = 0;
		int remaining = seq.length();
		
		while(remaining > 0) {
			int readValue = next();
			if (readValue == seq.charAt(index)) {
				remaining = remaining - 1;
				index = index + 1;
			} else error(ERR_UNEXPECTED_SYMBOL);
		}
		
		return returnValue;
	}
	
	private int next() {
		try {
			return reader.read();
		} catch(IOException e) {
			return error(ERR_UNEXPECTED_READ, e);
		}
	}
	
	private void pushBack(int value) {
		if (value == -1) return;		
		try {
			reader.unread(value);
		} catch(IOException e) {
			error(ERR_UNEXPECTED_UNREAD, e);
		}
	}
	
	private void expect(int value) {
		int read = next();
		
		if (read != value) { 
			error(ERR_ASSERTION);
		}
	}
	
	public void close() {
		try {
			reader.close();
		} catch(IOException e) {
			error(ERR_UNEXPECTED_CLOSE, e);
		}
	}
	
	private <T> T error(String msg) {
		throw new TokenizationException(msg);
	}
	
	private <T> T error(String msg, Throwable t) {
		throw new TokenizationException(msg, t);
	}
	
	private class NumberParser {
		
		private int ch;
		
		private boolean negative;
		private boolean negativePower;
		
		private StringBuilder beforePoint = new StringBuilder();
		private StringBuilder afterPoint = new StringBuilder();
		private StringBuilder power = new StringBuilder();
		
		public Double parse() {
			ch = next();
			if (ch == '-')
				negative = true;
			else 
				pushBack(ch);
			
			afterSign();
			
			return constructNumber();
		}
		
		private void afterSign() {
			ch = next();
			if (ch == '0')
				afterZero();
			else if (ch >= '1' && ch <= '9') {
				beforePoint.append((char)ch);
				digitsBeforePoint();
			}
			else 
				error(ERR_UNEXPECTED_SYMBOL);
		}
		
		private void afterZero() {
			ch = next();
			if (ch == '.') {
				digitsAfterPoint();
			}
		}
		
		private void digitsBeforePoint() {
			while(true) {
				ch = next();
				if (ch >= '0' && ch <= '9') {
					beforePoint.append((char)ch);
				}
				else break;
			}
			
			if (ch == '.')
				digitsAfterPoint();
			else {
				pushBack(ch);
				beforeExponent();
			}
		}
		
		private void digitsAfterPoint() {
			boolean found = false;
			while(true) {
				ch = next();
				if (ch >= '0' && ch <= '9') {
					found = true;
					afterPoint.append((char)ch);
				}
				else break;
			}
			
			if (found) {
				pushBack(ch);
				beforeExponent();
			}
			else
				error(ERR_UNEXPECTED_SYMBOL);
		}
		
		private void beforeExponent() {
			ch = next();
			if (ch == 'e' || ch == 'E') {
				afterExponent();
			}
			else
				pushBack(ch);
		}
		
		private void afterExponent() {
			ch = next();
			if (ch == '-')
				negativePower = true;
			else if (ch == '+')
				negativePower = false;
			else if (ch >= '0' && ch <= '9')
				pushBack(ch);
			else
				error(ERR_UNEXPECTED_SYMBOL);
			
			powerDigits();
		}
		
		private void powerDigits() {
			boolean found = false;
			while(true) {
				ch = next();
				if (ch >= '0' && ch <= '9') {
					found = true;
					power.append((char)ch);
				}
				else break;
			}
			
			if (found) {
				pushBack(ch);
			}
			else
				error(ERR_UNEXPECTED_SYMBOL);
		}
		
		private Double constructNumber() {
			double value = Integer.valueOf(beforePoint.toString());
			
			String ap = afterPoint.toString(); 
			if (ap.length() > 0)
				value = value + (Integer.valueOf(ap) / Math.pow(10, ap.length()));
			
			String pow = power.toString();
			if (pow.length() > 0) {
				if (negativePower)
					value = value / Math.pow(10, Integer.valueOf(pow));
				else
					value = value * Math.pow(10, Integer.valueOf(pow));
			}
			
			return value * (negative ? -1 : 1);
		}
	}
}

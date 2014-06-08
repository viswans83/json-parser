package com.sankar.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sankar.json.ast.JFalse;
import com.sankar.json.ast.JTrue;
import com.sankar.json.ast.JValue;

public class ParserTest {
	
	@Test
	public void testParseSimpleString() {
		JValue result = Parser.parse("\"sankar\"");
		assertTrue(result.isString());
		assertEquals("sankar",result.getAsString().value());
	}
	
	@Test
	public void testParseStringEscapedCharacter() {
		JValue result = Parser.parse("\"\\\",\\\\,\\/,\\b,\\f,\\n,\\r,\\t\"");
		assertTrue(result.isString());
		assertTrue(result.getAsString().value().split(",").length == 8);
	}
	
	@Test(expected = TokenizationException.class)
	public void testParseStringUnicodeNotAllowed() {
		Parser.parse("\\u001A");
	}
	
	@Test
	public void testParseStringUnicodeAllowed() {
		JValue result = Parser.parse("\"\\u005D\"");
		assertEquals('\u005d',result.getAsString().value().charAt(0));
	}
	
	@Test
	public void testParseNumber() {
		JValue result = Parser.parse("-23.05E-1");
		assertTrue(result.isNumber());
		assertEquals((Double)(-2.305),result.getAsNumber().value());
	}
	
	@Test
	public void testParseNamedLiterals() {
		JValue result;
		result = Parser.parse("null");
		assertTrue(result.isNull());
		
		result = Parser.parse("true");
		assertTrue(result instanceof JTrue);
		
		result = Parser.parse("false");
		assertTrue(result instanceof JFalse);
	}
	
	@Test
	public void testParseEmptyObject() {
		JValue result = Parser.parse("{}");
		assertTrue(result.isObject());
		assertTrue(result.getAsObject().propertyCount() == 0);
	}
	
	@Test
	public void testParseEmptyArray() {
		JValue result = Parser.parse("[]");
		assertTrue(result.isArray());
		assertTrue(result.getAsArray().size() == 0);
	}
	
}

package com.sankar.json;

import com.sankar.json.ast.JString;
import com.sankar.json.ast.JValue;

public interface JValueVisitor {
	void visitPrimitive(JValue value);
	void visitObjectStart(int propertyCount);
	void visitObjectProperty(JString key, JValue value);
	void visitObjectEnd();
	void visitArrayStart(int elementCount, boolean hasOnlyPrimitives);
	void visitArrayElement(JValue value);
	void visitArrayEnd();
}

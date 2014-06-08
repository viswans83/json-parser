package com.sankar.json;

import java.util.Deque;
import java.util.LinkedList;

import com.sankar.json.ast.JString;
import com.sankar.json.ast.JValue;

public class PrettyPrinter implements JValueVisitor {
	
	private StringBuilder out = new StringBuilder();
	private Deque<Nesting> nestingLevel = new LinkedList<Nesting>();
	
	public static String toJson(JValue value) {
		PrettyPrinter pp = new PrettyPrinter();
		value.accept(pp);
		return pp.out.toString();
	}
	
	@Override
	public void visitPrimitive(JValue value) {
		out.append(value.toJson());
		if (isNested()) {
			nextLine();
		}
	}
	
	@Override
	public void visitObjectStart(int propertyCount) {
		out.append('{');
		enterNesting(Nesting.OBJECT, propertyCount);
		nextLine();
	}
	
	@Override
	public void visitObjectProperty(JString key, JValue value) {
		currentNesting().entryDown();
		out.append(key.toJson());
		out.append(" : ");
		value.accept(this);
	}
	
	@Override
	public void visitObjectEnd() {
		out.append('}');
		leaveNesting();
		nextLine();
	}
	
	@Override
	public void visitArrayStart(int elementCount) {
		out.append('[');
		enterNesting(Nesting.ARRAY, elementCount);
		nextLine();
	}
	
	@Override
	public void visitArrayElement(JValue value) {
		currentNesting().entryDown();
		value.accept(this);
	}
	
	@Override
	public void visitArrayEnd() {
		out.append(']');
		leaveNesting();
		nextLine();
	}
	
	private void nextLine() {
		if (!isNested()) return;
		
		if (currentNesting().isEmptyStructure()) return;
		
		if (!currentNesting().isJustBegun() && currentNesting().isExpectingMore()) {
			out.append(',');
		}
		
		out.append('\n');
		
		int indentLevel = currentNesting().isExpectingMore() ? indentLevel() : indentLevel() - 1;
		while((indentLevel--) > 0) {
			out.append("  ");
		}
	}
	
	private int indentLevel() {
		return nestingLevel.size();
	}
	
	private Nesting currentNesting() {
		return nestingLevel.peek();
	}
	
	private boolean isNested() {
		return currentNesting() != null;
	}
	
	private void enterNesting(int type, int size) {
		nestingLevel.push(new Nesting(type, size));
	}
	
	private void leaveNesting() {
		nestingLevel.pop();
	}
	
	private class Nesting {
		
		static final int ARRAY = 0;
		static final int OBJECT = 0;
		
		int nestingType;
		int totalEntries;
		int remainingEntries;
		
		Nesting(int nestingType, int remainingEntries) {
			this.nestingType = nestingType;
			this.totalEntries = remainingEntries;
			this.remainingEntries = remainingEntries;
		}
		
		boolean isEmptyStructure() {
			return totalEntries == 0;
		}
		
		boolean isArray() {
			return nestingType == ARRAY;
		}
		
		boolean isObject() {
			return nestingType == OBJECT;
		}
		
		boolean isJustBegun() {
			return totalEntries == remainingEntries;
		}
		
		boolean isExpectingMore() {
			return remainingEntries != 0;
		}
		
		void entryDown() {
			remainingEntries--;
		}
		
	}
	
}

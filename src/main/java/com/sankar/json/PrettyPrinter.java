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
		enterNesting(propertyCount,false);
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
	public void visitArrayStart(int elementCount, boolean hasOnlyPrimitives) {
		out.append('[');
		enterNesting(elementCount,hasOnlyPrimitives);
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
		
		if (!currentNesting().isPrintInSingleLine()) {
			out.append('\n');
			
			int indentLevel = currentNesting().isExpectingMore() ? indentLevel() : indentLevel() - 1;
			while((indentLevel--) > 0) {
				out.append("  ");
			}
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
	
	private void enterNesting(int size, boolean singleLine) {
		nestingLevel.push(new Nesting(size, singleLine));
	}
	
	private void leaveNesting() {
		nestingLevel.pop();
	}
	
	private class Nesting {
		
		int totalEntries;
		int remainingEntries;
		
		boolean showInSingleLine;
		
		Nesting(int remainingEntries, boolean showInsingleLine) {
			this.totalEntries = remainingEntries;
			this.remainingEntries = remainingEntries;
			this.showInSingleLine = showInsingleLine;
		}
		
		boolean isEmptyStructure() {
			return totalEntries == 0;
		}
		
		boolean isJustBegun() {
			return totalEntries == remainingEntries;
		}
		
		boolean isExpectingMore() {
			return remainingEntries != 0;
		}
		
		boolean isPrintInSingleLine() {
			return showInSingleLine;
		}
		
		void entryDown() {
			remainingEntries--;
		}
		
	}
	
}

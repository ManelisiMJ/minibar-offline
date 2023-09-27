package org.grammaticalframework.pgf;

/** This is just a pair of an expression and its type */
public class TypedExpr {
	private final Expr expr;
	private final Type type;

	public TypedExpr(Expr expr, Type type) {
		this.expr = expr;
		this.type = type;
	}

	public Expr getExpr() {
		return expr;
	}

	public Type getType() {
		return type;
	}

	public String toString() {
		return "<"+expr.toString()+" : "+type.toString()+">";
	}
}

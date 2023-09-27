package org.grammaticalframework.pgf;

/** This exception is thrown when parsing a string fails. */
public class ParseError extends Exception {
	private static final long serialVersionUID = -6086991674218306569L;

	private final String  token;
	private final int     offset;
	private final boolean incomplete;

	public ParseError(String token, int offset, boolean incomplete) {
		super(incomplete ? "The sentence is incomplete" : "Unexpected token: \""+token+"\"");
		this.token      = token;
		this.offset     = offset;
		this.incomplete = incomplete;
	}

	public String getToken() {
		return token;
	}

	public int getOffset() {
		return offset;
	}
	
	public boolean isIncomplete() {
		return incomplete;
	}
}

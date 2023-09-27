package org.grammaticalframework.pgf;

import java.util.Iterator;

public interface LiteralCallback {
	CallbackResult match(String ann, int start_offset);

	Iterator<TokenProb> predict(String ann, String prefix);

	class CallbackResult {
		private final ExprProb ep;
		private final int offset;
		
		public CallbackResult(ExprProb ep, int offset) {
			this.ep     = ep;
			this.offset = offset;
		}
		
		public ExprProb getExprProb() {
			return ep;
		}
		
		public int getOffset() {
			return offset;
		}
	}
}

package org.grammaticalframework.pgf;

import java.util.*;

class ExprIterator implements Iterator<ExprProb> {
	private final PGF gr;
	private final Pool pool;
    private final Pool out_pool;
	private final long ref;
	private ExprProb ep;
	private boolean fetched;

	public ExprIterator(PGF gr, Pool pool, long out_pool, long ref) {
		this.gr       = gr;
		this.pool     = pool;
		this.out_pool = new Pool(out_pool);
		this.ref      = ref;
		this.ep       = null;
		this.fetched  = false;
	}

	private native static ExprProb fetchExprProb(long ref, Pool pool, PGF gr);

	private void fetch() {
		if (!fetched) {
			ep = fetchExprProb(ref, out_pool, gr);
			fetched = true;
		}
	}

	public boolean hasNext() {
		fetch();
		return (ep != null);
	}

	public ExprProb next() {
		fetch();
		fetched = false;
		
		if (ep == null)
			throw new NoSuchElementException();
		return ep;
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}
}

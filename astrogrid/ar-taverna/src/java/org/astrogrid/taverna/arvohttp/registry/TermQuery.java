/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

/**a single search tem - a single word.
 * @author Noel Winstanley
 * @since Aug 9, 20062:41:31 PM
 */
class TermQuery extends AbstractQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2972662206449731885L;
	protected String term;
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
}
	public String getTerm() {
		return this.term;
	}
	public void setTerm(String term) {
		this.term = term;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.term == null) ? 0 : this.term.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof TermQuery)) {
			return false;
		}
		final TermQuery other = (TermQuery) obj;
		if (this.term == null) {
			if (other.term != null)
				return false;
		} else if (!this.term.equals(other.term))
			return false;
		return true;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("TermQuery[");
			buffer.append("term = ").append(term);
			buffer.append("]");
			return buffer.toString();
		}
}

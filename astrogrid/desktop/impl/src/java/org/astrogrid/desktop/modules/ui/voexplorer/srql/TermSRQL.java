/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/**SRQL single search tem - a single word.
 * @author Noel Winstanley
 * @since Aug 9, 20062:41:31 PM
 */
public class TermSRQL extends SRQL {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2972662206449731885L;
	protected String term;
	@Override
    public <R> R accept(final SRQLVisitor<R> visitor) {
		return visitor.visit(this);
}
	public String getTerm() {
		return this.term;
	}
	public void setTerm(final String term) {
		this.term = term;
	}
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.term == null) ? 0 : this.term.hashCode());
		return result;
	}
	@Override
    public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (! (obj instanceof TermSRQL)) {
			return false;
		}
		final TermSRQL other = (TermSRQL) obj;
		if (this.term == null) {
			if (other.term != null) {
                return false;
            }
		} else if (!this.term.equals(other.term)) {
            return false;
        }
		return true;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
	
		 */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("TermQuery[");
			buffer.append("term = ").append(term);
			buffer.append("]");
			return buffer.toString();
		}
}

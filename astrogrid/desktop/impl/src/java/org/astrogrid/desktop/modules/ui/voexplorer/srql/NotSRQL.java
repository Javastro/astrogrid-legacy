/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** Negation of another SRQL term.
 * @author Noel Winstanley
 * @since Aug 9, 20062:46:11 PM
 */
public class NotSRQL extends SRQL {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7785830546764343584L;
	protected SRQL child;
	public Object accept(final SRQLVisitor visitor) {
		return visitor.visit(this);
}
	public SRQL getChild() {
		return this.child;
	}
	public void setChild(final SRQL child) {
		this.child = child;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.child == null) ? 0 : this.child.hashCode());
		return result;
	}
	public boolean equals(final Object obj) {
		if (this == obj) {
            return true;
        }
		if (obj == null) {
            return false;
        }
		if (! (obj instanceof NotSRQL)) {
			return false;
		}
		final NotSRQL other = (NotSRQL) obj;
		if (this.child == null) {
			if (other.child != null) {
                return false;
            }
		} else if (!this.child.equals(other.child)) {
            return false;
        }
		return true;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
	
		 */
		public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("NotQuery[");
			buffer.append("child = ").append(child);
			buffer.append("]");
			return buffer.toString();
		}
}

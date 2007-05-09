/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

/**negation of another query
 * @author Noel Winstanley
 * @since Aug 9, 20062:46:11 PM
 */
class NotQuery extends AbstractQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7785830546764343584L;
	protected AbstractQuery child;
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
}
	public AbstractQuery getChild() {
		return this.child;
	}
	public void setChild(AbstractQuery child) {
		this.child = child;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.child == null) ? 0 : this.child.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof NotQuery)) {
			return false;
		}
		final NotQuery other = (NotQuery) obj;
		if (this.child == null) {
			if (other.child != null)
				return false;
		} else if (!this.child.equals(other.child))
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
			buffer.append("NotQuery[");
			buffer.append("child = ").append(child);
			buffer.append("]");
			return buffer.toString();
		}
}

/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

/** logical OR of two subqueries.
 * @author Noel Winstanley
 * @since Aug 9, 20062:44:16 PM
 */
class OrQuery extends BinaryOperatorQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2532521864531858135L;
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.left == null) ? 0 : this.left.hashCode());
		result = PRIME * result + ((this.right == null) ? 0 : this.right.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof OrQuery)) {
			return false;
		}
		final BinaryOperatorQuery other = (BinaryOperatorQuery) obj;
		if (this.left == null) {
			if (other.left != null)
				return false;
		} else if (!this.left.equals(other.left))
			return false;
		if (this.right == null) {
			if (other.right != null)
				return false;
		} else if (!this.right.equals(other.right))
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
			buffer.append("OrQuery[");
			buffer.append("left = ").append(left);
			buffer.append(", right = ").append(right);
			buffer.append("]");
			return buffer.toString();
		}
}


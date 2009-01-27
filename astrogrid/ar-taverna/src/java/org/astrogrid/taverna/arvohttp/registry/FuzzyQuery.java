/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

/** A fuzzy search term
 * @author Noel Winstanley
 * @since Aug 9, 20062:43:48 PM
 */
class FuzzyQuery extends AbstractQuery {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8932519356361768655L;
	protected String value;
	public Object accept(QueryVisitor visitor) {
		return visitor.visit(this);
}
	public String getValue() {
		return this.value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof FuzzyQuery)) {
			return false;
		}
		final FuzzyQuery other = (FuzzyQuery) obj;
		if (this.value == null) {
			if (other.value != null)
				return false;
		} else if (!this.value.equals(other.value))
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
			buffer.append("FuzzyQuery[");
			buffer.append("value = ").append(value);
			buffer.append("]");
			return buffer.toString();
		}
}

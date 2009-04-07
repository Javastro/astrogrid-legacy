/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** Logical OR of two SRQL terms
 * @author Noel Winstanley
 * @since Aug 9, 20062:44:16 PM
 */
public class OrSRQL extends BinaryOperatorSRQL {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2532521864531858135L;
	@Override
    public <R> R accept(final SRQLVisitor<R> visitor) {
		return visitor.visit(this);
}
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.left == null) ? 0 : this.left.hashCode());
		result = PRIME * result + ((this.right == null) ? 0 : this.right.hashCode());
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
		if (! (obj instanceof OrSRQL)) {
			return false;
		}
		final BinaryOperatorSRQL other = (BinaryOperatorSRQL) obj;
		if (this.left == null) {
			if (other.left != null) {
                return false;
            }
		} else if (!this.left.equals(other.left)) {
            return false;
        }
		if (this.right == null) {
			if (other.right != null) {
                return false;
            }
		} else if (!this.right.equals(other.right)) {
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
			buffer.append("OrQuery[");
			buffer.append("left = ").append(left);
			buffer.append(", right = ").append(right);
			buffer.append("]");
			return buffer.toString();
		}
}


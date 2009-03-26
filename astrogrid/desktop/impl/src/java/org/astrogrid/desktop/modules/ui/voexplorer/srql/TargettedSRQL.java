/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** A SRQL clause that is targetted at a particular part of the registry resource.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 19, 20076:49:08 PM
 */
public class TargettedSRQL extends SRQL {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2393097896016240910L;
	@Override
    public Object accept(SRQLVisitor visitor) {
		return visitor.visit(this);
	}
	protected String target;
	protected SRQL child;
	public SRQL getChild() {
		return this.child;
	}
	public void setChild(SRQL child) {
		this.child = child;
	}
	public String getTarget() {
		return this.target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
	
		 */
		@Override
        public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("TargettedQuery[");
			buffer.append("target = ").append(target);
			buffer.append(", child = ").append(child);
			buffer.append("]");
			return buffer.toString();
		}
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.child == null) ? 0 : this.child.hashCode());
		result = PRIME * result + ((this.target == null) ? 0 : this.target.hashCode());
		return result;
	}
	@Override
    public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TargettedSRQL)) 
			return false;
		final TargettedSRQL other = (TargettedSRQL) obj;
		if (this.child == null) {
			if (other.child != null)
				return false;
		} else if (!this.child.equals(other.child))
			return false;
		if (this.target == null) {
			if (other.target != null)
				return false;
		} else if (!this.target.equals(other.target))
			return false;
		return true;
	}
}

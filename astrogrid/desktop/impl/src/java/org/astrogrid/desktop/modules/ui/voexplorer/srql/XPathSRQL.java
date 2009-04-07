/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** An inline xpath in SRQL.
 * The xpath is expected to return a boolean
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Feb 19, 20077:22:52 PM
 */
public class XPathSRQL extends SRQL {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1849502401267001744L;

	@Override
    public <R> R accept(final SRQLVisitor<R> visitor) {
		return visitor.visit(this);
	}
	
	private String xpath;

	public String getXpath() {
		return this.xpath;
	}

	public void setXpath(final String xpath) {
		this.xpath = xpath;
	}

	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
	
		 */
		@Override
        public String toString() {
			final StringBuffer buffer = new StringBuffer();
			buffer.append("XPathQuery[");
			buffer.append("xpath = ").append(xpath);
			buffer.append("]");
			return buffer.toString();
		}

	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.xpath == null) ? 0 : this.xpath.hashCode());
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
		if (! (obj instanceof XPathSRQL)) {
            return false;
        }
		final XPathSRQL other = (XPathSRQL) obj;
		if (this.xpath == null) {
			if (other.xpath != null) {
                return false;
            }
		} else if (!this.xpath.equals(other.xpath)) {
            return false;
        }
		return true;
	}
	
	

}

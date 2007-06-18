/**
 * 
 */
package org.astrogrid.desktop.modules.ui.voexplorer.srql;

/** a search phrase - delimited by " "
 * @author Noel Winstanley
 * @since Aug 9, 20062:41:45 PM
 */
public class PhraseSRQL extends SRQL {

	/**
	 * 
	 */
	private static final long serialVersionUID = -74242873320481568L;
	protected String phrase;
	public Object accept(SRQLVisitor visitor) {
		return visitor.visit(this);
}
	public String getPhrase() {
		return this.phrase;
	}
	public void setPhrase(String phrase) {
		this.phrase = phrase;
	}
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.phrase == null) ? 0 : this.phrase.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (! (obj instanceof PhraseSRQL)) {
			return false;
		}
		final PhraseSRQL other = (PhraseSRQL) obj;
		if (this.phrase == null) {
			if (other.phrase != null)
				return false;
		} else if (!this.phrase.equals(other.phrase))
			return false;
		return true;
	}
	/**
		 * toString methode: creates a String representation of the object
		 * @return the String representation
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("PhraseQuery[");
			buffer.append("phrase = ").append(phrase);
			buffer.append("]");
			return buffer.toString();
		}
}

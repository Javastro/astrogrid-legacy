/**
 * 
 */
package org.astrogrid.taverna.arvohttp.registry;

/** a search phrase - delimited by " "
 * @author Noel Winstanley
 * @since Aug 9, 20062:41:45 PM
 */
class PhraseQuery extends AbstractQuery {

	/**
	 * 
	 */
	private static final long serialVersionUID = -74242873320481568L;
	protected String phrase;
	public Object accept(QueryVisitor visitor) {
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
		if (! (obj instanceof PhraseQuery)) {
			return false;
		}
		final PhraseQuery other = (PhraseQuery) obj;
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
		 * @author info.vancauwenberge.tostring plugin
	
		 */
		public String toString() {
			StringBuffer buffer = new StringBuffer();
			buffer.append("PhraseQuery[");
			buffer.append("phrase = ").append(phrase);
			buffer.append("]");
			return buffer.toString();
		}
}

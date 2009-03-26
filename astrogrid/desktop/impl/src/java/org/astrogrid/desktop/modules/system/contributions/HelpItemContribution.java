/**
 * 
 */
package org.astrogrid.desktop.modules.system.contributions;

import java.net.MalformedURLException;
import java.net.URL;

/** A single help item contribution.
 * @author Noel Winstanley
 * @since Jan 11, 200712:44:44 PM
 */
public class HelpItemContribution {
	private String id;
	private URL url;
	public final static String HOME_ID = "top";
	public String getId() {
		return this.id;
	}
	public void setId(final String id) {
		this.id = id;
	}
	// necessary to have this a different name to the String setter for this propery - 
	// otherwise hivemind thinks it needs to convert the configuration to a URL - and fails.
	public URL getUrlObject() {
		return this.url;
	}
	public void setUrl(final String url) throws MalformedURLException {
		this.url = new URL(url);
	}
	@Override
    public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
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
		if (getClass() != obj.getClass()) {
            return false;
        }
		final HelpItemContribution other = (HelpItemContribution) obj;
		if (this.id == null) {
			if (other.id != null) {
                return false;
            }
		} else if (!this.id.equals(other.id)) {
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
			buffer.append("HelpItemContribution[");
			buffer.append("id = ").append(id);
			buffer.append(", url = ").append(url);
			buffer.append("]");
			return buffer.toString();
		}

}

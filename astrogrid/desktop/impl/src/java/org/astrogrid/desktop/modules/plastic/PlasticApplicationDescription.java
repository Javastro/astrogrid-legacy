/**
 * 
 */
package org.astrogrid.desktop.modules.plastic;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/** Description of an application that's connected via plastic.
 * 
 * @author John Taylor
 * @modified Noel Winstanley - renamed to remove ambiguity. added some extra info methods, 
 * strengthened types (more pressing if used by more clients)
 * @since Jun 16, 20061:19:37 PM
 */
public class PlasticApplicationDescription {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
			.getLog(PlasticApplicationDescription.class);

	// member variables never change.
	// interesting - could there ever be a case where a plastic app changes description / supported methods
	// whilst it's running. nnng. leave for now..
	private final URI id;
	private final String name;
	private final URI[] messages;
	private final String version;
	private final URI ivorn;
	private final URL iconURL;
	private final ImageIcon icon;
	private final String description;

	public PlasticApplicationDescription(final URI id, final String name,final String description, final List messages, final String version, final ImageIcon icon, final URL iconURL, final String ivorn) {
		this.id = id;
		this.name = name;
		this.description = description;
		if (messages != null) {
			this.messages = (URI[])messages.toArray(new URI[messages.size()]);
		} else { // dunno if this is ever going to happen really.
			this.messages = new URI[]{};
		}
		this.version = version;
		this.icon = icon;
		this.iconURL = iconURL;
		URI uri = null;
		try {
			uri = ivorn == null ? null : new URI(ivorn);
		} catch (final URISyntaxException x) {
			logger.warn("Malformed application ivorn",x);
		}
		this.ivorn = uri;
	}
	
	
	public PlasticApplicationDescription(final URI id, final String name, final String description, final URI[] messages, final String version, final ImageIcon icon, final URL iconURL, final URI ivorn) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.messages = messages;
		this.version = version;
		this.icon = icon;
		this.ivorn = ivorn;
		this.iconURL = iconURL;
	}

	public URI getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public URI[] getUnderstoodMessages() {
		return messages;
	}

	public String getVersion() {
		return version;
	}


	public URI getIvorn() {
		return ivorn;
	}
	
	public URL getIconUrl() {
		return iconURL;
	}


	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	
	//NWW additions.
	/** helper method to quickly check for support of a message 
	 * @return true if message in {@link #getUnderstoodMessages()} false otherwise, or if
	 * <tt>message</tt> == null*/
	public boolean understandsMessage(final URI message) {
		if (message == null) {
			return false;
		}
		return ArrayUtils.contains(getUnderstoodMessages(),message);
	}

	public ImageIcon getIcon() {
		return icon;
	}


	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.id == null) ? 0 : this.id.hashCode());
		return result;
	}

	/** equality is defined on {@link #getId} */
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
		final PlasticApplicationDescription other = (PlasticApplicationDescription) obj;
		if (this.id == null) {
			if (other.id != null) {
                return false;
            }
		} else if (!this.id.equals(other.id)) {
            return false;
        }
		return true;
	}
	
}
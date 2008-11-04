/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.URL;

import org.astrogrid.acr.system.WebServer;
import org.astrogrid.desktop.modules.util.Selftest;

/** Internal interface to webserver - adds methods necessary for providing sessions
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Mar 21, 20076:10:14 PM
 */
public interface WebServerInternal extends WebServer, Selftest {

	/** clones the default servlet context to this sessionId
	 * means that any URL requests with this id in the path get mapped to the 
	 * equivalent servlet in the default context.
	 * @param sessionId
	 * @return the root URL of this context
	 */
	URL createContext(String sessionId) ;
	
	/** removes a context from the webserver */
	void dropContext(String sessionId);
	
	
	/**get the base url for a context
	 * @return the equivalent baseurl, or null if the session does not have a context associated  */
	URL getContextBase(String sessionId);
}

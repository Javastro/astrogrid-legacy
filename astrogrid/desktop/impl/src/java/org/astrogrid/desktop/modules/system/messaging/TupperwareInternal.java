/**
 * 
 */
package org.astrogrid.desktop.modules.system.messaging;

import java.net.URI;
import java.util.List;

import javax.swing.Action;

import org.apache.xmlrpc.XmlRpcHandler;

/**
 * Tupperware is a plastic container that keeps things fresh.
 * 
 * The AR registeres as a single application with the Plastic Hub. This application
 * acts as a container for the remotely-available plastic apps (topcat, etc).
 * It maintains a store of currently available applications.
 * @see <a href="http://plastic.sourceforge.net">Plastic Homepage</a> 
 * @author Noel Winstanley
 * @since Jun 5, 20063:11:05 PM
 */
public interface TupperwareInternal extends XmlRpcHandler {

	// send messages.
	/** send a message to a single named application */
	public Object singleTargetRequestResponseMessage(URI message, List<Object> args, URI target) ;
	
	/** send a messagge to a single named application, don't wait for response */
	public void singleTargetFireAndForgetMessage(URI message, List<Object> args, URI target);
	
	
	/** access an action that can be used to connect to a plastic hub */
	public Action connectAction();
	
	/** access an action that can be used to disconnect from a hub */
	public Action disconnectAction();
	
	/** access an action that can be used to start an internal hub */
	public Action startInternalHubAction();


	
}

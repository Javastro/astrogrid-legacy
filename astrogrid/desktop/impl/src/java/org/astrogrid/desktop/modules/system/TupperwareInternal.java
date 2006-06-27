/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import java.net.URI;
import java.util.List;
import java.util.Map;

import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import org.astrogrid.desktop.modules.plastic.PlasticApplicationDescription;
import org.votech.plastic.incoming.handlers.MessageHandler;

/**
 * Tupperware is a plastic container that keeps things fresh.
 * 
 * The AR registeres as a single application with the Plastic Hub. This application
 * acts as a container for the remotely-available plastic apps (topcat, etc).
 * It maintains a store of currently available applications.
 * @see http://plastic.sourceforge.net
 * @author Noel Winstanley
 * @since Jun 5, 20063:11:05 PM
 */
public interface TupperwareInternal {

	// send messages.
	/** send a message to a single named application */
	public Object singleTargetPlasticMessage(URI message, List args, URI target) ;
	
	
	/** access a list model that contains {@link PlasticApplicationDescription} objects
	 * for each registered application. */
	public ReportingListModel getRegisteredApplicationsModel();

	
	public Map broadcastPlasticMessage(URI message, List args) ;
	
	public void  broadcastPlasticMessageAsynch(URI message, List args) ;
}

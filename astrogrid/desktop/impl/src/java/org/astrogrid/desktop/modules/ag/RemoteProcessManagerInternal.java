/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.workflow.beans.v1.Tool;
import org.w3c.dom.Document;

/** Internal interface to remote process manager which 
 * provides access to a process monitor
 *
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 200712:58:48 PM
 */
public interface RemoteProcessManagerInternal extends RemoteProcessManager {

	/** find a process monitor associated with this execution id
	 * @return a prociess monitor, or null if no associated one can be found*/
	ProcessMonitor findMonitor(URI id) ;
	
	/** delete a process monitor - final cleanup action */
	void delete(ProcessMonitor pm);
}

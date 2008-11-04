/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.w3c.dom.Document;

/** Internal interface that extends {@code RemoteProcessManager} to provide access to a {@code ProcessMonitor}. 

 *
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 200712:58:48 PM
 */
public interface RemoteProcessManagerInternal extends RemoteProcessManager {

    /** create a fresh, un-init'ed process monitor
     * This allows a step-by-step execution of a remote task - 
     * providing space for progress reporting and UI feedback.
     * For purely programmatic execution, prefer the {@link RemoteProcessManager#submit} methods
     * @throws InvalidArgumentException iif document is not a known type.
     * @return an uninitialized process monitor. never null
     * @throws ServiceException if connection to services fails.
     */
    ProcessMonitor create(Document doc) throws InvalidArgumentException, ServiceException, NotFoundException;
    
	/** find a running process monitor associated with this execution id
	 * @return a prociess monitor, or null if no associated one can be found*/
	ProcessMonitor findMonitor(URI id) ;

	/** register a process monitor (i.e. one that's been created from the 'create' method
	 * with the internal list of process monitors.
	 * can only be called after processMonitor.start() has initialized the process
	 */
	void addMonitor(ProcessMonitor pm);
	
	/** delete a process monitor - final cleanup action */
	void delete(ProcessMonitor pm);
}

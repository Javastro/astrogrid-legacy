/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.workflow.beans.v1.Tool;

/** Internal interface to remote process manager which 
 * provides access to a process monitor
 * -- note that this interface is CEA-specific, and so covers the external CEA services
 * and internal cea (which wraps cone, siap, etc). But JES can't be submitted this way
 * (and it's deprecated anyhow).
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 200712:58:48 PM
 */
public interface RemoteProcessManagerInternal extends RemoteProcessManager {
	/**
	 * submit a tool document for execution.
	 * @param t the tool document to exeucte
	 * @return a process monitor attached to the remote execution.
	 * @throws ServiceException if an error occurs communicating with servers
	 * @throws SecurityException if user is prevented from executing this tool
	 * @throws NotFoundException if no service that can execute this tool is found
	 * @throws InvalidArgumentException if the document is malformed in some way.
	 * @see #delete(ProcessMonitor) to cleanup after use
	 */
	ProcessMonitor submit(Tool t) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;
    
	/**
	 * submit a tool for execution on a named server
	 * @param t the document to execute.
	 * @param s the service to execute on
	 * @return a process montor attached to the remote execution.
	 * @throws InvalidArgumentException if the tool document is malformed, or the server inappropriate
	 * @throws ServiceException if an error occurs communicating with the server
	 * @throws SecurityException if user is prevented from executing this application
	 * @see #delete(ProcessMonitor) to cleanup after use
	 * 	 */
	ProcessMonitor submitTo(Tool t, Service s)  throws InvalidArgumentException, ServiceException, SecurityException;

	
	/** delete a process monitor - final cleanup action */
	void delete(ProcessMonitor pm);
}

/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import org.apache.commons.vfs.FileSystem;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.workflow.beans.v1.Tool;

/** Monitors the progress of a remote process.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 20071:10:27 PM
 */
public interface ProcessMonitor {
// informatin methods
	/** get the execution id for the remote process
	 * only returns a valid execution ID after {@link #start} has been called.
	 * before init, will return an {@link IllegalStateException}
	 *  */
	public URI getId() throws IllegalStateException;

	/** returns true if this process has started, or at least attempted
	 * to start and got far enough to be assigned an id.
	 * @return
	 */
	public boolean started();
	
// control methods.
	/** start the remote process running on the specified service
	 * this will have meaning for some kinds of remote process (cea), but not
	 * for others (JES, siap), where this method should be implemented by 
	 * redirecting to {@link #start()}
	 * @throws ServiceException on failure to connect to remote service
	 * @throws NotFoundException when no server providing this application is found
	 */
	public void start(URI serviceId) throws ServiceException, NotFoundException;
	/** start the remote process running 
     * @throws ServiceException on failure to connect to remote service
     * @throws NotFoundException when no server providing this application is found
     */
	public void start() throws ServiceException, NotFoundException;
	
	/** refresh progress information */
	public void refresh() throws ServiceException;
	
	/** halt the process */
	public void halt() throws NotFoundException, InvalidArgumentException,
			ServiceException, SecurityException;

// query methods.	
	/** access execution messages returned from the process */
	public ExecutionMessage[] getMessages() throws NotFoundException,
			ServiceException;

	/** access execution information - by default uses fields in this object. */
	public ExecutionInformation getExecutionInformation()
			throws NotFoundException, InvalidArgumentException,
			ServiceException, SecurityException;
	
	public String getStatus();


	/** access the results of the remote process.
	 * this might return an incomplete or empty map if the process has not yet 
	 * finished.
	 * @return
	 * @throws ServiceException
	 * @throws SecurityException
	 * @throws NotFoundException
	 * @throws InvalidArgumentException
	 */
	public Map getResults() throws ServiceException, SecurityException,
			NotFoundException, InvalidArgumentException;

	
	/** access the result of the remote process as a filesystem
	 * this can then be displayed,  */
	FileSystem getResultsFileSystem(); 
// listener methods
	// remote listener interface - used by remote RMI clients.
	public void addRemoteProcessListener(RemoteProcessListener listener);
	public void removeRemoteProcessListener(RemoteProcessListener listener);
	
	// public listener inteface - use this for in-jvm process listeneing.
	public void addProcessListener(ProcessListener listener);

	public void removeProcessListener(ProcessListener listener);

	/** an optional aditional interface that process monitors may implement to 
	 * provide more information
	 * @author Noel.Winstanley@manchester.ac.uk
	 * @since Jul 24, 200711:12:48 AM
	 */
	public static interface Advanced extends ProcessMonitor {
	    /** returns the tool document (or a facsimile of it) which was used to invoke the 
	     * process
	     * @return
	     */
	    Tool getInvocationTool();
	    
	}

	/** event listener for notification of process progress */
	public static interface ProcessListener extends EventListener {
		public void messageReceived(ProcessEvent ev);
		public void resultsReceived(ProcessEvent ev);
		public void statusChanged(ProcessEvent ev);
	}
	/** event object for process progress */
	public static class ProcessEvent extends EventObject {

		/**
		 * @param source
		 */
		public ProcessEvent(final Object source) {
			super(source);
		}
	}
}
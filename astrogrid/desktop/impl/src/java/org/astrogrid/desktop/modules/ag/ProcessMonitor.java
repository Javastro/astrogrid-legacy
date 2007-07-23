/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.util.EventListener;
import java.util.EventObject;
import java.util.Map;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;

/** Inteface to a remote process.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 16, 20071:10:27 PM
 */
public interface ProcessMonitor {

	/** get the execution id for the remote process */
	public URI getId();

	/** halt the process */
	public void halt() throws NotFoundException, InvalidArgumentException,
			ServiceException, SecurityException;

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

	// remote listener interface - used by remote RMI clients.
	public void addRemoteProcessListener(RemoteProcessListener listener);
	public void removeRemoteProcessListener(RemoteProcessListener listener);
	
	// public listener inteface - use this for in-jvm process listeneing.
	public void addProcessListener(ProcessListener listener);

	public void removeProcessListener(ProcessListener listener);

	
	
	
	public static interface ProcessListener extends EventListener {
		public void messageReceived(ProcessEvent ev);
		public void resultsReceived(ProcessEvent ev);
		public void statusChanged(ProcessEvent ev);
	}
	
	public static class ProcessEvent extends EventObject {

		/**
		 * @param source
		 */
		public ProcessEvent(Object source) {
			super(source);
		}
	}
}
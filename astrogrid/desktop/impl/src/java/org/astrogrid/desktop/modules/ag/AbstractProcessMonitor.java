/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;

/** abstract class that's an interface to a componoent that monitors the progress of a remote process.
 * and also possibly accumulates results and messages, and holds references to resources
 * used by running this task.
 * 
 * A callback driven monitor can extend this class directly, ovrriding the basic implementation as needed.
 * For a monitor that polls - driven by a timer - see {@link TimerDrivenProcessMonitor}
 * 
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Jul 12, 20071:37:05 PM
 */
public abstract class AbstractProcessMonitor implements ProcessMonitor {
	private final URI id;
	public AbstractProcessMonitor(URI id) {
		this.id = id;
	}
	/** get the execution id for the remote process */
	public final URI getId() {
		return id;
	}
	
	/** halt the process */
	public abstract void halt() throws NotFoundException, InvalidArgumentException,
    ServiceException, SecurityException;
	
	/** access execution messages returned from the process */
	public ExecutionMessage[] getMessages() throws NotFoundException,     ServiceException {
		return (ExecutionMessage[])messages.toArray(new ExecutionMessage[messages.size()]);
	}
	protected java.util.List messages = new ArrayList();
	
	/** helper method for default implementaiton of getMessages(),
	 * @fires {@link #fireMessageReceived(ExecutionMessage)}
	 * @param m
	 */
	protected void addMessage(ExecutionMessage m) {
		messages.add(m);
		fireMessageReceived(m);
	}


	/** access execution information - by default uses fields in this object. */
	public ExecutionInformation getExecutionInformation()  throws NotFoundException, InvalidArgumentException,
    ServiceException, SecurityException {
		return new ExecutionInformation(getId(),name,description,status,startTime,finishTime);
	}
	
	// helper method to signal an error.
	protected final void signalError(String msg) {
		ExecutionMessage em = new ExecutionMessage(getId().toString()
				,LogLevel.ERROR.toString()
				,getStatus()
				,new Date()
				,msg);
		addMessage(em);
		setStatus(ExecutionPhase.ERROR.toString());
	}	
	
	
	// mutable member variables - can be adjusted at will.
	protected String name;
	protected String description;
	private String status = ExecutionInformation.UNKNOWN;
	protected java.util.Date startTime = new Date();
	protected java.util.Date finishTime;
	
	public String getStatus() {
		return status;
	}
	
	/** set the status
	 * @fires {@link #fireStatusChanged(String)}
	 * @param newStatus
	 */
	public final void setStatus(String newStatus) {
		if (! status.equals(newStatus)) {
			status = newStatus;
			fireStatusChanged(status);
		}
	}
	
		
	/** access the results of the remote process.
	 * this might return an incomplete or empty map if the process has not yet 
	 * finished.
	 * @return
	 * @throws ServiceException
	 * @throws SecurityException
	 * @throws NotFoundException
	 * @throws InvalidArgumentException
	 */
    public Map getResults() throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException{
    	return resultMap;
    }
    
    
    
    protected Map resultMap = new HashMap();
    /* helper method for default implementation of getResults - not that this does not fire
     * an event.
     */
    protected void addResult(String resultname,Object result) {
    	resultMap.put(resultname,result);
    }
	/** clean up - override to remove any associated resources, in part of deleting this
	 * howver, always call <tt>super.cleanUp()</tt> which will  deregister all listeners.
	 *  */
	public void cleanUp() {
		listeners.clear();
	}
    
    private final Set listeners = new HashSet();
	
    protected void fireMessageReceived(ExecutionMessage m) {
    	for (Iterator i = listeners.iterator(); i.hasNext();) {
			RemoteProcessListener rpl = (RemoteProcessListener) i.next();
			rpl.messageReceived(id,m);
		}
    }
    
    protected void fireResultsReceived(Map m) {
    	for (Iterator i = listeners.iterator(); i.hasNext();) {
			RemoteProcessListener rpl = (RemoteProcessListener) i.next();
			rpl.resultsReceived(id,m);
		}
    }
    
    protected void fireStatusChanged(String s) {
    	for (Iterator i = listeners.iterator(); i.hasNext();) {
			RemoteProcessListener rpl = (RemoteProcessListener) i.next();
			rpl.statusChanged(id,s);
		}
    }
    
    
    
	// public listener interace
	public void addRemoteProcessListener(RemoteProcessListener listener) {
		listeners.add(listener);
	}
	
	public void removeRemoteProcessListener(RemoteProcessListener listener) {
		listeners.remove(listener);
	}
}
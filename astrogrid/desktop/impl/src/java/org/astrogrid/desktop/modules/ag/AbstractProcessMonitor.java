/**
 * 
 */
package org.astrogrid.desktop.modules.ag;

import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.event.EventListenerList;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystem;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.FileUtil;
import org.apache.commons.vfs.Selectors;
import org.apache.commons.vfs.provider.AbstractFileSystem;
import org.apache.commons.vfs.util.FileObjectUtils;
import org.apache.tools.ant.util.FileUtils;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.desktop.modules.ag.ProcessMonitor.ProcessListener;
import org.astrogrid.desktop.modules.ivoa.resource.HtmlBuilder;
import org.astrogrid.io.Piper;

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
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(AbstractProcessMonitor.class);

	private URI id;
	private final FileSystemManager vfs;
	protected FileSystem sys;
    private FileObject localResultsRoot;
	public AbstractProcessMonitor(FileSystemManager vfs) {
		this.vfs = vfs;
        this.id = UNINITIALIZED;
	}
	public static final URI UNINITIALIZED = URI.create("uninitialized:/task/");
	
	public boolean started() {
	    return id != UNINITIALIZED;
	}
	
	/** get the execution id for the remote process */
	public final URI getId() throws IllegalStateException {
	    if (id == UNINITIALIZED) {
	        throw new IllegalStateException("Process ID not set - as start() has not yet been called");
	    } else {
	        return id;
	    }
	}
	
	/** set the execution id - will throw if id has already been set
	 * also initiates the results storage.
	 *  
	 *  */
	protected synchronized final void setId(URI id) throws RuntimeException{
	    if (this.id != UNINITIALIZED) {
	        throw new IllegalStateException("Process ID has already been set");
	    }
	    if (id == null) {
	        throw new IllegalArgumentException("Supplied ID is null");
	    }
	    this.id = id;	    
        try {
            sys = vfs.createVirtualFileSystem("monitor://").getFileSystem();
            localResultsRoot = vfs.resolveFile("tmp://" + URLEncoder.encode(id.toString()));
        } catch (FileSystemException x) {
            throw new RuntimeException("Not expected to fail",x);
        }	    
	}

	
	/** access execution messages returned from the process */
	public ExecutionMessage[] getMessages() throws NotFoundException,     ServiceException {
		return (ExecutionMessage[])messages.toArray(new ExecutionMessage[messages.size()]);
	}
	
	public FileSystem getResultsFileSystem() {
	    return sys;
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
	
	protected void info(String message) {
        ExecutionMessage em = new ExecutionMessage(MONITOR_MESSAGE_SOURCE
                ,LogLevel.INFO.toString()
                ,getStatus()
                ,new Date()
                ,message);
        addMessage(em);
	}
	
    protected void warn(String message) {
        ExecutionMessage em = new ExecutionMessage(MONITOR_MESSAGE_SOURCE
                ,LogLevel.WARN.toString()
                ,getStatus()
                ,new Date()
                ,message);
        addMessage(em);
    }
    
    public static final String MONITOR_MESSAGE_SOURCE = "monitor";

    


	/** access execution information - by default uses fields in this object. */
	public ExecutionInformation getExecutionInformation()  throws NotFoundException, InvalidArgumentException,
    ServiceException, SecurityException {
		return new ExecutionInformation(this.id,name,description,status,startTime,finishTime);
	}
	
	// helper method to signal an error, record a message, change status to error, and fire a notification
	protected final void error(String msg) {
		ExecutionMessage em = new ExecutionMessage(MONITOR_MESSAGE_SOURCE
				,LogLevel.ERROR.toString()
				,getStatus()
				,new Date()
				,msg);
		addMessage(em);
		setStatus(ExecutionPhase.ERROR.toString());
	}	
	
	// helper method to signal an error, extracting useful information from an exception
	// use the messages from the  last exception in the cause chain.
	protected final void error(String msg,Throwable t) {
	    HtmlBuilder sb = new HtmlBuilder();
	    sb.append(msg);
	 //   sb.br().append(t.getMessage());
	    while(t.getCause() != null) {
	        t = t.getCause();
	    }
	    sb.br().append(t.getMessage());
	    error(sb.toString());
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
    /** helper method for default implementation of getResults - note that this does not fire
     * an event.
     * 
     * adds result into map, and presents result as a file in the file view.
     * 
     * returns the temporary file containing this result - whiose basename will == resultname
     */
    protected void addResult(String resultname,String result) {
        OutputStream os = null;
        InputStream is = null;
        try {
            FileObject tmp = vfs.resolveFile(localResultsRoot,resultname);
            tmp.createFile();
            is = new ByteArrayInputStream(result.getBytes());
            os = tmp.getContent().getOutputStream();
            Piper.pipe(is,os);
            sys.addJunction(resultname,tmp);
            resultMap.put(resultname,result); // doh! don't really want to cache value in memory too, but can't be helped - map is passed back throught eh RemoteProcessManager AR api, so can't contain any weirdness.
        } catch (Exception x) {
            throw new RuntimeException("Unexpected storage error",x);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }  
        }
    }
    /** helper methods that adds a remote result.
     * this result is mounted in the filesystem view, and the resultMap contains
     * the <b>URI</b> of this file.
     * @param resultname
     * @param resultLocation
     * @throws FileSystemException 
     */
    protected void addResult(String resultname,FileObject resultLocation) throws FileSystemException {
        if (! resultLocation.isAttached()) {
            resultLocation.getType(); // forces attachment, meaning that contacting filesystem happens on this bg thread, not on EDT thread
            //@todo need to verify that this is enough.
        }
       // will use native result name instead sys.addJunction(resultname,resultLocation);
        sys.addJunction(resultLocation.getName().getBaseName(),resultLocation);
        resultMap.put(resultname,resultLocation.getURL());
    }
    
	/** clean up - override to remove any associated resources, in part of deleting this
	 * howver, always call <tt>super.cleanUp()</tt> which will  deregister all listeners.
	 *  */
	public void cleanUp() {
	    listeners = null;
	    resultMap.clear();
	    ((AbstractFileSystem)sys).close();
	    try {
            localResultsRoot.delete(Selectors.SELECT_ALL);
        } catch (FileSystemException x) {
            logger.warn("Failed to delete temporary results",x);
        }
	    
	}
    
    private EventListenerList listeners = new EventListenerList();
	
    protected void fireMessageReceived(ExecutionMessage m) {
        if (listeners == null) {// deleted the listener list - we've already cleaned up.
            return;
        }
    	ProcessListener[] pls = (ProcessListener[]) listeners.getListeners(ProcessListener.class);
    	if (pls.length > 0) {
    		ProcessEvent pe = new ProcessEvent(this);
    		for (int i = 0; i < pls.length; i++) {
				pls[i].messageReceived(pe);
			}
    	}
    	RemoteProcessListener[] rpls = (RemoteProcessListener[])listeners.getListeners(RemoteProcessListener.class);
    	for (int i = 0; i < rpls.length; i++) { 
			rpls[i].messageReceived(id,m);
		}
    }
    
    protected void fireResultsReceived(Map m) {
        if (listeners == null) {// deleted the listener list - we've already cleaned up.
            return;
        }        
    	ProcessListener[] pls = (ProcessListener[]) listeners.getListeners(ProcessListener.class);
    	if (pls.length > 0) {
    		ProcessEvent pe = new ProcessEvent(this);
    		for (int i = 0; i < pls.length; i++) {
				pls[i].resultsReceived(pe);
			}
    	}    	
    	RemoteProcessListener[] rpls = (RemoteProcessListener[])listeners.getListeners(RemoteProcessListener.class);
    	for (int i = 0; i < rpls.length; i++) { 
			rpls[i].resultsReceived(id,m);
		}
    }
    
    protected void fireStatusChanged(String s) {
        if (listeners == null) {// deleted the listener list - we've already cleaned up.
            return;
        }        
    	ProcessListener[] pls = (ProcessListener[]) listeners.getListeners(ProcessListener.class);
    	if (pls.length > 0) {
    		ProcessEvent pe = new ProcessEvent(this);
    		for (int i = 0; i < pls.length; i++) {
				pls[i].statusChanged(pe);
			}
    	}    	
    	RemoteProcessListener[] rpls = (RemoteProcessListener[])listeners.getListeners(RemoteProcessListener.class);
    	for (int i = 0; i < rpls.length; i++) { 
			rpls[i].statusChanged(id,s);
		}
    }
    
    public void addProcessListener(ProcessListener pl) {
    	listeners.add(ProcessListener.class,pl);
    }
    
    public void removeProcessListener(ProcessListener pl) {
    	listeners.remove(ProcessListener.class,pl);
    }
    
    
	// public listener interace
	public void addRemoteProcessListener(RemoteProcessListener listener) {
		listeners.add(RemoteProcessListener.class,listener);
	}
	
	public void removeRemoteProcessListener(RemoteProcessListener listener) {
		listeners.remove(RemoteProcessListener.class,listener);
	}
}

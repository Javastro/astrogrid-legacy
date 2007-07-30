/*$Id: RemoteProcessManagerImpl.java,v 1.16 2007/07/30 17:59:55 nw Exp $
 * Created on 08-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.collections.map.ListOrderedMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.RemoteProcessListener;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.desktop.modules.system.SnitchInternal;
import org.astrogrid.workflow.beans.v1.Tool;
import org.w3c.dom.Document;

/** implementation of a remote process manager.
 *  - handles running cea / jes / whatever else.
 * 
 *  - kind of a central clearing house - delegates to appropriate strategy based on uri / document type.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 08-Nov-2005
 *
 */
public class RemoteProcessManagerImpl implements RemoteProcessManagerInternal{
	
	/** internal datastructure of process monitors
	 *  */
	public static class MonitorMap {
		/** map of process montiors */
		private final Map m = new ListOrderedMap();
		/** set of listeners which listen to all monitors
		 * 	- so, need to be registered as listeners with all new tasks 
		 */
		private final Set wildcardListeners = new HashSet();
		
		/** add a listener to all remoteProcesses.
		 * adds it to all current processes, and takes note that it wishes to 
		 * be added to all subsequent processes
		 * @param rpl
		 */
		public void addWildcardListener(RemoteProcessListener rpl) {
			wildcardListeners.add(rpl);
			for (Iterator i = m.values().iterator(); i.hasNext();) {
				ProcessMonitor rpm = (ProcessMonitor) i.next();
				rpm.addRemoteProcessListener(rpl);
			}
		}
		/** remote a listener to all remote processes.
		 * removes it from all current processes, and discards the
		 * fact that it wishes to be added to subsequent processes.
		 * @param rpl
		 */
		public void removeWildcardListener(RemoteProcessListener rpl) {
			wildcardListeners.remove(rpl);
			for (Iterator i = m.values().iterator(); i.hasNext();) {
				ProcessMonitor rpm = (ProcessMonitor) i.next();
				rpm.removeRemoteProcessListener(rpl);
			}			
		}
		
		/** add a new remot process monitor */
		public void add(ProcessMonitor monitor) {
			m.put(monitor.getId(),monitor);
			for (Iterator i = wildcardListeners.iterator(); i.hasNext();) {
				RemoteProcessListener l = (RemoteProcessListener) i.next();
				monitor.addRemoteProcessListener(l);
			}
		}
		/** remote a remote processes monitor */
		public void remove(URI id) {
			ProcessMonitor rpm = get(id);
			if (rpm != null) {
				remove(rpm);
			}
			
		}
		
		public void remove(ProcessMonitor pm) {
			m.remove(pm.getId());
			if (pm instanceof AbstractProcessMonitor) {
				((AbstractProcessMonitor)pm).cleanUp();
			}
		}
		/** access a monitor - may return null */
		public ProcessMonitor get(URI id) {
			return (ProcessMonitor)m.get(id);
		}
		/** list the keys of the current monitors */
		public URI[] listKeys() {
			return (URI[])m.keySet().toArray(new URI[m.size()]);
		}
	}
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(RemoteProcessManagerImpl.class);

    //@todo remove usage of myspace.
    public RemoteProcessManagerImpl(List strategies, 
            MyspaceInternal vos, SnitchInternal snitch ) {
        super();
        this.vos = vos;
        this.strategies = strategies;
        this.snitch = snitch;
        this.monitors = new MonitorMap();
    }
    private final MonitorMap monitors;
    final List strategies;
    final MyspaceInternal vos;
    final SnitchInternal snitch;

    /** unused at the moment - but will be used when we want to enable resumable cea apps */
    private RemoteProcessStrategy selectStrategy(URI uri) throws InvalidArgumentException {
        for (Iterator i = strategies.iterator(); i.hasNext(); ) {
            RemoteProcessStrategy s= (RemoteProcessStrategy)i.next();
            if (s.canProcess(uri)) {
                return s;
            }
        }
        throw new InvalidArgumentException("Could not find handler for " +uri);
    }
    
    public ProcessMonitor create(Document doc) throws InvalidArgumentException, ServiceException {
        for (Iterator i = strategies.iterator(); i.hasNext(); ) {
            RemoteProcessStrategy s= (RemoteProcessStrategy)i.next();
            String name = s.canProcess(doc);
            if (name != null) {
            	if (snitch != null) { // be extra safe..
                	Map m = new HashMap();
                	m.put("name",name);
                	snitch.snitch("SUBMIT",m); 
                	// not strictly true.. but at least snitching here means that snitching is uniform across all strategies            		
            	}
                return s.create(doc);
            }
        }
        throw new InvalidArgumentException("Unrecognized kind of execution document ");        
    }
    
    public URI[] list() throws ServiceException {
    	return monitors.listKeys();
    }

    public URI submit(Document arg0) throws ServiceException, SecurityException, NotFoundException,
            InvalidArgumentException {
        ProcessMonitor rpm = create(arg0);
        rpm.start();
        //@todo would like to be able to add rpm to the monitors list before I've called
        // start, but not possible, as no ID has been associated.
        // only impacts listeners on this map.
        // work-around woul be to assign a temporary ID to the processMonitor
        // but then would need to change the ID halfway through the monitor's life
        // which sounds like a source for bugs (and confusion on the part of AR clients).
        // so monitors remains a map of _running_ processes only.
        monitors.add(rpm);
        return rpm.getId();
    }


    public URI submitTo(Document arg0, URI arg1) throws NotFoundException,
            InvalidArgumentException, ServiceException, SecurityException {
        ProcessMonitor rpm = create(arg0);
        rpm.start(arg1);
        monitors.add(rpm);
        return rpm.getId();
    }

   
    private Document loadDocument(URI location) throws NotFoundException, InvalidArgumentException {
        InputStream is = null;
        try {
            is = vos.getInputStream(location);
            return XMLUtils.newDocument(is);
        } catch (ACRException e) {
            throw new NotFoundException("Failed to load document from " + location,e);
        } catch (Exception e) {
            throw new InvalidArgumentException("Failed to parse document from " + location,e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.warn("Failed to close read stream",e);
                }
            }
        }
            
    }
    
    public URI submitStored(URI arg0) throws NotFoundException, InvalidArgumentException,
            SecurityException, ServiceException {
        Document doc = loadDocument(arg0);
        return submit(doc);
    }


    public URI submitStoredTo(URI arg0, URI arg1) throws NotFoundException,
            InvalidArgumentException, ServiceException, SecurityException {
        Document doc = loadDocument(arg0);
        return submitTo(doc,arg1);
    }


    public void halt(URI arg0) throws NotFoundException, InvalidArgumentException,
            ServiceException, SecurityException {
    	ProcessMonitor rpm = monitors.get(arg0);
    	if (rpm == null) {
    		throw new NotFoundException(arg0.toString());
    	}
    	rpm.halt();      
    }


    public void delete(URI arg0) throws NotFoundException, ServiceException, SecurityException, InvalidArgumentException {
    	monitors.remove(arg0);        
    }

	public void delete(ProcessMonitor pm) {
		monitors.remove(pm);
	}
    
    public ExecutionInformation getExecutionInformation(URI arg0) throws ServiceException,
            NotFoundException, SecurityException, InvalidArgumentException {
    	ProcessMonitor rpm = monitors.get(arg0);
    	if (rpm == null) {
    		throw new NotFoundException(arg0.toString());
    	}
    	return rpm.getExecutionInformation();
    }


    public ExecutionMessage[] getMessages(URI arg0) throws ServiceException, NotFoundException {
    	ProcessMonitor rpm = monitors.get(arg0);
    	if (rpm == null) {
    		throw new NotFoundException(arg0.toString());
    	}
    	return rpm.getMessages();
    }

    public Map getResults(URI arg0) throws ServiceException, SecurityException, NotFoundException,
            InvalidArgumentException {
    	ProcessMonitor rpm = monitors.get(arg0);
    	if (rpm == null) {
    		throw new NotFoundException(arg0.toString());
    	}
    	return rpm.getResults();
    }
    
    public String getSingleResult(URI arg0, String resultName) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        Map m = getResults(arg0);
        if (!m.containsKey(resultName)) {
            if (m.size() == 1) { // return the single result - it's probably what they want.
                return m.values().iterator().next().toString();
            } else {
            throw new NotFoundException("Result " + resultName +" not present");
            }
        }
        return m.get(resultName).toString();
    }
    
    public void addRemoteProcessListener(URI arg0, RemoteProcessListener arg1) {
        if (arg0 == null) {
            monitors.addWildcardListener(arg1);
        } else {
        	ProcessMonitor rpm = monitors.get(arg0);
        	if (rpm != null) {
        		rpm.addRemoteProcessListener(arg1);
        	}
        }
    }


    public void removeRemoteProcessListener(URI arg0, RemoteProcessListener arg1) {
        if (arg0 == null) {
            monitors.removeWildcardListener(arg1);
        } else {
        	ProcessMonitor rpm = monitors.get(arg0);
        	if (rpm != null) {
        		rpm.removeRemoteProcessListener(arg1);
        	}
        }
    }

	public ProcessMonitor findMonitor(URI id) {
		return monitors.get(id);
	}

    public void addMonitor(ProcessMonitor pm) {
        monitors.add(pm);
    }




}


/* 
$Log: RemoteProcessManagerImpl.java,v $
Revision 1.16  2007/07/30 17:59:55  nw
RESOLVED - bug 2257: More feedback, please
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2257

Revision 1.15  2007/07/26 18:21:45  nw
merged mark's and noel's branches

Revision 1.14.2.1  2007/07/24 17:54:52  nw
added tasks vfs view

Revision 1.14  2007/07/23 12:17:31  nw
finished implementation of process manager framework.

Revision 1.13  2007/07/16 13:02:15  nw
Complete - task 90: integrate vomon with remote process manager

Revision 1.12  2007/07/16 12:21:23  nw
Complete - task 91: make remoteprocessmanager a full fledged ar member , and added internal interface.

Revision 1.11  2007/07/13 23:14:54  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.10  2007/01/29 11:11:35  nw
updated contact details.

Revision 1.9  2007/01/09 16:20:59  nw
use linkedhashmap instead of map

Revision 1.8  2006/06/27 19:11:09  nw
adjusted todo tags.

Revision 1.7  2006/05/26 15:23:45  nw
implemented snitching.

Revision 1.6  2006/04/26 15:55:57  nw
fixed jdic-tray bug by refactoring.

Revision 1.5  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.4.30.4  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.4.30.3  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.4.30.2  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.4.30.1  2006/03/22 18:01:30  nw
merges from head, and snapshot of development

Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.2.3  2005/11/23 18:10:03  nw
reviewed and tuned up.

Revision 1.3.2.2  2005/11/23 04:56:00  nw
adjusted to work with changed return type in recorder

Revision 1.3.2.1  2005/11/17 21:06:26  nw
moved store to be user-dependent
debugged message monitoring.

Revision 1.3  2005/11/11 17:53:27  nw
added cea polling to lookout.

Revision 1.2  2005/11/10 16:28:26  nw
added result display to vo lookout.

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/
/*$Id: InMemoryExecutionHistory.java,v 1.6 2008/09/18 09:13:39 pah Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager.persist;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import junit.framework.Test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.joda.time.DateTime;

/** In-memory implementation of an execution history component - keeps everything in memory, persists nothing
 * <p />
 * So not such a good one to use in a deployment environment. handy for testing however.
 * <p />
 * Implemented using two maps- one for current set, one for archive.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 *
 */
public class InMemoryExecutionHistory implements ExecutionHistory , ComponentDescriptor{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(InMemoryExecutionHistory.class);

    /** Construct a new InMemoryExecutionHistory
     * 
     */
    public InMemoryExecutionHistory() {
        super();
        currentSet = new HashMap();
        archive= new SimpleHashMap();
        DatatypeFactory dataF = null;
	try {
	    dataF = DatatypeFactory.newInstance();
	} catch (DatatypeConfigurationException e) {
	   logger.error("configuration problem", e);
	}
	dataFactory = dataF;
    }
    protected  Map currentSet;
    protected  SimpleMap archive;
    private final DatatypeFactory dataFactory;
    /**
     * @see org.astrogrid.applications.manager.persist.ExecutionHistory#isApplicationInCurrentSet(java.lang.String)
     */
    public boolean isApplicationInCurrentSet(String execID) {
        return currentSet.containsKey(execID);
    }
    /**
     * @see org.astrogrid.applications.manager.persist.ExecutionHistory#getApplicationFromCurrentSet(java.lang.String)
     */
    public Application getApplicationFromCurrentSet(String execID) throws ExecutionIDNotFoundException {
        final Application app = (Application)currentSet.get(execID);
        if (app == null) {
            throw new ExecutionIDNotFoundException(execID);
        }
        return app;
    }
    /**
     * @see org.astrogrid.applications.manager.persist.ExecutionHistory#addApplicationToCurrentSet(org.astrogrid.applications.Application)
     */
    public void addApplicationToCurrentSet(Application app) {
        currentSet.put(app.getId(),app);
    }
    /**
     * @see org.astrogrid.applications.manager.persist.ExecutionHistory#moveApplicationFromCurrentSetToArchive(java.lang.String)
     */
    public void moveApplicationFromCurrentSetToArchive(String execID) throws ExecutionIDNotFoundException, PersistenceException{
        final Application app = getApplicationFromCurrentSet(execID);
        ExecutionSummaryType summary = SummaryHelper.summarize(execID, app);
        try {
            archive.put(execID,summary);
        } catch (Exception e) {
            throw new PersistenceException("moveApplicationFromCurrentSetToArchive failed '" + execID + "'",e);
        }
        currentSet.remove(execID);
      }
      
    /**
     * @see org.astrogrid.applications.manager.persist.ExecutionHistory#getApplicationFromArchive(java.lang.String)
     */
    public ExecutionSummaryType getApplicationFromArchive(String execID) throws ExecutionIDNotFoundException, PersistenceException{
        try {
            ExecutionSummaryType summary = archive.get(execID);
            if (summary == null) {
                throw new ExecutionIDNotFoundException(execID);
            }
            return summary;
        } catch (ExecutionIDNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenceException("getApplicationFromArchive failed '" + execID + "'",e);
        }
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "In-memory execution history";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     * @todo add dump of stored data.
     */
    public String getDescription() {
        return "Simplest possible implementation - not for use in production environments";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
    
    /** defined a limited map interface here, to make it simpler to provide different implementations - as full
     * features of the {@link java.util.Map} interface not needed
     * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
     * @TODO does this really make sense any more....
     *
     */
    public static interface SimpleMap /* restricts Map !! */ {
        public void put(String key,ExecutionSummaryType value) throws Exception;
        public ExecutionSummaryType get(String key) throws Exception;
        public Set<String> keys();
        public boolean delete(String key);
    }
    
    /** implementation of the SimpleMap interface backed by a {@link java.util.HashMap}
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
     *
     */
    static class SimpleHashMap extends HashMap implements SimpleMap {
	/**
	 * Logger for this class
	 */
	private static final Log logger = LogFactory
		.getLog(SimpleHashMap.class);

        /**
         * @see org.astrogrid.applications.manager.persist.InMemoryExecutionHistory.SimpleMap#put(java.lang.String, org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType)
         */
        public void  put(String key, ExecutionSummaryType value) throws Exception {
            super.put(key,value);
        }

        /**
         * @see org.astrogrid.applications.manager.persist.InMemoryExecutionHistory.SimpleMap#get(java.lang.String)
         */
        public ExecutionSummaryType get(String key) throws Exception {
            return (ExecutionSummaryType)super.get(key);
            
        }

	public Set<String> keys() {
	   return super.keySet();
	}

	public boolean delete(String key) {
	   return (super.remove(key) != null);
	}
        
        

    }

    public String[] getExecutionIDs() {
	Set<String> all = new HashSet<String>(currentSet.keySet());
	all.addAll(archive.keys());
	return all.toArray(new String[]{});
    }
    public boolean delete(String execId) {
	if(archive.keys().contains(execId))
	{
	   return archive.delete(execId);
	}else if (isApplicationInCurrentSet(execId))
	{
	    logger.warn("attempting to delete from current set");
	    return false;
	}
	else {
	    logger.warn("attempting to delete non-existant job");
	    return false;
	}
    }
    public void setDestructionTime(String execID, Date destruction) throws PersistenceException {
	if (isApplicationInCurrentSet(execID)) {
	    getApplicationFromCurrentSet(execID).setDestruction(destruction);
	} else {
            try {
		ExecutionSummaryType eh = archive.get(execID);
		eh.setDestruction(new DateTime(destruction));
		archive.put(execID, eh);
	    } catch (Exception e) {
		throw new PersistenceException("Problem setting persistence", e);
	    }
	}
   }
    public List<String> getExecutionIdDestructionBefore(Date time) {
	List<String> retval = new ArrayList<String>();
	for (Iterator<String> iterator = archive.keys().iterator(); iterator.hasNext();) {
	    String jobId = iterator.next();
	    try {
		ExecutionSummaryType summ = archive.get(jobId);
		if(summ.getDestruction().isBefore(time.getTime()))
		{
		    retval.add(jobId);
		}
	    } catch (Exception e) {
		logger.error("internal error - should not be possible", e);
	    }
	    
	}
	
	return retval;
   }
}


/* 
$Log: InMemoryExecutionHistory.java,v $
Revision 1.6  2008/09/18 09:13:39  pah
improved javadoc

Revision 1.5  2008/09/13 09:51:02  pah
code cleanup

Revision 1.4  2008/09/03 14:18:48  pah
result of merge of pah_cea_1611 branch

Revision 1.3.266.4  2008/06/16 21:58:58  pah
altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.

Revision 1.3.266.3  2008/05/08 22:40:53  pah
basic UWS working

Revision 1.3.266.2  2008/04/23 14:14:29  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.3.266.1  2008/04/17 16:08:32  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.3  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/
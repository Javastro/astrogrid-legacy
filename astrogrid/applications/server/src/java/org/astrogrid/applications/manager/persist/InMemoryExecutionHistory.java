/*$Id: InMemoryExecutionHistory.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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

import org.astrogrid.applications.Application;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.util.HashMap;
import java.util.Map;

import junit.framework.Test;

/** Simplest possible implementation of an execution history component - keeps everything in memory, persists nothing
 * <p />
 * So not such a good one to use in a deployment environment. handy for testing however.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 *
 */
public class InMemoryExecutionHistory implements ExecutionHistory , ComponentDescriptor{
    /** Construct a new InMemoryExecutionHistory
     * 
     */
    public InMemoryExecutionHistory() {
        super();
        currentSet = new HashMap();
        archive= new SimpleHashMap();
    }
    protected  Map currentSet;
    protected  SimpleMap archive;
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
        Application app = (Application)currentSet.get(execID);
        if (app == null) {
            throw new ExecutionIDNotFoundException(execID);
        }
        return app;
    }
    /**
     * @see org.astrogrid.applications.manager.persist.ExecutionHistory#addApplicationToCurrentSet(org.astrogrid.applications.Application)
     */
    public void addApplicationToCurrentSet(Application app) {
        currentSet.put(app.getID(),app);
    }
    /**
     * @see org.astrogrid.applications.manager.persist.ExecutionHistory#moveApplicationFromCurrentSetToArchive(java.lang.String)
     */
    public void moveApplicationFromCurrentSetToArchive(String execID) throws ExecutionIDNotFoundException, PersistenceException{
        Application app = getApplicationFromCurrentSet(execID);
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
     *
     */
    public static interface SimpleMap /* restricts Map !! */ {
        public void put(String key,ExecutionSummaryType value) throws Exception;
        public ExecutionSummaryType get(String key) throws Exception;
    }
    
    /** implementation of the SimpleMap interface backed by a {@link java.util.HashMap}
     * 
     * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
     *
     */
    static class SimpleHashMap extends HashMap implements SimpleMap {

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

    }
}


/* 
$Log: InMemoryExecutionHistory.java,v $
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
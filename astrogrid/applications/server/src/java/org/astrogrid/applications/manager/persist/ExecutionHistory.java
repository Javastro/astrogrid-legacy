/*$Id: ExecutionHistory.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 25-May-2004
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

/** Interface to a persistence component that records execution histories for
 * each application.
 * maintains 'map' of currently executing apps, and summary history for past executions
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 */
public interface ExecutionHistory {
    /** returns true if the execID passed in refers to a 'current' application invokation */
    boolean isApplicationInCurrentSet(String execID);
    
    /** returns the application object for this execID
     * pre: isExecIDCurrent
     * @param execID
     * @return
     */
    Application getApplicationFromCurrentSet(String execID) throws ExecutionIDNotFoundException, PersistenceException;
    /** add an application to the set of currently executing apps */
    void addApplicationToCurrentSet(Application app) throws PersistenceException;
    /** archive an applicatioin - remove it from the set of currently executing apps, and store a summary of it in the archive */
    void moveApplicationFromCurrentSetToArchive(String execID) throws ExecutionIDNotFoundException, PersistenceException;
    /** retreive a summary of an application execution from the archive */
    ExecutionSummaryType getApplicationFromArchive(String execID) throws ExecutionIDNotFoundException, PersistenceException;
}


/* 
$Log: ExecutionHistory.java,v $
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
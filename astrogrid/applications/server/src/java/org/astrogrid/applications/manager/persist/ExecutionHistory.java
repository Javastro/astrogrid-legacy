/*$Id: ExecutionHistory.java,v 1.5 2008/09/13 09:51:02 pah Exp $
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

import java.util.Date;
import java.util.List;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.manager.QueryService;

/** Interface to a  component that records execution histories for
 * each application.<p>
 * maintains 'map' of executing apps (the current set) , and summary history for past executions (the archive)
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 * @see QueryService as this is more often useful when trying to find out about jobs in a uniform way - this interface is rather more low level.
 * @TODO not necessarily nice that this interface treats objects differently if they are in current set or not...
 * 
 */
public interface ExecutionHistory {
    /** check if an application is currently pending or executing (as compared to completed and archived). 
     * @param execID the cea-assigned id of the application to look for
     * @return true if the application is in the current set. */
    boolean isApplicationInCurrentSet(String execID);
    
       
    /** access an application in the current set.
     * <p>
     * precondition : {@link #isApplicationInCurrentSet(String)}
     * @param execID  the cea-assigned id of the application to retreive.
     * @return the associated applicaiton
     * @throws ExecutionIDNotFoundException if the id does not refer to an application in the current set
     * @throws PersistenceException if a storage fault occurs.
     */
    Application getApplicationFromCurrentSet(String execID) throws ExecutionIDNotFoundException, PersistenceException;
    /** add an application to the set of currently executing apps 
     * @param app the application to add to the current set
     * @throws PersistenceException if a storage fault occurs.*/
    void addApplicationToCurrentSet(Application app) throws PersistenceException;
    /** archive an applicatioin - remove it from the set of currently executing apps, and store a summary of it in the archive 
     * @param execID cea-assigned id of the application to archive.
     * @throws ExecutionIDNotFoundException if the id does not refer to an applicatioin in the current set
     * @throws PersistenceException if a storage fault occurs.*/
    void moveApplicationFromCurrentSetToArchive(String execID) throws ExecutionIDNotFoundException, PersistenceException;
    /** retreive a summary of an application execution from the archive 
     * @param execID the cea-assigned id of the application execution to retreive.
     * @return the summary of this execution
     * @throws ExecutionIDNotFoundException if the id foes not refer to an archived application
     * @throws PersistenceException if a storage fault occurs*/
    ExecutionSummaryType getApplicationFromArchive(String execID) throws ExecutionIDNotFoundException, PersistenceException;
    
    /**
     * @return
     */
    String [] getExecutionIDs();
    
    void setDestructionTime(String execID, Date destruction) throws  PersistenceException;
    
    boolean delete(String execId) throws ExecutionIDNotFoundException;
    
    List<String> getExecutionIdDestructionBefore(Date time);
    
}


/* 
$Log: ExecutionHistory.java,v $
Revision 1.5  2008/09/13 09:51:02  pah
code cleanup

Revision 1.4  2008/09/03 14:18:48  pah
result of merge of pah_cea_1611 branch

Revision 1.3.266.4  2008/05/08 22:40:53  pah
basic UWS working

Revision 1.3.266.3  2008/04/23 14:14:29  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.3.266.2  2008/04/17 16:08:32  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.3.266.1  2008/03/19 23:10:54  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

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
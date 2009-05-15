/*$Id: QueryService.java,v 1.7 2009/05/15 22:51:19 pah Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.authorization.AuthorizationPolicy;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.applications.description.execution.MessageType;
import org.astrogrid.applications.description.execution.ResultListType;
import org.astrogrid.security.SecurityGuard;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;

/** Defines a component used to query status of applications, access results, etc. 
 * This is primarily the interface that external queries about progress are made.
 * This interface contains {@link SecurityGuard}s in some of the methods - the implementations are expected to do something with this information, - i.e. implement policy decision points using an {@link AuthorizationPolicy}.
  
  @TODO would be better to do the authentication stuff with aspect oriented programming.
  @TODO make sure all methods covered by security.
  @TODO make sure that this is actually used in UWS facing parts.

 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 15 May 2009
 *
 */
public interface QueryService {
    
    /** register a remote progress listener with an application
     * 
     * @param executionId the server-assigned id of a current application (which may either be running, or waiting to run)
     * @param endpoint endpoint of a webservice implementing the {@link org.astrogrid.jes.delegate.v1.jobmonitor.JobMonitor} interface. This webservice will be
     * notified whenever the application changes state.
     * @throws CeaException
     * @return true if registered successfully.
     */
    public boolean registerProgressListener(String executionId,URI endpoint) throws CeaException;
    /** register a remote result listener with an application
     * 
     * @param executionId the server-assigned id of a current application (which may either be running, or waiting to run)
     * @param endpoint endpoint of a webservice implementing the {@link CeaResultListener} interface. This webservice will be
     * notified when the exection results for the application become available.
     * @return true if registered successfully
     * @throws CeaException
     */    
    public boolean registerResultsListener(String executionId,URI endpoint) throws CeaException;
    
    // direct query methods
    /** query the status of a running application 
     * @param executionId the (cea-assigned) id of the application to query.
     * @param secGuard TODO
     * @return a message containing information about the status of the application.
     * @throws CeaException if owt goes wrong.*/
    public MessageType queryExecutionStatus(String executionId, SecurityGuard secGuard) throws CeaException;

     /** get results from an application - list will be empty / semi-blank if the application hasn't finished producing results yet 
     * @param executionId the (cea-assigned) id of the application to query.
     * @param secGuard TODO
     * @return a list of resuls.
     * @throws CeaException*/     
     public ResultListType getResults(String executionId, SecurityGuard secGuard) throws CeaException;
    
     /** get summary of an application execution 
     * @param executionId the (cea-assigned) id of the application to query.
     * @param secGuard TODO
     * @return an executioin summary for this application.
     * @throws CeaException*/
     
     public ExecutionSummaryType getSummary(String executionId, SecurityGuard secGuard) throws CeaException;
    /**
     * Return a log file. This really only applies to the commandline case.
     * @TODO think of more general way of expressing this - or refactor into sub interface.
     * @param execututionId
     * @param type
     * @return
    * @throws FileNotFoundException 
     */
     public File getLogFile(String executionId, ApplicationEnvironmentRetriver.StdIOType type) throws CeaException, FileNotFoundException;
}


/* 
$Log: QueryService.java,v $
Revision 1.7  2009/05/15 22:51:19  pah
ASSIGNED - bug 2911: improve authz configuration
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2911
combined agast and old stuff
refactored to a more specific CEA policy interface
made sure that there are decision points nearly everywhere necessary  - still needed on the saved history

Revision 1.6  2008/09/03 14:18:56  pah
result of merge of pah_cea_1611 branch

Revision 1.5.102.2  2008/04/23 14:14:30  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.5.102.1  2008/04/17 16:08:32  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.5  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.4.152.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.4.138.1  2005/06/03 16:01:48  pah
first try at getting commandline execution log bz#1058

Revision 1.4  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.3  2004/07/09 14:48:24  nw
updated to match change in type of register*Listener methods in cec wsdl

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/
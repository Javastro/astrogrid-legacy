/*$Id: QueryService.java,v 1.2 2004/07/01 11:16:22 nw Exp $
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
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;

import java.net.URI;

/** Interface to the service used to query status of applications, access results, etc.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public interface QueryService {
    /** register a remote progress listener with an application
     * 
     * @param executionId the server-assigned id of a current application (which may either be running, or waiting to run)
     * @param endpoint endpoint of a webservice implementing the {@link JobMonitor} interface. This webservice will be
     * notified whenever the application changes state.
     * @throws CeaException
     */
    public void registerProgressListener(String executionId,URI endpoint) throws CeaException;
    /** register a remote result listener with an application
     * 
     * @param executionId the server-assigned id of a current application (which may either be running, or waiting to run)
     * @param endpoint endpoint of a webservice implementing the {@link CEAResultListener} interface. This webservice will be
     * notified when the exection results for the application become available.
     * @throws CeaException
     */    
    public void registerResultsListener(String executionId,URI endpoint) throws CeaException;
    
    // direct query methods
    /** query the status of a running application */
    public MessageType queryExecutionStatus(String executionId) throws CeaException;

     /** get results from an application - list will be empty if the applicatio hasn't produced results yet */     
     public ResultListType getResults(String executionId) throws CeaException;
    
     /** get summary of an application execution */
     
     public ExecutionSummaryType getSummary(String executionId) throws CeaException;
    
        
}


/* 
$Log: QueryService.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/
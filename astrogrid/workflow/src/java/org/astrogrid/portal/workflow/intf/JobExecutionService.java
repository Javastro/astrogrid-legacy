/*$Id: JobExecutionService.java,v 1.2 2004/03/03 01:36:38 nw Exp $
 * Created on 01-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.intf;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Interface to a component that provides a job execution service.
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 *
 */
public interface JobExecutionService {
    /** submit workflow to the job controller */
    JobURN submitWorkflow( Workflow workflow ) throws WorkflowInterfaceException;                               
    
    // plus add bits required by Jes   
    void deleteJob(JobURN jobURN) throws WorkflowInterfaceException;
    void cancelJob(JobURN jobURN) throws WorkflowInterfaceException;
    /** access Workflow job referred to by jobURN
     * 
     * @param userid
     * @param community
     * @param jobURN
     * @return document, or null if not found
     * @throws WorkflowInterfaceException
     */
    Workflow readJob(JobURN jobURN) throws WorkflowInterfaceException;
    String[] readJobList(Account account) throws WorkflowInterfaceException;
}


/* 
$Log: JobExecutionService.java,v $
Revision 1.2  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.1.2.2  2004/03/03 01:18:00  nw
commited first draft of interface design

Revision 1.1.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish
 
*/
/*$Id: JobExecutionService.java,v 1.4 2004/03/09 15:33:41 nw Exp $
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
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** A component that can execute and manage jobs
 * @author Noel Winstanley nw@jb.man.ac.uk 01-Mar-2004
 *
 */
public interface JobExecutionService {
    /** submit a workflow to the job controller
     * 
     * @param workflow workflow document to submit (contains user credentials to execute under)
     * @return new unique identifier for the job. never null
     * @throws WorkflowInterfaceException if job cannot be submitted
     */
    JobURN submitWorkflow( Workflow workflow ) throws WorkflowInterfaceException;                               
    
    /**
     * delete a job - remove all record of it from the jes store
     * <p>does nothing at the moment
     * @param jobURN unique identifier of the job to delete 
     */    
    void deleteJob(JobURN jobURN) throws WorkflowInterfaceException;
    /**cancel a job - halt the execution of it
     * <p> does nothing at the momenr
     * @param jobURN unique identifier of the job
     * @throws WorkflowInterfaceException
     */
    void cancelJob(JobURN jobURN) throws WorkflowInterfaceException;
    /** Retreive an annotated workflow document from the jes store
     * 
     * @param jobURN unique job id
     * @return workflow document, plus annotations. never null
     * @throws WorkflowInterfaceException if document cannot be retreived.
     */
    Workflow readJob(JobURN jobURN) throws WorkflowInterfaceException;
    /** retreive list of jobs for a particular user
     * 
     * @param account identifies a user
     * @return array of job summaries
     * @throws WorkflowInterfaceException
     */
    JobSummary[] readJobList(Account account) throws WorkflowInterfaceException;
}


/* 
$Log: JobExecutionService.java,v $
Revision 1.4  2004/03/09 15:33:41  nw
updated types

Revision 1.3  2004/03/03 11:15:23  nw
tarted up javadocs, reviewed types

Revision 1.2  2004/03/03 01:36:38  nw
merged interfaces in from branch nww-int05-bz#146

Revision 1.1.2.2  2004/03/03 01:18:00  nw
commited first draft of interface design

Revision 1.1.2.1  2004/03/01 19:02:57  nw
refined interfaces. almost ready to publish
 
*/
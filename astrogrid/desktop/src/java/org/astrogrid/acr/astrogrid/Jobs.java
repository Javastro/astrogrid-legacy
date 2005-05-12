/*$Id: Jobs.java,v 1.3 2005/05/12 15:37:40 clq2 Exp $
 * Created on 18-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.acr.astrogrid;

import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.net.URL;

/** Interface to the Job Execution Service
 * <p>
 * In particular an interface to a single JES server  - which is configured in the system properties for this application.
 * In future, JES servers should be registered, and a default server associated with a user in a community .
 * It may also be necessary to be able to browse a selection of job servers, and maybe aggregate a user's jobs from a set of servers. 
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Mar-2005
 *
 */
public interface Jobs {
    /**
     * list the identifiers of the jobs for the current user 
     * @return
     * @throws WorkflowInterfaceException
     */
    public abstract JobURN[] list() throws WorkflowInterfaceException;
    /** list summaries of the jobs for the current user */
    public WorkflowSummaryType[] listSummaries() throws WorkflowInterfaceException;

    /** retrieve a workflow transcript
     * 
     * @param jobURN the identifier of the job to retrieve
     * @return a workflow transcript 
     * @throws WorkflowInterfaceException
     */
    public abstract Workflow getJob(JobURN jobURN) throws WorkflowInterfaceException;

  /** retrive a string summarizing a job
   * 
   * @param jobURN the identifier of the job to summarize
   * @return a user-readable string
   * @throws WorkflowInterfaceException
   */
    public abstract String getJobSummary(JobURN jobURN) throws WorkflowInterfaceException;


    /** cancel the exeuciton of a running job
     * 
     * @param jobURN identifier of the job to cancel.
     * @throws WorkflowInterfaceException
     */
    public abstract void cancelJob(JobURN jobURN) throws WorkflowInterfaceException;

  
    /** delete all record of a job
     * 
     * @param jobURN identifier of the job to delete 
     * @throws WorkflowInterfaceException
     */
    public abstract void deleteJob(JobURN jobURN) throws WorkflowInterfaceException;

 
    /** submit a new workflow for execution 
     * 
     * @param workflow workflow document to submit
     * @return a new unique identifier for this job 
     * @throws WorkflowInterfaceException
     */
    public abstract JobURN submitJob(Workflow workflow) throws WorkflowInterfaceException;

    /** submit a new workflow (stored in a file) for execution 
     * 
     * @param workflowURL url  refernce to the workflow document to submit (may be file://, http://, ftp://)
     * @return a new unique identifier for this job.
     * @throws WorkflowInterfaceException
     * @throws MarshalException
     * @throws ValidationException
     * @throws IOException
     * @todo extend to support ivo:// references too.
     */
    public JobURN submitJobFile(URL workflowURL) throws WorkflowInterfaceException, MarshalException, ValidationException, IOException ;
        
}

/* 
 $Log: Jobs.java,v $
 Revision 1.3  2005/05/12 15:37:40  clq2
 nww 1111

 Revision 1.2.8.1  2005/05/11 11:55:19  nw
 javadoc

 Revision 1.2  2005/04/27 13:42:41  clq2
 1082

 Revision 1.1.2.1  2005/04/25 11:18:51  nw
 split component interfaces into separate package hierarchy
 - improved documentation

 Revision 1.2  2005/04/13 12:59:11  nw
 checkin from branch desktop-nww-998

 Revision 1.1.2.3  2005/04/05 11:42:15  nw
 added 'submit' and 'summary' methods

 Revision 1.1.2.2  2005/03/22 12:04:03  nw
 working draft of system and ag components.
 
 */
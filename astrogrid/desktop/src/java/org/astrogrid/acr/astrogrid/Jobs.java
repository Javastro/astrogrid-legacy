/*$Id: Jobs.java,v 1.5 2005/08/05 11:46:55 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;

import org.w3c.dom.Document;

import java.net.URI;

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
     * list the jobs for the current user 
     * @return list of identifiers for the user's jobs.
     * @throws ServiceException if an error occurs while talking to the server
     */   
    URI[] list() throws ServiceException;
    
    /** list summaries of the jobs for the current user 
     * @throws ServiceException if an error occurs while talking to the server*/
    ExecutionInformation[] listFully() throws ServiceException;

    /** retrieve  the execution transcript for a job.
     * 
     * @param jobURN the identifier of the job to retrieve
     * @return a workflow transcript  document
     * @throws ServiceException if an error occurs when connecting to the server.
     * @throws SecurityException if the user is not  permitted to access this job
     * @throws NotFoundException if this job could not be found
     * @throws InvalidArgumentException if the job identifier is malformed.
     */
     Document getJobTranscript(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;

  /** retrive information about a job.
   * 
   * @param jobURN the identifier of the job to summarize
   * @return information about this job.
     * @throws ServiceException if an error occurs when connecting to the server.
     * @throws SecurityException if the user is not  permitted to access this job
     * @throws NotFoundException if this job could not be found
     * @throws InvalidArgumentException if the job identifier is malformed.
   */
    ExecutionInformation getJobInformation(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;


    /** cancel the exeuciton of a running job
     * 
     * @param jobURN identifier of the job to cancel.
     * @throws ServiceException if an error occurs while connecting to the server
     * @throws SecurityException if the user is not permitted to access this job
     * @throws NotFoundException if the job could not be found
     * @throws InvalidArgumentException if the job is not currently running.
     */
    void cancelJob(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;

  
    /** delete all record of a job
     * 
     * @param jobURN identifier of the job to delete 
     * @throws NotFoundException if the job could not be found
     * @throws ServiceException if an error occurs while connecting to the server
     * @throws SecurityException if the user is not permitted to access this job.
     */
    void deleteJob(URI jobURN) throws NotFoundException, ServiceException, SecurityException;

 
    /** submit a workflow for execution 
     * 
     * @param workflow workflow document to submit
     * @return a new unique identifier for this job 
     * @throws ServiceException if an error occurs while connecting to server
     * @throws InvalidArgumentException if the workflow document is invalid or malformed
     */
    URI submitJob(Document workflow) throws ServiceException, InvalidArgumentException;

    /** submit a workflow (stored in a file) for execution 
     * 
     * @param workflowReference url  refernce to the workflow document to submit (may be file://, http://, ftp:// or ivo:// - a myspace reference.)
     * @return a new unique identifier for this job.
     * @throws ServiceException if an error occurs while connecting to server
     * @throws InvalidArgumentException if the workflow document is invalid or inaccessible.
     */
    URI submitStoredJob(URI workflowReference) throws ServiceException, InvalidArgumentException ;
        
}

/* 
 $Log: Jobs.java,v $
 Revision 1.5  2005/08/05 11:46:55  nw
 reimplemented acr interfaces, added system tests.

 Revision 1.4  2005/05/12 15:59:09  clq2
 nww 1111 again

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
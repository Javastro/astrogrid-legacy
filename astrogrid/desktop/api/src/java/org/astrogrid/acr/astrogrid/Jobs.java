/*$Id: Jobs.java,v 1.11 2008/09/25 16:02:04 nw Exp $
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

import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.w3c.dom.Document;

/** AR Service: Execute and control workflows on remote job servers.
 *@exclude
 *
 * For now, an interface to a single JES server  - which is configured in the system properties for the ACR.
 * In future, JES servers should be registered, and a default server associated with a user in a community .
 * It may also be necessary to be able to browse a selection of job servers, and maybe aggregate a user's jobs from a set of servers.
 * <br />
 * Each workflow submitted is assigned a globally-unique identifier. This takes the form of a URI, but should be treated as opaque - the structure is
 * liable to change  once JES servers are registered.
 * @see <a href="http://www.astrogrid.org/maven/docs/HEAD/jes/userguide-architecture.html">Workflow Userguide</a> 
 * @see <a href="http://wiki.astrogrid.org/bin/view/Astrogrid/JesScriptingFAQ">Workflow Scripting FAQ</a> 
 * <br/>
 * @see <a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/Workflow.html">Workflow Schema-Documentation</a>
 * @see <a href="http://www.astrogrid.org/maven/docs/HEAD/astrogrid-workflow-objects/schema/ExecutionRecord.html">Execution Record Schema-Document</a>
 * @see <a href="http://www.astrogrid.org/viewcvs/astrogrid/workflow-objects/schema/">XSD Schemas</a>
 * <br/>
 * @see <a href="doc-files/example-workflow.xml">Example workflow</a>
 * @see <a href="doc-files/example-workflow-transcript.xml">Example execution transcript</a>
 * @see <a href="doc-files/example-workflow-transcript.html">Html-formatted execution transcript</a>
 * <b/>
 * @see org.astrogrid.acr.astrogrid.Applications Information about executing single applications
 * @see org.astrogrid.acr.astrogrid.Myspace Information about distributed file storage
 * @see org.astrogrid.acr.astrogrid.RemoteProcessManager for more general process management - methods
 * in this class are convenicne wrapper around operations in <tt>RemoteProcessManager</tt>
 * <br/>
 * @see org.astrogrid.acr.ui.JobMonitor
 * @see org.astrogrid.acr.ui.WorkflowBuilder
 * @see org.astrogrid.acr.astrogrid.ExecutionInformation
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 18-Mar-2005
 * @service astrogrid.jobs
 * @exclude @deprecated JES is no longer supported, and this interface is no longer provided by AR
 */
public interface Jobs {
    /**
     * list the jobs for the current user.
     * @return list of identifiers for the user's jobs (both current and completed jobs )
     * @throws ServiceException if an error occurs while talking to the server
    * 
     */   
    URI[] list() throws ServiceException;
    
    /** list summaries of the jobs for the current user. 
     * @return a beanful of information on each job
     * @throws ServiceException if an error occurs while talking to the server
     * @xmlrpc returns a struct. see {@link ExecutionInformation} for details of keys available.
     * 
     * */
    ExecutionInformation[] listFully() throws ServiceException;

    /** create a new initialized workflow document, suitable as a starting point for building workflows.
     * 
     * @return a workflow document - a <tt>workflow</tt> document in the the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace
     * @throws ServiceException if an inavoidable error ocurs.
     */
    Document createJob() throws ServiceException;
    
    
    /** wrap a remote application invocation document, to create a single-step workflow.
     * @param taskDocument - a task document in the <tt> http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace
     *  @return a workflow document with a single step that executes the parameter task - a <tt>workflow</tt> document in the the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace
     * @throws ServiceException if an inavoidable error ocurs.
     */
    Document wrapTask(Document taskDocument) throws ServiceException;
    /** retrieve the execution transcript for a job.
     * 
     * @param jobURN the identifier of the job to retrieve
     * @return a workflow transcript  document - A <tt>workflow</tt> document in
     *  the <tt>http://www.astrogrid.org/schema/AGWorkflow/v1</tt> namespace, annotated with execution information from the 
     * <tt>http://www.astrogrid.org/schema/ExecutionRecord/v1</tt> namespace.
     * @throws ServiceException if an error occurs when connecting to the server.
     * @throws SecurityException if the user is not  permitted to access this job
     * @throws NotFoundException if this job could not be found
     * @throws InvalidArgumentException if the job identifier is malformed.
     * @xmlrpc returns a xml document string
     */
     Document getJobTranscript(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;

  /** retrive summary for a job.
   * 
   * @param jobURN the identifier of the job to summarize
   * @return information about this job.
     * @throws ServiceException if an error occurs when connecting to the server.
     * @throws SecurityException if the user is not  permitted to access this job
     * @throws NotFoundException if this job could not be found
     * @throws InvalidArgumentException if the job identifier is malformed.
     * @xmlrpc returns a struct. see {@link ExecutionInformation} for details of keys available. 
   */
    ExecutionInformation getJobInformation(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;


    /** cancel the exeuciton of a running job.
     * 
     * @param jobURN identifier of the job to cancel.
     * @throws ServiceException if an error occurs while connecting to the server
     * @throws SecurityException if the user is not permitted to access this job
     * @throws NotFoundException if the job could not be found
     * @throws InvalidArgumentException if the job is not currently running.
     */
    void cancelJob(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException;

  
    /** delete all record of a job from the job server.
     * 
     * @param jobURN identifier of the job to delete 
     * @throws NotFoundException if the job could not be found
     * @throws ServiceException if an error occurs while connecting to the server
     * @throws SecurityException if the user is not permitted to access this job.
     */
    void deleteJob(URI jobURN) throws NotFoundException, ServiceException, SecurityException;

 
    /** submit a workflow for execution. 
     * 
     * @param workflow workflow document to submit
     * @return a unique identifier for this new job 
     * @throws ServiceException if an error occurs while connecting to server
     * @throws SecurityException if the user is not permitted to submit this job
     * @throws InvalidArgumentException if the workflow document is invalid or malformed
     * @xmlrpc pass the workflow parameter as a string
     */
    URI submitJob(Document workflow) throws ServiceException, SecurityException, InvalidArgumentException;

    /** submit a workflow (stored in a file) for execution. 
     * 
     * @param workflowReference url  refernce to the workflow document to submit (may be file://, http://, ftp:// or ivo:// - a myspace reference.)
     * @return a unique identifier for this new job.
     * @throws ServiceException if an error occurs while connecting to server
     * @throws SecurityException if the user is not permitted to submit this job.
     * @throws InvalidArgumentException if the workflow document is invalid or inaccessible.
     */
    URI submitStoredJob(URI workflowReference) throws ServiceException, SecurityException, InvalidArgumentException ;
        
}

/* 
 $Log: Jobs.java,v $
 Revision 1.11  2008/09/25 16:02:04  nw
 documentation overhaul

 Revision 1.10  2008/04/14 09:39:45  nw
 removed obsolete interface.

 Revision 1.9  2007/03/08 17:46:56  nw
 removed deprecated interfaces.

 Revision 1.8  2007/01/24 14:04:44  nw
 updated my email address

 Revision 1.7  2006/02/02 14:19:48  nw
 fixed up documentation.

 Revision 1.6  2005/11/24 01:18:42  nw
 merged in final changes from release branch.

 Revision 1.5.2.1  2005/11/23 04:32:36  nw
 added new method

 Revision 1.5  2005/11/11 10:09:01  nw
 improved javadoc

 Revision 1.4  2005/11/10 12:13:52  nw
 interface changes for lookout.

 Revision 1.3  2005/09/12 15:21:43  nw
 added stuff for adql.

 Revision 1.2  2005/08/12 08:45:16  nw
 souped up the javadocs

 Revision 1.1  2005/08/11 10:15:00  nw
 finished split

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
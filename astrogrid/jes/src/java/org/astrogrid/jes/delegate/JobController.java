/*$Id: JobController.java,v 1.7 2004/03/15 01:30:30 nw Exp $
 * Created on 06-Feb-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.delegate;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

/** Interface to a service for managing jobs - listing, deleting, submitting for execution, etc.
 * <p />
 * same as axis-generated service interface, but with castor types instead
 * @author Noel Winstanley nw@jb.man.ac.uk 06-Feb-2004
 *
 */
public interface JobController extends Delegate {
    /** submit a workflow document for execution as a new job
     * 
     * @param wf workflow to execute
     * @return a unique identifier for this job
     * @throws JesDelegateException
     */ 
    public JobURN submitWorkflow(Workflow wf) throws JesDelegateException;
    /** cancel the execution of a job
     * 
     * @param urn unique identifier of the job to cancel
     * @throws JesDelegateException
     */
    public void cancelJob(JobURN urn) throws JesDelegateException;
    /** delete all record of a job
     * 
     * @param urn unique identifier of the job to delete
     * @throws JesDelegateException
     */
    public void deleteJob(JobURN urn) throws JesDelegateException;
    /** retreive a list of all jobs (pending, running and completed) for a particular user
     * 
     * @param acc account object for the user in question
     * @return array of job summary objects, one summary for each job the user has in the system
     * @throws JesDelegateException
     */
    public JobSummary[] readJobList(Account acc) throws JesDelegateException;
    /** retrive the workflow document for a job.
     * 
     * @param urn unique identifier for a job.
     * @return annotated workflow document - will contains details of step execution, etc.
     * @throws JesDelegateException
     */
    public Workflow readJob(JobURN urn) throws JesDelegateException;
}
/* 
$Log: JobController.java,v $
Revision 1.7  2004/03/15 01:30:30  nw
jazzed up javadoc

Revision 1.6  2004/03/09 15:04:42  nw
renamed JobInfo to JobSummary

Revision 1.5  2004/03/09 14:23:12  nw
integrated new JobController wsdl interface

Revision 1.4  2004/03/05 16:16:23  nw
worked now object model through jes.
implemented basic scheduling policy
removed internal facade

Revision 1.3  2004/02/27 00:46:03  nw
merged branch nww-itn05-bz#91

Revision 1.2.2.4  2004/02/17 12:39:18  nw
added impelemnts Delegate

Revision 1.2.2.3  2004/02/17 12:25:38  nw
improved javadocs for classes

Revision 1.2.2.2  2004/02/17 11:00:15  nw
altered delegate interfaces to fit strongly-types wsdl2java classes

Revision 1.2.2.1  2004/02/11 16:09:10  nw
refactored delegates (again)

Revision 1.2  2004/02/09 11:41:44  nw
merged in branch nww-it05-bz#85

Revision 1.1.2.1  2004/02/06 18:11:21  nw
reworked the delegate classes
- introduced wrapper class and interfaces, plus separate impl
package with abstract base class. moved delegate classes into the correct
packages, deprecated old methods / classes. fitted in castor object model
 
*/
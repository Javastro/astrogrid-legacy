/*$Id: JobScheduler.java,v 1.54 2004/07/01 21:15:00 nw Exp $
 * Created on 05-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.jobscheduler;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.jes.types.v1.cea.axis.ResultListType;
/** Interface to a component that executes jobs.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Mar-2004
 *
 */
public interface JobScheduler {
    /** register a  new pending job with the scheduler.
     *  <p>
     * This method doesn't itself start the job running - it merely notifies the scheduler of the existence of a new job. The scheduler is 
     * free to decide when and how to execute the job  
     * @param jobURN unique identifier of the workflow document that forms this job. It is assumed that the scheduler can use this to retreive the workflow definition 
     * from some store.
     * @throws Exception catch all
     */
    public abstract void scheduleNewJob(JobURN jobURN) throws Exception;
    
    /** resume execution of a job
     * <p>
     * Called upon completion of the execution of a job step - whatever executes a job step calls this afterwards to notify the scheduler
     * that the step is completed.
     * <p>
     * The scheduler is then expected to examine the results of the step execution, and possible schedule further steps for execution.
     * @param id unique identifier for the job and step. details of encoding are privae to a particular scheduler implementation.
     * @param info reporting information on the execution of the step.
     * @throws Exception
     */
    public abstract void resumeJob(JobIdentifierType id, org.astrogrid.jes.types.v1.cea.axis.MessageType info) throws Exception;
    
    /** abort execution of a job
     * <p>
     * Leaves record of job in the store */
    public abstract void abortJob(JobURN jobURN) throws Exception;
    
    /** delete all record of a job
     * <p>
     * aborts if still running, then deletes the job record from the store */
    public abstract void deleteJob(JobURN jobURN) throws Exception;
 
    /** report results of a job step
     * 
     * @param id unique id of the job and step
     * @param results results information for this step.
     * @throws Exception
     */
    public abstract void reportResults(JobIdentifierType id,ResultListType results) throws Exception;
    
}
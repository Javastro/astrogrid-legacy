/*$Id: JobScheduler.java,v 1.2 2004/03/15 00:06:57 nw Exp $
 * Created on 05-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.comm;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
/** Interface to a component that schedules jobs for execution 
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Mar-2004
 *
 */
public interface JobScheduler {
    /** add a new job to the list of jobs to be executed
     *  <p>
     * This method doesn't itself start the job running - it merely notifies the scheduler of the existence of a new job. The scheduler is 
     * free to decide when and how to execute the job  
     * @param jobURN unique identifier of the workflow document that forms this job.
     * @throws Exception catch all
     */
    public abstract void scheduleNewJob(JobURN jobURN) throws Exception;
    
    /** resume execution of a job
     * <p>
     * Called upon completion of the execution of a job step - whatever executes a job step calls this afterwards to notify the scheduler
     * that the step is completed.
     * <p>
     * The scheduler is then expected to examine the results of the step execution, and possible schedule further steps for execution.
     * @param id unique identifier for the job and step.
     * @param info reporting information on the execution of the step.
     * @throws Exception
     */
    public abstract void resumeJob(JobIdentifierType id, org.astrogrid.jes.types.v1.cea.axis.MessageType info) throws Exception;
}
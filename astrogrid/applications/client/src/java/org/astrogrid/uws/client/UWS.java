/*
 * $Id: UWS.java,v 1.1 2008/09/24 13:47:17 pah Exp $
 * 
 * Created on 19 Sep 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.uws.client;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Period;

import net.ivoa.uws.ExecutionPhase;
import net.ivoa.uws.JobSummary;
import net.ivoa.uws.ResultList;

/**
 * Universal Worker service (UWS) operations. This is an interpretation of the UWS pattern that is intended to be a natural programming interface for java.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 19 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public interface UWS {
    
    /**
     * List of jobs known to the UWS system.
     * @return
     * @throws UWSException 
     */
    List<String> listJobs() throws UWSException;
    
    /**
     * Get the summary of the current status of a job.
     * @param jobId
     * @return
     * @throws UWSException 
     */
    JobSummary jobDetail(String jobId) throws UWSException;
    
    /**
     * Set the execution phase of a job. This can be used to start the job running, or abort the job.
     * @param jobId
     * @param newPhase
     * @return the phase that the job has actually been set to.
     * @throws UWSException 
     */
    ExecutionPhase setPhase(String jobId, String newPhase) throws UWSException;
    /**
     * Get the execution phase of a job.
     * @param jobId
     * @return
     * @throws UWSException 
     */
    ExecutionPhase getPhase(String jobId) throws UWSException;
    
    /**
     * set the destruction time for a job. The destruction time is the time at which a job will be deteted from the UWS system.
     * @param jobId
     * @return
     * @throws UWSException 
     */
    DateTime setDestruction(String jobId, DateTime destructionTime) throws UWSException;
    /**
     * @param jobId
     * @param time A length of time that the job can run for.
     * @return
     * @throws UWSException 
     */
    Period setTermination(String jobId, Period time) throws UWSException;
    
    
    /**
     * Create a new Job.
     * @param jobParams a string suitable for POSTing directly to the job creation point.
     * @return
     * @throws UWSJobCreationException 
     */
    JobSummary createJob(String jobParams) throws UWSJobCreationException;
    
    /**
     * Delete a job.
     * @param jobId
     * @throws UWSException 
     */
    void deleteJob(String jobId) throws UWSException;
    
    /**
     * Get the result list for a job. This will not return until the results have been created.
     * @param jobId
     * @return
     * @throws UWSException 
     */
    ResultList getResults(String jobId) throws UWSException;
    
    /**
     * Utility method for setting a job into the running phase.
     * @param jobId
     * @return
     * @throws UWSException 
     */
    ExecutionPhase runJob(String jobId) throws UWSException;
    
    /**
     * Utility method for aborting a Job.
     * @param jobId
     * @throws UWSException 
     */
    void abortJob(String jobId) throws UWSException;
}


/*
 * $Log: UWS.java,v $
 * Revision 1.1  2008/09/24 13:47:17  pah
 * added generic UWS client code
 *
 */

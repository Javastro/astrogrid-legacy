/*
 * @(#)JobController.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.jes.jobcontroller;

import org.astrogrid.common.bean.Axis2Castor;
import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.descriptor.ComponentDescriptor;
import org.astrogrid.jes.delegate.v1.jobcontroller.JesFault;
import org.astrogrid.jes.job.BeanFacade;
import org.astrogrid.jes.job.JobException;
import org.astrogrid.jes.job.JobFactory;
import org.astrogrid.jes.job.SubmitJobRequest;
import org.astrogrid.jes.jobscheduler.JobScheduler;
import org.astrogrid.jes.types.v1.JobURN;
import org.astrogrid.jes.types.v1.WorkflowString;
import org.astrogrid.jes.types.v1.WorkflowSummary;
import org.astrogrid.jes.util.JesUtil;
import org.astrogrid.workflow.beans.v1.Workflow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import junit.framework.Test;



/**
 * Management interface into the job execution system.
 * <p>
 * This class allows clients to interact with a job store - listing, removing, retreiving jobs, etc. Adding a job to the store has the effect of 
 * scheduling it for execution too.
 * <p>
 * This class could be accessed directly, but would more usually be called from a web delegate via a SOAP call.
 *
 * @author  Jeff Lusted
 * @version 1.0 28-May-2003
 * @since   AstroGrid 1.2
 * Bug#12   Jeff Lusted - 30-June-2003   NullPointerException under error conditions.
 */
public class JobController implements org.astrogrid.jes.delegate.v1.jobcontroller.JobController, ComponentDescriptor{
    
    public JobController(BeanFacade facade,JobScheduler nudger) {
        assert facade != null;
        assert nudger != null;
        this.facade = facade;
        this.nudger = nudger;
    }
    protected final BeanFacade facade;
    protected final JobScheduler nudger;

	private static final Log
		logger = LogFactory.getLog( JobController.class ) ;
    
    /** Submit a job to the execution service
     * 
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#submitWorkflow(org.astrogrid.jes.types.v1.WorkflowString)
     */
    public JobURN submitWorkflow(WorkflowString workflowXML) throws JesFault{
        logger.debug("in submit workflow");
        try {
            SubmitJobRequest req = facade.createSubmitJobRequest(workflowXML.getValue());
            return this.submitJob(req);
        }  catch (JesException e) {
            throw createFault("Could not submit job",e);
        }
    }
	
/** 
 * Submit a job to the execution service
 * @todo remove this extra method - merge with submitWorkflow(String); 
 * @param req abstract request object
 * @return unique identifier for the new job.
 * @throws JesFault
 */    
    public JobURN submitJob( SubmitJobRequest req ) throws JesFault {
        logger.debug("Submit Job");
		JobFactory factory = null ;
        Workflow job= null;
			
        try { 
	        factory = facade.getJobFactory() ;
	        factory.begin() ;
	        job = factory.createJob( req) ;
            nudger.scheduleNewJob(JesUtil.castor2axis(job.getJobExecutionRecord().getJobId()));                    		
			factory.end ( true ) ;   // Commit and cleanup                    		
            JobURN result = new JobURN(job.getJobExecutionRecord().getJobId().getContent());
            logger.debug("Submit Job: new URN = " + result.toString());
            return result;
        }
        catch(Exception jex ) {
        	logger.error(jex);
            if (job != null) {
                try {
                   factory.deleteJob(job);
                } catch (JobException e) {
                    logger.warn("failed to delete corrupted job - " + job.getJobExecutionRecord().getJobId() );
                }
            }
            throw createFault("Failed to submit job",jex);
        }
         	
    } // end of submitJob()
    

    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#cancelJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void cancelJob(JobURN arg0)   {
        logger.debug("Cancel Job:" + arg0.toString());
        try {
            nudger.abortJob(arg0);
        } catch (Exception jex) {
            logger.warn("cancelJob:",jex);
        }
    }

    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#deleteJob(org.astrogrid.jes.types.v1.JobURN)
     */
    public void deleteJob(JobURN arg0){
        logger.debug("Delete Job: " + arg0.toString());
        try {
            nudger.deleteJob(arg0);
        } catch (Exception jex) {
            logger.warn("deleteJob:",jex);
        }
    }


    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Standard Job Controller";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Bog standard implementation";
    }

    /**
     * @see org.astrogrid.jes.component.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }


    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#readJobList(org.astrogrid.community.beans.v1.axis._Account)
     */
    public WorkflowSummary[] readJobList(_Account arg0) throws JesFault {
        try {
        logger.debug("in read job list");
        JobFactory fac = facade.getJobFactory();
        Iterator i = fac.findUserJobs(Axis2Castor.convert(arg0));
        List itemList = new ArrayList();
        while (i.hasNext()) {
            Workflow w = (Workflow)i.next();
            WorkflowSummary item = new WorkflowSummary();
            item.setWorkflowName(w.getName());
            item.setJobUrn(JesUtil.castor2axis(w.getJobExecutionRecord().getJobId()));
            itemList.add(item);
        }

        return (WorkflowSummary[])itemList.toArray(new WorkflowSummary[]{});
        } catch (JesException e) {
            throw createFault("Failed to read workflow list",e);
        }
    }
    
    private JesFault createFault(String message,Exception e) {
        logger.info("failed with exception",e);
        JesFault jf = new JesFault(message);
        jf.setStackTrace(e.getStackTrace());
        jf.setFaultReason(e.getMessage());
        jf.setFaultCodeAsString(e.getClass().getName());
        return jf;
    }

    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#readJob(org.astrogrid.jes.types.v1.JobURN)

     */
    public WorkflowString readJob(JobURN arg0) throws JesFault {
        try {
            logger.debug("in readJob()");
        JobFactory fac = facade.getJobFactory();
        Workflow w = fac.findJob(Axis2Castor.convert(arg0));
        if ( w == null) {
            logger.error("Factory  returned null workflow");
            throw new JesFault("factory returned null workflow");
        }
        StringWriter sw = new StringWriter();
        w.marshal(sw); 
        sw.close(); 
        return new WorkflowString(sw.toString());
        } catch (Exception e) {
            throw createFault("Failed to read job",e);
        }        
    } // end of jobList()
       



} // end of class JobController
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

import org.astrogrid.community.beans.v1.axis._Account;
import org.astrogrid.jes.JesException;
import org.astrogrid.jes.component.ComponentDescriptor;
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
 * The <code>JobController</code> class represents one of the top level
 * components in the AstroGrid Job Entry System (JES), the other components
 * being the <code>JobScheduler</code> and the <code>JobMonitor</code>. 
 * <p>
 * The <code>JobController</code> accepts a request for job submission and
 * creates the necessary job structures within the Job database for scheduling
 * and tracking the Job through the AstroGrid system. It informs the JobScheduler
 * that there is a new candidate for scheduling before returning a reply which
 * contains the unique identifier for the job.
 * <p>
 * The mainline argument (the workflow) is held within the method submitJob(),
 * which should be referred to for further detail. The basic workflow is:
 *      1. Load the JobController properties (if not already loaded).
 *      2. Analyse the job submission document and create the appropriate 
 *         data structures within the Job database.
 *      3  Inform the JobScheduler.
 *      4. Format a reply, passing back the unique job identifier.
 * <p>	
 * The above does not cover use cases where errors occur.
 * <p>
 * An instance of a JobController is stateless, with some provisos:
 * 1. The JobController is driven by a properties file, held at class level.
 * 2. AstroGrid messages are held in a manner amenable to internationalization.
 * These are also loaded from a properties file, held at class level.
 * 3. Finally, and importantly, the JobController utilizes an entity which does
 * contain state - the Job entity, which currently represents a number of tables 
 * held in any suitable JDBC compliant database. However, again this is not 
 * an absolute restriction. Note well: the JobController does not hold Job
 * as an instance variable.
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
    
    /** adapter, to enable this class to implement the generated JobController interface */
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
	  * <p> 
	  * Represents the mainline workflow argument for the JobController. 
	  * <p>
	  * Shows the JobController to be a component with no state.
	  * It neither uses nor creates instance variables. In the EJB model 
	  * it would be considered a stateless session bean.
	  * 
	  * @param jobXML - The service request XML received as a String.
	  * @return A String containing the reponse document in XML.
	  * 
	  * @see SubmitJobRequest.xsd in CVS
	  * @see SubmitJobResponse.xsd in CVS
	  * 
	  * Bug#12   Jeff Lusted - 30-June-2003
	  **/     
    public JobURN submitJob( SubmitJobRequest req ) throws JesFault {
        logger.debug("in submit job");
		JobFactory factory = null ;
        Workflow job= null;

			
        try { 

	        factory = facade.getJobFactory() ;
	        factory.begin() ;
	        job = factory.createJob( req) ;
            nudger.scheduleNewJob(JesUtil.castor2axis(job.getJobExecutionRecord().getJobId()));                    		
			factory.end ( true ) ;   // Commit and cleanup                    		
            return new JobURN(job.getJobExecutionRecord().getJobId().getContent());
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
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#cancelJob(org.astrogrid.jes.types.v1.JobURN)
     * @todo implement - pass on to jobmonitor
     */
    public void cancelJob(JobURN arg0)  {
        logger.warn("call to cancelJob - unimplemented");
    }

    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#deleteJob(org.astrogrid.jes.types.v1.JobURN)
     * @todo implement - pass on to jobmonitor.
     */
    public void deleteJob(JobURN arg0){
        logger.warn("call to deleteJob - unimplemented");
    }

    /**
     * @see org.astrogrid.jes.delegate.v1.jobcontroller.JobController#readJobList(org.astrogrid.community.beans.v1.axis._Account)
     */
    public WorkflowSummary[] readJobList(_Account arg0) throws JesFault {
        try {
        logger.debug("in read job list");
        JobFactory fac = facade.getJobFactory();
        Iterator i = fac.findUserJobs(JesUtil.axis2castor(arg0));
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
        Workflow w = fac.findJob(JesUtil.axis2castor(arg0));
        if ( w == null) {
            throw new JesFault("factory returned null workflow");
        }
        StringWriter sw = new StringWriter();
        w.marshal(sw); 
        sw.close(); 
        System.out.println(sw.toString());
        return new WorkflowString(sw.toString());
        } catch (Exception e) {
            throw createFault("Failed to read job",e);
        }        
    } // end of jobList()
       



} // end of class JobController
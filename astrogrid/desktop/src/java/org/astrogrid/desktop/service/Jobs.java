/*$Id: Jobs.java,v 1.2 2005/02/22 01:10:31 nw Exp $
 * Created on 02-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.desktop.service.conversion.CastorBeanUtilsConvertor;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import org.apache.commons.beanutils.ConvertUtils;

import org.astrogrid.desktop.service.conversion.*;
import org.astrogrid.desktop.service.annotation.*;

/** Job management service.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Feb-2005
 *@@ServiceDoc("jobs","submit and manage workflows")
 */
public class Jobs {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Jobs.class);

    // register parameter converters here - link them into the default infrastructure,
    // then no need to annotate each method separately. :)
    static {
        ConvertUtils.register(CastorBeanUtilsConvertor.getInstance(),Workflow.class);
        ConvertUtils.register(CastorBeanUtilsConvertor.getInstance(),JobURN.class);

    }
    
    /** Construct a new Jes
     * 
     */
    public Jobs(Community community) {
        this.community = community;
    }
    protected final Community community;
    
    /** todo - need to check whether getWorkflowManager() creates a new instance each time.. - inefficient*/
    private JobExecutionService getJes() throws WorkflowInterfaceException {
        return community.getEnv().getAstrogrid().getWorkflowManager().getJobExecutionService();
    }
    
    private Account getAccount() {
        return community.getEnv().getAccount();
    }
    /**@@MethodDoc("list","list my jobs")
     * @@.return ReturnDoc("List of job identifiers",rts=ArrayResultTransformerSet.getInstance())
     * @todo add an axmlrpc convertor for this type.
     * @throws WorkflowInterfaceException
     */
    public JobURN[] list() throws WorkflowInterfaceException {
        WorkflowSummaryType[] summs = getJes().listJobs(getAccount());
        JobURN[] result = new JobURN[summs.length];
        for (int i = 0; i < summs.length; i++) {
            result[i] = summs[i].getJobId();
        }
        return result;
    }
    
    /**@@MethodDoc("fullList","list summaries for each job")
     * @@.return ReturnDoc("List of workflow summaries",rts=ArrayResultTransformerSet.getInstance())
     * @todo this method won't work at the moment - as underlying method doesn't return full info
     */
    public WorkflowSummaryType[] fullList() throws WorkflowInterfaceException {
        return getJes().listJobs(getAccount());
    }
    
    
    /** @@MethodDoc("getJob","Retreive a workflow transcript")
     * @@.return ReturnDoc("A workflow document",rts=WorkflowResultTransformerSet.getInstance())
     * @@.jobURN ParamDoc("jobURN","identifier to retrive workflow for");

     */
    public Workflow getJob(JobURN jobURN) throws WorkflowInterfaceException {
        return getJes().readJob(jobURN);
    }
    
    /**@throws WorkflowInterfaceException
     * @@MethodDoc("getJobSummary","Retreive summary for a job")
     * @@.return ReturnDoc("A summary of the jobs progress")
     * @@.jobURN ParamDoc("jobURN","identifier to retreive workflow for");
     */
    public String getJobSummary(JobURN jobURN) throws WorkflowInterfaceException {
        WorkflowSummaryType[] summs =  getJes().listJobs(getAccount());
        for (int i = 0; i < summs.length; i++) {
            if (summs[i].getJobId().equals(jobURN)) {
                return summs[i].getStatus().toString();
            }
        }
        return "not found";
        
    }
    
    /** @@MethodDoc("cancelJob","cancel a running job")
     * @.return ReturnDoc("true if successful",rts=BooleanResultTransformerSet.getInstance())
     * @.jobURN ParamDoc("jobURN","identifier of workflow to cancel")
     * @param jobURN
     * @return
     * @throws WorkflowInterfaceException
     */
    public boolean cancelJob(JobURN jobURN) throws WorkflowInterfaceException {
        getJes().cancelJob(jobURN);
        return true;
    }
    
    /** @@MethodDoc("deleteJob","delete a job")
     * @.return ReturnDoc("true if successful",rts=BooleanResultTransformerSet.getInstance())
     * @.jobURN ParamDoc("jobURN","identifier of workflow to delete")
     * @param jobURN
     * @return
     * @throws WorkflowInterfaceException
     */    
    public boolean deleteJob(JobURN jobURN) throws WorkflowInterfaceException {
        getJes().deleteJob(jobURN);
        return true;
    }
    
    /** @@MethodDoc("submitJob","Submit a job for execution")
     * @@.return ReturnDoc("unique identifier of the new job")
     * @@.workflow ParamDoc("workflow","document of job to execute")
     * @param workflow
     * @return
     * @throws WorkflowInterfaceException
     */
    public JobURN submitJob(Workflow workflow) throws WorkflowInterfaceException {
        return getJes().submitWorkflow(workflow);
    }

}


/* 
$Log: Jobs.java,v $
Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/
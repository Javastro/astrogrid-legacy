/*$Id: JesJobExecutionService.java,v 1.3 2004/03/15 17:01:01 nw Exp $
 * Created on 09-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.portal.workflow.impl;

import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.jes.delegate.JobSummary;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;

import java.net.URL;

/** Implementation of JobExecutionService over a Jes JobController delegate.
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Mar-2004
 *
 */
public class JesJobExecutionService implements JobExecutionService {

    /** fallback constructor - let jes delegate try to find its own endpoint.
     *  Construct a new JesJobExecutionService
     * @param endpoint
     */
    public JesJobExecutionService() {
        controller = JesDelegateFactory.createJobController();
        assert controller != null;
    }

    /** Construct a new JesJobExecutionService
     * 
     */
    public JesJobExecutionService(String endpoint) {
        controller = JesDelegateFactory.createJobController(endpoint);
    }
    protected final JobController controller;
    /**
     * @see org.astrogrid.portal.workflow.intf.JobExecutionService#submitWorkflow(org.astrogrid.workflow.beans.v1.Workflow)
     */
    public JobURN submitWorkflow(Workflow workflow) throws WorkflowInterfaceException {
        try {
        return controller.submitWorkflow(workflow);
        } catch(JesDelegateException e) {
            throw new WorkflowInterfaceException(e);
        }
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.JobExecutionService#deleteJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public void deleteJob(JobURN jobURN) throws WorkflowInterfaceException {
        try {
            controller.deleteJob(jobURN);
        } catch (JesDelegateException e) {
            throw new WorkflowInterfaceException(e);
        }
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.JobExecutionService#cancelJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public void cancelJob(JobURN jobURN) throws WorkflowInterfaceException {
        try {
            controller.cancelJob(jobURN);
        } catch (JesDelegateException e) {
            throw new WorkflowInterfaceException(e);
        }
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.JobExecutionService#readJob(org.astrogrid.workflow.beans.v1.execution.JobURN)
     */
    public Workflow readJob(JobURN jobURN) throws WorkflowInterfaceException {
        try { 
            return controller.readJob(jobURN);
        } catch (JesDelegateException e) {
            throw new WorkflowInterfaceException(e);
        }
    }
    /**
     * @see org.astrogrid.portal.workflow.intf.JobExecutionService#readJobList(org.astrogrid.community.beans.v1.Account)
     */
    public JobSummary[] readJobList(Account account) throws WorkflowInterfaceException {
        try {
            return controller.readJobList(account);
        } catch (JesDelegateException e) {
            throw new WorkflowInterfaceException(e);
        }
            
    }
}


/* 
$Log: JesJobExecutionService.java,v $
Revision 1.3  2004/03/15 17:01:01  nw
loosened type of endpoint for JesJobExecutionService from URL to String -
allows the dummy urn:test to be passed in.

Revision 1.2  2004/03/11 13:53:36  nw
merged in branch bz#236 - implementation of interfaces

Revision 1.1.2.2  2004/03/11 13:36:10  nw
added implementations for the workflow interfaces

Revision 1.1.2.1  2004/03/09 17:41:59  nw
created a bunch of implementations,
 
*/
/*$Id: JobsImpl.java,v 1.6 2005/07/08 14:06:30 nw Exp $
 * Created on 02-Feb-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/** Job management service.implementation
 */
public class JobsImpl implements Jobs, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JobsImpl.class);

    
    /** Construct a new Jes
     * 
     */
    public JobsImpl(Community community) {
        this.community = community;
        community.addUserLoginListener(this);
    }
    protected final Community community;
   private JobExecutionService jes; 
     private JobExecutionService getJes() throws WorkflowInterfaceException {
         if (jes == null) {
        jes = community.getEnv().getAstrogrid().getWorkflowManager().getJobExecutionService();
         } 
         return jes;
    }
    
    private Account getAccount() {
        return community.getEnv().getAccount();
    }

    public JobURN[] list() throws WorkflowInterfaceException {
        WorkflowSummaryType[] summs = getJes().listJobs(getAccount());
        JobURN[] result = new JobURN[summs.length];
        for (int i = 0; i < summs.length; i++) {
            result[i] = summs[i].getJobId();
        }
        return result;
    }
    
  
    public WorkflowSummaryType[] fullList() throws WorkflowInterfaceException {
        return getJes().listJobs(getAccount());
    }
    
    

    public Workflow getJob(JobURN jobURN) throws WorkflowInterfaceException {
        return getJes().readJob(jobURN);
    }
    

    public String getJobSummary(JobURN jobURN) throws WorkflowInterfaceException {
        WorkflowSummaryType[] summs =  getJes().listJobs(getAccount());
        for (int i = 0; i < summs.length; i++) {
            if (summs[i].getJobId().getContent().equals(jobURN.getContent())) {
                return summs[i].getStatus().toString();
            }
        }
        return "not found";
    }
    
 
    public void cancelJob(JobURN jobURN) throws WorkflowInterfaceException {
        getJes().cancelJob(jobURN);
    }
    

    public void deleteJob(JobURN jobURN) throws WorkflowInterfaceException {
        getJes().deleteJob(jobURN);
    }
    

    public JobURN submitJob(Workflow workflow) throws WorkflowInterfaceException {
        // check workflow belongs to us - if not, alter it.
        workflow.getCredentials().setAccount(getAccount());
        return getJes().submitWorkflow(workflow);
    }
    public JobURN submitJobFile(URL workflowURL) throws WorkflowInterfaceException, MarshalException, ValidationException, IOException {
        InputStream is = workflowURL.openStream();
        Workflow wf = Workflow.unmarshalWorkflow(new InputStreamReader(is));
        wf.getCredentials().setAccount(getAccount());        
        return getJes().submitWorkflow(wf);
    }

    public WorkflowSummaryType[] listSummaries() throws WorkflowInterfaceException {
        return getJes().listJobs(getAccount());
    }
    
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
    }

    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent e) {
        jes = null;
    }

}


/* 
$Log: JobsImpl.java,v $
Revision 1.6  2005/07/08 14:06:30  nw
final fixes for the workshop.

Revision 1.5  2005/05/12 15:59:11  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:40  clq2
1082

Revision 1.2.2.1  2005/04/25 11:18:51  nw
split component interfaces into separate package hierarchy
- improved documentation

Revision 1.2  2005/04/13 12:59:11  nw
checkin from branch desktop-nww-998

Revision 1.1.2.3  2005/04/05 11:42:14  nw
added 'submit' and 'summary' methods

Revision 1.1.2.2  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.1  2005/03/22 12:04:03  nw
working draft of system and ag components.

Revision 1.1.2.1  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.

Revision 1.2  2005/02/22 01:10:31  nw
enough of a prototype here to do a show-n-tell on.

Revision 1.1  2005/02/21 11:25:07  nw
first add to cvs
 
*/
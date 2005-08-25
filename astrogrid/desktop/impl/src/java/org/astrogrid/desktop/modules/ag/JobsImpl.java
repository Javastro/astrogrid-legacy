/*$Id: JobsImpl.java,v 1.2 2005/08/25 16:59:58 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.Myspace;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManagerFactory;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

/** Job management service.implementation
 * @todo refine to call job delegate directly - more efficient, and better exception reporting.
 */
public class JobsImpl implements Jobs, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JobsImpl.class);

    
    /** Construct a new Jes
     * 
     */
    public JobsImpl(Community community, Myspace vos) {
        this.community = community;
        this.vos = vos;
        community.addUserLoginListener(this);
    }
    protected final Community community;
    protected final Myspace vos;
   private JobExecutionService jes;
   private Account acc;
     private synchronized JobExecutionService getJes() throws WorkflowInterfaceException {
         if (jes == null) {
             WorkflowManagerFactory fac = new WorkflowManagerFactory();
             jes = fac.getManager().getJobExecutionService();
         } 
         return jes;
    }
    
    private synchronized Account getAccount() {
        if (acc == null) {
        acc = new Account();
        acc.setCommunity(community.getUserInformation().getCommunity());
        acc.setName(community.getUserInformation().getName());
        }
        return acc;
    }

    public URI[] list() throws ServiceException {
        try {
        WorkflowSummaryType[] summs = getJes().listJobs(getAccount());
        URI[] result = new URI[summs.length];
        for (int i = 0; i < summs.length; i++) {
            result[i] = cvt(summs[i].getJobId());
        }
        return result;
        } catch (WorkflowInterfaceException e) {
            throw new ServiceException(e);
        }
    }
    
  
    public WorkflowSummaryType[] fullList() throws WorkflowInterfaceException {
        return getJes().listJobs(getAccount());
    }
       
    public Document getJobTranscript(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        try {        
        Workflow wf =  getJes().readJob(cvt(jobURN));
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(wf, doc);
        return doc;
        } catch (Exception e) {
            throw new ServiceException(e);            
        }
    }
    
    private JobURN cvt(URI uri) {
        JobURN urn = new JobURN();
        urn.setContent(uri.toString());
        return urn;
    }
    
    private URI cvt(JobURN urn) throws ServiceException {       
        try {
            return new URI(urn.getContent());
        } catch (URISyntaxException e) {
            throw new ServiceException(e);
        }       
    }
    

    public ExecutionInformation getJobInformation(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        try {
            WorkflowSummaryType[] summs =  getJes().listJobs(getAccount());
            for (int i = 0; i < summs.length; i++) {
                if (jobURN.equals(cvt(summs[i].getJobId()))) {
                    String status =summs[i].getStatus().toString();
                    return new ExecutionInformation(
                        cvt(summs[i].getJobId())
                        ,summs[i].getWorkflowName()
                        ,summs[i].getDescription()
                        ,status
                        ,summs[i].getStartTime()
                        ,summs[i].getFinishTime()
                        );
                }
            }            
            } catch (WorkflowInterfaceException e) {
                throw new ServiceException(e);
            } 
            throw new NotFoundException(jobURN.toString());
    }
    
 
    public void cancelJob(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        try {
        getJes().cancelJob(cvt(jobURN));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    

    public void deleteJob(URI jobURN) throws NotFoundException, ServiceException, SecurityException {
        try {
        getJes().deleteJob(cvt(jobURN));
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }
    

    public URI submitJob(Document doc) throws ServiceException, InvalidArgumentException {
        // check workflow belongs to us - if not, alter it.
        try {
        Workflow workflow = (Workflow)Unmarshaller.unmarshal(Workflow.class, doc);
        workflow.getCredentials().setAccount(getAccount());
        return cvt(getJes().submitWorkflow(workflow));
        } catch (WorkflowInterfaceException e) {
            throw new ServiceException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }
    }
    public URI submitStoredJob(URI workflowReference) throws ServiceException, InvalidArgumentException {
        Workflow wf = null;
        try {
        if ( workflowReference.getScheme() == null || workflowReference.getScheme().equals("ivo")) {
            String content = vos.read(workflowReference);
            wf = Workflow.unmarshalWorkflow(new StringReader(content));            
        } else {
            InputStream is = workflowReference.toURL().openStream();
            wf = Workflow.unmarshalWorkflow(new InputStreamReader(is));
        }
        wf.getCredentials().setAccount(getAccount());        
        return cvt(getJes().submitWorkflow(wf));        
        } catch (WorkflowInterfaceException e) {
            throw new ServiceException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        } catch (NotFoundException e) {
            throw new InvalidArgumentException(e);
        } catch (SecurityException e) {
            throw new InvalidArgumentException(e);
        } catch (IOException e) {
            throw new InvalidArgumentException(e);
        } catch (NotApplicableException e) {
            throw new InvalidArgumentException(e);
        }
    }

    public ExecutionInformation[] listFully() throws ServiceException {
        try {
        WorkflowSummaryType[] summs =  getJes().listJobs(getAccount());
        ExecutionInformation[] result = new ExecutionInformation[summs.length];
        for (int i = 0; i < summs.length; i++) {
            String status =summs[i].getStatus().toString();
            result[i]= new ExecutionInformation(
                    cvt(summs[i].getJobId())
                    ,summs[i].getWorkflowName()
                    ,summs[i].getDescription()
                    ,status
                    ,summs[i].getStartTime()
                    ,summs[i].getFinishTime()
                    );
        }
        return result;
        } catch (WorkflowInterfaceException e) {
            throw new ServiceException(e);
        }
    }
    
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent e) {
    }

    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.desktop.modules.ag.UserLoginEvent)
     */
    public synchronized void userLogout(UserLoginEvent e) {
        acc = null;
    }

}


/* 
$Log: JobsImpl.java,v $
Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.8  2005/08/09 17:33:07  nw
finished system tests for ag components.

Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

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
/*$Id: JobsImpl.java,v 1.6 2006/04/18 23:25:44 nw Exp $
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.RemoteProcessManager;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManagerFactory;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;

/** Job management service.implementation
 */
public class JobsImpl implements JobsInternal, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JobsImpl.class);

    
    /** Construct a new Jes
     * 
     */
    public JobsImpl(Community community, RemoteProcessManager manager) {
        this.community = community;
        this.manager = manager;
    }
    protected final RemoteProcessManager manager;
    protected final Community community;
   private WorkflowManagerFactory fac;
   private Account acc;  

    private synchronized WorkflowManagerFactory getFactory() {
        if (fac == null) {
            fac = new WorkflowManagerFactory();
        } 
        return fac;
    }

    private synchronized Account getAccount() {
        if (acc == null) {
        acc = new Account();
        acc.setCommunity(community.getUserInformation().getCommunity());
        acc.setName(community.getUserInformation().getName());
        }
        return acc;
    }

    public Document createJob() throws ServiceException {
        Workflow wf = createWorkflow();
        try {
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(wf, doc);
        return doc;
        } catch(Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     * @see org.astrogrid.acr.astrogrid.Jobs#wrapTask(org.w3c.dom.Document)
     */
    public Document wrapTask(Document arg0) throws ServiceException {
        try {
        Workflow wf = createWorkflow();
        Tool  t = (Tool)Unmarshaller.unmarshal(Tool.class,arg0);
        Step step = new Step();
        step.setDescription("Wrapped Task");
        step.setName("Task #1");
        step.setTool(t);       
        wf.getSequence().addActivity(step);
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(wf,doc);
        return doc;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }    
    
    public Workflow createWorkflow() throws ServiceException {
        Credentials creds = new Credentials();
        creds.setAccount(getAccount());
        creds.setSecurityToken("ignored");
        Group group = new Group();
        group.setCommunity(community.getUserInformation().getCommunity());
        group.setName(community.getUserInformation().getName());
        creds.setGroup(group);
        try {
            return getFactory().getManager().getWorkflowBuilder().createWorkflow(creds,"New Workflow","Enter Description");
        } catch (WorkflowInterfaceException e) {
            throw new ServiceException(e);
        }
    }
    
    public URI[] list() throws ServiceException {
        //  implement as filter on list from manager.
        Collection c = new ArrayList(Arrays.asList(manager.list()));
        CollectionUtils.filter(c,new Predicate() {
            public boolean evaluate(Object arg0) {
                return "jes".equals(((URI)arg0).getScheme());
            }
        });
        return (URI[])c.toArray(new URI[]{});     
    }
    
  

       
    public Document getJobTranscript(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {        
        String val = (String)manager.getResults(jobURN).get("transcript");
        InputStream is= new ByteArrayInputStream(val.getBytes());
        try {
            return XMLUtils.newDocument(is);
        } catch (Exception e) {
            throw new ServiceException("Failed to parse result as job transcript",e);
        } 
    }
    
  
    public ExecutionInformation getJobInformation(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        return manager.getExecutionInformation(jobURN);     
    }
    
 
    public void cancelJob(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        manager.halt(jobURN);
    }
    

    public void deleteJob(URI jobURN) throws NotFoundException, ServiceException, SecurityException {
        try {
            manager.delete(jobURN);
        } catch (InvalidArgumentException e) {
            throw new NotFoundException(e); // just wrap it up as something else.
        } 
    }
    

    public URI submitWorkflow(Workflow workflow) throws InvalidArgumentException, ServiceException {
        try {
        Document doc = XMLUtils.newDocument(); 
        Marshaller.marshal(workflow,doc);
       return manager.submit(doc);
        } catch (SecurityException e) {
            throw new ServiceException(e);
        } catch (NotFoundException e) {
            throw new InvalidArgumentException(e);            
        } catch (ParserConfigurationException e) {
            throw new ServiceException(e);
        } catch (CastorException e) {
            throw new InvalidArgumentException(e);
            
        } 
    }
    public URI submitJob(Document doc) throws ServiceException, InvalidArgumentException {
        try {
        return manager.submit(doc);
        } catch (SecurityException e) {
            throw new ServiceException(e);
        } catch (NotFoundException e) {
            throw new InvalidArgumentException(e);            
        }         
    }
    public URI submitStoredJob(URI workflowReference) throws ServiceException, InvalidArgumentException {
        try {
            return manager.submitStored(workflowReference);

        } catch (SecurityException e) {
            throw new ServiceException(e);
        } catch (NotFoundException e) {
            throw new InvalidArgumentException(e);            
        } 
    }

    public ExecutionInformation[] listFully() throws ServiceException {
        URI[] jobs = this.list(); // get filtered list of job ids.
        ExecutionInformation[] result = new ExecutionInformation[jobs.length];
        try {
        for (int i = 0; i < jobs.length; i++) {
            result[i] = manager.getExecutionInformation(jobs[i]);
        }
        return result;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
   
    }

	public void userLogin(UserLoginEvent arg0) {
	}

	public void userLogout(UserLoginEvent arg0) {
		this.acc = null;
		this.fac = null;
	}



}


/* 
$Log: JobsImpl.java,v $
Revision 1.6  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.5.30.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.5  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.4.2.1  2005/11/23 04:52:18  nw
implemented new method

Revision 1.4  2005/11/10 12:05:43  nw
big change around for vo lookout

Revision 1.3  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

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
/*$Id: JesStrategyImpl.java,v 1.8 2006/07/20 12:30:15 nw Exp $
 * Created on 05-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal;
import org.astrogrid.desktop.modules.ag.MessagingInternal;
import org.astrogrid.desktop.modules.ag.RemoteProcessStrategy;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.ag.MessagingInternal.SourcedExecutionMessage;
import org.astrogrid.desktop.modules.ag.recorder.ResultsExecutionMessage;
import org.astrogrid.desktop.modules.ag.recorder.StatusChangeExecutionMessage;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.mortbay.util.MultiException;
import org.w3c.dom.Document;


/** 
 * remote process strategy for jobs.
 *  - see RemoteProcessManagerImpl
 * periodically poll job service, inject messages into the system
 * temporary , until jes supports CEA interface like it should.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2005
 *
 */
public class JesStrategyImpl implements RemoteProcessStrategy, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JesStrategyImpl.class);

    public JesStrategyImpl(MessagingInternal messaging
            , MessageRecorderInternal recorder
            , Community comm
            , ApplicationsInternal apps
            , UIInternal ui
            ) {
        super();
        this.messaging = messaging;
        this.comm = comm;    
        this.apps = apps;
        this.recorder = recorder;
        this.ui = ui;
    }
    final Community comm;
    final MessagingInternal messaging;
    final MessageRecorderInternal recorder;
    final ApplicationsInternal apps;
    final UIInternal ui;
    private Account acc;      
    boolean poll = false;
    
    private JobController jes;
    private synchronized JobController getJes() {
        if (jes == null) {
            jes = JesDelegateFactory.createJobController();
        } 
        return jes;
   }

    private synchronized Account getAccount() {
        if (acc == null) {
        acc = new Account();
        acc.setCommunity(comm.getUserInformation().getCommunity());
        acc.setName(comm.getUserInformation().getName());
        }
        return acc;
    }
    public long getPeriod() {
        return 1000 * refreshSeconds;
    }
    private long refreshSeconds;
    public void setRefreshSeconds(long period) {
    	this.refreshSeconds = period;
    }
    
    

	public BackgroundWorker createWorker() {
		return new BackgroundWorker(ui,"Checking for workflow progress") {
			protected Object construct() throws Exception {
				List errors = null;
			       if (!poll) { // only run if this flag is set.
			            return null;
			        }
			       WorkflowSummaryType[] arr = getJes().listJobs(getAccount());
			        for (int i = 0 ; i < arr.length; i++) {
			        	try {
			        		checkSingleJob(arr[i]);
			        	} catch (Exception e) {
    						if (errors == null) {
    							errors = new ArrayList();
    						}			        		
			        		errors.add(e);
			        	}
			        } 
			         return errors;
			    }
			  		// report any that we failed to refresh.
		    		protected void doFinished(Object result) {
		    			List l = (List)result;
		    			if (l != null && l.size() > 0) {
		    				MultiException e = new MultiException();
		    				for (Iterator i = l.iterator(); i.hasNext(); ) {
		    					e.add((Exception)i.next());
		    				}
		    				/*@fixme popping up a modal error dialog everytime a service is unavailable is too intrusive.
		    				especially as it happens as a regular scheduled task. will just log for now, and work out what to do later.		    				
		    				parent.showError("Failed to check status of some workflows",e);
		    				*/
		    				logger.warn("Failed to check status of some workflows",e);
		    			}
		    		}			    
			    /** check a single job for updates.
			     * to be called within shceduler thread.
			     * @param jobSummary
			     * @throws URISyntaxException 
			     * @throws IOException 
			     * @throws JesDelegateException 
			     * @throws JesDelegateException
			     * @throws URISyntaxException 
			     * @throws ValidationException 
			     * @throws MarshalException 
			     */
			    private void checkSingleJob(WorkflowSummaryType jobSummary) throws IOException, JesDelegateException, CastorException, URISyntaxException {			        
			            Folder f= recorder.getFolder(new URI(jobSummary.getJobId().getContent()));
			            String currentStatus;
			            int messageCount;            
			            if (f == null) {
			                // totally new.. upload everything
			                currentStatus = ExecutionInformation.UNKNOWN;
			                messageCount = 0;
			            } else if (f.isDeleted()) {
			            	// don't care about thiis one anymore
			            	return;
			            } else {                    
			                currentStatus = f.getInformation().getStatus();
			                if(isCompletedOrError(currentStatus) ) {
			                   return; // nothing moreto report here.
			                } // otherwise..
			                messageCount = recorder.listFolder(f).length;
			            }
			            String newStatus = jobSummary.getStatus().toString();            
			            if (! currentStatus.equals(newStatus)) {   
			            	ExecutionMessage em = new StatusChangeExecutionMessage(
			            		    jobSummary.getJobId().getContent()
			            			,newStatus
			            			,new Date()
			            			);
			            	SourcedExecutionMessage sem = new SourcedExecutionMessage(
			            			new URI(jobSummary.getJobId().getContent())
			                        ,jobSummary.getWorkflowName()
			            			,em
			            			  ,jobSummary.getStartTime()
			                          ,jobSummary.getFinishTime()
			            			);
			                messaging.injectMessage(sem);   
			            }
			           
			            if (messageCount < jobSummary.getMessageCount()) { // some messages to pass on
			                for (int j = messageCount;  j < jobSummary.getMessageCount(); j++) {
			            		MessageType mt = jobSummary.getMessage(j);
			            		ExecutionMessage em = new ExecutionMessage(
			            				jobSummary.getJobId().getContent()
			            				,mt.getLevel().toString()
			            				,mt.getPhase().toString()
			            				,mt.getTimestamp()
			            				,mt.getContent()
			            		);
			                	SourcedExecutionMessage sem = new SourcedExecutionMessage(
			                			new URI(jobSummary.getJobId().getContent())
			                            ,jobSummary.getWorkflowName()
			                			,em
			                			  ,jobSummary.getStartTime()
			                              ,jobSummary.getFinishTime()
			                			);                    
			                    messaging.injectMessage(sem);
			                }
			            }
			            if (isCompletedOrError(newStatus)) { // emit result message  
			                Workflow wf = getJes().readJob(jobSummary.getJobId());
			                wf.getJobExecutionRecord().clearExtension(); // clears out junk
			                ResultListType results = new ResultListType();
			                ParameterValue v = new ParameterValue();
			                v.setIndirect(false);
			                v.setName("transcript");
			                StringWriter s = new StringWriter();
			                wf.marshal(s);
			                v.setValue(s.toString());
			                results.setResult(new ParameterValue[]{v });
			                ExecutionMessage em = new ResultsExecutionMessage(
			                		jobSummary.getJobId().getContent()
			                		,jobSummary.getFinishTime()
			                		,results
			                		);
			            	SourcedExecutionMessage sem = new SourcedExecutionMessage(
			            			new URI(jobSummary.getJobId().getContent())
			                        ,jobSummary.getWorkflowName()
			            			,em
			            			  ,jobSummary.getStartTime()
			                          ,jobSummary.getFinishTime()
			            			);                   
			                messaging.injectMessage(sem);             
			        }
			    }

		};
	}

 
    public static boolean isCompletedOrError(String s) {
        return s.equals(ExecutionInformation.COMPLETED) 
        || s.equals(ExecutionInformation.ERROR);
    }


    // user login listener interface
    public void userLogin(UserLoginEvent arg0) {
        // enable polling of the server.
        poll = true;
        // do a poll straight away, to get things started.
        createWorker().start();
        
    }

    public synchronized void userLogout(UserLoginEvent arg0) {
        poll = false;
        acc = null;
    }

    // remote process strategy.
    public boolean canProcess(URI execId) {
        return "jes".equals(execId.getScheme());
    }

    public String canProcess(Document doc) {    
        try {
            Workflow w = (Workflow)Unmarshaller.unmarshal(Workflow.class,doc);
            return "workflow: " + w.getName(); 
        } catch (Exception e) {
            return null;
        }            
    }

    public URI submit(Document doc) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        try {
            Workflow wf = (Workflow) Unmarshaller.unmarshal(Workflow.class, doc);
            adjustWorkflow(wf);
            URI uri =  cvt(getJes().submitWorkflow(wf));
            triggerUpdate(); // give the user some feedback asap.
            return uri;           
            } catch (CastorException e) {
                throw new InvalidArgumentException("Malformed workflow",e);
        } catch (JesDelegateException e) {
            throw new ServiceException("Failed to submit document to service",e);
        } 
    }
    
    // schedule a poll of this job to happen as soon as possible
    // (needs to be on scheduler thread because of multithreading issues)    
    // would like to be able to poll a single job - but delegate doesn't provide methods for this
    // so may as well trigger update of whole lot.
    public void triggerUpdate() {
        createWorker().start();
    }
    
    // make the workflow belong to the current user, fiddle the stirng adql, etc.
    private void adjustWorkflow(Workflow workflow) throws ServiceException, InvalidArgumentException{
        workflow.getCredentials().setAccount(getAccount());
        // fiddle any string-adql..
        //@todo improve this query, to cut down on amout of work..       
        Iterator i = workflow.findXPathIterator("//tool[input/parameter/indirect='false']" ); // find all tools with at least one inline parameter.
        while(i.hasNext()) {
            Tool t = (Tool)i.next();
            try {
            ApplicationInformation info =apps.getInfoForTool(t);
            apps.translateQueries(info,t);
            } catch (NotFoundException e) {
                throw new InvalidArgumentException("Workflow contains unknown tool " + t.getName(),e);
            }
        }        
    }

    public URI submitTo(Document doc, URI service) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {
        // only handle a single jes server at the moment - so always send to the defailt.
        logger.warn("Sending to default jes server");
        return submit(doc);
    }

    public void halt(URI arg0) throws NotFoundException, InvalidArgumentException, ServiceException, SecurityException {
        try {
            getJes().cancelJob(cvt(arg0));
        } catch (JesDelegateException e) {
            throw new ServiceException("Failed to halt job",e);
        }
    }

    public void delete(URI arg0) throws NotFoundException, ServiceException, SecurityException {
        try {
            getJes().deleteJob(cvt(arg0));
        } catch (JesDelegateException e) {
            throw new ServiceException("Failed to delete job",e);
        }    
    }

    public Map getLatestResults(URI jobURN) throws ServiceException, SecurityException, NotFoundException, InvalidArgumentException {        
            try {
                Workflow wf = getJes().readJob(cvt(jobURN));
                wf.getJobExecutionRecord().clearExtension(); // remove a load of implementation-junk that takes up a _lot_ of space.
            StringWriter sw = new StringWriter();
             wf.marshal(sw);
            HashMap m = new HashMap();
            m.put("transcript",sw.toString());
            return m;
            } catch (JesDelegateException e) {
                throw new ServiceException("Failed to communicate with job server",e);
            } catch (CastorException e) {
                throw new ServiceException("Failed to process resultr",e);
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


}


/* 
$Log: JesStrategyImpl.java,v $
Revision 1.8  2006/07/20 12:30:15  nw
fixed to not display errors if refresh fails.

Revision 1.7  2006/06/27 10:26:11  nw
findbugs tweaks

Revision 1.6  2006/05/26 15:18:43  nw
reworked scheduled tasks,

Revision 1.5  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.4.30.3  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.4.30.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.4.30.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.4  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.3.2.2  2005/11/23 18:09:28  nw
tuned up.

Revision 1.3.2.1  2005/11/23 04:50:49  nw
tidied up

Revision 1.3  2005/11/11 17:53:27  nw
added cea polling to lookout.

Revision 1.2  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.1  2005/11/10 10:46:58  nw
big change around for vo lookout
 
*/
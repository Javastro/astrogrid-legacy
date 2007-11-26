/*$Id: JesStrategyImpl.java,v 1.17 2007/11/26 14:44:46 nw Exp $
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

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.RemoteProcessStrategy;
import org.astrogrid.desktop.modules.ag.TimerDrivenProcessMonitor;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobExecutionRecord;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;


/** 
 * remote process strategy for jobs.
 *  - see RemoteProcessManagerImpl
 * periodically poll job service, inject messages into the system
 * only here for backwards compatability - expected to go eventually.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 05-Nov-2005
 *
 */
public class JesStrategyImpl implements RemoteProcessStrategy {
	/** monitor for a single jes workflow */
	private class WorkflowMonitor extends TimerDrivenProcessMonitor {
	    
		private final Workflow wf;

        public WorkflowMonitor(final Workflow wf) {
            super(vfs);
			this.wf = wf;
            name = wf.getName();
			description = wf.getDescription();
		} 

		// retrieve results straight from service, unless we've got them already.
		public Map getResults() throws ServiceException, SecurityException,
		NotFoundException, InvalidArgumentException {
			if (resultMap.size() != 0) { // already got results - just return these
				return resultMap;
			}
			// ask server for interim results.
			try {
				Workflow wf = getJes().readJob(cvt(getId()));
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

		public void halt() throws NotFoundException, InvalidArgumentException,
		ServiceException, SecurityException {
			try {
				getJes().cancelJob(cvt(getId()));
			} catch (JesDelegateException e) {
				throw new ServiceException("Failed to halt job",e);
			}			
		}
		
		/** ignores any suggestion of what server to use 
		 * @throws ServiceException */
        public void start(URI serviceId) throws ServiceException {
            this.start(); 
        }

        public void start() throws ServiceException {
            try {
            setId(cvt(getJes().submitWorkflow(wf)));
            sched.schedule(this);
            } catch (JesDelegateException e) {
                throw new ServiceException("Failed to submit document to service",e);
            }             
        }		

		public void cleanUp() {
			super.cleanUp();
			try {
				getJes().deleteJob(cvt(getId()));
			} catch (JesDelegateException e) {
				logger.warn("Failed to delete job " + getId()+  " : " + e.getMessage());
				logger.debug("failed to delete job",e);
			}    			
		}

		// different amounts of time to wait, depending on what we've found.
		public static final long VERY_SOON = 1000 * 30;
		public static final long SOON = 1000 * 60;
		public static final long A_BIT = 1000 * 60 * 5;

		public DelayedContinuation execute() {  
			runAgain = A_BIT;
			try {
				final Workflow wf = getJes().readJob(cvt(getId()));
				final JobExecutionRecord jer = wf.getJobExecutionRecord();
				jer.clearExtension(); // frees a bit of space right away.

				if (getStatus() ==  ExecutionInformation.UNKNOWN) { // brrand new.
					runAgain = SOON; // poll more often.
					if (jer.getStartTime() != null) {
						startTime = jer.getStartTime();
					}
				}            

				final ExecutionPhase phase = jer.getStatus();
				String newStatus = phase.toString();   // string equivalent, even though it's a different enumeration
				if (! getStatus().equals(newStatus)) { // things have moved on.
					runAgain = VERY_SOON;
					setStatus(newStatus);
				}

				// process new messages
				for (int j = messages.size();  j < jer.getMessageCount(); j++) {
					runAgain = VERY_SOON;
					MessageType mt = jer.getMessage(j);
					addMessage( new ExecutionMessage(
							getId().toString()
							,mt.getLevel().toString()
							,mt.getPhase().toString()
							,mt.getTimestamp()
							,mt.getContent()
					));
				}

				// just completed.
				if (newStatus.equals(ExecutionInformation.ERROR)) {
					return null; // we're done.
				} else if (newStatus.equals(ExecutionInformation.COMPLETED)) { // emit result message  
					StringWriter sw = new StringWriter();
					try {
						wf.marshal(sw);
						addResult("transcript","trascript.xml",sw.toString());
						HashMap m = new HashMap();
						m.put("transcript",sw.toString());

						fireResultsReceived(resultMap);
						return null; // all finished;
					} catch (CastorException x) {
						logger.error("MarshalException",x);
						runAgain = A_BIT;
					} 
				}
			} catch (Exception e) {
				logger.error("Failed to contact service",e);
				runAgain = A_BIT;
			}
			return this;
		}

		private long runAgain = VERY_SOON;
		public long getDelay() {
			return runAgain;
		}

		public Principal getPrincipal() {
			return sess.currentSession();
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

        public void refresh() throws ServiceException {
            execute();
        }

        public String getTitle() {
            return "Tracking execution of " + wf.getName();
        }		

	}

	/**
	 * Commons Logger for this class
	 */
	static final Log logger = LogFactory.getLog(JesStrategyImpl.class);
    private final FileSystemManager vfs;

	public JesStrategyImpl( Community comm
			, ApplicationsInternal apps
			,FileSystemManager vfs
			, SessionManagerInternal sess
			, SchedulerInternal sched
	) {
		super();
		this.comm = comm;    
		this.apps = apps;
        this.vfs = vfs;
		this.sess = sess;
		this.sched = sched;
	}
	final Community comm;
	final ApplicationsInternal apps;
	final SessionManagerInternal sess;
	final SchedulerInternal sched;
	private Account acc;      

	private JobController jes;
	synchronized JobController getJes() {
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
	
    public ProcessMonitor create(Document doc) throws InvalidArgumentException, ServiceException {
        try {
        Workflow wf = (Workflow) Unmarshaller.unmarshal(Workflow.class, doc);
        adjustWorkflow(wf);
        WorkflowMonitor mon = new WorkflowMonitor(wf);
        return mon;
        } catch (CastorException e) {
            throw new InvalidArgumentException("Malformed workflow",e);
        }      
    }

	// make the workflow belong to the current user, fiddle the stirng adql, etc.
	private void adjustWorkflow(Workflow workflow) throws InvalidArgumentException, ServiceException{
		workflow.getCredentials().setAccount(getAccount());
		// fiddle any string-adql..     
		Iterator i = workflow.findXPathIterator("//tool[input/parameter/indirect='false']" ); // find all tools with at least one inline parameter.
		while(i.hasNext()) {
			Tool t = (Tool)i.next();
			try {
				CeaApplication info =apps.getInfoForTool(t);
				apps.translateQueries(info,t);
			} catch (NotFoundException e) {
				throw new InvalidArgumentException("Workflow contains unknown tool " + t.getName(),e);
			}
		}        
	}

}


/* 
$Log: JesStrategyImpl.java,v $
Revision 1.17  2007/11/26 14:44:46  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.16  2007/11/20 06:31:38  nw
re-fixed result parameter naming so that that file-types are provided, yet AR interface isn't affected.

Revision 1.15  2007/08/13 19:29:48  nw
merged mark's and noel's changes.

Revision 1.14.4.1  2007/08/09 19:08:21  nw
Complete - task 126: Action to 'Reveal' location of remote results in FileExplorer

Complete - task 122: plastic messaging of myspace resources

Revision 1.14  2007/07/30 17:59:56  nw
RESOLVED - bug 2257: More feedback, please
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2257

Revision 1.13  2007/07/13 23:14:55  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.12  2007/04/18 15:47:11  nw
tidied up voexplorer, removed front pane.

Revision 1.11  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.10  2006/08/31 21:29:28  nw
doc fix.

Revision 1.9  2006/08/15 10:15:34  nw
migrated from old to new registry models.

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
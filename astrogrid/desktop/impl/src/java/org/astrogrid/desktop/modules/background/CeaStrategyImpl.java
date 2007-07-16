/*$Id: CeaStrategyImpl.java,v 1.17 2007/07/16 12:21:23 nw Exp $
 * Created on 11-Nov-2005
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.CeaHelper;
import org.astrogrid.desktop.modules.ag.AbstractProcessMonitor;
import org.astrogrid.desktop.modules.ag.ProcessMonitor;
import org.astrogrid.desktop.modules.ag.RemoteProcessStrategy;
import org.astrogrid.desktop.modules.ag.TimerDrivenProcessMonitor;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.w3c.dom.Document;


/** remote process strargey for cea.
 * see RemoteProcessManagerImpl
 * periodically poll remote cea servers, injjct messages into the system
 *  - temporary, until cea actually calls back into the workbench.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 11-Nov-2005
 *
 */
public class CeaStrategyImpl implements RemoteProcessStrategy{
	/** monitor for a single remote cea task */
	private class RemoteTaskMonitor extends TimerDrivenProcessMonitor {
		
		public RemoteTaskMonitor(String ceaid, CeaApplication app, CeaService service) throws ServiceException {
			super(ceaHelper.mkRemoteTaskURI(ceaid,service));
			this.ceaid = ceaid;
			this.delegate = ceaHelper.createCEADelegate(service);
			this.name = app.getTitle();
			this.description = app.getContent().getDescription();
			// kick it off.
    		try {
				if (delegate.execute(ceaid)) {
					// task has started, so register this monitor with the scheduler...
					sched.schedule(this);
				} else {
					signalError("Failed to start application, for unknown reason");
				}
			} catch (CEADelegateException x) {
				signalError("Failed to start application: " + x.getMessage());
			}    	

		}
		private final CommonExecutionConnectorClient delegate;
		private final String ceaid;

	    /**
	     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#getLatestResults(java.net.URI)
	     */
		public Map getResults() throws ServiceException, SecurityException,
		NotFoundException, InvalidArgumentException {
			if (resultMap.size() != 0) { // already got results - just return these
				return resultMap;
			}
			try {
			// ask server for interim results.
	            final ResultListType results = delegate.getResults(ceaid);
	            Map map = new HashMap();
	            ParameterValue[] vals = results.getResult();        
	            for (int i = 0; i < vals.length; i++) {
	                map.put(vals[i].getName(),vals[i].getValue());
	            }
	            return map;
	        } catch (CEADelegateException e) {
	            throw new ServiceException(e);
	        }  
	    }		
		
		public void halt() throws NotFoundException, InvalidArgumentException,
				ServiceException, SecurityException {
	        try { 
	            delegate.abort(ceaid);
	        } catch (CEADelegateException e) {
	            throw new ServiceException(e);
	        } 			
		}
		
		//@todo find out how to cleanup a remote service.

		public static final long SHORTEST = 1000 * 5;
		public static final long LONGEST = 1000 * 60 * 10;
		public static final double FACTOR = 2.5;
		
		private void standOff() {
            runAgain = Math.min(LONGEST,Math.round(runAgain*FACTOR)); // stand off for three times as long, up to a limit.
		}
		
		public DelayedContinuation execute() {
			//@ffuture - should I just create a single delegate and hang onto it?
			try {
				MessageType qes = delegate.queryExecutionStatus(ceaid);
				String newStatus = qes.getPhase().toString(); 

				if  (getStatus().equals(newStatus)) { // nothing changed.
					standOff();
					return this;
				}

				// something interesting has happended - shorten the run-again period again.
				runAgain = SHORTEST;

				if (getStatus() == ExecutionInformation.UNKNOWN) {// brand new
					// use the messae time as our execution start - a bit of a fudge.
					startTime = qes.getTimestamp();
				}

				// ok, send a status-change message
				ExecutionMessage em = new ExecutionMessage(
						getId().toString()
						,"information"
						,newStatus
						,qes.getTimestamp()
						, "Status changed to " + newStatus
				);
				addMessage(em);
				fireStatusChanged(newStatus);

				if (newStatus.equals(ExecutionInformation.ERROR) 
						||newStatus.equals(ExecutionInformation.COMPLETED)) {
					// retrive the results.
					ExecutionSummaryType summ = delegate.getExecutionSumary(ceaid);
					if (summ != null && summ.getResultList() != null) {
						ParameterValue[] arr = summ.getResultList().getResult();
						for (int i = 0 ; i < arr.length; i++) {
							addResult(arr[i].getName(),arr[i].getValue());
						}
						fireResultsReceived(resultMap);
						// done
						return null;
					}
				}
				return this;
			} catch (CEADelegateException x) {
				standOff();
				return this;
			}

		}

		private long runAgain = SHORTEST;
		public long getDelay() {
			return runAgain;
		}

		public Principal getPrincipal() {
			return sess.currentSession();
		}
	}
	
	/** monitor for a single local cea task 
	 * 
	 * peeks directly into the local cea engine to get stuff - by implementing observer
	 * */
	private class LocalTaskMonitor extends AbstractProcessMonitor implements Observer {

		private final String ceaid;
		private final Application application;

		public LocalTaskMonitor(String ceaid, CeaApplication appDesc) throws ServiceException {
			super(ceaHelper.mkLocalTaskURI(ceaid));
			this.ceaid = ceaid;
			this.name = appDesc.getTitle();
			this.description = appDesc.getContent().getDescription();
			// register an interest to messages and status changes.
			this.application = ceaInternal.getExecutionController().getApplication(ceaid);
			// now, once everythiong is ready, start the application
			try {
				if (! ceaInternal.getExecutionController().execute(ceaid)) {
					signalError("Failed to start application, for unknown reason");
				} else {
					// attach myself as a listener for further events.
					this.application.addObserver(this);
				}
			} catch (CeaException x) {
				signalError("Failed to start application: " + x.getMessage());
			}		
			
		}
		// observer interface.
	    public void update(final Observable o, final Object arg) {
	    	if (arg instanceof Status) {
	    		Status stat = (Status)arg;    
	    		fireStatusChanged(stat.toExecutionPhase().toString());

	    		if (stat.equals(Status.COMPLETED) || stat.equals(Status.ERROR)) {// send a results notification.
	    			fireResultsReceived(cvtResultList2Map(application.getResult()));
	    		}
	    	} else if (arg instanceof MessageType) {
	    		MessageType mt = (MessageType)arg;
	    		ExecutionMessage em = new ExecutionMessage(
	    				getId().toString()
	    				,mt.getLevel().toString()
	    				,mt.getPhase().toString()
	    				,mt.getTimestamp()
	    				,mt.getContent()
	    		);
	    		addMessage(em);
	    	}
	    }
	    
	    // overridden methods - look directly at the application.
		public String getStatus() {
			return application.getStatus().toExecutionPhase().toString();
		}
		
		public Map getResults() throws ServiceException, SecurityException,
		NotFoundException, InvalidArgumentException {
			try {
				ResultListType results = ceaInternal.getQueryService().getResults(ceaid);
				return cvtResultList2Map(results);
			} catch (CeaException e) {
				throw new ServiceException(e);
			}              
		}
		/**
		 * @param results
		 * @return
		 */
		private Map cvtResultList2Map(ResultListType results) {
			Map map = new HashMap();
			ParameterValue[] vals = results.getResult();        
			for (int i = 0; i < vals.length; i++) {
				map.put(vals[i].getName(),vals[i].getValue());
			}
			return map;
		}

					
		public void cleanUp() {
			super.cleanUp();
			// remove the local record.
            ceaInternal.getExecutionController().delete(ceaid);
		}
		
		public void halt() throws NotFoundException, InvalidArgumentException,
				ServiceException, SecurityException {
	        try { 
	             ceaInternal.getExecutionController().abort(ceaid);	           
	        } catch (CeaException e) { //@todo improve this - match subtypes to different kinds of excpeiton.
	            throw new ServiceException(e);
	        }		
		}
	}
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CeaStrategyImpl.class);

    private final ApplicationsInternal apps;
    private final CeaHelper ceaHelper;
    private final TasksInternal ceaInternal;
    private final CommunityInternal community;
	private final SessionManagerInternal sess;
	private final SchedulerInternal sched;   
    
    /** Construct a new CeaStrategyImpl
     * 
     */
    public CeaStrategyImpl(TasksInternal ceaInternal
            , Registry reg
            , ApplicationsInternal apps
            , CommunityInternal community, SessionManagerInternal sess, SchedulerInternal sched) {
        super();         
        this.apps = apps;
        this.ceaInternal = ceaInternal;
        this.ceaHelper = new CeaHelper(reg);
        this.community = community;
        this.sess= sess;
        this.sched = sched;
    }

    /** this strategy can process anything that's a cea document
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#canProcess(org.w3c.dom.Document)
     */
    public String canProcess(Document doc) {
        try {
            Tool t = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
            return t.getName();
        } catch (Exception e) {
            return null;
        }         
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#canProcess(java.net.URI)
     */
    public boolean canProcess(URI execId) {
        return "ivo".equals(execId.getScheme()) || "local".equals(execId.getScheme());
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#submit(org.w3c.dom.Document)
     */
    public ProcessMonitor submit(Document doc) throws ServiceException, SecurityException, NotFoundException,
    InvalidArgumentException {
    	try {
    		Tool tool = ceaHelper.parseTool(doc);

    		URI appId = new URI("ivo://" + tool.getName());
    		Service[] arr = apps.listServersProviding(appId);

    		Service target = null;
    		switch(arr.length) {            
    		case 0:
    			logger.debug("No providers");
    			throw new NotFoundException(appId +" has no registered providers");
    		case 1:
    			target = arr[0];
    			logger.debug("One provider " + target.getId()); 
    			break;
    		default:
    			List l =  Arrays.asList(arr);
    			Collections.shuffle(l);
    			target = (Service)l.get(0);
    			logger.debug("Multiple providers, selected " + target.getId());
    		}
    		CeaApplication info = apps.getCeaApplication(appId);        
    		return invoke(info, tool, target);    
    	} catch (URISyntaxException e) {
    		throw new InvalidArgumentException(e);
    	}        
    }
    
    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#submitTo(org.w3c.dom.Document, java.net.URI)
     */
    public ProcessMonitor submitTo(Document doc, URI server) throws ServiceException, SecurityException,
    NotFoundException, InvalidArgumentException {
    	try {
    		Tool tool = ceaHelper.parseTool(doc);
    		
    		URI appId = new URI("ivo://" + tool.getName());
    		Service[] arr = apps.listServersProviding(appId);
    		
    		Service target = null;
    		for (int i = 0; i < arr.length ; i++) {
    			if (arr[i].getId().equals(server)) { 
    				target = arr[i];
    				break;
    			}
    		}
    		if (target == null) {
    			throw new NotFoundException(server + " does not provide application " + appId);            
    		}        
    		CeaApplication info = apps.getCeaApplication(appId);
    		return invoke(info, tool, target);
    	} catch (URISyntaxException e) {
    		throw new InvalidArgumentException(e);
    	}
    }



	/**
     * actually executes an applicaiton.
     * first checks whether it can be serviced by the local cea lib.
     * if not, delegates to a remote cea server 
     * @param application
     * @param v1Tool
     * @param server
     * @param securityAction The kind of authentication to be performed.
     * @return
     * @throws ServiceException
     */
    private ProcessMonitor invoke(CeaApplication application, 
    		Tool tool, 
    		Service server)
    throws ServiceException {
    	apps.translateQueries(application, tool); //@todo - maybe move this into manager too???
    	try {
    		if (logger.isDebugEnabled()) { // log the adjusted tool document before executing it.
	        	try {
	        		StringWriter sw = new StringWriter();
					Marshaller.marshal(tool,sw);
					logger.debug(sw.toString());
				} catch (CastorException x) {
					logger.debug("MarshalException",x);
				} 
    		}
    		
    		JobIdentifierType jid = new JobIdentifierType(server.getId().toString());
    		logger.debug("Id will be " + jid);
    		//try local invocation.
    		if (ceaInternal.getAppLibrary().hasMatch(application)) {
    			String primId = ceaInternal.getExecutionController().init(tool,jid.toString());
    			return new LocalTaskMonitor(primId,application);
    		}
    		// check remote invocation is possible
    		if (! (server instanceof CeaService)) {
    			throw new ServiceException("Can't dispatch a cea application to non-cea server");
    		}
    		// try remote invocation.
    		CeaService ceaService = (CeaService)server;
    		logger.debug("Using remote invocation to" + ceaService.getId());
    		
    		// create an authenticated delegate.
    		CommonExecutionConnectorClient del = ceaHelper.createCEADelegate(ceaService);    		
    		SecurityGuard guard = this.community.getSecurityGuard();
    		if (community.isLoggedIn()) {
    			del.setCredentials(guard);
    		}
    		
    		logger.info("Initializing document on server" );

    		String primId = del.init(tool,jid);
    		logger.info("Server returned taskID " + primId);
    		return new RemoteTaskMonitor(primId, application, ceaService);


    	} catch (CeaException e) {
    		throw new ServiceException(e);
    	} catch (CEADelegateException e) {
    		throw new ServiceException(e);
    	} 
    }
    
}


/* 
$Log: CeaStrategyImpl.java,v $
Revision 1.17  2007/07/16 12:21:23  nw
Complete - task 91: make remoteprocessmanager a full fledged ar member , and added internal interface.

Revision 1.16  2007/07/13 23:14:55  nw
Complete - task 1: task runner

Complete - task 54: Rewrite remoteprocess framework

Revision 1.15  2007/04/18 15:47:11  nw
tidied up voexplorer, removed front pane.

Revision 1.14  2007/03/22 19:03:48  nw
added support for sessions and multi-user ar.

Revision 1.13  2007/01/29 11:11:36  nw
updated contact details.

Revision 1.12  2006/08/31 21:29:28  nw
doc fix.

Revision 1.11  2006/08/15 10:15:34  nw
migrated from old to new registry models.

Revision 1.10  2006/07/20 12:30:15  nw
fixed to not display errors if refresh fails.

Revision 1.9  2006/07/18 08:59:59  gtr
Fix for BZ1717.

Revision 1.8.8.1  2006/07/13 16:56:33  gtr
Fix for BZ1717: I added a next() call on an iterator that lacked one, thereby avoiding an infinite loop.

Revision 1.8  2006/06/27 19:11:17  nw
adjusted todo tags.

Revision 1.7  2006/06/27 10:26:11  nw
findbugs tweaks

Revision 1.6  2006/06/15 18:21:14  nw
merge of desktop-gtr-1537

Revision 1.5.10.1  2006/06/09 11:12:33  gtr
The constructor now takes a CommunityInternal argument.

Revision 1.5  2006/05/26 15:18:43  nw
reworked scheduled tasks,

Revision 1.4  2006/05/13 16:34:55  nw
merged in wb-gtr-1537

Revision 1.3  2006/04/18 23:25:43  nw
merged asr development.

Revision 1.2.30.3  2006/04/14 02:45:01  nw
finished code.
extruded plastic hub.

Revision 1.2.30.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.2.30.1  2006/03/28 13:47:35  nw
first webstartable version.

Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.2  2005/11/23 18:09:28  nw
tuned up.

Revision 1.1.2.1  2005/11/23 04:50:11  nw
got working

Revision 1.1  2005/11/11 17:53:27  nw
added cea polling to lookout.
 
*/
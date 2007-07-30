/*$Id: CeaStrategyImpl.java,v 1.21 2007/07/30 17:59:56 nw Exp $
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
import org.astrogrid.desktop.modules.votech.VoMonInternal;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.votech.VoMon;
import org.votech.VoMonBean;
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
	private class RemoteTaskMonitor extends TimerDrivenProcessMonitor implements ProcessMonitor.Advanced {
		
		public RemoteTaskMonitor(Tool t,CeaApplication app) throws ServiceException {
			this.tool = t;
			this.app = app;
			this.name = app.getTitle();
			if (app.getInterfaces().length > 1) { // more than one interface
				this.name = tool.getInterface() + " - " + this.name;
			}
			this.description = app.getContent().getDescription();
		
		}
		private final Tool tool;		
		private final CeaApplication app;

		private String ceaid;
		private CommonExecutionConnectorClient delegate;
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
		
        public void start(URI server) throws ServiceException, NotFoundException{
                Service[] arr;
                try {
                    arr = apps.listServersProviding(app.getId());
                } catch (InvalidArgumentException x) {
                    error(x.getMessage());
                    throw new NotFoundException(x);
                }
                CeaService target = null;
                for (int i = 0; i < arr.length ; i++) {
                    if (arr[i].getId().equals(server)) { 
                        target = (CeaService)arr[i];
                        break;
                    }
                }
                if (target == null) {
                    error("Requested server does not provide this application");
                    throw new NotFoundException(server + " does not provide application " + app.getId());            
                }        
                invoke(target);         
        }

        public void start() throws ServiceException, NotFoundException{
                Service[] arr;
                try {
                    arr = apps.listServersProviding(app.getId());
                } catch (InvalidArgumentException x) {
                    error(x.getMessage());                    
                    throw new NotFoundException(x);
                }
                
                CeaService target = null;
                switch(arr.length) {            
                case 0:
                    error("No providers for this application");
                    throw new NotFoundException(app.getId() +" has no registered providers");
                case 1:
                    target = (CeaService)arr[0];
                    info("Using service " + target.getId()); 
                    break;
                default:
                    List l =  Arrays.asList(arr);
                // now filter on perceived availability.
                    l = filterOnAvailability(l);
                    Collections.shuffle(l);
                    target = (CeaService)l.get(0);
                    info("Multiple available services, selected " + target.getId());
                }     
                invoke(target);                     
        }		
        
        // actually set the process running.
        private void invoke(CeaService target) throws ServiceException {
            
            try {
            JobIdentifierType jid = new JobIdentifierType(target.getId().toString());
            delegate = ceaHelper.createCEADelegate(target);                                   
            info("Initializing on server " + target.getId() );

            ceaid = delegate.init(tool,jid);
            info("Server returned taskID " + ceaid);            
            setId(ceaHelper.mkRemoteTaskURI(ceaid,target));
            // kick it off.
                if (delegate.execute(ceaid)) {
                    info("Started application");
                    // task has started, so register this monitor with the scheduler...
                    sched.schedule(this);
                } else {
                    error("Failed to execute application");
                    throw new ServiceException("Failed to execute application");
                }
            } catch (CEADelegateException x) {
                error("Failed: " + x.getMessage());
                throw new ServiceException("Failed to execute application");
            }       
        }
        
        /**
         * use vomon to filter a list of services.
         * if vomon has no knowledge of a service, return it unchanged.
         *  if vomon thinks all services are down, return the list unchanged.
         *  else only return services vomon thinks are up.
         *  
         * @param l
         * @return
         */
        private List filterOnAvailability(List l) {
            List results = new ArrayList();
            for (Iterator i = l.iterator(); i.hasNext();) {
                Service name = (Service) i.next();
                VoMonBean b = vomon.checkAvailability(name.getId());
                if (b == null || b.getCode() == VoMonBean.UP_CODE) {
                    results.add(name);
                }
            }
            return results.size() == 0 ? l : results;
        }        
		
        public void refresh() {
            execute();
        }
        
		public void halt() throws NotFoundException, InvalidArgumentException,
				ServiceException, SecurityException {
	        try { 
	            info("Halting");
	            delegate.abort(ceaid);
	        } catch (CEADelegateException e) {
	            error("Failed: " + e.getMessage());
	            throw new ServiceException(e);
	        } finally {
	            // cause a status check.
	            refresh();
	        }
		}
		
		//@todo find out how to cleanup a remote service.

		public static final long SHORTEST = 1000 * 1; // 1 second
		public static final long LONGEST = 1000 * 60 * 10; // 10 minutes
		public static final double FACTOR = 2; // double every time
		
		/** increase the time before running again */
		private void standOff() {
            runAgain = Math.min(LONGEST,Math.round(runAgain*FACTOR));
            int secs = (int)runAgain / 1000;
            int mins = secs / 60; 
            info("Will check again in " + (secs < 120 ? secs + " seconds" : mins + " minutes" ));
		}
		
		public DelayedContinuation execute() {
			try {
			    info("Checking progress");
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
						, newStatus
				);
				addMessage(em);
				setStatus(newStatus);

				if (newStatus.equals(ExecutionInformation.ERROR) 
						||newStatus.equals(ExecutionInformation.COMPLETED)) {
					finishTime = qes.getTimestamp();
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
			    info("Failed: " + x.getMessage());
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

        public Tool getInvocationTool() {
            return tool;
        }

        public CeaApplication getApplicationDescription() {
            return app;
        }


	}
	
	/** monitor for a single local cea task 
	 * 
	 * peeks directly into the local cea engine to get stuff - by implementing observer
	 * @todo if we ever expose this functionality, add more message reporting.
	 * */
	private class LocalTaskMonitor extends AbstractProcessMonitor implements Observer, ProcessMonitor.Advanced {

		private Application application;
		private final Tool tool;
        private final CeaApplication appDesc;
        private String ceaid;
		public LocalTaskMonitor(Tool t, CeaApplication appDesc) throws ServiceException {
			this.tool = t;
            this.appDesc = appDesc;
			this.name = appDesc.getTitle();
			if (appDesc.getInterfaces().length > 1) { // more than one interface
				this.name = tool.getInterface() + " - " + this.name;
			}			
			this.description = appDesc.getContent().getDescription();
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
	    //@todo should override something so that starttime and endtime are correctly set.
	    
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
        public void start(URI serviceId) throws ServiceException {
            // service id is ignored.
            start();
        }

        public void start() throws ServiceException { 
            try {
                this.ceaid = ceaInternal.getExecutionController().init(tool,appDesc.getId().toString());
                setId(ceaHelper.mkLocalTaskURI(ceaid));

                // register an interest to messages and status changes.
                this.application = ceaInternal.getExecutionController().getApplication(ceaid);
                // now, once everythiong is ready, start the application
                    if (! ceaInternal.getExecutionController().execute(ceaid)) {
                        error("Failed to start application, for unknown reason");
                    } else {
                        // attach myself as a listener for further events.
                        this.application.addObserver(this);
                    }
                } catch (CeaException x) {
                    //@todo do I signal an error, or throw it here?
                    error("Failed to start application: " + x.getMessage());
                }       
            
        }       
		public void halt() throws NotFoundException, InvalidArgumentException,
				ServiceException, SecurityException {
	        try { 
	             ceaInternal.getExecutionController().abort(ceaid);	           
	        } catch (CeaException e) { //@todo improve this - match subtypes to different kinds of excpeiton.
	            throw new ServiceException(e);
	        }		
		}
        public Tool getInvocationTool() {
            return tool;
        }
        public CeaApplication getApplicationDescription() {
            return appDesc;
        }
        public void refresh() throws ServiceException {
            // does nothing - as this monitor is callback-driven anyhow, no way to poll on demand.
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
	private final VoMon vomon;
    
    /** Construct a new CeaStrategyImpl
     * 
     */
    public CeaStrategyImpl(TasksInternal ceaInternal
            , Registry reg
            ,VoMon vomon
            , ApplicationsInternal apps
            , CommunityInternal community, SessionManagerInternal sess, SchedulerInternal sched) {
        super();         
        this.apps = apps;
        this.vomon = vomon;
        this.ceaInternal =ceaInternal;
        this.ceaHelper = new CeaHelper(reg,community);
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

    public ProcessMonitor create(Document doc) throws InvalidArgumentException, ServiceException {
            Tool tool = ceaHelper.parseTool(doc);
            URI appId;
            CeaApplication info;
            try {
                appId = new URI("ivo://" + tool.getName());
                info = apps.getCeaApplication(appId);
            } catch (URISyntaxException x1) {
                throw new InvalidArgumentException(x1);
            } catch (NotFoundException x1) {
                throw new InvalidArgumentException(x1);                
            }            
            apps.translateQueries(info , tool); //@todo - maybe move this into manager too???

                if (logger.isDebugEnabled()) { // log the adjusted tool document before executing it.
                    try {
                        StringWriter sw = new StringWriter();
                        Marshaller.marshal(tool,sw);
                        logger.debug(sw.toString());
                    } catch (CastorException x) {
                        logger.debug("MarshalException",x);
                    } 
                }
        
                if (ceaInternal.getAppLibrary().hasMatch(info)) {
                    logger.info("Dispatching to local cea server");
                    return new LocalTaskMonitor(tool,info);
                } else {
                    logger.info("Dispatching to remote cea server");
                    return new RemoteTaskMonitor(tool,info);
                }
        
        }
    

    
  
}


/* 
$Log: CeaStrategyImpl.java,v $
Revision 1.21  2007/07/30 17:59:56  nw
RESOLVED - bug 2257: More feedback, please
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2257

Revision 1.20  2007/07/26 18:21:45  nw
merged mark's and noel's branches

Revision 1.19.2.3  2007/07/26 13:40:31  nw
Complete - task 96: get authenticated access working

Revision 1.19.2.2  2007/07/26 11:23:15  nw
Incomplete - task 49: Implement file Tasks

Revision 1.19.2.1  2007/07/24 17:57:26  nw
added tasks vfs view

Revision 1.19  2007/07/23 12:18:23  nw
finished implementation of process manager framework.

Revision 1.18  2007/07/16 13:02:15  nw
Complete - task 90: integrate vomon with remote process manager

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
/*$Id: CeaStrategyImpl.java,v 1.1 2008/07/18 17:15:52 nw Exp $
 * Created on 11-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.CeaApplication;
import org.astrogrid.acr.astrogrid.CeaService;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.ExecutionMessage;
import org.astrogrid.acr.astrogrid.ParameterBean;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Interface;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.desktop.framework.SessionManagerInternal;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.desktop.modules.system.SchedulerInternal.DelayedContinuation;
import org.astrogrid.desktop.modules.ui.comp.ExceptionFormatter;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.core.exceptions.CastorException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.joda.time.Duration;
import org.votech.VoMon;
import org.votech.VoMonBean;
import org.w3c.dom.Document;


/** remote process strargey for cea.
 * see RemoteProcessManagerImpl
 * periodically poll remote cea servers, injjct messages into the system
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 11-Nov-2005
 * @TODO remove the concrete myspace ivorn hack
 *
 */
public class CeaStrategyImpl implements RemoteProcessStrategy{
    public static final Duration SHORTEST = new Duration(1000); // 1 second
    public static final Duration LONGEST = new Duration(1000 * 60 * 10); // 10 minutes
	/** monitor for a single remote cea task */
	private class RemoteTaskMonitor extends TimerDrivenProcessMonitor implements ProcessMonitor.Advanced {
		
		public RemoteTaskMonitor(Tool t,CeaApplication app) throws ServiceException {
		    super(vfs);
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
                    error("Unable to start application",x);
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
                    error("Unable to find servers providing this application",x);                    
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
            //check whether we need to login first.
            if (! community.isLoggedIn()) {
                Interface[] interfaces = target.findCeaServerCapability().getInterfaces();
                for (int i = 0; i < interfaces.length; i++) {
                    Interface ifa = interfaces[i];
                    if (ifa.getSecurityMethods().length != 0) { //assume for now that any kind of security will require login
                        community.guiLogin();
                        break;
                    }
                }
            }
            try {
            JobIdentifierType jid = new JobIdentifierType(target.getId().toString());
            delegate = ceaHelper.createCEADelegate(target);                                   
            info("Initializing on server " + target.getId() );

            //make a version of the tool with concrete ivorns, just for the remote service
            Tool remotetool = ceaHelper.makeMySpaceIvornsConcrete(tool);
            ceaid = delegate.init(remotetool,jid);
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
                error("Failed to execute application ",x);
                Throwable e = x; // get to the core of the problem - too much wrapping here.
                while (e.getCause() != null) {
                    e = e.getCause();
                }
                throw new ServiceException(e.getMessage()); 
            } catch (InvalidArgumentException x) {
                error("Failed to execute application ",x);
                throw new ServiceException(x);
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
		
        /** not perfect - refreshes immediately, but inteacts badly with the shceduled task.
         * would like to reset the scheduled task time, so that it runs immediately.
         * but can't do that.
         * instead, we run the refresh, and then the next time the scheduled task runs, the refresh-period
         * will be set to SHORTEST - so that it runs soon the time after that.
         * 
         * however, this means that there might still be large pause between a refresh and the next 
         * scheduled operation.
         */
        public void refresh() {            
            runAgain = SHORTEST; // 
            execute(false);
        }
        
		public void halt() throws NotFoundException, InvalidArgumentException,
				ServiceException, SecurityException {
	        try { 
	            info("Halting");
	            delegate.abort(ceaid);
	        } catch (CEADelegateException e) {
	            error("Failed to halt" ,e);
	            throw new ServiceException(e);
	        } finally {
	            // cause a status check.
	            refresh();
	        }
		}
	
		//@todo find out how to cleanup a remote service.


		public static final long FACTOR = 2; // double every time
		/** increase the time before running again */
		private void standOff(boolean increaseStandoff) {
		    if (increaseStandoff) {
		        Duration nu =new Duration(runAgain.getMillis() *FACTOR);
		        runAgain = nu.isLongerThan(LONGEST) ? LONGEST : nu;
		       int secs = (int)runAgain.getMillis() / 1000;
		        info("No change: will re-check in " + (secs < 120 ? secs + " seconds" : secs/60 + " minutes" ));		        
		    } else {
		        info("No change");
		    }
		}
		
		public DelayedContinuation execute() {
		    return execute(true);
		}
		
		/** do a poll, and if increaseStandoff=true, increate the standoff value if nothing has happended */
		public DelayedContinuation execute(boolean increaseStandoff) {
		    if (getStatus().equals(ExecutionInformation.ERROR) 
                            ||getStatus().equals(ExecutionInformation.COMPLETED)) {
		        // we're done already (probably by an intermediate refresh..)
		        return null; // halts the progress checking.
		    }
			try {
			    info("Checking progress");
				MessageType qes = delegate.queryExecutionStatus(ceaid);
				String newStatus = qes.getPhase().toString(); 

				if  (getStatus().equals(newStatus)) { // nothing changed.
					standOff(increaseStandoff);
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
				    // retrive the results - might take us more than one execute() to get them all.
				    ExecutionSummaryType summ = delegate.getExecutionSumary(ceaid);
				    if (summ != null && summ.getResultList() != null) {
				        ParameterBean[] descs = app.getParameters();
				        ParameterValue[] arr = summ.getResultList().getResult();
				        for (int i = 0 ; i < arr.length; i++) {
				            final ParameterValue val = arr[i];
				            ParameterBean desc = findDescriptionFor(val,descs);
						if (val.getIndirect()) { 
					            String value = val.getValue(); 

				                    //FIXME this is a hack to prevent concrete ivorns leaking back into the desktop - taking the indirect parameter value from the input tool, rather than reading back what the CEA server sends back - should be removed when myspace leaves us....		the line above should take precedence			            
				                    value = ((ParameterValue)tool.findXPathValue("/output/parameter[name='"+val.getName()+"']")).getValue();
				                    // do something clever - get a pointer to the remote file, and then add this as the result.
				                    //@issue hope this doesn't force login. if it does, need to create an lazily-initialized file object here instead.
				                    // however, user would probably have logged in to setup this indirection in the first place.
				                    try {
				                        FileObject src = vfs.resolveFile(value);
				                            src.refresh();
				                        if (! src.exists()) {
				                            // caught by surrounding block.
				                            throw new FileSystemException(value + " does not exist");
				                        }
				                        addResult(val.getName(),src);
				                    } catch (FileSystemException e) {
				                        logger.debug("Failed to retrieve " + value,e);
				                        warn("Failed to retrieve " + value + "<br>" + exFormatter.format(e,ExceptionFormatter.ALL));
				                    }
				                } else {
				                    addResult(val.getName(),val.getName() + suggestExtension(desc), val.getValue());
				                }
				            
				        }
				        fireResultsReceived(resultMap); // send the results we've got, no matter whether they're all there or not.
				        // done
				        return  null; 
				    }
				}
				return this;
			} catch (Throwable x) {
			    standOff(increaseStandoff);
			    logger.debug("Failed",x);
			    warn("Failed: " + exFormatter.format(x,ExceptionFormatter.ALL));
				return this;
			}

		}

		/** work out a suitable file extension for this parameter, if none was provided
		 * 
		 *  not a good idea - as changes the key names- which is a pain.
		 *  */
	    private String suggestExtension(ParameterBean pb) {
	        if (pb == null) {
	            return "";
	        }
	        String type = pb.getType();
	        if (type.equalsIgnoreCase("fits")) {
	            return ".fits";
	        } else if (type.equalsIgnoreCase("binary")) {
	            return ".bin";
	        } else if (type.equalsIgnoreCase("anyxml")) {
	            return ".xml";
	        } else if (type.equalsIgnoreCase("votable")) {
	            return ".vot";
	        } else if(type.equalsIgnoreCase("adql")) {
	            return ".adql";
	        } else {
	            return ".txt";
	        }
	    }

		private Duration runAgain = SHORTEST;
		public Duration getDelay() {
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

        public String getTitle() {
           
            return "Tracking execution of " + app.getTitle();
        }


	}
	
	/**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CeaStrategyImpl.class);

    private final ApplicationsInternal apps;
    private final CeaHelper ceaHelper;
    private final CommunityInternal community;
	private final SessionManagerInternal sess;
	private final SchedulerInternal sched; 
	private final VoMon vomon;

    private final FileSystemManager vfs;
    
    /** Construct a new CeaStrategyImpl
     * 
     */
    public CeaStrategyImpl(Registry reg
            ,VoMon vomon
            , ApplicationsInternal apps
            , CommunityInternal community
            , FileSystemManager vfs
            ,SessionManagerInternal sess, SchedulerInternal sched) {
        super();         
        this.apps = apps;
        this.vomon = vomon;
        this.vfs = vfs;
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
        } catch(org.exolab.castor.core.exceptions.CastorException e) {
            return null;
        }         
    }

    /** @todo make this rule more precise when we've got more than one kind of strategy.
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#canProcess(java.net.URI)
     */
    public boolean canProcess(URI execId) {
        return "ivo".equals(execId.getScheme()) ;
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

                    logger.info("Dispatching to remote cea server");
                    return new RemoteTaskMonitor(tool,info);
        
        }
    
    // should this be elsewere?
    static ParameterBean findDescriptionFor(ParameterValue pv,ParameterBean[] descs) {
        for (int i = 0; i < descs.length; i++) {
            if (pv.getName().equals(descs[i].getName())) {
                return descs[i];
            }            
        }
        return null;
        
    }
    
  
}


/* 
$Log: CeaStrategyImpl.java,v $
Revision 1.1  2008/07/18 17:15:52  nw
Complete - task 433: Strip out unused internal CEA

Revision 1.36  2008/05/09 11:32:10  nw
Incomplete - task 392: joda time

Revision 1.35  2008/04/23 10:53:37  nw
fix to get castor working under 1.5

Revision 1.34  2008/03/30 09:42:47  nw
minor tweak

Revision 1.33  2008/03/27 10:03:46  nw
improved error reporting when trying to run a secured app without logging in.

Revision 1.32  2008/03/26 03:47:21  nw
Complete - task 353: add 'requires login' to taskrunner & resource formatter.

Revision 1.31  2008/03/10 18:08:45  nw
moved computation inside conditional

Revision 1.30  2008/02/15 14:31:25  pah
RESOLVED - bug 2547: Resolve MySpace IVORNs before job submission
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2547

Revision 1.29  2007/11/26 14:44:46  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.28  2007/11/20 06:31:38  nw
re-fixed result parameter naming so that that file-types are provided, yet AR interface isn't affected.

Revision 1.27  2007/11/12 11:53:42  nw
fixed result parameter naming.

Revision 1.26  2007/10/08 08:31:06  nw
improved locating myspace files.

Revision 1.25  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.24  2007/08/13 19:29:48  nw
merged mark's and noel's changes.

Revision 1.23.4.2  2007/08/13 18:44:08  nw
Complete - task 140: clear selection in one fileresult space when another is selected in a different resultspace.

Complete - task 138: add index column to metadata viewer

Incomplete - task 112: implement vfsoperations

Incomplete - task 49: Implement file Tasks

Incomplete - task 73: filechooser dialogue.

RESOLVED - bug 2272: Task runer locks up AR, menu bar
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2272

Revision 1.23.4.1  2007/08/09 19:08:21  nw
Complete - task 126: Action to 'Reveal' location of remote results in FileExplorer

Complete - task 122: plastic messaging of myspace resources

Revision 1.23  2007/07/31 13:44:11  nw
Complete - task 128: Fix refresh

Revision 1.22  2007/07/31 11:26:43  nw
Complete - task 107: Polish Execution Tracker

tweaked implementation and use of HtmlBuilder

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
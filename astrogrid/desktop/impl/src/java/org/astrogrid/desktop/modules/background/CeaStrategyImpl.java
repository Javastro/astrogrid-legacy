/*$Id: CeaStrategyImpl.java,v 1.14 2007/03/22 19:03:48 nw Exp $
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

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.CeaHelper;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal;
import org.astrogrid.desktop.modules.ag.MessagingInternal;
import org.astrogrid.desktop.modules.ag.RemoteProcessStrategy;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.ag.MessagingInternal.SourcedExecutionMessage;
import org.astrogrid.desktop.modules.ag.recorder.ResultsExecutionMessage;
import org.astrogrid.desktop.modules.ag.recorder.StatusChangeExecutionMessage;
import org.astrogrid.desktop.modules.auth.CommunityInternal;
import org.astrogrid.desktop.modules.system.UIInternal;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.workflow.beans.v1.Tool;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.mortbay.util.MultiException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;


/** remote process strargey for cea.
 * see RemoteProcessManagerImpl
 * periodically poll remote cea servers, injjct messages into the system
 *  - temporary, until cea actually calls back into the workbench.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 11-Nov-2005
 *
 */
public class CeaStrategyImpl implements RemoteProcessStrategy, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CeaStrategyImpl.class);

    private long refreshSeconds;
 final ApplicationsInternal apps;
    final CeaHelper ceaHelper;
    final TasksInternal ceaInternal;
    final MessagingInternal messaging;
    boolean poll = false;
    final MessageRecorderInternal recorder;
    final CommunityInternal community;
    

    /** Construct a new CeaStrategyImpl
     * @throws JMSException
     * 
     */
    public CeaStrategyImpl(MessagingInternal messaging
            , MessageRecorderInternal recorder
            , TasksInternal ceaInternal
            , Registry reg
            , ApplicationsInternal apps
            , UIInternal ui
            , CommunityInternal community) {
        super();         
        this.apps = apps;
        this.messaging = messaging;
        this.ceaInternal = ceaInternal;
        this.ceaHelper = new CeaHelper(reg);
        this.recorder = recorder;
        this.ui = ui;
        this.community = community;
        
        
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

    private final UIInternal ui;
    
    /** creates a worker that will refresh status of all apps */
    public BackgroundWorker createWorker() {
    	return new BackgroundWorker(ui,"Checking status of Remote Applications") {
    		protected Object construct() throws Exception {
    			List errors = null;
    			if (!poll) {
    				logger.debug("run() - bailing out");
    				return null;
    			}
    			List ids = recorder.listLeaves();
    			for (int i = 0; i < ids.size(); i++) {               
    				URI uri = ((URI)ids.get(i));
    				if ("ivo".equals(uri.getScheme())) {
    					try {
    						checkSingleApp(uri);
    					} catch (Exception e) {
    						if (errors == null) {
    							errors = new ArrayList();
    						}
    						errors.add(e);
    					}                    
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
    				/*@issue popping up a modal error dialog everytime a service is unavailable is too intrusive.
    				especially as it happens as a regular scheduled task. will just log for now, and work out what to do later.
    				parent.showError("Failed to check status of some applications",e);
    				*/
    				logger.warn("Failed to check status of some applications",e);
    			}
    		}
    		/** check a single cea for updates. to be called within scheduler thread 
    	     * @throws IOException
    	     * @throws CEADelegateException
    	     * @throws ServiceException
    	     * @throws NotFoundException
    	     * @throws JMSException
    	     * @throws ValidationException
    	     * @throws MarshalException*/
    	    private void checkSingleApp(URI executionId) throws IOException, NotFoundException, ServiceException, CEADelegateException, MarshalException, ValidationException {
    	        if (ceaHelper.isLocal(executionId)) {
    	            logger.warn("Shouldn't have seen a local id here " + executionId);
    	            return;            
    	        }
    	        Folder f= recorder.getFolder(executionId);
    	        if (f == null) {
    	            logger.warn("Odd - can't find folder - must have been there a moment ago" + executionId);
    	            return;
    	        }
    	        if (f.isDeleted()) {
    	        	// don't care about this folder anymore
    	        	return;
    	        }
    	        String currentStatus = f.getInformation().getStatus().trim();
    	        logger.debug("Current status is " + currentStatus);
    	        if (JesStrategyImpl.isCompletedOrError(currentStatus)) {
    	            logger.debug("checkSingleApp() - end: app already finished.");
    	            return; // nothing more to see here.
    	        }
    	        String ceaId = ceaHelper.getAppId(executionId);
    	        MessageType mType = ceaHelper.createCEADelegate(executionId).queryExecutionStatus(ceaId);       
    	        String newStatus = mType.getPhase().toString().trim();
    	        if  (currentStatus.equals(newStatus)) { // nothing changed.

    	            logger.debug("checkSingleApp() - end: app no change");
    	            return;
    	        }
    	        // ok, send a status-change message
    	    	ExecutionMessage em = new StatusChangeExecutionMessage(
    	    			executionId.toString()
    	    			,newStatus
    	    			,new Date()
    	    			);
    	    	SourcedExecutionMessage sem = new SourcedExecutionMessage(
    	    			executionId
    	    			,f.getInformation().getName()
    	    			,em
    	    			,new Date()
    	    			,null
    	    			);
    	        messaging.injectMessage(sem);        
    	               
    	        if (JesStrategyImpl.isCompletedOrError(newStatus)) { // new status is completion - send a results message        
    	            ExecutionSummaryType ex =  ceaHelper.createCEADelegate(executionId).getExecutionSumary(ceaId);
    	            if (ex != null && ex.getResultList() != null) {
    	    			em = new ResultsExecutionMessage(executionId.toString(),new Date(), ex.getResultList());    		                	
    	    			sem = new SourcedExecutionMessage(
    	            			executionId
    	            			,f.getInformation().getName()
    	            			,em
    	            			,new Date()
    	            			,null
    	            			);
    	                messaging.injectMessage(sem);              	  
    	            }
    	        }

    	        logger.debug("checkSingleApp() - end");
    	    }
    	    
    	};
    }

    /**
     * @see org.astrogrid.desktop.modules.system.ScheduledTask#getPeriod()
     */
    public long getPeriod() {
        return 1000 * refreshSeconds;
    }
    
    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#delete(java.net.URI)
     */
    public void delete(URI arg0) throws NotFoundException, ServiceException, SecurityException {
        //@todo - only works on cea internal - find a way of deleting remote apps too.
        if (ceaHelper.isLocal(arg0)) {
            String ceaId = ceaHelper.getAppId(arg0);
            ceaInternal.getExecutionController().delete(ceaId);
        }
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#getLatestResults(java.net.URI)
     */
    public Map getLatestResults(URI executionId) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        try {
            final ResultListType results;
            String ceaId = ceaHelper.getAppId(executionId);
            if (ceaHelper.isLocal(executionId)) {                
                results = ceaInternal.getQueryService().getResults(ceaId);
            } else {
                results = ceaHelper.createCEADelegate(executionId).getResults(ceaId);
            }     
            Map map = new HashMap();
            ParameterValue[] vals = results.getResult();        
            for (int i = 0; i < vals.length; i++) {
                map.put(vals[i].getName(),vals[i].getValue());
            }
            return map;
        } catch (CeaException e) {
            throw new ServiceException(e);
        } catch (CEADelegateException e) {
            throw new ServiceException(e);
        }  
    }


    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#halt(java.net.URI)
     */
    public void halt(URI executionId) throws NotFoundException, InvalidArgumentException,
            ServiceException, SecurityException {
        try { 
            String ceaid = ceaHelper.getAppId(executionId);
            if (ceaHelper.isLocal(executionId)) {
                ceaInternal.getExecutionController().abort(ceaid);
            } else {
                CommonExecutionConnectorClient del = ceaHelper.createCEADelegate(executionId);
                del.abort(ceaid);
            }
        } catch (CeaException e) { //@todo improve this - match subtypes to different kinds of excpeiton.
            throw new ServiceException(e);
        } catch (CEADelegateException e) {
            throw new ServiceException(e);
        }        
    }
                  
    /** sets update rate. - configuration option */
    public void setRefreshSeconds(long period) {
    	this.refreshSeconds = period;
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#submit(org.w3c.dom.Document)
     */
    public URI submit(Document doc) throws ServiceException, SecurityException, NotFoundException,
            InvalidArgumentException {
            
        CeaApplication info;
      Tool tool;
      Set securityActions;
        try {
        tool = (Tool)Unmarshaller.unmarshal(Tool.class, doc);
        securityActions = this.extractSecurityInstructions(doc);
        
        // The application name is supposed to be an IVOID without the
        // ivo:// prefix. Strip the prefix if it is present.
        if (tool.getName().startsWith("ivo://")) {
            tool.setName(tool.getName().substring(6));
        }
        URI application = new URI("ivo://" + tool.getName());
            info = apps.getCeaApplication(application);
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }
        
        Service[] arr = apps.listServersProviding(info.getId());
        Service target = null;
        switch(arr.length) {            
            case 0:
                throw new NotFoundException(info.getTitle() +" has no registered providers");
            case 1:
                target = arr[0];
                break;
            default:
                List l =  Arrays.asList(arr);
                Collections.shuffle(l);
                target = (Service)l.get(0);
        }
        return invoke(info, tool, target, securityActions);            
    }
    
    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#submitTo(org.w3c.dom.Document, java.net.URI)
     */
    public URI submitTo(Document doc, URI server) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        // munge name in document, if incorrect..       
        CeaApplication info;
      Tool tool;
      Set securityActions;
        try {
        tool = (Tool)Unmarshaller.unmarshal(Tool.class, doc);
        securityActions = this.extractSecurityInstructions(doc);
        
        // The application name is supposed to be an IVOID without the
        // ivo:// prefix. Strip the prefix if it is present.
        if (tool.getName().startsWith("ivo://")) {
            tool.setName(tool.getName().substring(6));
        }
        URI application = new URI("ivo://" + tool.getName());
            info = apps.getCeaApplication(application);
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }
        Service[] arr = apps.listServersProviding(info.getId());
        Service target = null;
        for (int i = 0; i < arr.length ; i++) {
            if (arr[i].getId().equals(server)) { 
                target = arr[i];
                break;
            }
        }
        if (target == null) {
            throw new NotFoundException(server + " does not provide application " + info.getTitle());            
        }        
      return invoke(info, tool, target, securityActions);
    }

    /**
     * @see org.astrogrid.desktop.modules.background.CeaStrategyInternal#triggerUpdate()
     */
    public void triggerUpdate() {
        createWorker().start();
    }
    
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.acr.astrogrid.UserLoginEvent)
     */
    public void userLogin(UserLoginEvent arg0) {
        poll = true;
        createWorker().start();
    }    
    
    /**
     * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.acr.astrogrid.UserLoginEvent)
     */
    public void userLogout(UserLoginEvent arg0) {
        poll = false;        
    }
    
    /**
     * Handles security annotations in a tool document.
     * These annotations are processing instructions aimed at the code that
     * sends the tool to a web service; hence, they are intended for this
     * class. This method detects all the processing instructions in
     * the document, deletes those to do with security actions and returns
     * their values.
     */
    private Set extractSecurityInstructions(Document document) {
      Set instructions = new HashSet();
      NodeList nodes = document.getChildNodes();
      for (int i = 0; i < nodes.getLength(); i++) {
        org.w3c.dom.Node n = nodes.item(i);
        if (n.getNodeType() == org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE) {
          ProcessingInstruction p = (ProcessingInstruction)n;
          if ("CEA-strategy-security".equals(p.getTarget())) {
            instructions.add(p.getData());
            document.removeChild(n);
          }
        }
      }
      return instructions;
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
    private URI invoke(CeaApplication application, 
    		Tool document, 
    		Service server,
    		Set securityActions)
    throws ServiceException {
    	apps.translateQueries(application, document); //@todo - maybe move this into manager too???
    	try {
    		//fudge some kind of job id type. hope this will do.
    		JobIdentifierType jid = new JobIdentifierType(server.getId().toString());
    		//try local invocation.
    		if (ceaInternal.getAppLibrary().hasMatch(application)) {           
    			String primId = ceaInternal.getExecutionController().init(document,jid.toString());
    			if (!ceaInternal.getExecutionController().execute(primId)) {
    				throw new ServiceException("Failed to start application, for unknown reason");
    			}
    			return ceaHelper.mkLocalTaskURI(primId);
    		}
    		// check remote invocation is possible
    		if (! (server instanceof CeaService)) {
    			throw new ServiceException("Can't dispatch a cea application to non-cea server");
    		}
    		// try remote invocation.
    		CeaService ceaService = (CeaService)server;
    		CommonExecutionConnectorClient del = ceaHelper.createCEADelegate(ceaService);
    		Iterator i = securityActions.iterator();
    		while (i.hasNext()) {
    			i.next(); // Read the items so that the loop terminates, but discard the result.
    			// @todo actually react to the name of the action.
    			SecurityGuard guard = this.community.getSecurityGuard();
    			del.setCredentials(guard);
    		}
    		String primId = del.init(document,jid);
    		del.execute(primId);
    		URI id = ceaHelper.mkRemoteTaskURI(primId,ceaService);
    		// notifyy recorder of new remote cea.
    		ExecutionMessage em = new StatusChangeExecutionMessage(
    				id.toString()
    				,ExecutionInformation.PENDING
    				,new Date()
    		);
    		SourcedExecutionMessage sem = new SourcedExecutionMessage(
    				id
    				,document.getName()
    				,em
    				,new Date()
    				,null
    		);
    		messaging.injectMessage(sem);
    		return id;

    	} catch (CeaException e) {
    		throw new ServiceException(e);
    	} catch (CEADelegateException e) {
    		throw new ServiceException(e);
    	} 
    }

	public Principal getSession() {
		return null;
	}  
    
}


/* 
$Log: CeaStrategyImpl.java,v $
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
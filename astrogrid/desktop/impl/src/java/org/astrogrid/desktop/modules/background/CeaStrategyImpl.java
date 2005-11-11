/*$Id: CeaStrategyImpl.java,v 1.1 2005/11/11 17:53:27 nw Exp $
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

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.acr.astrogrid.ResourceInformation;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.cea.castor.ExecutionSummaryType;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase;
import org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.delegate.CEADelegateException;
import org.astrogrid.applications.delegate.CommonExecutionConnectorClient;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.CeaHelper;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal;
import org.astrogrid.desktop.modules.ag.MessageUtils;
import org.astrogrid.desktop.modules.ag.MessagingInternal;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/** remote process strargey for cea.
 * see RemoteProcessManagerImpl
 * periodically poll remote cea servers, injjct messages into the system
 *  - temporary, until cea actually calls back into the workbench.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Nov-2005
 *
 */
public class CeaStrategyImpl implements CeaStrategyInternal {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CeaStrategyImpl.class);

    /** Construct a new CeaStrategyImpl
     * @throws JMSException
     * 
     */
    public CeaStrategyImpl(MessagingInternal messaging
            , MessageRecorderInternal recorder
            , SchedulerInternal scheduler
            , TasksInternal ceaInternal
            , Registry reg
            , ApplicationsInternal apps) throws JMSException {
        super();         
        this.apps = apps;
        this.ceaInternal = ceaInternal;
        this.ceaHelper = new CeaHelper(reg);
        this.scheduler = scheduler;
        this.recorder = recorder;
        this.sess = messaging.createSession();
        this.prod = sess.createProducer(messaging.getEventQueue());
        prod.setDisableMessageID(true);
        txtMsg = sess.createTextMessage();
        
    }
    final DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT);
    final MessageProducer prod;
    final MessageRecorderInternal recorder;
    final TextMessage txtMsg;
    final Session sess;
    final SchedulerInternal scheduler; // already auto-registered with this.
    final ApplicationsInternal apps;
    final TasksInternal ceaInternal;
    final CeaHelper ceaHelper;
    

    /**
     * @see org.astrogrid.desktop.modules.background.CeaStrategyInternal#triggerUpdate()
     */
    public void triggerUpdate() {
        scheduler.runNow(this);
    }

    /**
     * @see org.astrogrid.desktop.modules.system.ScheduledTask#getPeriod()
     */
    public long getPeriod() {
        return 1000 * 60 *5; // would like to be more often, but think this is going to be quite intensive.
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#canProcess(java.net.URI)
     */
    public boolean canProcess(URI execId) {
        return "cea".equals(execId.getScheme()) || "local".equals(execId.getScheme());
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#canProcess(org.w3c.dom.Document)
     */
    public boolean canProcess(Document doc) {
        try {
            Unmarshaller.unmarshal(Tool.class,doc);
            return true;
        } catch (Exception e) {
            return false;
        }         
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#submit(org.w3c.dom.Document)
     */
    public URI submit(Document doc) throws ServiceException, SecurityException, NotFoundException,
            InvalidArgumentException {
        // munge name in document, if incorrect..       
        Tool document;
        ApplicationInformation info;
        try {
            document = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
        if (document.getName().startsWith("ivo://")) {
            document.setName(document.getName().substring(6));
        }
            URI application = new URI("ivo://" + document.getName());
            info = apps.getApplicationInformation(application);
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }

        
        ResourceInformation[] arr = apps.listProvidersOf(info.getId());
        ResourceInformation target = null;
        switch(arr.length) {            
            case 0:
                throw new NotFoundException(info.getName() +" has no registered providers");
            case 1:
                target = arr[0];
                break;
            default:
                List l =  Arrays.asList(arr);
                Collections.shuffle(l);
                target = (ResourceInformation)l.get(0);
        }
        return invoke(info,document,target);            
    }

    /**
     * @see org.astrogrid.desktop.modules.ag.RemoteProcessStrategy#submitTo(org.w3c.dom.Document, java.net.URI)
     */
    public URI submitTo(Document doc, URI server) throws ServiceException, SecurityException,
            NotFoundException, InvalidArgumentException {
        // munge name in document, if incorrect..       
        ApplicationInformation info;
        Tool document;
        try {
            document = (Tool)Unmarshaller.unmarshal(Tool.class,doc);
        if (document.getName().startsWith("ivo://")) {
            document.setName(document.getName().substring(6));
        }
            URI application = new URI("ivo://" + document.getName());
            info = apps.getApplicationInformation(application);
        } catch (URISyntaxException e) {
            throw new InvalidArgumentException(e);
        } catch (MarshalException e) {
            throw new InvalidArgumentException(e);
        } catch (ValidationException e) {
            throw new InvalidArgumentException(e);
        }
        ResourceInformation[] arr = apps.listProvidersOf(info.getId());
        ResourceInformation target = null;
        for (int i = 0; i < arr.length ; i++) {
            if (arr[i].getId().equals(server)) { 
                target = arr[i];
                break;
            }
        }
        if (target == null) {
            throw new NotFoundException(server + " does not provide application " + info.getName());            
        }        
       return invoke(info,document,target);
    }
    /** actually executes an applicaiton.
     * first checks whether it can be serviced by the local cea lib.
     * if not, delegates to a remote cea server 
     * @param application
     * @param document
     * @param server
     * @return
     * @throws ServiceException
     */
    private URI invoke(ApplicationInformation application, Tool document, ResourceInformation server)
    throws ServiceException {
        apps.translateQueries(application, document); //@todo - maybe move this into manager too???
        try {
        //fudge some kind of job id type. hope this will do.
            JobIdentifierType jid = new JobIdentifierType(server.getId().toString());
            if (ceaInternal.getAppLibrary().hasMatch(application)) {           
                String primId = ceaInternal.getExecutionController().init(document,jid.toString());
                if (!ceaInternal.getExecutionController().execute(primId)) {
                    throw new ServiceException("Failed to start application, for unknown reason");
                }
                return ceaHelper.mkLocalTaskURI(primId);
            } else {
                CommonExecutionConnectorClient del = ceaHelper.createCEADelegate(server);
                String primId = del.init(document,jid);
                del.execute(primId);
                URI id = ceaHelper.mkRemoteTaskURI(primId,server);
                // notifyy recorder of new remote cea.
                scheduler.runNow(new RemoteCeaSetup(document,application,id,jid));
                return id;
            }
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
     * @see java.lang.Runnable#run()
     */
    public void run() {
        try {
            URI[] ids = recorder.listLeaves();
            for (int i = 0; i < ids.length; i++) {
                if ("cea".equals(ids[i].getScheme())) {
                    try {
                    checkSingleApp(ids[i]);
                } catch (Exception e) {
                    logger.warn("Failed to refresh cea apps progress - " + ids[i],e);
                }                    
                }
            }
        } catch (IOException e) {
            logger.error("Failed to list folders in recorder",e);
        }
    }
    
    /**run-once class that gets added to the scheduler queue
     * when a new remote cea is executed - sets up a record of the new app
     * by injecting messages into the recorder. necessary to do this in a runnable to ensure 
     * that single-treaded access to messaging resources, no race conditions, etc.
     * @author Noel Winstanley nw@jb.man.ac.uk 11-Nov-2005
     *
     */
    private class RemoteCeaSetup implements Runnable {
        public RemoteCeaSetup(Tool document,ApplicationInformation info, URI id, JobIdentifierType jid) {
            this.document = document;
            this.info = info;
            this.id = id;
            this.jid = jid;
        }
        private final Tool document;
        private final ApplicationInformation info;
        private final URI id;
        private final JobIdentifierType jid;
        // inject a 'create folder' message
        public void run() {
            try {
                txtMsg.setStringProperty(MessageUtils.PROCESS_NAME_PROPERTY,document.getName());
                txtMsg.setStringProperty(MessageUtils.PROCESS_ID_PROPERTY,id.toString());
                // don't use jid for much at present - sort later.
                txtMsg.setStringProperty(MessageUtils.CLIENT_ASSIGNED_ID_PROPERTY,jid.toString());
                txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.STATUS_CHANGE_MESSAGE);
                txtMsg.setText(ExecutionInformation.PENDING);
                prod.send(txtMsg);
                // now send an information message
                txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.INFORMATION_MESSAGE);
                MessageType mt = new MessageType();
                mt.setLevel(LogLevel.INFO);
                mt.setPhase(ExecutionPhase.PENDING);
                mt.setSource("Invoker");
                mt.setTimestamp(new Date());
                StringWriter sw = new StringWriter();
                document.marshal(sw);
                mt.setContent(sw.toString());
                prod.send(txtMsg);
            } catch (JMSException e) {
                logger.warn("Failed to send creation message",e);
        } catch (CastorException e) {
                // @todo Auto-generated catch block
                logger.warn("CastorlException",e);            
            }        
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
    private void checkSingleApp(URI executionId) throws IOException, NotFoundException, ServiceException, CEADelegateException, JMSException, MarshalException, ValidationException {
        if (ceaHelper.isLocal(executionId)) {
            logger.warn("Shouldn't have seen a local id here " + executionId);
            return;            
        }
        Folder f= recorder.getFolder(executionId);
        if (f == null) {
            logger.warn("Odd - can't find folder - must hae been there a moment ago" + executionId);
            return;
        }
        if (JesStrategyImpl.isCompletedOrError(f.getInformation().getStatus())) {
            return; // nothing more to see here.
        }
        String ceaId = ceaHelper.getAppId(executionId);
        MessageType msg = ceaHelper.createCEADelegate(executionId).queryExecutionStatus(ceaId);
        if  (f.getInformation().getStatus().equals(msg.getPhase().toString())) { // nothing changed.
            return;
        }
        // ok, send a status-change message
        txtMsg.setStringProperty(MessageUtils.PROCESS_NAME_PROPERTY,f.getInformation().getName());
        txtMsg.setStringProperty(MessageUtils.PROCESS_ID_PROPERTY,executionId.toString());
        // don't use jid for much at present - sort later.
       // nowhere to store this. txtMsg.setStringProperty(MessageUtils.CLIENT_ASSIGNED_ID_PROPERTY,f.);
        txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.STATUS_CHANGE_MESSAGE);
        txtMsg.setText(msg.getPhase().toString());
        prod.send(txtMsg);        
        if (JesStrategyImpl.isCompletedOrError(msg.getPhase().toString())) { // new status is completion - send a results message        
            ExecutionSummaryType ex =  ceaHelper.createCEADelegate(executionId).getExecutionSumary(ceaId);
            if (ex != null && ex.getResultList() != null) {
                txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.RESULTS_MESSAGE);
                StringWriter sw = new StringWriter();
                ex.getResultList().marshal(sw);
                txtMsg.setText(sw.toString());
                prod.send(txtMsg);
            }
        }
    }  
    
}


/* 
$Log: CeaStrategyImpl.java,v $
Revision 1.1  2005/11/11 17:53:27  nw
added cea polling to lookout.
 
*/
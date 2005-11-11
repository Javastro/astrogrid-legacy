/*$Id: JesStrategyImpl.java,v 1.2 2005/11/11 10:08:18 nw Exp $
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

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.ApplicationInformation;
import org.astrogrid.acr.astrogrid.Applications;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.ExecutionInformation;
import org.astrogrid.acr.astrogrid.Jobs;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.applications.beans.v1.cea.castor.ResultListType;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.desktop.modules.ag.ApplicationsInternal;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal;
import org.astrogrid.desktop.modules.ag.MessageUtils;
import org.astrogrid.desktop.modules.ag.MessagingInternal;
import org.astrogrid.desktop.modules.ag.MessageRecorderInternal.Folder;
import org.astrogrid.desktop.modules.system.SchedulerInternal;
import org.astrogrid.jes.delegate.JesDelegateException;
import org.astrogrid.jes.delegate.JesDelegateFactory;
import org.astrogrid.jes.delegate.JobController;
import org.astrogrid.portal.workflow.intf.JobExecutionService;
import org.astrogrid.portal.workflow.intf.WorkflowInterfaceException;
import org.astrogrid.portal.workflow.intf.WorkflowManagerFactory;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;
import org.astrogrid.workflow.beans.v1.execution.JobURN;
import org.astrogrid.workflow.beans.v1.execution.WorkflowSummaryType;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.exolab.castor.xml.CastorException;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/** 
 * remote process strategy for jobs.
 * 
 * periodically poll job service, inject messages into the system
 * temporary class, until jes actually passes messages itself.
 * @author Noel Winstanley nw@jb.man.ac.uk 05-Nov-2005
 *
 */
public class JesStrategyImpl implements JesStrategyInternal, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(JesStrategyImpl.class);

    public JesStrategyImpl(MessagingInternal messaging
            , MessageRecorderInternal recorder
            , Community comm
            , SchedulerInternal scheduler
            , ApplicationsInternal apps
            ) throws JMSException {
        super();
        this.comm = comm;
        comm.addUserLoginListener(this);        
        this.apps = apps;
        this.scheduler = scheduler;
        this.recorder = recorder;
        this.sess = messaging.createSession();
        this.prod = sess.createProducer(messaging.getEventQueue());
        prod.setDisableMessageID(true);
            txtMsg = sess.createTextMessage();
        
    }
    final DateFormat df = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT,SimpleDateFormat.SHORT);
    final Community comm;
    final MessageProducer prod;
    final MessageRecorderInternal recorder;
    final TextMessage txtMsg;
    final Session sess;
    final SchedulerInternal scheduler; // already auto-registered with this.
    final ApplicationsInternal apps;
    private Account acc;      
    boolean poll = false;
    
    private JobController jes;
    private WorkflowManagerFactory fac;
    private synchronized JobController getJes() {
        if (jes == null) {
            // auto-configures.
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
 
    
    public final  long getPeriod() {
        return 1000 * 60 * 5;
    }

    // horribly inefficient munging, copying, and marshalling/unmarshalling.
    // only to be passed over an in-vm wire, and then the same rigmarole repeated at the other end
    // will stand in good stead for later developments though.
    public void run() {
        if (!poll) { // only run if this flag is set.
            return;
        }
        try {
            WorkflowSummaryType[] arr = getJes().listJobs(getAccount());

        for (int i = 0 ; i < arr.length; i++) {
            checkSingleJob(arr[i]);
        } 
        } catch (JesDelegateException e) {
            logger.warn("Failed to refresh jobs progress",e);
        }       
    }
    
    


    /** check a single job for updates.
     * to be called within shceduler thread.
     * @param jobSummary
     * @throws JesDelegateException
     */
    private void checkSingleJob(WorkflowSummaryType jobSummary) throws JesDelegateException {
        try {
            Folder f= recorder.getFolder(new URI(jobSummary.getJobId().getContent()));
            String currentStatus;
            int messageCount;
            if (f == null) {
                // totally new.. upload everything
                currentStatus = ExecutionInformation.UNKNOWN;
                messageCount = 0;
            } else {                    
                currentStatus = f.getInformation().getStatus();
                if(isCompletedOrError(currentStatus) ) {
                   return; // nothing moreto report here.
                } // otherwise..
                messageCount = recorder.listFolder(f).length;
            }
            String newStatus = jobSummary.getStatus().toString(); //@todo may need to convert between phase and status, or something - I forget..
            txtMsg.setStringProperty(MessageUtils.PROCESS_ID_PROPERTY,jobSummary.getJobId().getContent());
            txtMsg.setStringProperty(MessageUtils.PROCESS_NAME_PROPERTY,jobSummary.getWorkflowName());
            if (jobSummary.getStartTime() != null) {
                txtMsg.setStringProperty(MessageUtils.START_TIME_PROPERTY,df.format(jobSummary.getStartTime()));
            }
            if (jobSummary.getFinishTime() != null) {
                txtMsg.setStringProperty(MessageUtils.END_TIME_PROPERTY,df.format(jobSummary.getFinishTime()));
            }
            
            if (! currentStatus.equals(newStatus)) { // emit a status change
                txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.STATUS_CHANGE_MESSAGE);
                txtMsg.setText(newStatus);
                prod.send(txtMsg);
            }
            if (messageCount < jobSummary.getMessageCount()) { // some messages to pass on
                txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.INFORMATION_MESSAGE);
                for (int j = messageCount;  j < jobSummary.getMessageCount(); j++) {
                    StringWriter s = new StringWriter();
                    jobSummary.getMessage()[j].marshal(s);
                    txtMsg.setText(s.toString());
                    prod.send(txtMsg);
                }
            }
            if (isCompletedOrError(newStatus)) { // emit result message  
                txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.RESULTS_MESSAGE);                                       
                Workflow wf = getJes().readJob(jobSummary.getJobId());
                ResultListType results = new ResultListType();
                ParameterValue v = new ParameterValue();
                v.setIndirect(false);
                v.setName("transcript");
                StringWriter s = new StringWriter();
                wf.marshal(s);
                v.setValue(s.toString());
                results.setResult(new ParameterValue[]{v });
                s = new StringWriter();
                results.marshal(s);
                txtMsg.setText(s.toString());
                prod.send(txtMsg);
                // free space.
                txtMsg.clearBody();                
        }
        } catch (JMSException e) {
            logger.warn("Failed to process job info for " + jobSummary.getJobId().getContent());
        } catch (IOException e) {
            logger.warn("Failed to process job info for " + jobSummary.getJobId().getContent());
        } catch (CastorException e) {
            logger.warn("Failed to process job info for " + jobSummary.getJobId().getContent());
        } catch (URISyntaxException e) {
            logger.warn("Failed to process job info for " + jobSummary.getJobId().getContent());
        }
    }

    private boolean isCompletedOrError(String s) {
        return s.equals(ExecutionInformation.COMPLETED) 
        || s.equals(ExecutionInformation.ERROR);
    }


    // user login listener interface
    public void userLogin(UserLoginEvent arg0) {
        // enable polling of the server.
        poll = true;
        // do a poll straight away, to get things started.
        scheduler.runNow(this);
        
    }

    public void userLogout(UserLoginEvent arg0) {
        poll = false;
        acc = null;
    }

    // remote process strategy.
    public boolean canProcess(URI execId) {
        return "jes".equals(execId.getScheme());
    }

    public boolean canProcess(Document doc) {    
        try {
            Unmarshaller.unmarshal(Workflow.class,doc);
            return true;
        } catch (Exception e) {
            return false;
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
        scheduler.runNow(this);
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
Revision 1.2  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.1  2005/11/10 10:46:58  nw
big change around for vo lookout
 
*/
/*$Id: MessagingExecutionController.java,v 1.5 2005/11/24 01:13:24 nw Exp $
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.background;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Registry;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.ThreadPoolExecutionController;
import org.astrogrid.applications.manager.persist.ExecutionHistory;
import org.astrogrid.applications.manager.persist.FileStoreExecutionHistory;
import org.astrogrid.desktop.modules.ag.CeaHelper;
import org.astrogrid.desktop.modules.ag.MessageUtils;
import org.astrogrid.desktop.modules.ag.MessagingInternal;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;

import java.io.StringWriter;
import java.util.Observable;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/** Execution controller that adds a monitor to applicatoins that inserts progress messages into jms.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Oct-2005
 *
 */
public class MessagingExecutionController extends ThreadPoolExecutionController implements ManagingExecutionController {

    /** Construct a new MessagingExecutionController
     * @param arg0
     * @param arg1
     * @throws ServiceException
     * @throws JMSException
     */
    public MessagingExecutionController(ApplicationDescriptionLibrary arg0, ExecutionHistory arg1, PooledExecutor e,MessagingInternal messaging, Registry reg) throws ServiceException, JMSException {
        super(arg0, arg1, e);
        ceaHelper = new CeaHelper(reg);
        sess = messaging.createSession();
        prod = sess.createProducer(messaging.getEventQueue());       
        prod.setDisableMessageID(true);
        txtMsg = sess.createTextMessage();
    }
    final Session sess;
    final TextMessage txtMsg;
    final MessageProducer prod;
    final Executor exec = new QueuedExecutor();
    final CeaHelper ceaHelper;

    public void update(final Observable o, final Object arg) {
        super.update(o, arg);
        try {
            // now notify messaging system - always want to do this on the same thread (as sessions are single threaded) - hence using an executor
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        AbstractApplication app = (AbstractApplication)o;
                        //txtMsg.setStringProperty(MessageUtils.PROCESS_NAME_PROPERTY,app.getApplicationDescription().getName());
                        txtMsg.setStringProperty(MessageUtils.PROCESS_NAME_PROPERTY,app.getTool().getName());                        
                        txtMsg.setStringProperty(MessageUtils.PROCESS_ID_PROPERTY,ceaHelper.mkLocalTaskURI(app.getID()).toString());
                        txtMsg.setStringProperty(MessageUtils.CLIENT_ASSIGNED_ID_PROPERTY,app.getJobStepID());

                        if (arg instanceof Status) {
                            Status stat = (Status)arg;                            
                            txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.STATUS_CHANGE_MESSAGE);
                            txtMsg.setText(stat.toExecutionPhase().toString());
                            prod.send(txtMsg);
                            if (stat.equals(Status.COMPLETED) || stat.equals(Status.ERROR)) {// send a results message too.
                                txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.RESULTS_MESSAGE);
                                StringWriter sw = new StringWriter();
                                app.getResult().marshal(sw);
                                txtMsg.setText(sw.toString());
                                prod.send(txtMsg);
                            }
                        } else if (arg instanceof MessageType) {
                            MessageType m = (MessageType)arg;
                            txtMsg.setStringProperty(MessageUtils.MESSAGE_TYPE_PROPERTY,MessageUtils.INFORMATION_MESSAGE);
                            StringWriter s= new StringWriter();
                            m.marshal(s);                            
                            txtMsg.setText(s.toString());
                            prod.send(txtMsg);
                        }
                        txtMsg.clearBody();
                    } catch (JMSException e) {                        
                        logger.warn("Failed to send notification message",e);
                    } catch (MarshalException e) {
                        logger.warn("Failed to send notification message",e);
                    } catch (ValidationException e) {
                        logger.warn("Failed to send notification message",e);
                    } catch (ServiceException e) {
                        // @todo Auto-generated catch block
                        logger.debug("ServiceException",e);
                    }
                }
            });
        } catch (InterruptedException e) {
            logger.debug("InterruptedException",e);
        }
    }

    /**
     * @see org.astrogrid.desktop.modules.background.ManagingExecutionController#delete(java.lang.String)
     */
    public void delete(String execId) {
        if (executionHistory.isApplicationInCurrentSet(execId)) {
            return; // not finished yet.
        }
        ManagingFileStoreExecutionHistory f = (ManagingFileStoreExecutionHistory)executionHistory;
        f.delete(execId);
    }
}


/* 
$Log: MessagingExecutionController.java,v $
Revision 1.5  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.4.2.1  2005/11/23 18:09:28  nw
tuned up.

Revision 1.4  2005/11/11 17:53:27  nw
added cea polling to lookout.

Revision 1.3  2005/11/11 10:08:18  nw
cosmetic fixes

Revision 1.2  2005/11/10 10:46:58  nw
big change around for vo lookout

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/
/*$Id: MessagingExecutionController.java,v 1.1 2005/11/01 09:19:46 nw Exp $
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
import org.astrogrid.applications.Application;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.cea.castor.MessageType;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.ThreadPoolExecutionController;
import org.astrogrid.applications.manager.persist.ExecutionHistory;

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
public class MessagingExecutionController extends ThreadPoolExecutionController {

    /** Construct a new MessagingExecutionController
     * @param arg0
     * @param arg1
     * @throws ServiceException
     * @throws JMSException
     */
    public MessagingExecutionController(ApplicationDescriptionLibrary arg0, ExecutionHistory arg1, PooledExecutor e,MessagingInternal messaging) throws ServiceException, JMSException {
        super(arg0, arg1, e);
        sess = messaging.createSession();
        prod = sess.createProducer(messaging.getEventQueue());       
        prod.setDisableMessageID(true);
        msg = sess.createMapMessage();
        txtMsg = sess.createTextMessage();
        txtMsg.setStringProperty("type","results");        
    }
    final Session sess;
    final MapMessage msg;
    final TextMessage txtMsg;
    final MessageProducer prod;
    final Executor exec = new QueuedExecutor();

    public void update(final Observable o, final Object arg) {
        super.update(o, arg);
        try {
            // now notify messaging system - always want to do this on the same thread (as sessions are single threaded) - hence using an executor
            exec.execute(new Runnable() {
                public void run() {
                    try {
                        Application app = (Application)o;
                        msg.setStringProperty("cea_application_name",app.getApplicationDescription().getName());
                        msg.setStringProperty("cea_application_id",app.getID());
                        msg.setStringProperty("cea_application_jid",app.getJobStepID());

                        if (arg instanceof Status) {
                            Status stat = (Status)arg;                            
                            msg.setStringProperty("type","status-change");
                            msg.setString("phase",stat.toExecutionPhase().toString());
                            prod.send(msg);
                            if (stat.equals(Status.COMPLETED)) {// send a results message too.
                                txtMsg.setStringProperty("cea_application_name",app.getApplicationDescription().getName());                                                                
                                txtMsg.setStringProperty("cea_application_id",app.getID());
                                txtMsg.setStringProperty("cea_application_jid",app.getJobStepID());                                
                                StringWriter sw = new StringWriter();
                                app.getResult().marshal(sw);
                                txtMsg.setText(sw.toString());
                                prod.send(txtMsg);
                            }
                        } else if (arg instanceof MessageType) {
                            MessageType m = (MessageType)arg;
                            msg.setStringProperty("type","message");
                            // payload.
                            msg.setString("content",m.getContent());
                            msg.setString("level",m.getLevel().toString());
                            msg.setString("phase",m.getPhase().toString());
                            msg.setString("source",m.getSource());
                            msg.setString("timestamp",m.getTimestamp().toString()); //@todo if we need to parse this back, may need to specify a format
                            prod.send(msg);
                        }                      
                    } catch (JMSException e) {                        
                        logger.warn("Failed to send notification message",e);
                    } catch (MarshalException e) {
                        logger.warn("Failed to send notification message",e);
                    } catch (ValidationException e) {
                        logger.warn("Failed to send notification message",e);
                    }
                }
            });
        } catch (InterruptedException e) {
            logger.debug("InterruptedException",e);
        }
    }
}


/* 
$Log: MessagingExecutionController.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/
/*$Id: ActiveMQMessaging.java,v 1.1 2005/11/01 09:19:46 nw Exp $
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

import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.Configuration;

import org.activemq.ActiveMQConnectionFactory;
import org.activemq.broker.impl.BrokerContainerFactoryImpl;
import org.activemq.message.ActiveMQQueue;
import org.activemq.message.ActiveMQTopic;
import org.activemq.store.vm.VMPersistenceAdapter;
import org.picocontainer.Startable;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.Puttable;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;
import EDU.oswego.cs.dl.util.concurrent.Takable;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

/** @todo configure message transport
 *  @todo attach to server.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Oct-2005
 *
 */
public class ActiveMQMessaging implements MessagingInternal, Startable , UserLoginListener, ExceptionListener, MessageListener{
    public static final String ERROR_TOPIC_NAME = "error_report";
    public static final String TRACKING_TOPIC_NAME = "tracking_queue";
    public static final String ALERT_TOPIC_NAME = "system_alerts";
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(ActiveMQMessaging.class);

    /** Construct a new ActiveMQMessaging
     * @throws JMSException
     * 
     */
    public ActiveMQMessaging(Community comm, Configuration conf) throws JMSException {
        super();
        this.comm=comm;
        comm.addUserLoginListener(this);
        this.conf = conf;
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setUseEmbeddedBroker(true);
        factory.setBrokerContainerFactory(new BrokerContainerFactoryImpl(new VMPersistenceAdapter())); 
        factory.setBrokerURL("vm://localhost"); 
        factory.start();
        connection = factory.createConnection();
        connection.setExceptionListener(this);
        listenerSession = createSession();
        eventQueueMultiplexer = listenerSession.createTemporaryTopic();
        forwarderProducer = listenerSession.createProducer(eventQueueMultiplexer);
        
        // create reporting daemon thread.
        LinkedQueue q  = new LinkedQueue();
        reportingQueue = q;
        reporter = new Thread(new ReportingTask(q),"Reporter");
    }
    private final Connection connection;
    private final Community comm;
    private final Session listenerSession;

    private final Topic eventQueueMultiplexer;
    private final Configuration conf; 
    private final Map processors = new HashMap();
    private final Map subscribers = new HashMap();

    private MessageConsumer forwarderConsumer;
    private final MessageProducer forwarderProducer;
    
    private final Puttable reportingQueue;
    private final Thread reporter;
    
    /** error and tracking reporter - runs in a separate thread, forwarding messages */
    private  class ReportingTask implements Runnable {
        public ReportingTask(Takable q) throws JMSException {
            this.q = q;
            Destination trackingTopic = new ActiveMQQueue(TRACKING_TOPIC_NAME);        
            Destination errorQueue = new ActiveMQQueue(ERROR_TOPIC_NAME);                
            Session reportingSession =  connection.createSession(false,Session.DUPS_OK_ACKNOWLEDGE);
            reportingProducer = reportingSession.createProducer(trackingTopic);
            reportingProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // assume something is there to catch the messages
            reportingProducer.setDisableMessageID(true);
            reportingProducer.setPriority(0);
            
            errorProducer = reportingSession.createProducer(errorQueue);
            errorProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // assume something is there to catch the messages
            errorProducer.setDisableMessageID(true);
            errorProducer.setPriority(0);
                        
            errorMessage = reportingSession.createMapMessage();
            reportingMessage = reportingSession.createMapMessage();
        }
        private final MapMessage errorMessage;
        private final MapMessage reportingMessage;
        private final MessageProducer reportingProducer;   
        private final MessageProducer errorProducer;
        private final Takable q;
       
        public void run() {
            // sit in a loop, checking for messages and sending them
            while(true) {
                try {
                Object o = q.take();
                if (o instanceof UserInformation) {
                    // fill in error message template...
                    UserInformation u = (UserInformation)o;
                    errorMessage.setString("user",u.getName());
                    errorMessage.setString("community",u.getCommunity());
                } else if (o instanceof MethodInvocation) { // send tracking message
                    MethodInvocation mi = (MethodInvocation)o;
                    // filter out the trivial stuff.. - system and builtin are removed already.
                    String p = mi.getMethod().getDeclaringClass().getPackage().getName();
                    if (p.indexOf(".ui") == -1) { // @todo later apply a regexp from config - then can turn up tracing for development versions
                        continue;
                    }
                     reportingMessage.setString("class",mi.getMethod().getDeclaringClass().getName());
                     reportingMessage.setString("method",mi.getMethod().getName());
                     reportingMessage.setString("args",ArrayUtils.toString( mi.getArguments()));
                    reportingProducer.send(reportingMessage);
                    System.err.println(reportingMessage);
                } else if (o instanceof Exception) { // send error messaage
                    Exception e = (Exception)o;
                    o = q.take(); // expect another 
                    if (!(o instanceof MethodInvocation)) {
                        logger.warn("Something gone badly wrong - exception not followed by invocation");
                        continue;
                    }
                    MethodInvocation mi = (MethodInvocation)o;
                    errorMessage.setString("class",mi.getMethod().getDeclaringClass().getName());
                    errorMessage.setString("method",mi.getMethod().getName());
                    errorMessage.setString("args",ArrayUtils.toString( mi.getArguments()));
                    
                    errorMessage.setString("exceptionClass",e.getClass().getName());
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    e.printStackTrace(pw);
                    errorMessage.setString("exceptionMessage",sw.toString());
                    
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    System.getProperties().save(bos,"");
                    errorMessage.setString("systemProperties",bos.toString());
                    
                    
                    Map m = conf.list();
                    Properties props = new Properties();
                    props.putAll(m);
                    bos = new ByteArrayOutputStream();
                    props.save(bos,"");
                    errorMessage.setString("configuration",bos.toString());
                                                            
                    errorProducer.send(errorMessage);
                    System.err.println(errorMessage);
                }
                } catch(InterruptedException e) {
                    logger.info("Reporter interrupted");
                    break; // something has killed this thread
                } catch (Exception e) {
                    logger.warn("Reporter encountered a problem",e);
                    // deliberately do notjing.
                }
            }            
        }

        /**
         * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogin(org.astrogrid.acr.astrogrid.UserLoginEvent)
         */
        public void userLogin(UserLoginEvent arg0) {
        }

        /**
         * @see org.astrogrid.acr.astrogrid.UserLoginListener#userLogout(org.astrogrid.acr.astrogrid.UserLoginEvent)
         */
        public void userLogout(UserLoginEvent arg0) {
        }
    }
    
    public Session createSession() throws JMSException {
            return  connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
    }
    
    private Destination eventQueue;
    /** returns the multiplexer - a  local topic that contains all messages on the user's remote event queue
     *  - this allows events to be passed to more than one consumer
     * @see org.astrogrid.desktop.modules.background.MessagingInternal#getEventQueue()
     */
    public Destination getEventQueue() {
        return eventQueueMultiplexer;
    }
    
    /** access the real, remote event queue */
    private synchronized Destination getRealEventQueue() { 
        if (eventQueue == null) {
            eventQueue = new ActiveMQQueue(comm.getUserInformation().getCommunity() + "/" + comm.getUserInformation().getPassword());
        }
        return eventQueue;
    }
    
    /** adds an event processor to the local event multipllexer */
    public synchronized void addEventProcessor(String condition, MessageListener l) throws JMSException {
        MessageConsumer c = listenerSession.createConsumer(getEventQueue(),condition);
        c.setMessageListener(l);
        processors.put(l,c);
    }
    
    public synchronized void removeEventProcessor(MessageListener l) throws JMSException {
        MessageConsumer c = (MessageConsumer)processors.remove(l);
        if (c != null) {
            c.close();
            c.setMessageListener(null);
        }
    }
    
    private Destination alertTopic;
    private Destination getAlertTopic() {
        if (alertTopic == null) {
            alertTopic = new ActiveMQTopic(ALERT_TOPIC_NAME);
        }
        return alertTopic;
    }
    
    public void addAlertSubscriber(MessageListener l) throws JMSException {
        MessageConsumer c = listenerSession.createConsumer(getAlertTopic());
        c.setMessageListener(l);
        subscribers.put(l,c);
    }
    
    public void removeAlertSubscriber(MessageListener l) throws JMSException {
        MessageConsumer c = (MessageConsumer)subscribers.remove(l);
        if (c != null) {
            c.close();
            c.setMessageListener(null);
        }        
    }
    
    
    

    

    public void sendTrackingMessage(MethodInvocation m) {
        try {
            reportingQueue.put(m);
        } catch (InterruptedException e) {
            logger.debug("InterruptedException",e);
        }           
    }
    
    public void sendErrorMessage(MethodInvocation m, Exception ex) {
        try {
                reportingQueue.put(ex);
                reportingQueue.put(m);
        } catch (InterruptedException e) {
            logger.debug("InterruptedException",e);
        }     
    }


    /**
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        try {
        connection.start();
        reporter.start();
        } catch (JMSException e) {
            logger.fatal("Failed to start messaging",e);
        }      
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
        try {
            reporter.interrupt();
            connection.stop();
        } catch (JMSException e) {
            // @todo Auto-generated catch block
            logger.warn("Failed to stop messaging",e);
        }
       
    }

 /** when user logs in, start forwarding messages from their remote event queue to the local event topic */
    public void userLogin(UserLoginEvent arg0) {
        try {
        forwarderConsumer = listenerSession.createConsumer(getRealEventQueue());        
        forwarderConsumer.setMessageListener(this);
        reportingQueue.put(comm.getUserInformation());
        } catch (JMSException e) {
            logger.fatal("Failed to enable message forwarding",e);
        } catch (InterruptedException e) {
            logger.debug("Interrupted - oh well");
        }

    }

    /** when user logs out, stop forwarding messages */
    public void userLogout(UserLoginEvent arg0) {
        try { // remove the forwarder - so we don't receive any user-specific messags
        forwarderConsumer.close();
        forwarderConsumer.setMessageListener(null);        
        forwarderConsumer = null;
        } catch (JMSException e) {
            logger.warn("Failed to stop forwarding messages", e);
        }

    }
    /** jms exception handler */
    public void onException(JMSException arg0) {
        logger.warn(arg0);        
    }
    /** forwards a message from the user's remote event queue to a local topic - forwarding and multiplexing it, so it can have multiple consumers */    
    public void onMessage(Message arg0) {
        try {
        forwarderProducer.send(arg0);
        } catch (JMSException e) {
            logger.error("Failed to forward message",e);
        }
    }

}


/* 
$Log: ActiveMQMessaging.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/
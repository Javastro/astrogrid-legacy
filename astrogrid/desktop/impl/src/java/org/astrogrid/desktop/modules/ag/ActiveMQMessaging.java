/*$Id: ActiveMQMessaging.java,v 1.2 2005/11/24 01:13:24 nw Exp $
 * Created on 21-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.Configuration;

import org.activemq.ActiveMQConnectionFactory;
import org.activemq.broker.impl.BrokerContainerImpl;
import org.activemq.message.ActiveMQQueue;
import org.activemq.message.ActiveMQTopic;
import org.activemq.store.vm.VMPersistenceAdapter;
import org.activemq.transport.NetworkConnector;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.Startable;

import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.Puttable;
import EDU.oswego.cs.dl.util.concurrent.Takable;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.Topic;

/** 
 * Implementation of the messaging system.
 * maintains topics that other clients can connect to, plus provides way of sending tracking and error 
 * reports back tot he server.
 * 
 * @todo configure message transport
 * @todo tune down messaging thread priorities.
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
     * @throws URISyntaxException
     * 
     */
    public ActiveMQMessaging(Community comm, Configuration conf) throws JMSException, URISyntaxException {
        super();
        this.comm=comm;
        comm.addUserLoginListener(this);
        this.conf = conf;
        // ncessary to configure programmatically, as don't have spring on classpath to use xml
        // anyhow, want to be able to adjust the remote endpoint based on config - so an xml file is no good
        BrokerContainerImpl broker = new BrokerContainerImpl("workbench-broker",new VMPersistenceAdapter());
        
        // happy just with the default in-vm one.
        List transportConnectorList = new ArrayList();
        /*
        WireFormat wf = new JabberWireFormat();
        TransportServerChannel transportServerChannel = TransportServerChannelProvider.newInstance(wf,
                "reliable://http://localhost:8080");
        BrokerConnectorImpl brokerConnector = new BrokerConnectorImpl(broker,transportServerChannel );
        transportConnectorList.add(brokerConnector);
        */       
        broker.setTransportConnectors(transportConnectorList);
        
        List networkConnectorList = new ArrayList();
        NetworkConnector networkConnector= new NetworkConnector(broker);
        /*
        NetworkChannel channel = networkConnector.addNetworkChannel("http://localhost:8080");
        channel.setReconnectSleepTime(60000); // minute
        channel.setDemandBasedForwarding(true);
        */
        // get different behaviour if we say
       // NetworkChannel channel = new NetworkChannel(networkConnector,broker,"http://localhost:8080");
       // networkConnector.addNetworkChannel(channel);
       // channel.getLocalPrefetchPolicy().
       // networkConnectorList.add(networkConnector);
        
        
        broker.setNetworkConnectors(networkConnectorList);
        broker.start();
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(broker,"vm://workbench-broker");
        factory.start();

        connection = factory.createConnection();
        connection.setExceptionListener(this);
        listenerSession = createSession();
        eventQueueMultiplexer = listenerSession.createTemporaryTopic();
        forwarderProducer = listenerSession.createProducer(eventQueueMultiplexer);
        
        // create reporting daemon thread.
        LinkedQueue q  = new LinkedQueue();
        reportingQueue = q;
        //@todo - replace with real reporter when messaging server is available.
        reporter = new Thread(new DevNullReportingTask(q),"Reporter");
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
    
    
    /** reporting task that just keeps emptying the queue and discarding messages - use 
     * until we have an upstream server to connect to 
     * @author Noel Winstanley nw@jb.man.ac.uk 07-Nov-2005
     *
     */
    private class DevNullReportingTask implements Runnable {
        public DevNullReportingTask(Takable q) {
            this.q = q;
        }
        private final Takable q;
        public void run() {
            while(true) {
                try {
                    Object o = q.take();
                } catch (Exception e) {
                    // don't care
                }
            }
        }
    }
    
    public Session createSession() throws JMSException {
            return  connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
    }
    
    private Destination eventQueue;
    /** returns the multiplexer - a  local topic that contains all messages on the user's remote event queue
     *  - this allows events to be passed to more than one consumer
     * @see org.astrogrid.desktop.modules.ag.MessagingInternal#getEventQueue()
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
Revision 1.2  2005/11/24 01:13:24  nw
merged in final changes from release branch.

Revision 1.1.2.1  2005/11/23 04:56:44  nw
doc fix

Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout

Revision 1.2  2005/11/04 10:14:26  nw
added 'logo' attribute to registry beans.
added to astroscope so that logo is displayed if present

Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/
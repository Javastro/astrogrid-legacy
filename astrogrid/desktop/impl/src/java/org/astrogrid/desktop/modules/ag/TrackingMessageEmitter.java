/*$Id: TrackingMessageEmitter.java,v 1.1 2005/11/10 12:05:43 nw Exp $
 * Created on 07-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.acr.astrogrid.UserInformation;
import org.astrogrid.acr.astrogrid.UserLoginEvent;
import org.astrogrid.acr.astrogrid.UserLoginListener;
import org.astrogrid.acr.system.Configuration;

import org.activemq.message.ActiveMQQueue;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ArrayUtils;

import EDU.oswego.cs.dl.util.concurrent.Takable;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Session;


/** error and tracking reporter - runs in a separate thread, forwarding messages
 * 
 * todo - implement to return more info when user has logged in.
 *  */
class TrackingMessageEmitter implements Runnable, UserLoginListener {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(TrackingMessageEmitter.class);

    public TrackingMessageEmitter(Takable q, Connection connection, Configuration conf) throws JMSException {
        this.q = q;
        reportingSession =  connection.createSession(false,Session.AUTO_ACKNOWLEDGE); //DUPS_OK_ACKNOWLEDGE);
        reportingProducer = reportingSession.createProducer(new ActiveMQQueue(ActiveMQMessaging.TRACKING_TOPIC_NAME));
   //     reportingProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // assume something is there to catch the messages
    //    reportingProducer.setDisableMessageID(true);
    //    reportingProducer.setPriority(0);
        
        errorProducer = reportingSession.createProducer(new ActiveMQQueue(ActiveMQMessaging.ERROR_TOPIC_NAME));
        errorProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT); // assume something is there to catch the messages
        errorProducer.setDisableMessageID(true);
        errorProducer.setPriority(0);
                    
        errorMessage = reportingSession.createMapMessage();
        reportingMessage = reportingSession.createMapMessage();
        this.conf = conf;
    }
    private final Configuration conf;
    private final             Session reportingSession;
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
                System.err.println("Method invocation: " + reportingMessage);
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

/* 
$Log: TrackingMessageEmitter.java,v $
Revision 1.1  2005/11/10 12:05:43  nw
big change around for vo lookout
 
*/
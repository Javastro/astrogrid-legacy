/*$Id: MessagingInternal.java,v 1.1 2005/11/01 09:19:46 nw Exp $
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

import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.Session;

/** Internal interface to a messaging component.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-Oct-2005
 *
 */
public interface MessagingInternal {
   /** create a new JMS session - can only be used within one thread */
    public Session createSession() throws  JMSException;
    /** access the queue for notifying users of events 
     * 
     * this queue provides execution events relating to cea, jes, etc.
     * */
    public Destination getEventQueue();
    /** add a consumer to the event queue
     * @param condition a filter for events. may be null to indicate (all events)
     * @param l a listener 
     * @throws JMSException
     */
    void addEventProcessor(String condition, MessageListener l) throws JMSException;
    /**
     * @param l
     * @throws JMSException
     */
    void removeEventProcessor(MessageListener l) throws JMSException;
    /** add a subscriber to the alert topic
     * @param l
     * @throws JMSException
     */
    void addAlertSubscriber(MessageListener l) throws JMSException;
    /**
     * @param l
     * @throws JMSException
     */
    void removeAlertSubscriber(MessageListener l) throws JMSException;

    
    void sendTrackingMessage(MethodInvocation m) ;
    
    void sendErrorMessage(MethodInvocation m, Exception e);
}


/* 
$Log: MessagingInternal.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/
/* $Id: MessageToAdminActionTest.java,v 1.2 2004/03/24 18:31:33 jdt Exp $
 * Created on Mar 22, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.portal.cocoon.common;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;

import junit.framework.TestCase;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.astrogrid.config.Config;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.portal.cocoon.messaging.EmailMessenger;
import org.astrogrid.portal.cocoon.messaging.Messenger;
import org.astrogrid.portal.cocoon.messaging.MessengerException;
import org.astrogrid.portal.cocoon.messaging.MockEmailMessenger;

/**
 *JUnit tests
 * 
 * @author jdt
 */
public final class MessageToAdminActionTest extends TestCase {
    /** Test constants */
    private static final String recipient="jdt@roe.ac.uk";
    /** Test constants */
    private static final String server="abc.def.com";
    /** Test constants */
    private static final String user="jdt";
    /** Test constants */
    private static final String pass="secret";
    /** Test static constants */
    private final String sender="fred@foo.com";
    /**
     * Kick off the test
     * @param args unused
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(MessageToAdminActionTest.class);
    }
    /**
     * Class to test for void MessageToAdminAction(boolean)
     * Just checks we can instantiate using this ctor without problems.  
     */
    public void testMessageToAdminActionboolean() {
        final MessageToAdminAction act = new MessageToAdminAction(true);
        final Messenger messenger = act.getMessenger();
        assertTrue("The messenger should not be a mock using this ctor", messenger instanceof MockEmailMessenger);
        
    }
    /**
     * Class to test for void MessageToAdminAction()
     * Just checks we can instantiate using this ctor without problems.  
     */
    public void testMessageToAdminAction() {
        final MessageToAdminAction act = new MessageToAdminAction();
        final Messenger messenger = act.getMessenger();
        assertTrue("The messenger should not be a mock using this ctor", messenger instanceof EmailMessenger);
    }
    /**
     * Test correct failure on missing name
     *
     */
    public void testNoName() {
        missingParam(1);
    }
    /**
     * Test correct failure on missing email
     *
     */
    public void testNoEmail() {
        missingParam(2);
    }
    /**
     * Test correct failure on missing subject
     *
     */
    public void testNoSubject() {
        missingParam(3);
    }
    /**
     * Test correct failure on missing message
     *
     */
    public void testNoMessage() {
        missingParam(4);
    }
    
    /**
     * common bits for missing parameters
     * @param param the number of the param you wish to exlude 1-4
     */
    private void missingParam(final int param) {
        final String name="jdt";
        final String email="jdt@roe";
        final String subject="Attention";
        final String message="Hello World";
        
        final MessageToAdminAction action = new MessageToAdminAction(true);
        final MockEmailMessenger messenger = (MockEmailMessenger) action.getMessenger();
        messenger.setExpectedSendMessageCalls(0);
        
        final DummyRequest request = new DummyRequest();
        final Parameters params = new Parameters();
        if (param!=1) {
            request.addParameter("name", name);
        }
        if (param!=2) {
            request.addParameter("email", email);
        }
        if (param!=3) {
            params.setParameter("subject", subject);
        }
        if (param!=4) {
            params.setParameter("message", message);
        }
        
        final Map objectModel = new HashMap();
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        
        
        final Map result = action.act(null, null, objectModel, null, params);
        assertNull("expect null result due to missing param", result);
        messenger.verify();
    }
    
    /**
     * What happens if server problems?
     */
    public void testEmailProbs() {
        final String name="jdt";
        final String email="jdt@roe";
        final String subject="Attention";
        final String message="Hello World";
        
        final MessageToAdminAction action = new MessageToAdminAction(true);
        final MockEmailMessenger messenger = (MockEmailMessenger) action.getMessenger();
        messenger.setExpectedSendMessageCalls(1);
        MessengerException ex = new MessengerException("surprise");
        messenger.setupExceptionSendMessage(ex);
        
        final DummyRequest request = new DummyRequest();
        final Parameters params = new Parameters();
        
        request.addParameter("name", name);
        request.addParameter("email", email);
        params.setParameter("subject", subject);
        params.setParameter("message", message);
        
        final Map objectModel = new HashMap();
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        
        
        final Map result = action.act(null, null, objectModel, null, params);
        assertNull("expect null result due to exception", result);
        messenger.verify();
    }
    /**
     * Actually do a decent test
     *
     */
    public void testAct() {
        final String name="jdt";
        final String email="jdt@roe";
        final String subject="Attention";
        final String message="Hello World";
        
        final MessageToAdminAction act = new MessageToAdminAction(true);
        final MockEmailMessenger messenger = (MockEmailMessenger) act.getMessenger();
        messenger.setExpectedSendMessageCalls(1);
        messenger.addExpectedSendMessageValues(subject,message+"\n"+name+"\n"+email+"\n");
        messenger.setupExceptionSendMessage(new Exception("surprise"));
        
        final Parameters params = new Parameters();
        final DummyRequest request = new DummyRequest();

        request.addParameter("name", name);
        request.addParameter("email", email);
        params.setParameter("subject", subject);
        params.setParameter("message", message);
        
        final Map objectModel = new HashMap();
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        
        
        final Map result = act.act(null, null, objectModel, null, params);
        assertNotNull(result);
        messenger.verify(); //check we did get 1 call to sendmessage
    }
    
    /**
     * Do mail props get setup?
     */
    public void testEmailConfig() {

 
        final MessageToAdminAction action = new MessageToAdminAction(true);
        final MockEmailMessenger messenger = (MockEmailMessenger) action.getMessenger();

        assertEquals(messenger.getRecipient(),recipient);
        final Session sesh = messenger.getSession();
        final Properties props = sesh.getProperties();
        assertEquals(props.getProperty("mail.transport.protocol"),"smtp");
        assertEquals(props.getProperty("mail.host"),server);
        assertEquals(props.getProperty("mail.user"),user);
        assertEquals(props.getProperty("mail.password"),pass);
        assertEquals(props.getProperty("mail.from"),sender);
    }
    
    /**
     * Initialise test
     * @see junit.framework.TestCase#setUp()
     *
     */
    public void setUp() {      
        final Config config = SimpleConfig.getSingleton();
        config.setProperty(MessageToAdminAction.EMAIL_TO,recipient);
        config.setProperty(MessageToAdminAction.EMAIL_SERVER,server);
        config.setProperty(MessageToAdminAction.EMAIL_USER,user);
        config.setProperty(MessageToAdminAction.EMAIL_PWD,pass);
        config.setProperty(MessageToAdminAction.EMAIL_FROM,sender);
    }
    

}


/*
 *  $Log: MessageToAdminActionTest.java,v $
 *  Revision 1.2  2004/03/24 18:31:33  jdt
 *  Merge from PLGN_JDT_bz#201
 *
 *  Revision 1.1.2.2  2004/03/24 18:14:46  jdt
 *  some parameters are now passed in a map rather than
 *  in the request.  Changed the test accordingly.
 *
 *  Revision 1.1.2.1  2004/03/23 01:03:50  jdt
 *  At last, some unit tests
 *
 */
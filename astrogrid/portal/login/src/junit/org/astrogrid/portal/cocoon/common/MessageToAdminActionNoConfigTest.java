/* $Id: MessageToAdminActionNoConfigTest.java,v 1.1 2004/03/26 14:53:19 jdt Exp $
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
public final class MessageToAdminActionNoConfigTest extends TestCase {
    /**
     * Kick off the test
     * @param args unused
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(MessageToAdminActionNoConfigTest.class);
    }
    /**
     * What happens if we don't set up the props required by the emailer?
     * Should fall back to some default values.
     */
    public void testEmailConfig() {

 
        final MessageToAdminAction action = new MessageToAdminAction(true);
        final MockEmailMessenger messenger = (MockEmailMessenger) action.getMessenger();
        // defaults set up in action class
        Object recipient = "jdt@roe.ac.uk";
        Object server = "127.0.0.1";
        Object user = "astrogrid";
        Object pass = "";
        Object sender = "astrogrid@astrogrid.org";
        assertEquals(messenger.getRecipient(),recipient);
        final Session sesh = messenger.getSession();
        final Properties props = sesh.getProperties();
        assertEquals(props.getProperty("mail.transport.protocol"),"smtp");
        
        assertEquals(props.getProperty("mail.host"),server);
        
        assertEquals(props.getProperty("mail.user"),user);
        
        assertEquals(props.getProperty("mail.password"),pass);
        
        assertEquals(props.getProperty("mail.from"),sender);
        
        final Parameters params = new Parameters();
        final DummyRequest request = new DummyRequest();

        final String name="jdt";
        final String email="jdt@roe";
        final String subject="Attention";
        final String message="Hello World";
        request.addParameter("name", name);
        request.addParameter("email", email);
        params.setParameter("subject", subject);
        params.setParameter("message", message);
        
        final Map objectModel = new HashMap();
        objectModel.put(ObjectModelHelper.REQUEST_OBJECT, request);
        
        
        final Map result = action.act(null, null, objectModel, null, params);
        assertNotNull(result);
    }
    

    

}


/*
 *  $Log: MessageToAdminActionNoConfigTest.java,v $
 *  Revision 1.1  2004/03/26 14:53:19  jdt
 *  New test
 *
 */
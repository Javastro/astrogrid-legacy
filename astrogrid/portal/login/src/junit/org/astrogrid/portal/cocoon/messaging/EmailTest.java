/* $Id: EmailTest.java,v 1.1 2004/03/25 15:19:28 jdt Exp $
 * Created on Mar 25, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.portal.cocoon.messaging;

import junit.framework.TestCase;

/**
 * Test of the email code
 * 
 * @author jdt
 */
public class EmailTest extends TestCase {
    public static void main(String[] args) {
        junit.textui.TestRunner.run(EmailTest.class);
    }
    public void testFailToSendMessage() {
        EmailMessengerFactory factory = new EmailMessengerFactory("Duff address","duff user", "lousy password", "bad address" );
        Messenger messenger = factory.getEmailMessenger("noone");
        try {
            messenger.sendMessage("No subject","No message");
            fail("Expect an exception");
        } catch (MessengerException e) {
            return; //expected
        }
    }
}


/*
 *  $Log: EmailTest.java,v $
 *  Revision 1.1  2004/03/25 15:19:28  jdt
 *  Some refactoring of the debugging and added unit tests.
 *
 */
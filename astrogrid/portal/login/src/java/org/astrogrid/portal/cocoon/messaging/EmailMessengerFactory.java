/* $Id: EmailMessengerFactory.java,v 1.4 2004/03/24 18:31:33 jdt Exp $
 * Created on Mar 14, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.portal.cocoon.messaging;
import java.util.Properties;

import javax.mail.Session;
/**
 * Creates Messengers that dispatch emails
 * 
 * @author jdt
 */
public final class EmailMessengerFactory  {
    /**
     * The mail session
     */
    private Session session;

    /**
     * Constructor
     * @param smtpServer IP address of server, port 25 assumed
     * @param user username of sender's account
     * @param password password for said account
     * @param returnAddress who shall we say it's from?
     */
    public EmailMessengerFactory(final String smtpServer, final String user, final String password,final String returnAddress) {
        final Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", smtpServer);
        props.setProperty("mail.user", user);
        props.setProperty("mail.password", password);
        props.setProperty("mail.from", returnAddress);
        session = Session.getInstance(props,null);
   }

    /**
     * Create a messenger that can be used to message these recipients
     * @param recipients comma separated list of recipients
     * @return an EmailMessenger
     */
    public Messenger getEmailMessenger(final String recipients) {
            return new EmailMessenger(session, recipients);
    }
    
    /**
     * Create a messenger that can be used to message these recipients
     * @param recipients comma separated list of recipients
     * @param mock return a mock messenger if true
     * @return an EmailMessenger
     */
    public Messenger getEmailMessenger(final String recipients,final boolean mock) {
        if (mock) {
            return new MockEmailMessenger(session, recipients);
        } else {
            return getEmailMessenger(recipients);
        }
    }
    

    
}




/*
 *  $Log: EmailMessengerFactory.java,v $
 *  Revision 1.4  2004/03/24 18:31:33  jdt
 *  Merge from PLGN_JDT_bz#201
 *
 *  Revision 1.3.2.1  2004/03/23 00:47:39  jdt
 *  added facility to return a mock mailer for testing
 *
 *  Revision 1.3  2004/03/19 13:02:25  jdt
 *  Pruned the log messages - they cause conflicts on merge, 
 *  best just to reduce them to the merge message.
 *
 *  Revision 1.2  2004/03/19 12:40:09  jdt
 *  Merge from PLGN_JDT_bz199b.
 *  Refactored log in pages to use xsp and xsl style sheets.  
 *  Added pages for requesting a login, and requesting
 *  a password reminder.
 *
 */
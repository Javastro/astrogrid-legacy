/* $Id: EmailMessengerFactory.java,v 1.2 2004/03/19 12:40:09 jdt Exp $
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
    public EmailMessenger getEmailMessenger(final String recipients) {
            return new EmailMessenger(session, recipients);
    }
    
    /** 
     * Quick test
     * 
     * @param args server, user, password, return address, destinationAddress
     * @throws MessengerException but hopefully not
     */
    public static void main(final String[] args) throws MessengerException {
        if (args.length!=5) {
            System.out.println("Usage: server user password returnAddress destination");
            return;
        }
        final EmailMessengerFactory factory = new EmailMessengerFactory(args[0],args[1],args[2],args[3]);
        final EmailMessenger messenger = factory.getEmailMessenger(args[4]);
        messenger.sendMessage("Test message 1", "Hello!");
        messenger.sendMessage("Test message 2", "Hello again!");
        final EmailMessenger messenger2 = factory.getEmailMessenger(args[4]);
        messenger2.sendMessage("Test message 3", "Goodbye!");
    }
    
}




/*
 *  $Log: EmailMessengerFactory.java,v $
 *  Revision 1.2  2004/03/19 12:40:09  jdt
 *  Merge from PLGN_JDT_bz199b.
 *  Refactored log in pages to use xsp and xsl style sheets.  
 *  Added pages for requesting a login, and requesting
 *  a password reminder.
 *
 *  Revision 1.1.2.1  2004/03/16 10:50:33  jdt
 *  Added email messenging classes
 *
 */
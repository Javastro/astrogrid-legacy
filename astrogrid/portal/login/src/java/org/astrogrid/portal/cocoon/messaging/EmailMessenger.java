/* $Id: EmailMessenger.java,v 1.2 2004/03/19 12:40:09 jdt Exp $
 * Created on Mar 14, 2004 by jdt
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.portal.cocoon.messaging;
import java.util.Date;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
/**
 * Messenger that dispatches emails
 * 
 * @author jdt
 */
public final class EmailMessenger implements Messenger {
    /**
     * List of recipients, comma separated
     */
    private String recipients;
    /**
     * Mail session
     */
    private Session  session;
    /**
     * Constructor - used by EmailMessengerFactory
     * @param session an email session
     * @param recipients comma separated list of recipients
     * 
     */
     EmailMessenger(final Session session, final String recipients) {
        this.recipients=recipients;
        this.session=session;
    }
    /**
     * Does the donkey work of sending an email 
     *  
     * @param subject subject line
     * @param message what you want to say
     * @throws MessengerException if the email fails
     */
    public void sendMessage(final String subject, final String message)
        throws MessengerException {
        try {
            final Message mess = new MimeMessage(session);
            mess.setFrom(InternetAddress.getLocalAddress(session));
            mess.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipients));
            mess.setSubject(subject);
            mess.setSentDate(new Date());
            mess.setHeader("X-Mailer", "Maven Auto Build");
            mess.setContent(message, "text/plain");
            Transport.send(mess);
        } catch (MessagingException me){
            throw new MessengerException("Problem sending message", me);
        }
    }
    
}


/*
 *  $Log: EmailMessenger.java,v $
 *  Revision 1.2  2004/03/19 12:40:09  jdt
 *  Merge from PLGN_JDT_bz199b.
 *  Refactored log in pages to use xsp and xsl style sheets.  
 *  Added pages for requesting a login, and requesting
 *  a password reminder.
 *
 *  Revision 1.1.2.1  2004/03/16 10:50:33  jdt
 *  Added email messenging classes
 *
 *
 */
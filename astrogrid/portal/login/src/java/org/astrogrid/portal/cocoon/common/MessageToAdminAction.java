/*
 * $Id: MessageToAdminAction.java,v 1.4 2004/03/24 18:31:33 jdt Exp $
 * Created on Mar 16, 2004 by jdt Copyright (C) AstroGrid. All rights reserved.
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.astrogrid.portal.cocoon.common;
import java.util.HashMap;
import java.util.Map;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.AbstractAction;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.astrogrid.config.Config;
import org.astrogrid.config.PropertyNotFoundException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.portal.cocoon.messaging.EmailMessengerFactory;
import org.astrogrid.portal.cocoon.messaging.Messenger;
import org.astrogrid.portal.cocoon.messaging.MessengerException;
/**
 * Currently sends an email - could do this with one of Cocoon's built in
 * actions instead, but thought I'd do it this way so that we could change to
 * send the message to a log, or to jabber or whatever. Send a message to the
 * administrator
 * 
 * @author jdt
 */
public final class MessageToAdminAction extends AbstractAction {
    /**
     * What to look for in the request header This could be used in the xsp
     * pages, but it makes the code so disgusting it's clearer just to hardwire
     * it.
     */
    public static final String NAME = "name";
    /**
     * What to look for in the request header
     */
    public static final String EMAIL = "email";
    /**
     * What to look for in the request header
     */
    public static final String MESSAGE = "message";
    /**
     * What to look for in the request header
     */
    public static final String SUBJECT = "subject";
    /**
     * JNDI property keys for email - see DataCenter QuerierPlugin
     */
    public static final String EMAIL_SERVER = "emailserver.address";
    /**
     * JNDI property keys for email
     */
    public static final String EMAIL_USER = "emailserver.user";
    /**
     * JNDI property keys for email
     */
    public static final String EMAIL_PWD = "emailserver.password";
    /**
     * JNDI property keys for email
     */
    public static final String EMAIL_FROM = "emailserver.from";
    /**
     * JNDI property keys for email
     */
    public static final String EMAIL_TO = "astrogrid.portal.admin.email";
    /**
     * Emailer
     */
    private  Messenger cachedMessenger;
    /**
     * Determines whether or not to use a mock emailer
     * or a real one.
     */
    private boolean useMockEmailer;
    /**
     * Set up mailer Constructor
     * 
     * @param useMockEmailer true if you want to use a dummy emailer for
     *            testing
     */
    public MessageToAdminAction(final boolean useMockEmailer) {
        super();
        this.useMockEmailer=useMockEmailer;

    }
    /**
     * Set up mailer Constructor
     */
    public MessageToAdminAction() {
        this(false);
    }   
    /**
     * Lazily return the Messenger. 
     * Has default access since used by unit tests
     * 
     * @return the cachedMessenger
     */
     Messenger getMessenger() {
         if (cachedMessenger!=null) {
             return cachedMessenger;
         }
        Config config = SimpleConfig.getSingleton();
        //Dummy values in case of no config
        String smtpServer = "127.0.0.1";
        String smtpUser = "jdt";
        String smtpPass = "";
        String returnAddress = "jdt@roe.ac.uk";
        String recipient = "jdt@roe.ac.uk";
        try {
            config = SimpleConfig.getSingleton();
            smtpServer = (String) config.getProperty(EMAIL_SERVER);
            smtpUser = (String) config.getProperty(EMAIL_USER);
            smtpPass = (String) config.getProperty(EMAIL_PWD);
            returnAddress = (String) config.getProperty(EMAIL_FROM);
            //who should be notified of these messages?
            recipient = (String) config.getProperty(EMAIL_TO);
        } catch (PropertyNotFoundException pe) {
            getLogger().error("Problem getting portal email configuration",pe);
            //TODO leave defaults in for now but need to decide what to do
            //do we need to catch this even?
        }
        final EmailMessengerFactory factory =
            new EmailMessengerFactory(
                smtpServer,
                smtpUser,
                smtpPass,
                returnAddress);
        cachedMessenger = factory.getEmailMessenger(recipient, useMockEmailer);
        assert cachedMessenger != null;
        return cachedMessenger;
    }



    /**
     * The Action's main method. Sends an email to the administrator containing
     * the "name", "email", stored in the request and "subject" and "message" stored in the params
     * object.
     * 
     * @param redirector see org.apache.cocoon.acting.Action#act
     * @param resolver see org.apache.cocoon.acting.Action#act
     * @param objectModel see org.apache.cocoon.acting.Action#act
     * @param source see org.apache.cocoon.acting.Action#act
     * @param params see org.apache.cocoon.acting.Action#act
     * @return null on failure, an empty HashMap on success
     * @see org.apache.cocoon.acting.Action#act(org.apache.cocoon.environment.Redirector,
     *      org.apache.cocoon.environment.SourceResolver, java.util.Map,
     *      java.lang.String,
     *      org.apache.avalon.framework.parameters.Parameters)
     */
    public Map act(
        final Redirector redirector,
        final SourceResolver resolver,
        final Map objectModel,
        final String source,
        final Parameters params) {
        
        checkLogger();
        final Logger log = getLogger();
        final Request request = ObjectModelHelper.getRequest(objectModel);
        assert log != null;
        assert request != null;
        final String username = request.getParameter(NAME);
        final String userEmail = request.getParameter(EMAIL);
        String subject = null;
        String message = null;
        try {
            subject = params.getParameter(SUBJECT);
            message = params.getParameter(MESSAGE);
        } catch (ParameterException pe) {
            log.error(
                "Couldn't get subject and message from params...",
                pe);
       }
         
        log.debug(
                "act(): username="
                + username
                + ", userEmail="
                + userEmail
                + ", subject="
                + subject
                + ",message="
                + message);
        
        if (username == null) {
            log.error("User name was null");
            return null;
        }
        if (userEmail == null) {
            log.error("User email was null");
            return null;
        }
        if (subject == null) {
            log.error("subject was null");
            return null;
        }
        if (message == null) {
            log.error("message was null");
            return null;
        }

        final Messenger messenger = getMessenger();
        
        final String fullMessage =
            message + "\n" + username + "\n" + userEmail + "\n";
        try {
            messenger.sendMessage(subject, fullMessage);
        } catch (MessengerException me) {
            log.error("Unable to send message ", me);
            return null; //indicates failure
        }
        return new HashMap(); //indicates success
    }
    /**
     * During unit tests the logger isn't setup properly, hence this method to
     * use a console logger instead.
     *  
     */
    private void checkLogger() {
        final Logger log = super.getLogger();
        if (log == null) {
            enableLogging(new ConsoleLogger());
        }
    }
}
/*
 * $Log: MessageToAdminAction.java,v $
 * Revision 1.4  2004/03/24 18:31:33  jdt
 * Merge from PLGN_JDT_bz#201
 *
 * Revision 1.3.2.5  2004/03/24 18:13:40  jdt
 * Refactored, moved potentially failing stuff out of constructor
 *
 * Revision 1.3.2.4  2004/03/23 02:03:49  jdt
 * Changed to get subject and message from parameters
 * rather than request object
 * Revision 1.3.2.3 2004/03/23 01:42:00 jdt
 * rejigged sitemap for register and reminder pages
 * 
 * Revision 1.3.2.2 2004/03/23 00:49:17 jdt Lots of refactoring
 * 
 * Revision 1.3.2.1 2004/03/19 17:46:41 jdt Added most of the meat to
 * MessageToAdminAction. Refactored LoginAction to agree with community Itn05
 * refactoring.
 * 
 * Revision 1.3 2004/03/19 13:02:25 jdt Pruned the log messages - they cause
 * conflicts on merge, best just to reduce them to the merge message.
 * 
 * Revision 1.2 2004/03/19 12:40:09 jdt Merge from PLGN_JDT_bz199b. Refactored
 * log in pages to use xsp and xsl style sheets. Added pages for requesting a
 * login, and requesting a password reminder.
 *  
 */
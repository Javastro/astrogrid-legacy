/* <cvs:id> 
 * $Id: MessageToAdminAction.java,v 1.5 2004/03/25 15:18:13 jdt Exp $
 * </cvs:id>
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
     * @TODO what to do about defaults?  If we remove the defaults then this
     * method will fail with a PropertyNotFoundException if the props aren't set
     * up.  THis is probably what we want, but leave defaults in for now for testing.
     * @return the cachedMessenger
     */
     Messenger getMessenger() {
         if (cachedMessenger!=null) {
             return cachedMessenger;
         }
        Config config = SimpleConfig.getSingleton();

        
        final String smtpServer =  config.getString(EMAIL_SERVER,"127.0.0.1");
        final String smtpUser =  config.getString(EMAIL_USER,"jdt");
        final String smtpPass =  config.getString(EMAIL_PWD,"");
        final String returnAddress =  config.getString(EMAIL_FROM,"admin@astrogrid.org");
        //who should be notified of these messages?
        final String recipient =  config.getString(EMAIL_TO, "jdt@roe.ac.uk");

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
/* <cvs:log>
 * $Log: MessageToAdminAction.java,v $
 * Revision 1.5  2004/03/25 15:18:13  jdt
 * Some refactoring of the debugging and added unit tests.
 *
 * Revision 1.4  2004/03/24 18:31:33  jdt
 * Merge from PLGN_JDT_bz#201
 * </cvs:log>
 *  
 */
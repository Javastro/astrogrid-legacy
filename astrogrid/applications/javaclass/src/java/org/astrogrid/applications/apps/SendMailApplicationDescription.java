/*$Id: SendMailApplicationDescription.java,v 1.2 2007/02/19 16:20:23 gtr Exp $
 * Created on 11-Aug-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.apps;

import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.AbstractApplicationDescription;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseApplicationInterface;
import org.astrogrid.applications.description.base.BaseParameterDescription;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.community.User;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.workflow.beans.v1.Tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/** Implementation of a send-mail application
 * <P>
 * Requires a javax.mail.Session object to be placed in jndi under <tt>java:comp/env/mail/session</tt>
 * @see http://jakarta.apache.org/tomcat/tomcat-5.0-doc/jndi-resources-howto.html for details of how to configure mail session object for Tomcat
 * @todo add support ofr optional parameters, advanced emailing features - attachements, etc. maybe use jakarta-commons-email to make this easier.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Aug-2004
 *
 */
public class SendMailApplicationDescription extends AbstractApplicationDescription implements ComponentDescriptor {
    private static final String SIMPLE = "simple";
    private static final String ADVANCED = "advanced";
    private static final String ATTACHMENT = "attachment";
    private static final String CC = "cc";
    private static final String TO = "to";
    private static final String REPLYTO = "replyTo";
    private static final String SUBJECT = "subject";
    private static final String MESSAGE = "message";
    /** Construct a new SendMailApp
     * @param env
     */
    public SendMailApplicationDescription(ApplicationDescriptionEnvironment env ) {
        super(env);
        this.setMetaData();
    }
    
    /** set up metadata for this instance */
    private final void setMetaData() {
        StringBuffer thename = new StringBuffer(env.getAuthIDResolver().getAuthorityID());
        thename.append("/sendmail");
        setName(thename.toString());
        BaseParameterDescription to = new BaseParameterDescription();
        to.setName(TO);
        to.setDisplayName("To-Address");
        to.setDisplayDescription("Email address to send message to");
        to.setType(ParameterTypes.TEXT);
        this.addParameterDescription(to);
        /* later..
        BaseParameterDescription cc = new BaseParameterDescription();
        cc.setName(CC);
        cc.setDisplayName("CC-Address");
        cc.setDisplayDescription("Email address to CC message to");
        cc.setType(ParameterTypes.TEXT);
        this.addParameterDescription(cc);
        */
        /* later
        BaseParameterDescription replyTo = new BaseParameterDescription();
        replyTo.setName( REPLYTO );
        replyTo.setDisplayName("ReplyTo-Address");
        replyTo.setDisplayDescription("Email address to reply to");
        replyTo.setType(ParameterTypes.TEXT);
        this.addParameterDescription(replyTo);             
        */
        BaseParameterDescription message = new BaseParameterDescription();
        message.setName( MESSAGE);
        message.setDisplayName("Your Message");
        message.setDisplayDescription("The message to send");
        message.setType(ParameterTypes.TEXT);
        this.addParameterDescription(message);
        
        BaseParameterDescription subject = new BaseParameterDescription();
        subject.setName(SUBJECT);
        subject.setDisplayName("Subject of the email");
        subject.setDisplayDescription("text to use as the subject of this email");
        subject.setDefaultValue("Message from JES");
        subject.setType(ParameterTypes.TEXT);
        this.addParameterDescription(subject);
        /* later
        BaseParameterDescription attachment = new BaseParameterDescription();
        attachment.setName(ATTACHMENT);
        attachment.setDisplayName("Message Attachment");
        attachment.setDisplayDescription("Data to attach to message");
        attachment.setType(ParameterTypes.BINARY);
        this.addParameterDescription(attachment);
        */

        BaseApplicationInterface simple = new BaseApplicationInterface(SIMPLE,this);
        try {
            simple.addInputParameter(to.getName());
            simple.addInputParameter(subject.getName());
            simple.addInputParameter(message.getName());
        } catch (ParameterDescriptionNotFoundException e) {
            logger.fatal("Programming error",e);// really shouldn't happen.
            throw new RuntimeException("Programming error",e);
        }
        this.addInterface(simple);
        /* maybe add later - could do with having optioinal parameters
        BaseApplicationInterface full = new BaseApplicationInterface(ADVANCED,this);
        try {
        full.addInputParameter(to.getName());
        full.addInputParameter(cc.getName());
        full.addInputParameter(replyTo.getName());
        full.addInputParameter(subject.getName());
        full.addInputParameter(message.getName());
        full.addInputParameter(attachment.getName());        
        } catch (ParameterDescriptionNotFoundException e) {
            logger.fatal("Programming error",e);// really shouldn't happen.
            throw new RuntimeException("Programming error",e);
        }        
        this.addInterface(full);
        */
    }

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(SendMailApplicationDescription.class);



    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Email application\n" + this.toString();
    }

    /** installation test verifies mailer session object is present in jndi.
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {        
        return new TestSuite(InstallationTest.class);
    }
    
    public static class InstallationTest extends TestCase {
        public void testJNDI() throws Exception {
            assertNotNull(SendMailApplication.getSessionFromContext());
        }
    }

    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, org.astrogrid.community.User, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String callerAssignedID, User user, Tool tool) throws Exception {
        String newID = env.getIdGen().getNewID();
        final DefaultIDs ids = new DefaultIDs(callerAssignedID,newID,user);
        ApplicationInterface iface = this.getInterface(tool.getInterface());
        return new SendMailApplication(ids,tool,iface,env.getProtocolLib());
    }

    public static class SendMailApplication extends AbstractApplication {

        /** Construct a new SendMailApplication
         * @param ids
         * @param tool
         * @param applicationInterface
         * @param lib
         */
        public SendMailApplication(IDs ids, Tool tool, ApplicationInterface applicationInterface, ProtocolLibrary lib) {
            super(ids, tool, applicationInterface, lib);
        }

        public Runnable createExecutionTask() throws CeaException {

            createAdapters();
            setStatus(Status.INITIALIZED);
            return new Runnable() {
                    public void run() {
                        final Map args = new HashMap();
                        try {
                        for (Iterator i = inputParameterAdapters(); i.hasNext();) {
                            ParameterAdapter a = (ParameterAdapter)i.next();
                            args.put(a.getWrappedParameter().getName(),a.process());
                        }                        
                        setStatus(Status.RUNNING);
                        // send the message here.

                        Session session = getSessionFromContext();
                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress("sendmail@builtin.astrogrid.org"));
                        message.setSubject(args.get(SUBJECT).toString());
                        message.setContent(args.get(MESSAGE).toString(),"text/plain");
                        message.setRecipient(Message.RecipientType.TO,new InternetAddress(args.get(TO).toString()));
                        Transport.send(message);
                        setStatus(Status.COMPLETED);
                        reportMessage("message sent");
                        } catch (NamingException e) {
                            reportError("Could not find mail session in JNDI",e);
                        } catch (MessagingException e) {
                            reportError("Failed to construct email message",e);
                        } catch (Throwable t) {
                            reportError("Something went wrong",t);
                        }
                    }
            };

            
        }

        /** Get mail session from JNDI context.
         * expects to find it under 'java:comp/env/mail/session'
         * @return
         * @throws NamingException
         */
        static Session getSessionFromContext() throws NamingException {
            Context init = new InitialContext();
            Context env = (Context) init.lookup("java:comp/env");
            Session session = (Session)env.lookup("mail/session");
            return session;
        }
    }
    
}


/* 
$Log: SendMailApplicationDescription.java,v $
Revision 1.2  2007/02/19 16:20:23  gtr
Branch apps-gtr-1061 is merged.

Revision 1.1.2.1  2007/01/18 18:29:17  gtr
no message

Revision 1.6  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.5.16.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.5  2004/09/17 01:21:12  nw
altered to work with new threadpool

Revision 1.4.12.1  2004/09/14 13:46:04  nw
upgraded to new threading practice.

Revision 1.4  2004/09/03 13:19:14  nw
added some progress messages

Revision 1.3  2004/08/28 11:25:10  nw
tried to improve error trapping - seems to fail to complete at the moment.

Revision 1.2  2004/08/16 11:03:46  nw
first stab at a cat application

Revision 1.1  2004/08/11 17:40:49  nw
implemented send mail application
 
*/
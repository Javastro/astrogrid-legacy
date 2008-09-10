/*$Id: SendMailApplicationDescription.java,v 1.8 2008/09/10 23:27:16 pah Exp $
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.FutureTask;

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
import net.ivoa.resource.cea.CeaApplication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.AbstractApplication;
import org.astrogrid.applications.Application;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.DefaultIDs;
import org.astrogrid.applications.Status;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.contracts.CEAConfiguration;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.base.ApplicationDescriptionEnvironment;
import org.astrogrid.applications.description.base.BaseParameterDefinition;
import org.astrogrid.applications.description.base.InterfaceDefinition;
import org.astrogrid.applications.description.base.InternallyConfiguredApplicationDescription;
import org.astrogrid.applications.description.base.ParameterTypes;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.javainternal.JavaInternalApplication;
import org.springframework.stereotype.Service;

/** Implementation of a send-mail application
 * <P>
 * Requires a javax.mail.Session object to be placed in jndi under <tt>java:comp/env/mail/session</tt>
 * @see http://jakarta.apache.org/tomcat/tomcat-5.0-doc/jndi-resources-howto.html for details of how to configure mail session object for Tomcat
 * @todo add support ofr optional parameters, advanced emailing features - attachements, etc. maybe use jakarta-commons-email to make this easier.
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Aug-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 14 Mar 2008
 *
 */
@Service
public class SendMailApplicationDescription extends InternallyConfiguredApplicationDescription implements ComponentDescriptor {
    private static final String SIMPLE = "simple";
    private static final String ADVANCED = "advanced";
    private static final String ATTACHMENT = "attachment";
    private static final String CC = "cc";
    private static final String TO = "to";
    private static final String REPLYTO = "replyTo";
    private static final String SUBJECT = "subject";
    private static final String MESSAGE = "message";
    private static CeaApplication app = new CeaApplication();
    
    static {
	app.setIdentifier("org.astrogrid.util/sendmail");
	addParameter(app, TO, ParameterTypes.TEXT,"To-Address" , "Email address to send message to");
	addParameter(app, MESSAGE, ParameterTypes.TEXT, "Your Message", "The message to send");
	BaseParameterDefinition par = addParameter(app, SUBJECT, ParameterTypes.TEXT, "Subject of the email", "text to use as the subject of this email");
	par.setDefaultValue("Message from JES");
	InterfaceDefinition intf = addInterface(app, "default");
	intf.addInput(TO);
	intf.addInput(SUBJECT);
	intf.addInput(MESSAGE);
	  	
    }
    /** Construct a new SendMailApp
     * @param conf 
     */
    public SendMailApplicationDescription(CEAConfiguration conf ) {
        super(app , conf);
      
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
            assertNotNull(SendMailApplicationDescription.getSessionFromContext());
        }
    }

    /**
     * @see org.astrogrid.applications.description.ApplicationDescription#initializeApplication(java.lang.String, SecurityGuard, org.astrogrid.workflow.beans.v1.Tool)
     */
    public Application initializeApplication(String callerAssignedID, SecurityGuard secGuard, Tool tool) throws Exception {
        ApplicationInterface iface = this.getInterface(tool.getInterface());
        ApplicationEnvironment env = new ApplicationEnvironment(callerAssignedID, secGuard, getInternalComponentFactory().getIdGenerator(), conf);
	return new SendMailApplication(tool,iface, env , getInternalComponentFactory().getProtocolLibrary());
    }

    public  class SendMailApplication extends JavaInternalApplication {

        /** Construct a new SendMailApplication
         * @param ids
         * @param tool
         * @param applicationInterface
         * @param lib
         */
        public SendMailApplication( Tool tool, ApplicationInterface applicationInterface, ApplicationEnvironment env, ProtocolLibrary lib) {
            super( tool, applicationInterface, env, lib);
        }

                     public void run() {
                        final Map args = new HashMap();
                        try {
                        for (Iterator i = inputParameterAdapters(); i.hasNext();) {
                            ParameterAdapter a = (ParameterAdapter)i.next();
                            args.put(a.getWrappedParameter().getId(),a.process());
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
           

            
    

        /** Get mail session from JNDI context.
         * expects to find it under 'java:comp/env/mail/session'
         * @return
         * @throws NamingException
         */
    }
    static Session getSessionFromContext() throws NamingException {
        Context init = new InitialContext();
        Context env = (Context) init.lookup("java:comp/env");
        Session session = (Session)env.lookup("mail/session");
        return session;
    }
   
}


/* 
$Log: SendMailApplicationDescription.java,v $
Revision 1.8  2008/09/10 23:27:16  pah
moved all of http CEC and most of javaclass CEC code here into common library

Revision 1.3  2008/09/03 14:18:33  pah
result of merge of pah_cea_1611 branch

Revision 1.2.10.5  2008/09/03 12:01:56  pah
should perhaps be moved out of javaclass

Revision 1.2.10.4  2008/08/02 13:33:40  pah
safety checkin - on vacation

Revision 1.2.10.3  2008/05/13 15:14:07  pah
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708

Revision 1.2.10.2  2008/03/27 13:37:24  pah
now producing correct registry documents

Revision 1.2.10.1  2008/03/19 23:28:58  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

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
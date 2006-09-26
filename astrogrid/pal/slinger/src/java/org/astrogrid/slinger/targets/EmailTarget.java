/*
 * $Id: EmailTarget.java,v 1.2 2006/09/26 15:34:42 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.WriterStream;

/**
 * Used to indicate an email target.
 * This can be done using a URL 'mailto:' but there isn't much control over
 * from and subject etc and I don't know how the login works....
 *
 */

public class EmailTarget implements TargetIdentifier  {
   
   public final static String EMAIL_SERVER = "mail.host";
   public final static String EMAIL_USER   = "mail.user";
   public final static String EMAIL_PWD    = "mail.password";
   public final static String EMAIL_FROM   = "mail.from";
 
   String mimeType = null;
   
   // Get email server from configuration file
   String emailServer = null;
   String emailUser = null;
   String emailPassword = null;
   String emailFrom = null;
   
   String emailAddress = null;
   
   /** Email constructor - see also makeIndicator */
   public EmailTarget(String mailto) throws URISyntaxException {
      assert mailto.startsWith("mailto") : "email target indicator should start with 'mailto'";
    
      this.emailAddress = mailto.substring(7); //chop off email to
     
      // Get email server from configuration file
      emailServer = ConfigFactory.getCommonConfig().getString(EMAIL_SERVER);
      emailUser = ConfigFactory.getCommonConfig().getString(EMAIL_USER, null);
      emailPassword = ConfigFactory.getCommonConfig().getString(EMAIL_PWD, null);
      emailFrom = ConfigFactory.getCommonConfig().getString(EMAIL_FROM);
   }
   
   public String getEmailAddress() {
      return emailAddress;
   }
   
   public String toURI() {
      return "mailto:"+emailAddress;
   }
   
   
   public Writer openWriter() throws IOException {

      String targetAddress = getEmailAddress();
      
      try {
         // create properties required by Session constructor, and get the default Session
         Properties props = new Properties();
         props.put("mail.smtp.host", emailServer);
         Session session = Session.getDefaultInstance(props, null);
         
         //create message
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(emailFrom));
         message.setRecipient(Message.RecipientType.TO, new InternetAddress(targetAddress));
         message.setSubject("(Slinger auto-email)"); //would be nice to have more useful subject
         //message.setSentDate(new Date());
         
         return new MessageWriter(message, session);
      }
      catch (MessagingException e) {
         throw new IOException(e+", mailing to "+emailServer);
      }
   }
   
   
   public void testServer() throws IOException {
      
      //check email server is available
      String server = ConfigFactory.getCommonConfig().getString(EmailTarget.EMAIL_SERVER);
      String emailUser = ConfigFactory.getCommonConfig().getString(EmailTarget.EMAIL_USER, null);
      String password = ConfigFactory.getCommonConfig().getString(EmailTarget.EMAIL_PWD, null);
      
      try {
         Properties props = new Properties();
         props.put("mail.smtp.host", server);
         Session session = Session.getDefaultInstance(props, null);
         
         //            Provider[] p = session.getProviders();
         Transport transport = session.getTransport(session.getProvider("smtp"));
         transport.connect(server, emailUser, password);
      }
      catch (MessagingException e) {
         throw new IOException("Cannot connect to server "+server+": "+e);
      }
      
   }
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream openOutputStream() throws IOException {
      return new WriterStream(openWriter());
   }
   
   public String toString() {
      return toURI();
   }
   
   
   /** Used to set the mime type of the data about to be sent to the target.  */
   public void setMimeType(String aMimeType) {
      this.mimeType = aMimeType;
   }
   
   /** OutputStream to write to email.  On close, sends email */
   private class MessageWriter extends StringWriter {

      Message message = null;
      Session session = null;
      
      public MessageWriter(Message emailMessage, Session emailSession) {
         this.message = emailMessage;
         this.session = emailSession;
      }
      
      public void close() throws IOException {
         
         try {
            message.setText(toString());
            // Send
            Transport transport = session.getTransport(session.getProvider("smtp"));
            transport.connect(emailServer, emailUser, emailPassword);
            transport.sendMessage(message, new Address[] { new InternetAddress(getEmailAddress()) });
         }
         catch (MessagingException e) {
            throw new IOException(e+", sending message to "+emailServer);
         }
      }
   }
   
}





/*
 * $Id: EmailTarget.java,v 1.1 2005/02/14 20:47:38 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URISyntaxException;
import java.security.Principal;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.slinger.SRI;

/**
 * Used to indicate the target where the results are to be sent.  May be an AGSL, or an email address, or
 * some IVO based thingamy that is still to be resolved
 *
 */

public class EmailTarget implements SRI, TargetIdentifier  {
   
   public final static String EMAIL_SERVER = "emailserver.address";
   public final static String EMAIL_USER   = "emailserver.user";
   public final static String EMAIL_PWD    = "emailserver.password";
   public final static String EMAIL_FROM   = "emailserver.from";
   
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
      emailServer = SimpleConfig.getSingleton().getString(EMAIL_SERVER);
      emailUser = SimpleConfig.getSingleton().getString(EMAIL_USER, null);
      emailPassword = SimpleConfig.getSingleton().getString(EMAIL_PWD, null);
      emailFrom = SimpleConfig.getSingleton().getString(EMAIL_FROM);

   }

   public String getEmailAddress() {
      return emailAddress;
   }

   public String toURI() {
      return "mailto:"+emailAddress;
   }
   
   public Writer resolveWriter(Principal user) throws IOException {
         
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
         
      } catch (MessagingException e)
      {
         throw new IOException(e+", mailing to "+emailServer);
      }
   }

   public void testServer() throws IOException {
         //check email server is available
         String server = SimpleConfig.getSingleton().getString(EmailTarget.EMAIL_SERVER);
         String emailUser = SimpleConfig.getSingleton().getString(EmailTarget.EMAIL_USER, null);
         String password = SimpleConfig.getSingleton().getString(EmailTarget.EMAIL_PWD, null);

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
   public OutputStream resolveOutputStream(Principal user) throws IOException {
      throw new UnsupportedOperationException("Todo");
   }
   


   public String toString() {
      return "[TargetId "+toURI()+"]";
   }
   
   /** Can be forwarded to remote services */
   public boolean isForwardable() { return true; }
   
   /** Used to set the mime type of the data about to be sent to the target.  */
   public void setMimeType(String aMimeType, Principal user) {
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
         catch (MessagingException e)
         {
            throw new IOException(e+", sending message to "+emailServer);
         }
         
      }
   }
   
}
/*
 $Log: EmailTarget.java,v $
 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.3  2005/01/26 14:35:26  mch
 Separating slinger and scapi

 Revision 1.1.2.2  2004/12/08 18:37:11  mch
 Introduced SPI and SPL

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package


 */




/*
 * $Id: EmailTarget.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
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

/**
 * Used to indicate the target where the results are to be sent.  May be an AGSL, or an email address, or
 * some IVO based thingamy that is still to be resolved
 *
 */

public class EmailTarget extends UriTarget implements TargetIdentifier  {
   
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

   /** Email constructor - see also makeIndicator */
   public EmailTarget(String mailto) throws URISyntaxException {
      super(new URI(mailto));
      assert mailto.startsWith("mailto") : "email target indicator should start with 'mailto'";
      // Get email server from configuration file
      emailServer = SimpleConfig.getSingleton().getString(EMAIL_SERVER);
      emailUser = SimpleConfig.getSingleton().getString(EMAIL_USER, null);
      emailPassword = SimpleConfig.getSingleton().getString(EMAIL_PWD, null);
      emailFrom = SimpleConfig.getSingleton().getString(EMAIL_FROM);

   }

   public String getEmailAddress() {
      return uri.toString().substring(7);
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
      return "Email TargetIndicator "+uri;
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
 Revision 1.2  2004/12/07 01:33:36  jdt
 Merge from PAL_Itn07

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 Revision 1.1.6.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.1  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.2  2004/10/12 17:41:41  mch
 added isForwardable

 Revision 1.1  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.5  2004/10/05 14:55:00  mch
 Added factory methods

 Revision 1.4  2004/09/07 01:39:27  mch
 Moved email keys from TargetIndicator to Slinger

 Revision 1.3  2004/09/07 01:01:29  mch
 Moved testConnection to server slinger

 Revision 1.2  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.1  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.3  2004/08/19 08:35:54  mch
 Fix to email constructor

 Revision 1.2  2004/08/18 22:27:57  mch
 Better error checking

 Revision 1.1  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.5  2004/07/15 17:07:23  mch
 Added factory method to make from a string

 Revision 1.4  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.3  2004/03/15 17:08:11  mch
 Added compression adn format placeholders

 Revision 1.2  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.1  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 */




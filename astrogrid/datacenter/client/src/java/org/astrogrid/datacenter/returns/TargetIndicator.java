/*
 * $Id: TargetIndicator.java,v 1.2 2004/09/07 00:54:20 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.returns;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Transport;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.Ivorn;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreException;
import org.astrogrid.store.delegate.VoSpaceResolver;

/**
 * Used to indicate the target where the results are to be sent.  May be an AGSL, or an email address, or
 * some IVO based thingamy that is still to be resolved
 *
 */

public class TargetIndicator  {

   protected String email = null;
   protected Agsl agsl = null;
   protected Ivorn ivorn = null;
   protected Writer out = null;
   
   public final static String EMAIL_SERVER = "emailserver.address";
   public final static String EMAIL_USER   = "emailserver.user";
   public final static String EMAIL_PWD    = "emailserver.password";
   public final static String EMAIL_FROM   = "emailserver.from";
   
   
   
   /** Email constructor - see also makeIndicator */
   public TargetIndicator(String mailto) {
      assert mailto.startsWith("mailto") : "email target indicator should start with 'mailto'";
      this.email = mailto.substring(7);  //chop off mailto:
   }

   public TargetIndicator(Agsl targetAgsl) {
      this.agsl = targetAgsl;
   }

   public TargetIndicator(Ivorn targetIvorn) {
      this.ivorn = targetIvorn;
   }
   
   public TargetIndicator(Writer targetOut) {
      this.out = targetOut;
   }

   /**
    * Tests the string & creates the right kind of TargetIndicator
    */
   public static TargetIndicator makeIndicator(String id) throws MalformedURLException, URISyntaxException {
      if ((id == null) || (id.length() == 0)) {
         return null;
      }
      else if (id.startsWith("mailto:")) {
         return new TargetIndicator(id);
      }
      else if (Agsl.isAgsl(id)) {
         return new TargetIndicator(new Agsl(id));
      }
      else if (Ivorn.isIvorn(id)) {
         return new TargetIndicator(new Ivorn(id));
      }
      else {
         throw new IllegalArgumentException("'"+id+" should start 'mailto:' or '"+Ivorn.SCHEME+"' or '"+Agsl.SCHEME+"'");
      }
   }
   /**/
      
   public String getEmail() {
      return email;
   }
   
   public Writer getWriter() {
      return out;
   }

   public boolean isIvorn() {
      return (ivorn != null);
   }
   
   public Agsl resolveAgsl() throws IOException {
      if (agsl != null) {
         return agsl;
      }
      else if (ivorn != null) {
         return new VoSpaceResolver().resolveAgsl(ivorn);
      }
      else {
         return null;
      }
   }

   public Writer resolveWriter(Account user) throws IOException {
      if (out != null) {
         return out;
      }
      
      Agsl target = resolveAgsl();
      if (target != null) {
         return new OutputStreamWriter(target.openOutputStream(user.toUser()));
      }
      else {
         return null;
      }
   }

   
   public String toString() {
      if (email != null) {
         return "Email TargetIndicator "+email;
      }
      else if (agsl != null) {
         return "Agsl TargetIndicator "+agsl;
      }
      else if (ivorn != null) {
         return "Ivorn TargetIndicator "+ivorn;
      }
      else if (out != null) {
         return "Writer TargetIndicator ";
      }
      else {
         return super.toString();
      }
   }
   
   /**
    * Tests the destination exists and a file can be created on it.  This ensures
    * that the given server url is correct, that the server is running and that
    * the user has the right permissions on that particular one.
    * We do this
    * before, eg, running the query to try and ensure the query is not wasted.
    *
    * @throws IOException if the operation fails for any reason
    */
   public void testConnection(Account user) throws IOException {
      
      if (getEmail() != null) {
         //check email server is available
         String server = SimpleConfig.getSingleton().getString(EMAIL_SERVER);
         String emailUser = SimpleConfig.getSingleton().getString(EMAIL_USER, null);
         String password = SimpleConfig.getSingleton().getString(EMAIL_PWD, null);

         try {
            Properties props = new Properties();
            props.put("mail.smtp.host", server);
            Session session = Session.getDefaultInstance(props, null);
   
            Provider[] p = session.getProviders();
            Transport transport = session.getTransport(session.getProvider("smtp"));
            transport.connect(server, emailUser, password);
         }
         catch (MessagingException e) {
            throw new IOException("Cannot connect to server "+server+": "+e);
         }
            
      }

      // test to see that the agsl for the results is valid
      if (resolveAgsl() != null) {
         
         StoreClient store = StoreDelegateFactory.createDelegate(user.toUser(), resolveAgsl());

         try {
            store.putString("This is a test file to make sure we can create a file at the target before we start, so our query results are not lost",
                              resolveAgsl().getPath(), false);
         }
         catch (StoreException se) {
            //rethrow with more info
            throw new StoreException("Test to create '"+resolveAgsl().getPath()+"' on target store failed "+se.getMessage(), se.getCause());
         }
         
         try {
            store.delete(resolveAgsl().getPath());
         }
         catch (StoreException se) {
            //log it but don't fail
            LogFactory.getLog(TargetIndicator.class).error("Could not delete test file",se);
         }
            
      }
   }
   
}
/*
 $Log: TargetIndicator.java,v $
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




/*
 * $Id: Slinger.java,v 1.3 2004/10/08 15:16:45 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.slinger;

import java.io.IOException;
import java.io.Writer;
import java.util.Properties;
import javax.mail.MessagingException;
import javax.mail.Provider;
import javax.mail.Session;
import javax.mail.Transport;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.slinger.AgslTarget;
import org.astrogrid.slinger.EmailTarget;
import org.astrogrid.slinger.IvornTarget;
import org.astrogrid.slinger.TargetIndicator;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;
import org.astrogrid.store.delegate.StoreException;

/**
 * Something to do with sending things. Not really properly thought out yet
 *
 */

public class Slinger  {

   public final static String EMAIL_SERVER = "emailserver.address";
   public final static String EMAIL_USER   = "emailserver.user";
   public final static String EMAIL_PWD    = "emailserver.password";
   public final static String EMAIL_FROM   = "emailserver.from";
   
   /**
    * Tests the destination exists and a file can be created on it.  This ensures
    * that the given server url is correct, that the server is running and that
    * the user has the right permissions on that particular one.
    * We do this
    * before, eg, running the query to try and ensure the query is not wasted.
    *
    * @throws IOException if the operation fails for any reason
    */
   public static void testConnection(TargetIndicator target, Account user) throws IOException {
      
      if (target instanceof EmailTarget) {
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
      if ((target instanceof AgslTarget) || (target instanceof IvornTarget)) {

         Writer out = target.resolveWriter(user);

         try {
            out.write("This is a test file to make sure we can create a file at the target before we start, so our query results are not lost");
         }
         catch (IOException se) {
            //rethrow with more info
            throw new IOException("Test to write to '"+target+"' failed: "+se.getMessage());
         }
         //erm
         /*
         try {
            store.delete(target.resolveAgsl().getPath());
         }
         catch (StoreException se) {
            //log it but don't fail
            LogFactory.getLog(Slinger.class).error("Could not delete test file",se);
         }
          */
      }
   }
   
}
/*
 $Log: Slinger.java,v $
 Revision 1.3  2004/10/08 15:16:45  mch
 Removed unnecessary imports

 Revision 1.2  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.1  2004/09/07 01:39:27  mch
 Moved email keys from TargetIndicator to Slinger

 
 */




/*
 * $Id: QuerierPlugin.java,v 1.7 2004/03/15 19:16:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import javax.mail.*;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;

/**
 * Querier Plugins are used to carry out all the database specific backend
 * tasks.  They are given a parent at construction time; this is the Querier
 * class that provides all the information a young plugin needs, such as the
 * Query itself, a way of updating status, etc
 *
 */

public abstract class QuerierPlugin  {

   protected final Querier querier;

   /** Marker used to indicate an abort has been requested */
   protected boolean aborted = false;
   
   protected static final Log log = LogFactory.getLog(QuerierPlugin.class);

   public final static String EMAIL_SERVER = "datacenter.emailserver.address";
   public final static String EMAIL_USER   = "datacenter.emailserver.user";
   public final static String EMAIL_PWD    = "datacenter.emailserver.password";
   public final static String EMAIL_FROM   = "datacenter.emailserver.from";
   
   /** All Plugins implementations will have to have the same constructor as this */
   public QuerierPlugin(Querier givenParent) {
      this.querier = givenParent;
   }

   /** Subclasses override this method to carry out the query.
     * Used by both synchronous (blocking) and asynchronous (threaded) querying
     * through processQuery. Should run the query and send the results although
     * the parent has methods to help with this.  The plugin should have everyting
     * tidied up and discarded as nec before returning - there is no close() method
     */
   public abstract void askQuery() throws IOException;
   
   /** Abort - if this is called, try and top the query and tidy up.  This
    * default implementation sets the 'aborted' flag for the askQuery to
    * check for */
   public void abort() {
      aborted = true;
   }
   
   /** This is a helper method for subclasses; it is meant to be called
    * from the askQuery method.  It transforms the results and sends them
    * as required, updating the querier status appropriately.
    */
   protected void processResults(QueryResults results) throws IOException {
      
      assert (results != null) : "Plugin has given null results to set";

      QuerierProcessingResults resultsStatus = new QuerierProcessingResults(querier);
      querier.setStatus(resultsStatus);

      TargetIndicator target = querier.getResultsTarget();

      if (target.getEmail() != null) {

         log.info(querier+", emailing results to "+target.getEmail());
         resultsStatus.setNote("emailing results to "+target.getEmail());
         
         emailResults(results, target.getEmail(), resultsStatus);
      }
      else {

         resultsStatus.setNote("Sending results to "+target);

         Writer writer = target.resolveWriter(querier.getUser());
      
         if (writer == null) {
            throw new IOException("Could not resolve writer from "+target);
         }

         results.write(writer, resultsStatus, querier.getRequestedFormat());
         writer.close();
      }

      String s = "Results sent to "+target;
      if (target.isIvorn()) s = s + " => "+target.resolveAgsl();
      resultsStatus.addDetail(s);
      resultsStatus.setNote("");
        
      log.info(querier+" results sent");
   }

   /**
    * Experimental emailler
    */
   protected void emailResults(QueryResults results, String targetAddress, QuerierProcessingResults resultsStatus) throws IOException {

      // Get email server from configuration file
      String emailServer = SimpleConfig.getSingleton().getString(EMAIL_SERVER);
      String emailUser = SimpleConfig.getSingleton().getString(EMAIL_USER, null);
      String emailPassword = SimpleConfig.getSingleton().getString(EMAIL_PWD, null);
      String emailFrom = SimpleConfig.getSingleton().getString(EMAIL_FROM);
         
      try {
         // create properties required by Session constructor, and get the default Session
         Properties props = new Properties();
         props.put("mail.smtp.host", emailServer);
         Session session = Session.getDefaultInstance(props, null);
   
         //create message
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress(emailFrom));
         message.setRecipient(Message.RecipientType.TO, new InternetAddress(targetAddress));
         message.setSubject("Results of Query "+querier.getId());
         //message.setSentDate(new Date());
      
         // Set message contents - should really do this as a ZIP filed attachment. Later...
         StringWriter sw = new StringWriter();
         results.write(sw, resultsStatus, querier.getRequestedFormat());
         message.setText(sw.toString());

         // Send
         Transport transport = session.getTransport(session.getProvider("smtp"));
         transport.connect(emailServer, emailUser, emailPassword);
         transport.sendMessage(message, new Address[] { new InternetAddress(targetAddress) });
         
         
      } catch (MessagingException e)
      {
         log.error(e);
         throw new IOException(e+", mailing to "+emailServer);
      }
   }

   
}
/*
 $Log: QuerierPlugin.java,v $
 Revision 1.7  2004/03/15 19:16:12  mch
 Lots of fixes to status updates

 Revision 1.6  2004/03/15 17:13:21  mch
 changed to configurable from address

 Revision 1.5  2004/03/15 11:25:35  mch
 Fixes to emailer and JSP targetting

 Revision 1.4  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.3  2004/03/14 04:13:04  mch
 Wrapped output target in TargetIndicator

 Revision 1.2  2004/03/14 02:17:07  mch
 Added CVS format and emailer

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 */




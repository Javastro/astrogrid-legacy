/*
 * $Id: QuerierPlugin.java,v 1.2 2004/03/14 02:17:07 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import javax.mail.*;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreClient;
import org.astrogrid.store.delegate.StoreDelegateFactory;

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

      Agsl targetAgsl = querier.getResultsTargetAgsl();
      
      if (targetAgsl != null) {
         
         log.info(querier+", sending results to "+targetAgsl);
      
         if (targetAgsl.getEndpoint().toLowerCase().startsWith("mailto")) {
            
            emailResults(results, targetAgsl.getEndpoint().substring(Agsl.SCHEME.length()+1), resultsStatus);
         }
         else {
            StoreClient store = StoreDelegateFactory.createDelegate(Account.ANONYMOUS.toUser(), targetAgsl);

            OutputStream out = store.putStream(targetAgsl.getPath());
            
            results.write(new OutputStreamWriter(out), resultsStatus, querier.getRequestedFormat());
            out.close();
         }
         
         log.info(querier+" results sent");

         //add information to status
         //resultsLoc = myspace.getUrl("/"+user.getAstrogridId()+"/"+myspaceFilename).toString();
      }
      
      if (querier.getResultsTargetStream() != null) {

         log.info(querier+", streaming results");
         
         results.toVotable(querier.getResultsTargetStream(), resultsStatus);
         
         log.info(querier+" results sent");
      }
      
   }

   /**
    * Experimental emailler
    */
   protected void emailResults(QueryResults results, String targetAddress, QuerierProcessingResults resultsStatus) throws IOException {

      // Get email server from configuration file
      String emailServer = SimpleConfig.getSingleton().getString("datacenter.emailserver.address");
      String emailUser = SimpleConfig.getSingleton().getString("datacenter.emailserver.user");
      String emailPassword = SimpleConfig.getSingleton().getString("datacenter.emailserver.password");
         
      try {
         // create some properties and get the default Session
         Properties props = new Properties();
         props.put("mail.smtp.host", emailServer);
         Session session = Session.getDefaultInstance(props, null);
   
         //create message
         Message message = new MimeMessage(session);
         message.setFrom(new InternetAddress("Datacenter"));
         message.setRecipient(Message.RecipientType.TO, new InternetAddress(targetAddress));
         message.setSubject("Results of Query "+querier.getId());
         //message.setSentDate(new Date());
      
         // Set message contents - should really do this as a ZIP filed attachment. Later...
         StringWriter sw = new StringWriter();
         results.write(sw, resultsStatus, querier.getRequestedFormat());
         message.setText(sw.toString());

         // Send
         Transport transport = session.getTransport(emailServer);
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
 Revision 1.2  2004/03/14 02:17:07  mch
 Added CVS format and emailer

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 */




/*
 * $Id: QueryResults.java,v 1.4 2004/10/08 09:42:58 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.datacenter.slinger.Slinger;
import org.astrogrid.slinger.EmailTarget;
import org.astrogrid.slinger.IvornTarget;

/** A container interface that holds the results of a query until needed.
 * <p>
 *   Basically we
 * don't know what format the raw results will be in (eg, they may be SqlResults for
 * an JDBC connection, but something else altogether for other catalogue formats)
 * so this is a 'container' to hold those results until it needs to be
 * translated. It would be fully
 * implemented by the same package that implements the DatabaseQuerier and
 * QueryTranslater.
 *
 * @author M Hill
 */

public abstract class QueryResults
{

   Log log = LogFactory.getLog(QueryResults.class);
   
   protected Querier querier = null;
   
   /** Construct with a link to the Querier that spawned these results, so we
    * can include info from it if need be */
   public QueryResults(Querier parentQuerier) {
      this.querier = parentQuerier;
   }
   
   /** All Virtual Observatories must be able to provide the results in VOTable
    * format.  The statusToUpdate can be used to change the querier's status so that
    * monitors can see how things are going.
    */
   public abstract void toVotable(Writer out, QuerierProcessingResults statusToUpdate) throws IOException;
   
   /** HTML output suitable for display in a browser
    */
   public abstract void toHtml(Writer out, QuerierProcessingResults statusToUpdate) throws IOException;

   /** Comma Seperated Variable format does not contain the metadata of VOtable, but is
    * very common and can be put straight into spreadsheets, etc.
    */
   public abstract void toCSV(Writer out, QuerierProcessingResults statusToUpdate) throws IOException;
   
   /** Returns the number of results - or -1 if unknown */
   public abstract int getCount() throws IOException;
   
   /** Looks at given format and decides which output method to use */
   protected void write(Writer out, QuerierProcessingResults statusToUpdate, ReturnSpec returns) throws IOException {
      
      assert (out != null);

      String format = returns.getFormat();
      if (format == null) {
         format = ReturnTable.VOTABLE; //default to votable
      }
      
      if (format.toUpperCase().equals(ReturnTable.VOTABLE.toUpperCase())) {
         toVotable(out, statusToUpdate);
      }
      else if (format.toUpperCase().equals(ReturnTable.CSV.toUpperCase())) {
         toCSV(out, statusToUpdate);
      }
      else if (format.toUpperCase().equals(ReturnTable.HTML.toUpperCase())) {
         toHtml(out, statusToUpdate);
      }
      else {
         throw new IllegalArgumentException("Unknown results format "+format+" given");
      }
   }

   /** This is a helper method for subclasses; it is meant to be called
    * from the askQuery method.  It transforms the results and sends them
    * as required, updating the querier status appropriately.
    */
   public void send(ReturnSpec returns, Account user) throws IOException {
      
      QuerierProcessingResults status = new QuerierProcessingResults(querier.getStatus());
      querier.setStatus(status);

      log.info(querier+", sending results to "+returns);

      if (returns.getTarget() instanceof EmailTarget) {

         email(returns, status);
      }
      else {

         status.setMessage("Sending results to "+returns.getTarget());

         Writer writer = returns.getTarget().resolveWriter(user);
      
         if (writer == null) {
            throw new IOException("Could not resolve writer from "+returns.getTarget());
         }

         write(writer, status, returns);
         
         //we shouldn't actually close the writer, as for JSPs for example the
         //page may still have writing to do
         //@todo close targets when necessary
         writer.flush();
      }

      String s = "Results sent to "+returns.getTarget();
      if (returns.getTarget() instanceof IvornTarget) { s = s + " => "+((IvornTarget) returns.getTarget()).resolveAgsl(); }
      status.addDetail(s);
      status.setMessage("");
        
      log.info(querier+" results sent");
   }

   /**
    * Experimental emailler
    */
   protected void email(ReturnSpec returns, QuerierProcessingResults status) throws IOException {

      EmailTarget target = (EmailTarget) returns.getTarget();
      
      status.setMessage("emailing results to "+target.getEmailAddress());
         
      String targetAddress = target.getEmailAddress();
      
      // Get email server from configuration file
      String emailServer = SimpleConfig.getSingleton().getString(Slinger.EMAIL_SERVER);
      String emailUser = SimpleConfig.getSingleton().getString(Slinger.EMAIL_USER, null);
      String emailPassword = SimpleConfig.getSingleton().getString(Slinger.EMAIL_PWD, null);
      String emailFrom = SimpleConfig.getSingleton().getString(Slinger.EMAIL_FROM);
         
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
         write(sw, status, returns);
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


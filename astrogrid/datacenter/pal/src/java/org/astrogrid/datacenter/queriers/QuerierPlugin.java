/*
 * $Id: QuerierPlugin.java,v 1.2 2004/10/06 21:12:17 mch Exp $
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
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.w3c.dom.Document;

/**
 * Querier Plugins are used to carry out all the database specific backend
 * tasks.  They are given a parent at construction time; this is the Querier
 * class that provides all the information a young plugin needs, such as the
 * Query itself, a way of updating status, etc
 *
 */

public abstract class QuerierPlugin  {

   protected Querier querier;

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
   

   /**
    * While the 'askQuery' should do all tidying up required, this method
    * exists just to separate the plugin from the Querier when the querier
    * closes, ready for garbage collection
    */
   public void close() {
      querier = null;
   }
}
/*
 $Log: QuerierPlugin.java,v $
 Revision 1.2  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.16  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.15  2004/09/06 20:23:00  mch
 Replaced metadata generators/servers with plugin mechanism. Added Authority plugin

 Revision 1.14  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.13  2004/08/17 20:19:36  mch
 Moved TargetIndicator to client

 Revision 1.12  2004/04/01 17:15:21  mch
 Attempt to remove plugin on close

 Revision 1.11  2004/03/22 12:31:10  mch
 Removed datacenter prefix from emailing keys

 Revision 1.10  2004/03/22 12:26:12  mch
 Removed writer close

 Revision 1.9  2004/03/18 18:32:45  mch
 Added info

 Revision 1.8  2004/03/18 00:31:33  mch
 Added adql 7.3.1 tests and max row information to status

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





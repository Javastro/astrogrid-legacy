/*
 * $Id: DefaultPlugin.java,v 1.4 2004/11/11 23:23:29 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers;


import java.io.IOException;
import java.io.StringWriter;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetMaker;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;

/**
 * Default plugin has an aborted flag and a logger
 */

public abstract class DefaultPlugin implements QuerierPlugin {

   protected static final Log log = LogFactory.getLog(QuerierPlugin.class);
   
   protected boolean aborted = false;
   
   /** Abort - if this is called, try and top the query and tidy up.   */
   public void abort() {
      aborted = true;
   }

   /** Used by plugins that can't do a proper count, but have to get the results
    * and count them.  Often proxy services where the proxied service doesn't provide
    * the functionality. */
   public long getCountFromResults(Account user, Query query, Querier querier) throws IOException {
      StringWriter sw = new StringWriter();
      query.setResultsDef(new ReturnTable(TargetMaker.makeIndicator(sw), ReturnTable.VOTABLE));
      askQuery(user, query, querier);
      try {
         return DomHelper.newDocument(sw.toString()).getElementsByTagName("TR").getLength();
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Server not configured correctly ",e);
      }
      catch (SAXException e) {
         throw new IOException("Proxied service returned invalid VOTable: "+e);
      }
   }

   
   
}
/*
 $Log: DefaultPlugin.java,v $
 Revision 1.4  2004/11/11 23:23:29  mch
 Prepared framework for SSAP and SIAP

 Revision 1.3  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate


 */





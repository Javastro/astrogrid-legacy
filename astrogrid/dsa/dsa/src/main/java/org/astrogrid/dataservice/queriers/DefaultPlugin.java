/*
 * $Id: DefaultPlugin.java,v 1.1.1.1 2009/05/13 13:20:25 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers;


import java.io.IOException;
import java.io.StringWriter;
import java.security.Principal;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.xml.DomHelper;
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
   public long getCountFromResults(Principal user, Query query, Querier querier) throws IOException, QueryException {
      StringWriter sw = new StringWriter();
      query.setResultsDef(new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));
      askQuery(user, query, querier);
      try {
         return DomHelper.newDocument(sw.toString()).getElementsByTagName("TR").getLength();
      }
      catch (SAXException e) {
         throw new IOException("Proxied service returned invalid VOTable: "+e);
      }
   }

   
   
}
/*
 $Log: DefaultPlugin.java,v $
 Revision 1.1.1.1  2009/05/13 13:20:25  gtr


 Revision 1.4  2006/06/15 16:50:08  clq2
 PAL_KEA_1612

 Revision 1.3.64.3  2006/04/21 12:10:37  kea
 Renamed ReturnSimple back to ReturnTable (since it is indeed intended
 for returning tables).

 Revision 1.3.64.2  2006/04/21 11:54:05  kea
 Changed QueryException from a RuntimeException to an Exception.

 Revision 1.3.64.1  2006/04/20 15:08:28  kea
 More moving sideways.

 Revision 1.3  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.2.16.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.4.2.2  2004/11/23 11:55:06  mch
 renamved makeTarget methods

 Revision 1.4.2.1  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

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





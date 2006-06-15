/*
 * $Id: ExistPlugin.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.xdbserver.xql;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Principal;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.DefaultPlugin;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.status.QuerierQuerying;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.xql.XqlMaker;
import org.astrogrid.xml.DomHelper;
import org.xml.sax.SAXException;

/**
 * Plugin to use an eXist database that is colocated with this PAL (ie, as another
 * webapp on the same machine)
 *
 * @author Kevin B, Martin H
 */

public class ExistPlugin extends DefaultPlugin {
   
   /** Plugin implementation - carries out query.
    */
   public void askQuery(Principal user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus(), query.toString()));

      InputStream eXistIn = askExist(query);
      
      if (!aborted) {
         XmlResults results = new XmlResults(querier, eXistIn);
         results.send(query.getResultsDef(), querier.getUser());
      }
   }
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Principal user, Query query, Querier querier) throws IOException {
      throw new UnsupportedOperationException("To do");
   }
   

   /** Returns the limit configured for this application */
   public long getLocalLimit() {
      return ConfigFactory.getCommonConfig().getInt("datacenter.results.limit",300);
   }
   
   /** queries eXist with the given query */
   public InputStream askExist(Query query) throws IOException {
      XqlMaker maker = new XqlMaker();
      String xql = maker.getXql(query);
      long limit = query.getLimit();
      if ((limit <=0) ||
             ((limit > getLocalLimit()) && (getLocalLimit() >0))) {
         limit = getLocalLimit();
      }
      
      return askExist(xql, limit);
   }
   
   /** queries exist with the given Xql and max limit number of returned values,
    * returning a stream ot the results */
   public InputStream askExist(String xql, long limit) throws IOException {

         String query = "<query xmlns='http://exist.sourceforge.net/NS/exist'" +
            " start='1' max='" + limit + "\">" +
            "<text><![CDATA[" + xql + "]]></text></query>";

         try {
            DomHelper.newDocument(query); //check that it's valid
         }
         catch(SAXException se) {
            throw new QueryException(se+" in generated XQL (Internal Server Error)",se);
         }
         URL postUrl = new URL(ConfigFactory.getCommonConfig().getUrl("exist.db.url")+
                                  "/servlet/db/");
         
         HttpURLConnection huc = (HttpURLConnection) postUrl.openConnection();
         huc.setRequestProperty("Content-Type", "text/xml");
         huc.setDoOutput(true);
         huc.setDoInput(true);
         huc.connect();
         //write query
         DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
         dos.writeChars(query);
         dos.flush();
         dos.close();
         //read results
         if (!aborted) {
            return huc.getInputStream();
         }
         else {
            return null;
         }
      
      
   }

   
 
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return XmlResults.listFormats();
   }
   
   
 
}

/*
 $Log: ExistPlugin.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/20 15:23:08  kea
 Checking old sources in in oldserver directory (rather than just
 deleting them, might still be useful).

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1  2005/03/10 16:42:55  mch
 Split fits, sql and xdb

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.5.2.4  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.5.2.3  2004/11/24 20:59:37  mch
 doc fixes and added slinger browser

 Revision 1.5.2.2  2004/11/22 00:57:16  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.5.2.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.5  2004/11/12 13:49:12  mch
 Fix where keyword maker might not have had keywords made

 Revision 1.4  2004/11/11 23:23:29  mch
 Prepared framework for SSAP and SIAP

 Revision 1.3  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/27 00:43:40  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:06  mch
 Lots of changes during trip to CDS to improve int test pass rate


 */






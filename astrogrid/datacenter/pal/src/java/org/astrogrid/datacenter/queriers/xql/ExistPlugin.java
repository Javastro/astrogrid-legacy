/*
 * $Id: ExistPlugin.java,v 1.5 2004/11/12 13:49:12 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.xql;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.community.Account;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DefaultPlugin;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.status.QuerierQuerying;
import org.astrogrid.datacenter.queriers.xql.XqlMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.util.DomHelper;
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
   public void askQuery(Account user, Query query, Querier querier) throws IOException {

      querier.setStatus(new QuerierQuerying(querier.getStatus()));

      InputStream eXistIn = askExist(query);
      
      if (!aborted) {
         XmlResults results = new XmlResults(querier, eXistIn);
         results.send(query.getResultsDef(), querier.getUser());
      }
   }
   
   /** Returns just the number of matches rather than the list of matches */
   public long getCount(Account user, Query query, Querier querier) throws IOException {
      throw new UnsupportedOperationException("To do");
   }
   

   /** Returns the limit configured for this application */
   public long getLocalLimit() {
      return SimpleConfig.getSingleton().getInt("datacenter.results.limit",300);
   }
   
   /** queries eXist with the given query */
   public InputStream askExist(Query query) throws IOException {
      XqlMaker maker = new XqlMaker();
      String xql = maker.getXql(query);
      long limit = query.getLimit();
      if ((limit == -1) ||
             ((limit > getLocalLimit()) && (getLocalLimit() != -1))) {
         limit = getLocalLimit();
      }
      
      return askExist(xql, limit);
   }
   
   /** queries exist with the given Xql and max limit number of returned values,
    * returning a stream ot the results */
   public InputStream askExist(String xql, long limit) throws IOException {

      try {
         String query = "<query xmlns='http://exist.sourceforge.net/NS/exist'" +
            " start='1' max='" + limit + "\">" +
            "<text><![CDATA[" + xql + "]]></text></query>";

         try {
            DomHelper.newDocument(query); //check that it's valid
         }
         catch(SAXException se) {
            throw new QueryException(se+" in generated XQL (Internal Server Error)",se);
         }
         URL postUrl = new URL(SimpleConfig.getSingleton().getUrl("exist.db.url")+
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
      catch(ParserConfigurationException pce) {
         throw new RuntimeException("Server configuration error",pce);
      }
      
   }

   
 
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return XmlResults.getFormats();
   }
   
   
 
}

/*
 $Log: ExistPlugin.java,v $
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





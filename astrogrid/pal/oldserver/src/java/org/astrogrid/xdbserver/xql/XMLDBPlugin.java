/*
 * $Id: XMLDBPlugin.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.xmldb.client.XMLDBService;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;

/**
 * Plugin to use an eXist database that is colocated with this PAL (ie, as another
 * webapp on the same machine)
 *
 * @author Kevin B, Martin H
 */

public class XMLDBPlugin extends DefaultPlugin {
    
   protected static Log log = LogFactory.getLog(XMLDBPlugin.class);
    
   private String xql = null;
   /**Plugin implementation - carries out query.*/
   public void askQuery(Principal user, Query query, Querier querier) throws IOException {
      querier.setStatus(new QuerierQuerying(querier.getStatus(), query.toString()));
      ResourceSet rs = askExist(query);
      
      if (!aborted && rs != null) {
         XmlResults results = new XmlResults(querier, rs, xql);
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
   public ResourceSet askExist(Query query) throws IOException {
      XqlMaker maker = new XqlMaker();
      //get the XQuery
      xql = maker.getXql(query);
      XMLDBService xdbService = XMLDBFactory.createXMLDBService();
      String collectionName = ConfigFactory.getCommonConfig().getString("datacenter.xmldb.collection","");
      Collection coll = null;
      try {
          coll = xdbService.openCollection(collectionName);
          log.info("Now querying in colleciton = " + collectionName + " query = " + xql);
          //start a time to see how long the query took.
          long beginQ = System.currentTimeMillis(); 
          ResourceSet rs = xdbService.queryXQuery(coll,xql);
          log.info("Total Query Time = " + (System.currentTimeMillis() - beginQ));
          log.info("Number of results found in query = " + rs.getSize());
          return rs;
      }catch(XMLDBException xmldb) {
          log.error(xmldb);
          xmldb.printStackTrace();
          return null;
      }finally {
          try {
              xdbService.closeCollection(coll);
          }catch(XMLDBException xmldb) {
              log.error(xmldb);
          }//try
      }//finally      
   }
 
   /** Returns the formats that this plugin can provide.  Asks the results class; override in subclasse if nec */
   public String[] getFormats() {
      return XmlResults.listFormats();
   }
   
   
 
}

/*
 $Log: XMLDBPlugin.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/25 08:48:44  kea
 Moving Exist functionality from Kev's branch into oldserver for the
 time being.

 Revision 1.1.4.2  2006/04/24 13:49:59  clq2
 merged with Pal_KMB_XMLDB

 Revision 1.1.2.3  2006/02/27 16:45:08  KevinBenson
 get rid of some println statements

 Revision 1.1.2.2  2006/02/27 16:41:51  KevinBenson
 removed a xqlmaker class that is already being used in the query package/component
 added to the xmldb plugin

 Revision 1.1.2.1  2006/02/09 13:30:25  KevinBenson
 adding xmldb type war/service for the dsa

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






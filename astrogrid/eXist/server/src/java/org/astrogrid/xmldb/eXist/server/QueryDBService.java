/*
 * $Id: QueryDBService.java,v 1.6 2004/11/03 00:05:00 mch Exp $
 */
package org.astrogrid.xmldb.eXist.server;

import org.astrogrid.config.Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.net.MalformedURLException;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import org.astrogrid.util.DomHelper;
import org.apache.axis.AxisFault;

import java.io.DataOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.namespace.QName;



public class QueryDBService {

   private static final Log log = LogFactory.getLog(QueryDBService.class);

   public static Config conf = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }
   }


   public Document query(String collectionName, String xql) throws AxisFault {
      log.debug("start query");

      try {
         return runQuery(collectionName,xql);
      }catch(MalformedURLException mue) {
         log.error(mue);
         throw new AxisFault("URL incorrect", mue);
      }catch(ParserConfigurationException pce) {
         log.error(pce);
         throw new AxisFault("XML Parser Configuration error", pce);
      }catch(IOException ioe) {
         log.error(ioe);
         throw new AxisFault("IO Excepiton error", ioe);
      }catch(SAXException se) {
         log.error(se);
         throw new AxisFault("SAX Exception - could not parse the result into xml", se);
      }finally {
         log.debug("end query");
      }
   }

   private URL getQueryUrl(String collectionName) throws MalformedURLException, UnsupportedEncodingException
   {
      log.debug("start getQueryUrl");
      String location = conf.getString("exist.db.url");
      if(collectionName == null || collectionName.trim().length() == 0) {
         collectionName = "";
      }
      location += "/servlet/db/" + collectionName;
      log.debug("end getQueryUrl");
      return new URL(location);
   }

   public Document getResource(String collectionName,String resource)
       throws MalformedURLException, ParserConfigurationException,
       IOException, UnsupportedEncodingException, SAXException
   {

       String resourceUniqueName = resource.replaceAll("[^\\w*]","_");
       return DomHelper.newDocument(getQueryUrl(collectionName + "/" + resourceUniqueName + ".xml"));
   }


   public Document runQuery(String collectionName, String xql)
              throws MalformedURLException, ParserConfigurationException,
                     IOException, UnsupportedEncodingException, SAXException {
      log.debug("start runQuery");
      Document queryDoc = null;
      int numberOfResourcesReturned = conf.getInt("exist.query.returncount", 25);
      log.info("max number of resources to be returned = " + numberOfResourcesReturned);

      String query = "<query xmlns=\"http://exist.sourceforge.net/NS/exist\"" +
         " start=\"1\" max=\"" + numberOfResourcesReturned + "\">" +
         "<text><![CDATA[" + xql + "]]></text></query>";
      queryDoc = DomHelper.newDocument(query);
      //System.out.println("the exist query to be posted = " + query);
      log.info("query to be posted = " + query);
      long beginQ = System.currentTimeMillis();
      log.info("Query begin time: " + beginQ);
      URL postURL = getQueryUrl(collectionName);

      HttpURLConnection huc = (HttpURLConnection)postURL.openConnection();
      huc.setRequestProperty("Content-Type", "text/xml");
      huc.setDoOutput(true);
      huc.setDoInput(true);
      huc.connect();
      DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
      DomHelper.DocumentToStream(queryDoc,dos);
      dos.flush();
      dos.close();
      Document resultDoc = DomHelper.newDocument(huc.getInputStream());
      //System.out.println("the resultDoc in QueryDbservice = " + DomHelper.DocumentToString(resultDoc));
      long endQ = System.currentTimeMillis();
      log.info("Query end time: " + endQ);
      log.info("Query total time: " + (endQ - beginQ));
      log.debug("end runQuery");
      return resultDoc;
   }
}

/*
$Log: QueryDBService.java,v $
Revision 1.6  2004/11/03 00:05:00  mch
Fix for returncount default

 */


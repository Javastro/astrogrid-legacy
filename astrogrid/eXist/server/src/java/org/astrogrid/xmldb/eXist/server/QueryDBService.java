/*
 * $Id: QueryDBService.java,v 1.7 2004/12/18 18:30:16 jdt Exp $
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

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;



public class QueryDBService {

   private static final Log log = LogFactory.getLog(QueryDBService.class);

   public static Config conf = null;
   private static String existLocation = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         existLocation = conf.getString("exist.db.url");
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

   private String getQueryUrl(String collectionName) throws MalformedURLException, UnsupportedEncodingException
   {
      log.debug("start getQueryUrl");
      String location = existLocation;
      if(collectionName == null || collectionName.trim().length() == 0) {
         collectionName = "";
      }
      location += "/servlet/db/" + collectionName;
      log.debug("end getQueryUrl");
      return location;
      //return new URL(location);
   }
   
   public Document getCollection(String collectionName)
   throws MalformedURLException, ParserConfigurationException,
   IOException, UnsupportedEncodingException, SAXException {
       return DomHelper.newDocument(new URL(getQueryUrl(collectionName)));
   }


   public Document getResource(String collectionName,String resource)
       throws MalformedURLException, ParserConfigurationException,
       IOException, UnsupportedEncodingException, SAXException
   {

       String resourceUniqueName = resource.replaceAll("[^\\w*]","_");
       return DomHelper.newDocument(new URL(getQueryUrl(collectionName + "/" + resourceUniqueName + ".xml")));
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
      
      HttpClient httpclient = new HttpClient();
      /*
      httpclient.getState().setCredentials(
              new AuthScope("www.verisign.com", 443, "realm"),
              new UsernamePasswordCredentials("username", "password")
          );
      */
      //httpclient.getState().setCredentials(null,existLocation,new UsernamePasswordCredentials("kevindd", "testkevindd"));
      //System.out.println("now trying nullwrealm");
      //httpclient.getState().setCredentials("realm",null,new UsernamePasswordCredentials("kevindd", "testkevindd"));

      
      
      //URL postURL = getQueryUrl(collectionName);
      String queryURL = getQueryUrl(collectionName);
      PostMethod post = new PostMethod(queryURL);
      //post.setDoAuthentication(true);

      /*
      HttpURLConnection huc = (HttpURLConnection)postURL.openConnection();
      huc.setRequestProperty("Content-Type", "text/xml");
      huc.setDoOutput(true);
      huc.setDoInput(true);
      huc.connect();
      DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
      DomHelper.DocumentToStream(queryDoc,dos);      
      dos.flush();
      dos.close();
      */
      String xml = DomHelper.DocumentToString(queryDoc);
      post.setRequestBody(xml);
      if (xml.length() < Integer.MAX_VALUE) {
          post.setRequestContentLength((int)xml.length());
      } else {
          post.setRequestContentLength(EntityEnclosingMethod.CONTENT_LENGTH_CHUNKED);
      }
//    Specify content type and encoding
      // If content encoding is not explicitly specified
      // ISO-8859-1 is assumed
      post.setRequestHeader("Content-type", "text/xml; charset=ISO-8859-1");
//    Get HTTP client
      
      // Execute request
      int result = httpclient.executeMethod(post);
      log.info("result of status code of query = " + result);
      if(result != 200) {
          String error = post.getResponseBodyAsString();
          log.error("Seems to be a problem with query = " + error);
          throw new IOException(error);          
      }
      //Document resultDoc = DomHelper.newDocument(post.getResponseBodyAsString());
      Document resultDoc = DomHelper.newDocument(post.getResponseBodyAsStream());
      // Release current connection to the connection pool once you are done
      post.releaseConnection();

      //Document resultDoc = DomHelper.newDocument(huc.getInputStream());
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
Revision 1.7  2004/12/18 18:30:16  jdt
KMB's merges:
		Reg_KMB_728 astrogrid/registry
          o Reg_KMB_728 astrogrid/workflow
          o Reg_KMB_728 astrogrid/community

          o Exist_KMB_613 astrogrid/exist
          o Reg_KMB_605 astrogrid/auto-integration -- only r

Revision 1.6.10.3  2004/12/13 21:20:15  KevinBenson
added a default on the maven xml, changed where it logs and throws an exception on something that is not a 200 status code returned

Revision 1.6.10.2  2004/12/02 23:22:24  KevinBenson
added a getCollection to it

Revision 1.6.10.1  2004/11/26 22:00:16  KevinBenson
new changes to apache commons http

Revision 1.6  2004/11/03 00:05:00  mch
Fix for returncount default

 */


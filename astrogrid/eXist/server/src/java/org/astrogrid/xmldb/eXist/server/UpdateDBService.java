package org.astrogrid.xmldb.eXist.server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import org.astrogrid.util.DomHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.io.DataOutputStream;

import org.astrogrid.config.Config;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;

import org.apache.axis.AxisFault;


public class UpdateDBService {
 
   private static final Log log = LogFactory.getLog(UpdateDBService.class);
   
   public static Config conf = null;
   private static String existLocation = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         existLocation = conf.getString("exist.db.url");
      }      
   }   
    
 
   public void update(String uniqueName, String type, 
                      String collectionName, Document update) throws AxisFault {
      log.debug("start update");
      try {
         updateQuery(uniqueName, type, collectionName, update.getDocumentElement());
      }catch(MalformedURLException mue) {
         log.error(mue);
         throw new AxisFault("URL incorrect", mue);
      }catch(IOException ioe) {
         log.error(ioe);
         throw new AxisFault("IO Excepiton error", ioe);
      }finally {
         log.debug("end update");
      }       
   }
 
   private String getUpdateURL(String collectionName, String xmlDocName)  throws MalformedURLException
   {
      log.debug("start getUpdateURL");
      String location = existLocation;
      location += "/servlet/db/" + collectionName + "/" + xmlDocName;
      URL fullQueryURL = null;
      log.info("the full update put url = " + location);
      log.debug("end getUpdateURL");
      return location;
      /*
      try {
         fullQueryURL = new URL(location);
      }
      catch(MalformedURLException mue) {
         mue.printStackTrace();
         log.error(mue);   
      } 
      
      return fullQueryURL;
      */
   } 
      
   
   public void updateQuery(String uniqueName, String type, 
                                  String collectionName, Node updateNode)  
                  throws MalformedURLException, IOException
   {
      log.debug("start updateQuery");
      log.info("THE IDENTIFIER = " +  uniqueName);
      String xmlDocName = uniqueName.replaceAll("[^\\w*]","_") + "." + type;
      log.info("THE REPLACED DOC NAME = " + xmlDocName);
      //PostMethod post = new PostMethod(strURL);
      HttpClient httpclient = new HttpClient();
      //httpclient.getState().setCredentials(null,existLocation,new UsernamePasswordCredentials("kevindd", "testkevindd"));
      //System.out.println("now trying nullwrealm");
      //httpclient.getState().setCredentials("realm",null,new UsernamePasswordCredentials("kevindd", "testkevindd"));
      
      PutMethod put = new PutMethod(getUpdateURL(collectionName, xmlDocName));
      //put.setDoAuthentication(true);

      /*
      HttpURLConnection huc = (HttpURLConnection)
                               getUpdateURL(collectionName, xmlDocName).
                               openConnection();
      huc.setRequestProperty("Content-Type", "text/xml");
      huc.setDoOutput(true);
      huc.setRequestMethod("PUT");
      huc.connect();
       DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
      */
      
      String xml = null;
      if(updateNode instanceof Element) {
      //   DomHelper.ElementToStream((Element)updateNode,dos);
          xml = DomHelper.ElementToString((Element)updateNode);
      }else if(updateNode instanceof Document) {
      //   DomHelper.DocumentToStream((Document)updateNode,dos);
          xml = DomHelper.DocumentToString((Document)updateNode);
      }else {
         throw new IOException("The Node was not of an instance of Element or Document which is required for updating the eXist db");
      }
      if(xml == null || xml.trim().length() <= 0) {
          throw new IOException("Nothing was present to send; empty xml content");
      }
      put.setRequestBody(xml);
      if (xml.length() < Integer.MAX_VALUE) {
          put.setRequestContentLength((int)xml.length());
      } else {
          put.setRequestContentLength(EntityEnclosingMethod.CONTENT_LENGTH_CHUNKED);
      }
      
      put.setRequestHeader("Content-type", "text/xml");
      // Execute request
      int result = httpclient.executeMethod(put);
      log.info("Status code of the resulting post = " + result);
      if(result != 200) {
          String error = put.getResponseBodyAsString();
          log.error("Seems to be a problem with query = " + error);
          //throw new IOException(error);          
      }
      log.info("Response of the http put = " + put.getResponseBodyAsString());
      put.releaseConnection();

         
      /*
      dos.flush();
      log.info("closing outputstream and content type = " +
               huc.getContentType());
      dos.close();
      log.info("disconnecting");
      huc.disconnect();
      log.debug("end updateQuery");
      */
   }//updateQuery  
}
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

import org.apache.axis.AxisFault;


public class UpdateDBService {
 
   private static final Log log = LogFactory.getLog(UpdateDBService.class);
   
   public static Config conf = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
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
 
   private URL getUpdateURL(String collectionName, String xmlDocName)  throws MalformedURLException
   {
      log.debug("start getUpdateURL");
      String location = conf.getString("exist.db.url");
      location += "/servlet/db/" + collectionName + "/" + xmlDocName;
      URL fullQueryURL = null;
      log.info("the full update put url = " + location);
      try {
         fullQueryURL = new URL(location);
      }
      catch(MalformedURLException mue) {
         mue.printStackTrace();
         log.error(mue);   
      } 
      log.debug("end getUpdateURL");
      return fullQueryURL;
   } 
      
   
   public void updateQuery(String uniqueName, String type, 
                                  String collectionName, Node updateNode)  
                  throws MalformedURLException, IOException
   {
      log.debug("start updateQuery");
      log.info("THE IDENTIFIER = " +  uniqueName);
      String xmlDocName = uniqueName.replaceAll("[^\\w*]","_") + "." + type;
      log.info("THE REPLACED DOC NAME = " + xmlDocName);
      HttpURLConnection huc = (HttpURLConnection)
                               getUpdateURL(collectionName, xmlDocName).
                               openConnection();
      huc.setRequestProperty("Content-Type", "text/xml");
      huc.setDoOutput(true);
      huc.setRequestMethod("PUT");
      huc.connect();
      DataOutputStream dos = new DataOutputStream(huc.getOutputStream());
      if(updateNode instanceof Element) {
         DomHelper.ElementToStream((Element)updateNode,dos);
      }else if(updateNode instanceof Document) {
         DomHelper.DocumentToStream((Document)updateNode,dos);
      }else {
         throw new IOException("The Node was not of an instance of Element or Document which is required for updating the eXist db");
      }
      
      dos.flush();
      log.info("closing outputstream and content type = " +
               huc.getContentType());
      dos.close();
      log.info("disconnecting");
      huc.disconnect();
      log.debug("end updateQuery");
   }//updateQuery  
}
/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry.server.harvest;

import org.astrogrid.registry.server.QueryParser3_0;
import java.rmi.RemoteException; 

import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.apache.axis.utils.XMLUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import org.xml.sax.InputSource;
import org.astrogrid.registry.common.RegistryConfig;
import org.astrogrid.registry.server.QueryParser3_0;
import org.astrogrid.registry.server.RegistryFileHelper;
import java.net.URL;
import java.io.InputStream;

/**
 * 
 * The RegistryService class is a web service that submits an XML formatted
 * registry query to the QueryParser class.
 * This delegate helps the user browse the registry.  Queries should be formatted according to
 * the schema at IVOA schema version 0.9.  This class also uses the common RegistryInterface for
 * knowing the web service methods to call on the server side.
 * 
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryHarvestService implements
                      org.astrogrid.registry.common.RegistryHarvestInterface {
  
  /**
    * Queries it's own registry for all the Registry entries and performs a harvest on those registries.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query. Used internally.
    * @author Kevin Benson 
    */  
  public Document harvest(Document query) {
   //query the registry for all registries.
   RegistryConfig.loadConfig();
   File harvestQueryFile = RegistryConfig.getHarvestQueryTemplate();
   DocumentBuilder registryBuilder = null;
   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
   Document queryResponseDoc = null;
   try {
      registryBuilder = dbf.newDocumentBuilder();
      Document doc = registryBuilder.parse(harvestQueryFile);
      queryResponseDoc = QueryParser3_0.parseFullNodeQuery(doc);
   }catch(Exception e1) {
      e1.printStackTrace();
   }
   
   if(queryResponseDoc.hasChildNodes()) {
      NodeList nl = queryResponseDoc.getChildNodes();
      for(int i = 0;i < nl.getLength();i++) {
         //DocumentFragment df = queryResponseDoc.createDocumentFragment();
         //df.appendChild(nl.item(i));
         try {
         
         Document harvestedDoc = harvestCallableRegistry(nl.item(i));   
         if(harvestedDoc != null) {
            RegistryFileHelper.updateDocument(harvestedDoc,true, false);
         }
         }catch(Exception e) {
            e.printStackTrace();               
         }
      }            
   }
   //go through each registry caling harvestRegistry below.
   return null;
  }
  
  /**
    * Grabs Registry entries from a DOM object and performs harvests on those registries. Normally the DOM
    * object will only have 1 registry entry, but may contain more. Used externally by clients.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.
    * @author Kevin Benson 
    */  
   public Document harvestRegistry(Document query){

      Document harvestedDoc = null;
      System.out.println("inside harvestregistry");
      //NodeList nl = query.getElementsByTagName("vg:Registry");
      NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","Registry");
      /*
      Node nd = RegistryFileHelper.findElementNode("Registry",query.getDocumentElement());
      if(nd != null) {
         try {
            harvestedDoc = harvestCallableRegistry(nd);   
         }catch(Exception e) {
            e.printStackTrace();
         }//catch                    
      }else {
         System.out.println("the node found in harvestRegistry server side was null");
         
      }
      */
           
      System.out.println("the nodelist length = " + nl.getLength());
      for(int i = 0;i < nl.getLength();i++) {
           //DocumentFragment df = query.createDocumentFragment();
           //df.appendChild(nl.item(i));
         try {
            harvestedDoc = harvestCallableRegistry(nl.item(i));   
         }catch(Exception e) {
            e.printStackTrace();
         }//catch                    
      }//for      
      return harvestedDoc;
     //takes in a registry xml document conforming to the ivoa schema.
     //With the "updated" attribute changed to the request of the user.
     //get the timestamp from the xml document.
     //call the registry.
     //get the results back and update the registry.
     //update the registry here with it's results.  
   }
  
   public Document harvestCallableRegistry(Node regNode) throws Exception {
      RegistryFileHelper.writeRegistryFile();
      Node invoc = RegistryFileHelper.findElementNode("Invocation",regNode);
      Document doc = null;
      System.out.println("Okay in havestCallableRegistry");
      
      if("WebService".equals(invoc.getFirstChild().getNodeValue())) {
        
      }else if("WebBrowser".equals(invoc.getFirstChild().getNodeValue())) { 
         String accessURL = RegistryFileHelper.findElementNode("AccessURL",regNode).getFirstChild().getNodeValue();
         System.out.println("the harvestcallregistry's accessurl = " + accessURL);
         RegistryFileHelper.addStatusMessage("A Harvest has begun on this location: " + accessURL);
         String ending = "";
         String date = "1950-02-02";
         if(regNode.hasAttributes()) {
            NamedNodeMap nnm = regNode.getAttributes();
            Node attrNode = nnm.getNamedItem("updated");
            date = attrNode.getNodeValue();
         }
         if(date.indexOf("T") != -1) {
            date = date.substring(0,date.indexOf("T"));
         }
         if(accessURL.indexOf("?") == -1) {
            ending = "?verb=ListRecords&from=" + date;   
         }else {
            if(accessURL.indexOf("verb") == -1)
               ending = "&verb=ListRecords&from=" + date;
            else
               ending = "&from=" + date;
         }
         System.out.println("the harvestcallregistry's accessurl2 = " + accessURL + ending);

         DocumentBuilder registryBuilder = null;
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         registryBuilder = dbf.newDocumentBuilder();
         Document harvestDoc = registryBuilder.parse(accessURL + ending);
         System.out.println("in harvestcallregistry the harvestDoc = " + XMLUtils.DocumentToString(harvestDoc));
         RegistryFileHelper.updateDocument(harvestDoc,true, false);
         NodeList moreTokens = null;
         while( (moreTokens = harvestDoc.getElementsByTagName("resumptionToken")).getLength() > 0) {
            Node nd = moreTokens.item(0);
            if(accessURL.indexOf("?") != -1) {
               accessURL = accessURL.substring(0,accessURL.indexOf("?"));
            }
            ending = "?verb=ListRecords&resumptionToken=" + nd.getFirstChild().getNodeValue();
            System.out.println("the harvestcallregistry's accessurl for resumptionToken = " + accessURL + ending);
            harvestDoc = registryBuilder.parse(accessURL + ending);
            //System.out.println("in harvestcallregistry the harvestDoc2 = " + XMLUtils.DocumentToString(harvestDoc));
            RegistryFileHelper.updateDocument(harvestDoc.getDocumentElement(),true, false);
         }//while
         RegistryFileHelper.writeRegistryFile();
      }//elseif
     return doc;
  }
  
  
}
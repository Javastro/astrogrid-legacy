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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.File;
import org.xml.sax.InputSource;
import org.astrogrid.registry.RegistryConfig;
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
 * @see org.astrogrid.registry.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryHarvestService implements org.astrogrid.registry.RegistryHarvestInterface {
  
  /**
    * fullNodeQuery queries the registry with the a XML docuemnt object, and returns the results
    * in a XML Document object query.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.
    * @deprecated Being deprecated this method now only returns the full XML document.
    * @author Kevin Benson 
    */  
  public Document harvest(Document query) throws Exception {
   //query the registry for all registries.
   RegistryConfig.loadConfig();
   File harvestQueryFile = RegistryConfig.getHarvestQueryTemplate();
   DocumentBuilder registryBuilder = null;
   DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
   registryBuilder = dbf.newDocumentBuilder();
   Document doc = registryBuilder.parse(harvestQueryFile);
   Document queryResponseDoc = QueryParser3_0.parseFullNodeQuery(doc);
   
   //DocumentBuilder responseDocBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
   //Document responseDoc = responseDocBuilder.newDocument();
   //Element root = responseDoc.createElement("vodescription");
   //responseDoc.appendChild(root);
   
   
   
   if(queryResponseDoc.hasChildNodes()) {
      NodeList nl = queryResponseDoc.getChildNodes();
      for(int i = 0;i < nl.getLength();i++) {
         //DocumentFragment df = queryResponseDoc.createDocumentFragment();
         //df.appendChild(nl.item(i));
         Document harvestedDoc = harvestCallableRegistry(nl.item(i));   
         if(harvestedDoc != null) {
            RegistryFileHelper.updateDocument(RegistryFileHelper.loadRegistryFile(),harvestedDoc);
         }               
      }            
   }
   //go through each registry caling harvestRegistry below.
   return null;
  }
  
  /**
    * fullNodeQuery queries the registry with the a XML docuemnt object, and returns the results
    * in a XML Document object query.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query.
    * @deprecated Being deprecated this method now only returns the full XML document.
    * @author Kevin Benson 
    */  
  public Document harvestRegistry(Document query) throws Exception {
   
     Document harvestedDoc = null;  
     if(query.hasChildNodes()) {
        NodeList nl = query.getChildNodes();
        for(int i = 0;i < nl.getLength();i++) {
           //DocumentFragment df = query.createDocumentFragment();
           //df.appendChild(nl.item(i));
           harvestedDoc = harvestCallableRegistry(nl.item(i));   
           if(harvestedDoc != null) {
              RegistryFileHelper.updateDocument(RegistryFileHelper.loadRegistryFile(),harvestedDoc);
           }
           //               
        }            
     }
     return harvestedDoc;
     //takes in a registry xml document conforming to the ivoa schema.
     //With the "updated" attribute changed to the request of the user.
     //get the timestamp from the xml document.
     //call the registry.
     //get the results back and update the registry.
     //update the registry here with it's results.  
  }
  
   public Document harvestCallableRegistry(Node regNode) throws Exception {
      Node invoc = RegistryFileHelper.findElementNode("Invocation",regNode);
      Document doc = null;
      if("WebService".equals(invoc.getFirstChild().getNodeValue())) {
        
      }else if("WebBrowser".equals(invoc.getFirstChild().getNodeValue())) { 
         String accessURL = RegistryFileHelper.findElementNode("AccessURL",regNode).getFirstChild().getNodeValue();
         URL url = new URL(accessURL);
         InputStream is = url.openStream();
         DocumentBuilder registryBuilder = null;
         DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
         registryBuilder = dbf.newDocumentBuilder();
         doc = registryBuilder.parse(is);
         is.close();
         System.out.println("the returned doc = " + XMLUtils.DocumentToString(doc));
      }
     return doc;
  }
  
  
}
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
import org.xml.sax.InputSource;
import org.astrogrid.registry.server.RegistryFileHelper;
import org.astrogrid.registry.server.QueryParser3_0;
import org.astrogrid.registry.server.query.RegistryService;
import java.net.URL;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;

import org.astrogrid.config.Config;



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
  
   private static final String HARVEST_TEMPLATE_URL_PROPERTY = "org.astrogrid.registry.harvest.template.url";
  
   public static Config conf = null;
   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }      
   }
  
  /**
    * Queries it's own registry for all the Registry entries and performs a harvest on those registries.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query. Used internally.
    * @author Kevin Benson 
    */  
   public Document harvest(Document query) {
      try {
         RegistryService rs = new RegistryService();
         
         Document registryDoc = rs.loadRegistry(null);
         //NodeList regNL = registryDoc.getElementsByTagName("ManagedAuthority");
         NodeList regNL = registryDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","ManagedAuthority");
         String selectQuery = "<query><selectionSequence>" +
              "<selection item='searchElements' itemOp='EQ' value='all'/>" +
              "<selectionOp op='AND'/>";
         if(regNL.getLength() > 0) {
            selectQuery +=  
            "<selection item='AuthorityID' itemOp='EQ' value='" + regNL.item(0).getFirstChild().getNodeValue() + "'/>";
         }
         for(int i = 1;i < regNL.getLength();i++) {
            selectQuery += "<selectionOp op='OR'/>" +
            "<selection item='AuthorityID' itemOp='EQ' value='" + regNL.item(i).getFirstChild().getNodeValue() + "'/>";
         }
         selectQuery += "</selectionSequence></query>";      
         
         Reader reader2 = new StringReader(selectQuery);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);
         
         Document responseDoc = QueryParser3_0.parseFullNodeQuery(doc);
         return responseDoc;
      }catch(Exception e) {
         e.printStackTrace();
      }   
      return null;
  }
  
  /**
    * Queries it's own registry for all the Registry entries and performs a harvest on those registries.
    * 
    * @param query XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query. Used internally.
    * @author Kevin Benson 
    */  
   public Document harvestFrom(Document query) {
      try {
         NodeList nl = query.getElementsByTagName("date_since");
         if(nl.getLength() == 0) {
            return harvest(query);   
         }
         String updateVal = nl.item(0).getFirstChild().getNodeValue();
         
         //Probably should parse this with a date  and validat it is a date.
                 
         RegistryService rs = new RegistryService();         
         Document registryDoc = rs.loadRegistry(null);
        
         NodeList regNL = registryDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","ManagedAuthority");
         String selectQuery = "<query><selectionSequence>" +
             "<selection item='searchElements' itemOp='EQ' value='all'/>" +
             "<selectionOp op='AND'/>" + 
             "<selection item='@updated' itemOp='AFTER' value='" + updateVal + "'/>";;
         if(regNL.getLength() > 0) {
            selectQuery +=  
            "<selection item='AuthorityID' itemOp='EQ' value='" + regNL.item(0).getFirstChild().getNodeValue() + "'/>";
         }
         for(int i = 1;i < regNL.getLength();i++) {
            selectQuery += "<selectionOp op='OR'/>" +
            "<selection item='AuthorityID' itemOp='EQ' value='" + regNL.item(i).getFirstChild().getNodeValue() + "'/>";
         }
         selectQuery += "</selectionSequence></query>";      
         Reader reader2 = new StringReader(selectQuery);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);
         Document responseDoc = QueryParser3_0.parseFullNodeQuery(doc);
         return responseDoc;
      }catch(Exception e) {
        e.printStackTrace();
      }   
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
   public Document harvestResource(Document query){

      Document harvestedDoc = null;
      //This next statement will go away with Castor.            
      NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","Identifier");
      /*
       * 
       * Lets throw it to Castor and get an Object model instead and get the identifier that way.
       * Now make sure this identifier is  one that we manage.  (has an authorityID that is in our ManagedAuthority
       * element in the Registry resource entry.) Unless it is a Registry resource entry they do not need to have
       * a authority id we manage. 
       * Get a modification date from the db for this resource entry if it is in our db.
       * Call beginHarvest(date,resource entry)
       *        
       *       
       */      
       
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
   }
   
   /**
     * Grabs Registry entries from a DOM object and performs harvests on those registries. Normally the DOM
     * object will only have 1 registry entry, but may contain more. Used externally by clients.
     * 
     * @param query XML document object representing the query language used on the registry.
     * @return XML docuemnt object representing the result of the query.
     * @author Kevin Benson 
     */  
    public Document harvestFromResource(Document query){

       Document harvestedDoc = null;
       //This next statement will go away with Castor.            
       NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","Identifier");
       /*
        * 
        * Lets throw it to Castor and get an Object model instead and get the identifier that way.
        * Now make sure this identifier is  one that we manage.  (has an authorityID that is in our ManagedAuthority
        * element in the Registry resource entry.) Unless it is a Registry resource entry they do not need to have
        * a authority id we manage. 
        * Get a modification date from the db for this resource entry if it is in our db.
        * Call beginHarvest(date,resource entry)
        *        
        * 
       
        */      
       
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
    }
   
   
   /**
       * Grabs Registry entries from a DOM object and performs harvests on those registries. Normally the DOM
       * object will only have 1 registry entry, but may contain more. Used externally by clients.
       * 
       * @param query XML document object representing the query language used on the registry.
       * @return XML docuemnt object representing the result of the query.
       * @author Kevin Benson 
       */  
      public Document harvestAll(Document query){

         Document harvestedDoc = null;
         //This next statement will go away with Castor.            
         NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","Identifier");
         /*
          * Now lets see if this Document is null or empty.  If so then query for all a Registries and Service
          * resource entries we know of and manage.
          *   Now start calling beginHarvest(null,resource Entry)
          * Now if the dom object is not empty.
          * Lets throw it to Castor and get an Object model instead and get the identifier that way.
          * Now make sure this identifier is  one that we manage.  (has an authorityID that is in our ManagedAuthority
          * element in the Registry resource entry.) Unless it is a Registry resource entry they do not need to have
          * a authority id we manage.
          * Call beginHarvest(null,resource entry)
          */      
       
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
      }
   
   private void beginHarvest(Date dt, Document doc /*This should be the castor object model*/) {
      /*
       * Get the Invocation object
       * set a boolean if this is a Registry resource entry.
       * if it is a web browser
       *    set a string to the accessURL.
       *    if it registry resource browser and dt is not null then
       *       then add a "from" + date  to the end of the url
       *    Now doa document.parse on this url.
       * if it is a web service
       *    instantiate a Call object pointing to the WSDL.
       *    if it is a registry resource then
       *       if dt is null then
       *          call harvestAll(null) web service
       *       if dt is not null then
       *          call harvestFrom(dt) web service
       *    if it is not a registry resource then
       *       set the call object to the ResourceKey.
       */            
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
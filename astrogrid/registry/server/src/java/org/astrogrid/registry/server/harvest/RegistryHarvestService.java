/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry.server.harvest;

import org.astrogrid.registry.server.QueryParser3_0;
import java.rmi.RemoteException; 

import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.astrogrid.registry.server.RegistryFileHelper;
import org.astrogrid.registry.server.query.RegistryService;
import java.net.URL;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.axis.client.Call; 
import org.apache.axis.client.Service; 
import org.apache.axis.message.SOAPBodyElement;
import javax.xml.rpc.ServiceException;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;
import org.exolab.castor.xml.*;
import org.astrogrid.registry.beans.resource.*;
import org.astrogrid.registry.beans.resource.types.InvocationType;
import org.astrogrid.registry.beans.resource.registry.RegistryType;
import org.astrogrid.registry.RegistryException;

import org.astrogrid.registry.common.WSDLInformation;
import org.astrogrid.registry.common.WSDLBasicInformation;
import org.astrogrid.registry.common.XSLHelper;

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
    * @param registry This parameter is normally null.  Kicks off returning everything manged by this registry.
    * @return XML docuemnt object representing the result of the query. Used internally.
    * @author Kevin Benson 
    */  
   public Document harvest(Document registry) {
      try {
         RegistryService rs = new RegistryService();         
         Document registryDoc = rs.loadRegistry(null);
         NodeList regNL = registryDoc.getElementsByTagName("vg:ManagedAuthority");
         //NodeList regNL = registryDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority");
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
    * @param dateDom XML document object representing the query language used on the registry.
    * @return XML docuemnt object representing the result of the query. Used internally.
    * @author Kevin Benson 
    */  
   public Document harvestFrom(Document dateDom) {
      try {
         NodeList nl = dateDom.getElementsByTagName("date_since");
         if(nl.getLength() == 0) {
            return harvest(dateDom);   
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
   public Document harvestResource(Document resources) throws RegistryException{

      //This next statement will go away with Castor.
      ArrayList al = new ArrayList();
      try {
         System.out.println("what was passed in at harvestResoruce = " + DomHelper.DocumentToString(resources));
         XSLHelper xs = new XSLHelper();
         Document resourceChange = xs.transformDatabaseProcess(resources);
         System.out.println("the resourceChange = " + DomHelper.DocumentToString(resourceChange));
         Document castorXS = xs.transformCastorProcess(resourceChange);
         System.out.println("castorxs = " + DomHelper.DocumentToString(castorXS));
         VODescription vodesc = (VODescription)Unmarshaller.unmarshal(VODescription.class,castorXS);
         HashMap auths = RegistryFileHelper.getManagedAuthorities();
         int count = vodesc.getResourceCount();

         DocumentBuilder registryBuilder = null;
         Document doc = null;
         Document resultDoc = null;
         for(int i = 0;i < count;i++) {
            ResourceType rtTemp = vodesc.getResource(i);
            if(rtTemp instanceof ServiceType) {            
               if(rtTemp instanceof RegistryType) {
                  al.add(rtTemp);
                  registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                  doc = registryBuilder.newDocument();
                  try {                                       
                     Marshaller.marshal(rtTemp,doc);
                     System.out.println("updating the ResourceType document from the marshaller doc = " + DomHelper.DocumentToString(doc));
                     resultDoc = RegistryFileHelper.updateDocument(doc,true,false);
                     if(resultDoc != null) 
                        System.out.println("after updating the ResourceType document = " + DomHelper.DocumentToString(resultDoc));
                  }catch(ValidationException ve) {
                     ve.printStackTrace();
                  }catch(MarshalException me) {
                     me.printStackTrace();
                  }
               }else {
                  if(auths != null && auths.containsKey(rtTemp.getIdentifier().getAuthorityID())) {
                     al.add(rtTemp);
                     registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                     doc = registryBuilder.newDocument();
                     try {
                        Marshaller.marshal(rtTemp,doc);
                        resultDoc = RegistryFileHelper.updateDocument(doc,true,false);
                        System.out.println("after updating the ResourceType document = " + DomHelper.DocumentToString(resultDoc));
                     }catch(ValidationException ve) {
                        ve.printStackTrace();
                     }catch(MarshalException me) {
                        me.printStackTrace();
                     }
                  }//if
               }//else
            }//if
         }//for
      }catch(MarshalException me) {
         throw new RegistryException(me);
      }catch(ValidationException ve) {
         throw new RegistryException(ve);
      }catch(ParserConfigurationException pce) {
         throw new RegistryException(pce);   
      }
      
      for(int i = 0;i < al.size();i++) {
         try {
            beginHarvest(null,(ServiceType)al.get(i));
         }catch(RegistryException re) {
            re.printStackTrace();   
         }
      }
      
      
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
   public Document harvestFromResource(Document resource) throws RegistryException {
      try {
         XSLHelper xs = new XSLHelper();
         Document resourceChange = xs.transformDatabaseProcess(resource);
         Document castorXS = xs.transformCastorProcess(resourceChange);
         
          //Now get the dateFrom element value as well.
          //NodeList nl = resource.getElementsByTagName("VODescription");
          NodeList nl = castorXS.getElementsByTagName("VODescription");
          if(nl.getLength() > 0) {
             VODescription vodesc = (VODescription)Unmarshaller.unmarshal(VODescription.class,nl.item(0));
             HashMap auths = RegistryFileHelper.getManagedAuthorities();
             int count = vodesc.getResourceCount();
             ArrayList al = new ArrayList();
         
             for(int i = 0;i < count;i++) {
                ResourceType rtTemp = vodesc.getResource(i);
                if(rtTemp instanceof ServiceType) {            
                   if(rtTemp instanceof RegistryType) {
                      al.add(rtTemp);                        
                   }else {
                      if(auths != null && auths.containsKey(rtTemp.getIdentifier().getAuthorityID())) {
                         al.add(rtTemp);
                      }//if
                   }//else
                }//if
             }//for
             for(int i = 0;i < al.size();i++) {
                //remember to use the dateFrom element.
                beginHarvest(null,(ServiceType)al.get(i));
             }
         }//if
      }catch(MarshalException me) {
         throw new RegistryException(me);
      }catch(ValidationException ve) {
         throw new RegistryException(ve);
      }
      return null;         
    }
   
   
   /**
       * Grabs Registry entries from a DOM object and performs harvests on those registries. Normally the DOM
       * object will only have 1 registry entry, but may contain more. Used externally by clients.
       * 
       * @param resources XML document object representing the query language used on the registry.
       * @return XML docuemnt object representing the result of the query.
       * @author Kevin Benson 
       */  
      public Document harvestAll(Document resources) throws RegistryException {
         Document harvestedDoc = null;
         //This next statement will go away with Castor.            
         //NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","Identifier");
         if(resources != null && resources.getElementsByTagName("VODescription").getLength() > 0) {
            return harvestResource(resources);   
         }else {
            return harvestResource(harvest(null));   
         }
      }
      
      /**
       * Method to establish a Service and a Call to the server side web service.
       * @return Call object which has the necessary properties set for an Axis message style.
       * @throws Exception
       * @author Kevin Benson
       */     
      private Call getCall(String endPoint) {
         Call _call = null;
         try {
            Service  service = new Service();      
            _call = (Call) service.createCall();
            _call.setTargetEndpointAddress(endPoint);
            _call.setSOAPActionURI("");
            _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
            _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);        
            _call.setEncodingStyle(null);
         } catch(ServiceException se) {
            se.printStackTrace();
            _call = null;            
         }finally {
            return _call;   
         }       
      }      
   
   private void beginHarvest(Date dt, ServiceType st) throws RegistryException {
      String accessURL = st.getInterface().getAccessURL().getContent();
      System.out.println("accessURL = " + accessURL);
      Document doc = null;
      if(InvocationType.WEBSERVICE_TYPE == st.getInterface().getInvocation().getType()) {
         //call the service
         //remeber to look at the date
         WSDLBasicInformation wsdlBasic = WSDLInformation.getBasicInformationFromURL(accessURL);         
         if(wsdlBasic == null) {
            Call callObj = getCall((String)wsdlBasic.getEndPoint().values().iterator().next());
            DocumentBuilder registryBuilder = null;
            try {
               DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
               dbf.setNamespaceAware(true);
               registryBuilder = dbf.newDocumentBuilder();
               doc = registryBuilder.newDocument();
               Element root = null;
               String interfaceMethod = null;
               if(st instanceof RegistryType) {
                  interfaceMethod = "harvest";
                  //TODO make check of date here and actually do a harvestFrom   
               }else {
                  interfaceMethod = st.getIdentifier().getResourceKey();
                  if(interfaceMethod == null || interfaceMethod.trim().length() > 0) {
                     interfaceMethod = "getMetaData";
                  }
                  doc.createElementNS(wsdlBasic.getTargetNameSpace(),interfaceMethod);
                  doc.appendChild(root);
                  SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
                  sbeRequest.setName("harvestAll");               
                  sbeRequest.setNamespaceURI(wsdlBasic.getTargetNameSpace());
                  Vector result = (Vector) callObj.invoke (new Object[] {sbeRequest});
                  SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
                  RegistryFileHelper.updateResources(sbe.getAsDocument(),true,false);
                  RegistryFileHelper.writeRegistryFile();
               }
            }catch(RemoteException re) {
              //log error
              re.printStackTrace();   
            }catch(ParserConfigurationException pce) {
               pce.printStackTrace();   
            }catch(Exception e) {
               e.printStackTrace();   
            }
         }else {
            throw new RegistryException("Could not seem to get information from WSDL on this Web Service type");   
         }
         
      }else if(InvocationType.WEBBROWSER_TYPE == st.getInterface().getInvocation().getType()) {
         try {
            //might need to put some oai date stuff on the end.  This is unknown.
            System.out.println("inside the web broser");
            doc = DomHelper.newDocument(new URL(st.getInterface().getAccessURL().getContent()));
            System.out.println("Okay got this far to reading the url doc = " + DomHelper.DocumentToString(doc));
            RegistryFileHelper.updateResources(doc,true,false);
            RegistryFileHelper.writeRegistryFile();
         }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
         }catch(SAXException sax) {
            sax.printStackTrace();   
         }catch(IOException ioe){
            ioe.printStackTrace();   
         }
      }//elseif
   }
   
  /*
   public Document harvestCallableRegistry(Node regNode) throws Exception {
      RegistryFileHelper.writeRegistryFile();
      Node invoc = RegistryFileHelper.findElementNode("Invocation",regNode);
      Document doc = null;
      System.out.println("Okay in havestCallableRegistry");
      
      if("WebService".equals(invoc.getFirstChild().getNodeValue())) {
        //call the service
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
  */
  
}
/*
 * Created on 25-Apr-2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package org.astrogrid.registry.server.harvest;

import org.astrogrid.registry.server.XQueryExecution;
import java.rmi.RemoteException; 

import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.astrogrid.registry.server.RegistryFileHelper;
import org.astrogrid.registry.server.query.RegistryService;
import org.astrogrid.registry.server.admin.RegistryAdminService;
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
         //NodeList regNL = registryDoc.getElementsByTagNameNS("vg","ManagedAuthority" );
         NodeList regNL = RegistryFileHelper.findNodeListFromXML("ManagedAuthority",registry.getDocumentElement());
         //Okay for some reason vg seems to pick up the ManagedAuthority.
         //Lets try to find it by the url namespace.
         /*
         if(regNL.getLength() == 0) {
            regNL = registryDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority" );
         }

         if(regNL.getLength() == 0) {
            regNL = registryDoc.getElementsByTagName("vg:ManagedAuthority" );
         }
         */

         
         //NodeList regNL = registryDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority");
         String selectQuery = "<query><selectionSequence>" +
              "<selection item='searchElements' itemOp='EQ' value='Resource'/>" +
              "<selectionOp op='$and$'/>";
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
         
         Document responseDoc = XQueryExecution.parseFullNodeQuery(doc);
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
         if(dateDom == null)
            return harvest(dateDom);
         NodeList nl = dateDom.getElementsByTagName("date_since");
         if(nl.getLength() == 0) {
            return harvest(dateDom);   
         }
         String updateVal = nl.item(0).getFirstChild().getNodeValue();
         
         //Probably should parse this with a date  and validat it is a date.
                 
         RegistryService rs = new RegistryService();         
         Document registryDoc = rs.loadRegistry(null);
        
         //NodeList regNL = registryDoc.getElementsByTagNameNS("vg","ManagedAuthority" );
         NodeList regNL = RegistryFileHelper.findNodeListFromXML("ManagedAuthority",registryDoc.getDocumentElement());         
         //Okay for some reason vg seems to pick up the ManagedAuthority.
         //Lets try to find it by the url namespace.
         /*
         if(regNL.getLength() == 0) {
            regNL = registryDoc.getElementsByTagNameNS("http://www.ivoa.net/xml/VORegistry/v0.2","ManagedAuthority" );
         }

         if(regNL.getLength() == 0) {
            regNL = registryDoc.getElementsByTagName("vg:ManagedAuthority" );
         }
         */
         
         String selectQuery = "<query><selectionSequence>" +
             "<selection item='searchElements' itemOp='EQ' value='Resource'/>" +
             "<selectionOp op='$and$'/>" + 
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
         Document responseDoc = XQueryExecution.parseFullNodeQuery(doc);
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
   public Document harvestResource(Document resources){

      //NodeList voList = resources.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","VODescription");
      NodeList voList = RegistryFileHelper.findNodeListFromXML("VoDescription",resources.getDocumentElement());
      //This next statement will go away with Castor.
      ArrayList al = new ArrayList();
      RegistryAdminService ras = new RegistryAdminService();

      XSLHelper xs = new XSLHelper();
      Document resourceChange = xs.transformDatabaseProcess((Node)resources.getDocumentElement());
      ras.updateNoCheck(resourceChange);
      
      for(int i = 0;i < al.size();i++) {
         beginHarvest(null,resourceChange);
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
   public Document harvestFromResource(Document resource) {
      RegistryAdminService ras = new RegistryAdminService();
      XSLHelper xs = new XSLHelper();
      Document resourceChange = xs.transformDatabaseProcess((Node)resource.getDocumentElement());
      
         
          //Now get the dateFrom element value as well.
      ras.update(resourceChange);             
      beginHarvest(null,resourceChange);
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
   public Document harvestAll(Document resources) {
         Document harvestedDoc = null;
         //This next statement will go away with Castor.            
         //NodeList nl = query.getElementsByTagNameNS("http://www.ivoa.net/xml/VOResource/v0.9","Identifier");
      if(resources != null && resources.getElementsByTagName("VODescription").getLength() > 0) {
         return harvestResource(resources);   
      }else {
         return harvestResource(harvest(null));   
      }
   }
         
private class HarvestThread extends Thread {
   
   private RegistryAdminService ras = null;
   private Document updateDoc = null;
   
   public HarvestThread(RegistryAdminService ras, Document updateDoc) {
      this.ras = ras;
      this.updateDoc = updateDoc;   
   }
   
   public void run() {
      ras.updateNoCheck(updateDoc);
   }
   
}
      
   public void beginHarvest(Date dt, Document resources) {
      String accessURL = null;
      String invocationType = null;
      Document doc = null;
      RegistryAdminService ras = new RegistryAdminService();      
      NodeList resourceList = RegistryFileHelper.findNodeListFromXML("Resource",resources.getDocumentElement());
      
      for(int i = 0; i < resourceList.getLength();i++) {
         accessURL = RegistryFileHelper.findValueFromXML("AccessURL",(Element)resourceList.item(i));
         invocationType = RegistryFileHelper.findValueFromXML("InvocationType",(Element)resourceList.item(i));
         if("WEBSERVICE".equals(invocationType)) {
            //call the service
            //remeber to look at the date
            if("?wsdl".indexOf(accessURL) == -1) {
               accessURL += "?wsdl";
            }
            WSDLBasicInformation wsdlBasic = null;
            try {
               wsdlBasic = WSDLInformation.getBasicInformationFromURL(accessURL);
            }catch(RegistryException re) {
               re.printStackTrace();   
            }
            if(wsdlBasic != null) {
               System.out.println("calling call obj with endpoint = " + (String)wsdlBasic.getEndPoint().values().iterator().next());
               Call callObj = getCall((String)wsdlBasic.getEndPoint().values().iterator().next());
               DocumentBuilder registryBuilder = null;
               try {
                  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                  dbf.setNamespaceAware(true);
                  registryBuilder = dbf.newDocumentBuilder();
                  doc = registryBuilder.newDocument();
                  Element root = null;
                  String interfaceMethod = "ListResources";
                  String nameSpaceURI = WSDLInformation.getNameSpaceFromBinding(accessURL,interfaceMethod);
                  root = doc.createElementNS(nameSpaceURI,interfaceMethod);
                  doc.appendChild(root);
                  System.out.println("Creating soap request for operation name = " + interfaceMethod + " with namespaceuri = " + nameSpaceURI);
                     //System.out.println("the doc webservice = " + DomHelper.DocumentToString(doc));
                  SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
                     //sbeRequest.setName("harvestAll");
                  sbeRequest.setName(interfaceMethod);
                  sbeRequest.setNamespaceURI(wsdlBasic.getTargetNameSpace());
                  Vector result = (Vector) callObj.invoke (new Object[] {sbeRequest});
                  SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
                  (new HarvestThread(ras,sbe.getAsDocument())).start();
               }catch(RemoteException re) {
                 //log error
                 re.printStackTrace();   
               }catch(ParserConfigurationException pce) {
                  pce.printStackTrace();   
               }catch(Exception e) {
                  e.printStackTrace();   
               }
            }
         }else if("WebBrowser".equals(invocationType)) {
            try {
               String ending = "";
               //might need to put some oai date stuff on the end.  This is unknown.
               System.out.println("inside the web broser");
            
               if(accessURL.indexOf("?") == -1) {
                  ending = "?verb=ListRecords"; //&from=" + date;   
               }else {
                  /*
                  if(accessURL.indexOf("verb") == -1)
                     ending = "&verb=ListRecords&from=" + date;
                  else
                     ending = "&from=" + date;
                  */
               }
            
            
               doc = DomHelper.newDocument(new URL(accessURL + ending));
               System.out.println("Okay got this far to reading the url doc = " + DomHelper.DocumentToString(doc));
               (new HarvestThread(ras,doc)).start();
               //ras.updateNoCheck(doc);
               //RegistryFileHelper.updateResources(doc,true,false);
               //RegistryFileHelper.writeRegistryFile();
            
               NodeList moreTokens = null;
               while( (moreTokens = doc.getElementsByTagName("resumptionToken")).getLength() > 0) {
                  Node nd = moreTokens.item(0);
                  if(accessURL.indexOf("?") != -1) {
                     accessURL = accessURL.substring(0,accessURL.indexOf("?"));
                  }
                  ending = "?verb=ListRecords&resumptionToken=" + nd.getFirstChild().getNodeValue();
                  System.out.println("the harvestcallregistry's accessurl for resumptionToken = " + accessURL + ending);
                  doc = DomHelper.newDocument(new URL(accessURL + ending));
                  (new HarvestThread(ras,doc)).start();
                  //System.out.println("in harvestcallregistry the harvestDoc2 = " + XMLUtils.DocumentToString(harvestDoc));
                  //ras.updateNoCheck(doc);
                  //RegistryFileHelper.updateResources(doc,true,false);
               }//while
            }catch(ParserConfigurationException pce) {
               pce.printStackTrace();
            }catch(SAXException sax) {
               sax.printStackTrace();   
            }catch(IOException ioe){
               ioe.printStackTrace();   
            }
         }//elseif
      }//for
   }//beginHarvest

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
   }//getCall
}
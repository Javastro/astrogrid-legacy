/*
 * First created on 25-Apr-2003
 *
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
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;
import org.astrogrid.registry.server.RegistryServerHelper;
import org.astrogrid.registry.server.query.RegistryService;
import org.astrogrid.registry.server.admin.RegistryAdminService;
import java.net.URL;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Vector;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.rpc.ServiceException;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;
import org.astrogrid.registry.RegistryException;

import org.astrogrid.registry.common.WSDLInformation;
import org.astrogrid.registry.common.WSDLBasicInformation;
import org.astrogrid.registry.common.XSLHelper;

import java.net.MalformedURLException;
import org.apache.axis.AxisFault;
import org.astrogrid.xmldb.eXist.server.QueryDBService;

/**
 *
 * The RegistryHarvestService class is the main web service class dealing with
 * harvesting from registries and returning information from a registry.
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryHarvestService {

   private static final Log log =
                          LogFactory.getLog(RegistryHarvestService.class);
   private static final String HARVEST_TEMPLATE_URL_PROPERTY =
                          "org.astrogrid.registry.harvest.template.url";

   public static Config conf = null;

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }
   }


  /**
    * Takes a Resource entry (a Registry type entry).  And performs a full replicate
    * or harvest from that registry, to populate this registry.  Usually there is only one
    * Registry Resource, but there might be more.
    *
    * @param query XML document object representing the query language used on the registry.
    * @return null (nothing is returned on this web service operation).
    * @author Kevin Benson
    */
   public Document harvestResource(Node resource,Date dt)  throws RegistryException, IOException {
      log.debug("start harvestResource");
      log.info("update harvestResource");


      boolean harvestEnabled = conf.getBoolean("registry.harvest.enabled",false);
      if(!harvestEnabled) {
          return null;
      }
      //RegistryAdminService ras = new RegistryAdminService();

      //Change this up to look at the Node make sure it is a REgistryType or a Web service
      //if not then throw an exception if so then try to call it.

      //Okay this is just a small xsl sheet to make sure the xml is formatted in
      //a nice consistent way.  Because currently the schema espcially version 0.9
      //allows the user to put the xml in a few different ways.

      //Okay update this one resource entry.
      //ras.updateResource(resource);
      
      NodeList nl = null;
      if(Node.DOCUMENT_NODE == resource.getNodeType()) {
          nl = ((Document)resource).getElementsByTagNameNS("*","Resource");
      }
      else if(Node.ELEMENT_NODE == resource.getNodeType()) {
          nl = ((Element)resource).getElementsByTagNameNS("*","Resource");
      }
      for(int i = 0; i < nl.getLength();i++) {
          Element elem = (Element) nl.item(i);
          /*
          if(dt != null) {
              //Document statDoc = qdb.getResource("statv"+versionNumber,RegistryServerHelper.getIdentifier(elem));
              //String dateString = DomHelper.getNodeTextValue(statDoc,"StatsDateMillis");
              //Date dt = new Date(Long.parseLong(dateString));
              //harvestResource(elem,dt);
              beginHarvest(elem,dt);
          }else {
              //harvestResource(elem,null);
              beginHarvest(elem,null);
          }//else
          */   
          beginHarvest(elem,null);          
      }
      log.info("exiting harvestResource");
      log.debug("end harvestResource");
      return null;
   }

   /**
       * Will start a harvest of all the Registries known to this registry.
       *
       * @param resources XML document object representing the query language used on the registry.
       * @return XML docuemnt object representing the result of the query.
       * @author Kevin Benson
       */
   public void harvestAll(boolean onlyRegistries, boolean useDates) throws RegistryException  {
      log.debug("start harvestAll");
      Document harvestDoc = null;
      String xqlQuery = null;
      String ident = null;

      boolean harvestEnabled = conf.getBoolean("registry.harvest.enabled",false);
      if(!harvestEnabled) {
          return;
      }


      String versionNumber = null;
      //String collectionName = "astrogridv" + versionNumber;
      String collectionName = "";
      QueryDBService qdb = new QueryDBService();
      //instantiate the Admin service that contains the update methods.
      RegistryAdminService ras = new RegistryAdminService();
      Document tempDoc = null;
      try {
          tempDoc = DomHelper.newDocument();
          if(onlyRegistries) {
             //query for all the Registry types which should be all of them with an xsi:type="RegistryType"
             //xqlQuery = "declare namespace vr = \"http://www.ivoa.net/xml/VOResource/v0.9\"; //vr:Resource[@xsi:type='RegistryType']";
             xqlQuery = "//*:Resource[@xsi:type='RegistryType' or @xsi:type='Registry']";
             System.out.println("hello the xqlQuery = " + xqlQuery);
             log.info("the xqlQuery = " + xqlQuery);
             harvestDoc = qdb.runQuery(collectionName,xqlQuery);
             //System.out.println("The harvestDoc = " + DomHelper.DocumentToString(harvestDoc));
             if(harvestDoc != null && tempDoc != null) {
                 tempDoc.appendChild(
                         tempDoc.importNode(harvestDoc.getDocumentElement(),true));
                 ras.updateNoCheck(tempDoc);
             }               
             //log.info("try just the Resource");
             NodeList nl = harvestDoc.getElementsByTagNameNS("*","Resource");
             log.info("Harvest All found this number of resources = " + nl.getLength());
             for(int i = 0; i < nl.getLength();i++) {
               Element elem = (Element) nl.item(i);
               versionNumber = RegistryServerHelper.getRegistryVersionFromNode(elem);
               versionNumber = versionNumber.replace('.','_');               
               if(useDates) {
                  Document statDoc = qdb.getResource("statv"+versionNumber,RegistryServerHelper.getIdentifier(elem));
                  String dateString = DomHelper.getNodeTextValue(statDoc,"StatsDateMillis");
                  
                  Date dt = null;
                  if(dateString != null && dateString.trim().length() > 0) {
                      dt = new Date(Long.parseLong(dateString));
                  }
                  //harvestResource(elem,dt);
                  beginHarvest(elem,dt);
               }else {
                //harvestResource(elem,null);
                beginHarvest(elem,null);
               }//else
             }//for
          }else {
            //query all Registry Types for Webbrowser or WebService interface
             //xqlQuery = "declare namespace vr = \"http://www.ivoa.net/xml/VOResource/v0.9\"; //vr:Resource[vr:Interface/vr:Invocation='WebBrowser' or vr:Interface/vr:Invocation='WebService']";
             xqlQuery = "//*:Resource[*:/Interface/*:AccessURL]";
             harvestDoc = qdb.runQuery(collectionName,xqlQuery);
             if(harvestDoc != null) {
                 tempDoc.appendChild(
                         tempDoc.importNode(harvestDoc.getDocumentElement(),true));
                 ras.updateNoCheck(tempDoc);
             }             
             //NodeList nl = DomHelper.getNodeListTags(harvestDoc,"Resource","vr");
             NodeList nl = harvestDoc.getElementsByTagNameNS("*","Resource");
             for(int i = 0; i < nl.getLength();i++) {
               Element elem = (Element) nl.item(i);
               versionNumber = RegistryServerHelper.getRegistryVersionFromNode(elem);
               versionNumber = versionNumber.replace('.','_');
               if(useDates) {
                  Document statDoc = qdb.getResource("statv"+versionNumber,RegistryServerHelper.getIdentifier(elem));
                  String dateString = DomHelper.getNodeTextValue(statDoc,"StatsDateMillis");
                  Date dt = null;
                  if(dateString != null && dateString.trim().length() > 0) {
                      dt = new Date(Long.parseLong(dateString));
                  }
                  //harvestResource(elem,dt);
                  beginHarvest(elem,dt);
               }else {
               	//harvestResource(elem,null);
                beginHarvest(elem,null);
               }//else
             }//for
          }

      }catch(ParserConfigurationException pce) {
      	throw new RegistryException(pce);
      }catch(IOException ioe) {
      	throw new RegistryException(ioe);
      }catch(SAXException sax) {
         throw new RegistryException(sax);
      }
   }

/**
 * Small Thread class to update the registry with a particular number
 * of Resources.  This class inherits from the Thread class, so
 * the harvesting can continue to keep harvesting more Resources.
 * Some Registries require paging through the Resources hence, the
 * multithreading helps performance.
 *
 *
 */
private class HarvestThread extends Thread {

   private RegistryAdminService ras = null;
   private Document updateDoc = null;

   /**
    * HarvestThread class constructor.
    * @param ras The RegistryAdminService class which does updates of Resources
    * @param updateDoc a set of one or more Resources.
    */
   public HarvestThread(RegistryAdminService ras, Node updateNode) throws RegistryException {
      this.ras = ras;
      if(updateNode instanceof Element) {
        try {
      	updateDoc = DomHelper.newDocument();
         updateDoc.appendChild(updateDoc.importNode(updateNode, true));
        }catch(ParserConfigurationException pce) {
        	throw new RegistryException(pce);
        }
      }else if(updateNode instanceof Document) {
        this.updateDoc = (Document)updateNode;
      }
   }

   /**
    * Begin the update, Calls updateNoCheck from RegistryAdminService, because
    * it is assumed the Rewsources have been checked and valid and require no
    * special checking.
    */

   public void run() {
	  Element el = updateDoc.getDocumentElement();
      try {
         ras.updateNoCheck(updateDoc);
  //       ras.Update(updateDoc);
  //    }catch(MalformedURLException mue) {
  //       mue.printStackTrace();
      }catch(IOException ioe) {
         ioe.printStackTrace();
      }
   }

}

   /**
    * This is the main method which uses the HarvestThread class to begin
    * harvesting and updates.  This method will interrogate Resource entries
    * and see how to call the Resources via the AccessURL and determine if
    * it is a WebService or WebBrowser.  Then makes the appropriately call
    * to the web service or browser grabbing there Resources and update into
    * this Registry.
    *
    * @param dt An optional date used to harvest from a particular date
    * @param resources Set of Resources to harvest on, normally a Registry Resource.
    */
   public void beginHarvest(Node resource, Date dt)  throws RegistryException, IOException  {
      log.debug("start beginHarvest");
      log.info("entered beginharvest");
      String accessURL = null;
      String invocationType = null;
      boolean isRegistryType;
      Document doc = null;
      NodeList nl = null;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

      //instantiate the Admin service that contains the update methods.
      RegistryAdminService ras = new RegistryAdminService();

      System.out.println(resource.getNodeName() + " " + resource.getNodeValue());

      NamedNodeMap attributes = resource.getAttributes();
      // Debug (BKM)
      for(int i=0; i<attributes.getLength(); i++) {
		  System.out.println("Attribute " + i + " Name " + attributes.item(i).getNodeName() +
		                      " value " + attributes.item(i).getNodeValue());
      }

      //get the accessurl and invocation type.
      //invocationtype is either WebService or WebBrowser.
      Node typeAttribute = resource.getAttributes().getNamedItem("xsi:type");
      isRegistryType = (typeAttribute != null) &&
                       (typeAttribute.getNodeValue().indexOf("Registry") >= 0);
 //   System.out.println("RegistryType attribute =" + isRegistryType);

      nl = ((Element) resource).getElementsByTagNameNS("*","AccessURL");
      if(nl.getLength() == 0) {
          nl = ((Element) resource).getElementsByTagNameNS("*","accessURL");
      }
      accessURL = nl.item(0).getFirstChild().getNodeValue();

      nl = ((Element) resource).getElementsByTagNameNS("*","Invocation");
      if(nl.getLength() == 0) {
          //Need to look for interface here.
        //nl = ((Element) resource).getElementsByTagName("vr:Invocation");
          nl = ((Element) resource).getElementsByTagNameNS("*","interface");
          typeAttribute = resource.getAttributes().getNamedItem("xsi:type");
          invocationType = typeAttribute.getNodeValue();
      } else {
          invocationType = nl.item(0).getFirstChild().getNodeValue();
      }

//    accessURL = DomHelper.getNodeTextValue((Element)resourceList.item(i),"AccessURL","vr");
//    invocationType = DomHelper.getNodeTextValue((Element)resourceList.item(i),"Invocation","vr");
      log.info("The access URL = " + accessURL + " invocationType = " + invocationType);
//      System.out.println("The access URL = " + accessURL + " invocationType = " + invocationType);


      if("WebService".equals(invocationType)) {
         //call the service
         //remember to look at the date
         Element childElem = null;
         Element root = null;

         if("?wsdl".indexOf(accessURL) == -1) {
            accessURL += "?wsdl";
         }
         //Read in the wsdl for the endpoint and namespace
         WSDLBasicInformation wsdlBasic = null;
         try {
            wsdlBasic = WSDLInformation.getBasicInformationFromURL(accessURL);
         } catch(RegistryException re) {
            re.printStackTrace();
            log.error(re);
         }
         if(wsdlBasic != null) {
            log.info("calling call obj with endpoint = " +
                     (String)wsdlBasic.getEndPoint().values().iterator().next());
            //create a call object
            Call callObj = getCall((String)wsdlBasic.getEndPoint().
                           values().iterator().next());

            try {
               doc = DomHelper.newDocument();
               //set the operation name/interface method to ListResources
               String interfaceMethod = "getMetaData";
               if(isRegistryType) interfaceMethod = "ListRecords";
               String nameSpaceURI = WSDLInformation.
                                     getNameSpaceFromBinding(
								        accessURL,interfaceMethod);

               root = doc.createElementNS(nameSpaceURI,interfaceMethod);
               if(dt != null) {
                  childElem = doc.createElement("from");
                  childElem.appendChild(doc.createTextNode(sdf.format(dt)));
                  root.appendChild(childElem);
               }
               doc.appendChild(root);
               log.info("Creating soap request for operation name = " +
                         interfaceMethod + " with namespaceuri = " +
                         nameSpaceURI);

               SOAPBodyElement sbeRequest = new SOAPBodyElement(
                                                doc.getDocumentElement());
               //sbeRequest.setName("harvestAll");
               sbeRequest.setName(interfaceMethod);
               sbeRequest.setNamespaceURI(wsdlBasic.getTargetNameSpace());
               //invoke the web service call
               log.info("Calling invoke on service");
               Vector result = (Vector) callObj.invoke
                                        (new Object[] {sbeRequest});
               //Take the results and harvest.
               if(result.size() > 0) {
                   SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
                   Document soapDoc = sbe.getAsDocument();
                   log.info("SOAPDOC RETURNED = " + DomHelper.DocumentToString(soapDoc));
                   (new HarvestThread(ras,soapDoc.getDocumentElement())).start();
                   if(isRegistryType) {
                      nl = DomHelper.getNodeListTags(soapDoc,"resumptionToken");
                      while(nl.getLength() > 0) {
                         Document resumeDoc = DomHelper.newDocument();
                         root = doc.createElementNS(nameSpaceURI,"ResumeListRecords");
                          childElem = doc.createElement("resumptionToken");
                          childElem.appendChild(doc.createTextNode(nl.item(0).getFirstChild().getNodeValue()));
                          sbeRequest = new SOAPBodyElement(resumeDoc.getDocumentElement());
                          sbeRequest.setName("ResumeListRecords");
                          sbeRequest.setNamespaceURI(wsdlBasic.getTargetNameSpace());
                          //invoke the web service call
                          result = (Vector) callObj.invoke
    									    (new Object[] {sbeRequest});
                          soapDoc = sbe.getAsDocument();
                          (new HarvestThread(ras,soapDoc.getDocumentElement().cloneNode(true))).start();
                           nl = DomHelper.getNodeListTags(soapDoc,"resumptionToken");
                      }//while
                   }//if
               }//if
            } catch(RemoteException re) {
                //log error
                re.printStackTrace();
                log.error(re);
		    }
            catch(ParserConfigurationException pce) {
                pce.printStackTrace();
                log.error(pce);
            }
            catch(Exception e) {
                e.printStackTrace();
                log.error(e);
            }
         }
      }else if("WebBrowser".equals(invocationType)) {
         //its a web browser so assume for oai.
         try {
            String ending = "";
            //might need to put some oai date stuff on the end.  This is
            //unknown.
            log.info("inside the web browser");

            if(accessURL.indexOf("?") == -1) {
               ending = "?verb=ListRecords&metadataPrefix=ivo_vor"; //&from=" + date;
               if(dt != null) {
                  ending += "&from=" + sdf.format(dt);
               }
            }

            log.info("Grabbing Document from this url = " + accessURL + ending);
            doc = DomHelper.newDocument(new URL(accessURL + ending));
            log.info("Okay got this far to reading the url doc = " +
                      DomHelper.DocumentToString(doc));
            (new HarvestThread(ras,doc.getDocumentElement().cloneNode(true))).start();
            NodeList moreTokens = null;
            //log.info("resumptionToken length = " +
            //         doc.getElementsByTagName("resumptionToken").
            //         getLength());
            //if there are more paging(next) then keep calling them.
            while( (moreTokens = doc.getElementsByTagName("resumptionToken")).
                                     getLength() > 0) {
               Node nd = moreTokens.item(0);
               if(accessURL.indexOf("?") != -1) {
                  accessURL = accessURL.substring(0,accessURL.indexOf("?"));
               }
               ending = "?verb=ListRecords&resumptionToken=" +
                         nd.getFirstChild().getNodeValue();
               log.info(
               "the harvestcallregistry's accessurl inside the token calls = " +
                          accessURL + ending);
               doc = DomHelper.newDocument(new URL(accessURL + ending));
               log.info("INSIDE THE MORETOKENS = " +
                        DomHelper.DocumentToString(doc) +
                        " resumption token length = "   +
                        doc.getElementsByTagName("resumptionToken").
                            getLength() );
               (new HarvestThread(ras,doc)).start();
            }//while
         }catch(ParserConfigurationException pce) {
            pce.printStackTrace();
            log.error(pce);
         }catch(SAXException sax) {
            sax.printStackTrace();
            log.error(sax);
         }catch(IOException ioe){
            ioe.printStackTrace();
            log.error(ioe);
         }
      }//elseif
      log.info("exiting beginHarvest");
      log.debug("end beginHarvest");
   }//beginHarvest

   /**
    * Method to establish a Service and a Call to the server side web service.
    * @return Call object which has the necessary properties set for an Axis message style.
    * @throws Exception
    * @author Kevin Benson
    */
   private Call getCall(String endPoint) {
      log.debug("start getCall");
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
         log.error(se);
         _call = null;
      }finally {
         log.debug("end getCall");
      }
      return _call;
   }//getCall
}
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
import org.xml.sax.InputSource;
import org.astrogrid.registry.server.RegistryServerHelper;
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
    * Queries for all Resource entries managed by this registry.
    * And returns all of the Resource entries.  The use of this method is for all other
    * registries who wish to replicate this registry.
    *
    * @param registry This parameter is null.  Kicks off returning everything managed by this registry.
    * @return XML docuemnt object representing the result of the query.
    * @author Kevin Benson
    */
   public Document harvest(Document registry) throws AxisFault {
      log.debug("start harvest");
      try {
         RegistryService rs = new RegistryService();
         Document registryDoc = rs.loadRegistry(null);

         //Grab all the authority id's that is managed by this registry.
         NodeList regNL = DomHelper.getNodeListTags(registryDoc,"ManagedAuthority","vg"); 
         //Construct the query in a string.
         String selectQuery = "<query><selectionSequence>" +
              "<selection item='searchElements' itemOp='EQ' value='Resource'/>" +
              "<selectionOp op='$and$'/>";
         if(regNL.getLength() > 0) {
            selectQuery +=
            "<selection item='Identifer/AuthorityID' itemOp='EQ' value='" +
            regNL.item(0).getFirstChild().getNodeValue() + "'/>";
         }
         for(int i = 1;i < regNL.getLength();i++) {
            selectQuery += "<selectionOp op='OR'/>" +
            "<selection item='Identifier/AuthorityID' itemOp='EQ' value='" +
            regNL.item(i).getFirstChild().getNodeValue() + "'/>";
         }
         selectQuery += "</selectionSequence></query>";

         Reader reader2 = new StringReader(selectQuery);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().
                                                  newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);

         //performs the query and return the results.
         Document responseDoc = null;//XQueryExecution.parseFullNodeQuery(doc);
         return responseDoc;
      }catch(Exception e) {
         e.printStackTrace();
         log.error(e);
      }
      log.debug("end harvest");
      return null;
  }

  /**
    * Queries for all Resource entries managed by this registry based around a date.
    *
    * And returns all of the Resource entries.  The use of this method is for all other
    * registries who wish to harvest the resource entries based around a date.  This will
    * more commonly be used as other Registries will harvest daily or weekly.
    *
    * If no date is given then the results are the same as calling harvest method.
    *
    * @param registry This parameter is null.  Kicks off returning everything managed by this registry.
    * @return XML docuemnt object representing the result of the query.
    * @author Kevin Benson
    */
   public Document harvestFrom(Document dateDom)  throws AxisFault  {
      log.debug("start harvestFrom");
      try {
         //if there is no date or xml tag at all then call harvest
         if(dateDom == null)
            return harvest(dateDom);

         //grab the date_since tag.
         NodeList nl = dateDom.getElementsByTagName("date_since");
         //if there is no date_since element then replicate/harvest
         if(nl.getLength() == 0) {
            return harvest(dateDom);
         }

         //get the date value as a string.
         String updateVal = nl.item(0).getFirstChild().getNodeValue();

         //Probably should parse this with a date  and validat it is a date.

         RegistryService rs = new RegistryService();
         Document registryDoc = rs.loadRegistry(null);

         //NodeList regNL = registryDoc.getElementsByTagNameNS("vg",
         //                                               "ManagedAuthority" );
         NodeList regNL = DomHelper.getNodeListTags(registryDoc,"ManagedAuthority","vg");

         //This query for the moment is the same as harvest.  When the statistical
         //data is added which is coming soon, then this will be changed to get the
         //identifiers first of all the resources that have recently changed.
         String selectQuery = "<query><selectionSequence>" +
             "<selection item='searchElements' itemOp='EQ' value='Resource'/>"+
             "<selectionOp op='$and$'/>" +
             "<selection item='@updated' itemOp='AFTER' value='" + updateVal
                + "'/>";;
         if(regNL.getLength() > 0) {
            selectQuery +=
            "<selection item='AuthorityID' itemOp='EQ' value='" +
            regNL.item(0).getFirstChild().getNodeValue() + "'/>";
         }
         for(int i = 1;i < regNL.getLength();i++) {
            selectQuery += "<selectionOp op='OR'/>" +
            "<selection item='AuthorityID' itemOp='EQ' value='" +
            regNL.item(i).getFirstChild().getNodeValue() + "'/>";
         }
         selectQuery += "</selectionSequence></query>";
         Reader reader2 = new StringReader(selectQuery);
         InputSource inputSource = new InputSource(reader2);
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().
                                                  newDocumentBuilder();
         Document doc = registryBuilder.parse(inputSource);
         //perform the query and return it.
         Document responseDoc = null;//XQueryExecution.parseFullNodeQuery(doc);
         return responseDoc;
      }catch(Exception e) {
        e.printStackTrace();
        log.error(e);
      }
      return null;
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
   public Document harvestResource(Node resource,Date dt)  throws AxisFault {
      log.debug("start harvestResource");
      log.info("update harvestResource");
      
      /*
      RegistryAdminService ras = new RegistryAdminService();

      Change this up to look at the Node make sure it is a REgistryType or a Web service
      if not then throw an exception if so then try to call it.

      //Okay this is just a small xsl sheet to make sure the xml is formatted in
      //a nice consistent way.  Because currently the schema espcially version 0.9
      //allows the user to put the xml in a few different ways.
      XSLHelper xs = new XSLHelper();
      Document resourceChange = xs.transformDatabaseProcess
                                ((Node)resources.getDocumentElement());

      //Okay update this one resource entry.
      Node harvestCopy = resourceChange.cloneNode(true);
      ras.updateResource(resourceChange);
      log.info("THE RESOURCECHANGE IN HARVESTRESOURCE1 = " +
               DomHelper.DocumentToString((Document)harvestCopy));
      //Start a full (replicate) harvest on this registry.
      try {
         beginHarvest(null,(Document)harvestCopy);
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);   
      }
      */
      

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
   public Document harvestAll(boolean onlyRegistries, boolean useDates)  throws AxisFault  {
      log.debug("start harvestAll");
      Document harvestedDoc = null;
      //This next statement will go away with Castor.
      //NodeList nl = query.getElementsByTagNameNS(
      //              "http://www.ivoa.net/xml/VOResource/v0.9","Identifier");
      /*
      if(resources != null && resources.getElementsByTagName("VODescription").
                                        getLength() > 0) {
         return harvestResource(resources);
      } else {
         return harvestResource(harvest(null));
      }
      */
      if(onlyRegistries) {
         //query for all the Registry types which should be all of them with an xsi:type="RegistryType"
         
         /*
          NodeList nl = DomHelper.getNodeList(doc,"Resource","vr");
          for(int i = 0; i < nl.getLenth();i++) {
            Element elem = (Element) nl.item(i);
            if(useDates) {
               // now look up the stats data for the last time it was harvestes.
               //this should be fairly easy we can get the identifier and the stats file should be that
               //get identifier name.  
               //once you get the stats Document dom get the lastharvestdate element. 
               //harvestResource(elem,lastharvestdate); 
            }
            harvestResource(elem);
          }
          */
      }else {
        //query for all RegistryTypes or WebService interface
        /*
         NodeList nl = DomHelper.getNodeList(doc,"Resource","vr");
         for(int i = 0; i < nl.getLenth();i++) {
           Element elem = (Element) nl.item(i);
           if(useDates) {
              // now look up the stats data for the last time it was harvestes.
              //this should be fairly easy we can get the identifier and the stats file should be that
              //get identifier name.  
              //once you get the stats Document dom get the lastharvestdate element. 
              //harvestResource(elem,lastharvestdate); 
           }
           harvestResource(elem);
         }
         */
      }
      return null;
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
   public HarvestThread(RegistryAdminService ras, Document updateDoc) {
      this.ras = ras;
      this.updateDoc = updateDoc;
   }

   /**
    * Begin the update, Calls updateNoCheck from RegistryAdminService, because
    * it is assumed the Rewsources have been checked and valid and require no
    * special checking.
    */
    
   public void run() {
      try {
         ras.updateNoCheck(updateDoc);
      }catch(MalformedURLException mue) {
       mue.printStackTrace();   
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
   public void beginHarvest(Date dt, Document resources)  throws AxisFault, IOException  {
      log.debug("start beginHarvest");
      log.info("entered beginharvest");
      String accessURL = null;
      String invocationType = null;
      Document doc = null;
      //instantiate the Admin service that has update methods.
      RegistryAdminService ras = new RegistryAdminService();
      //log.info("THE FULL RESOURCES IN BEGINHARVEST = " +
      //         DomHelper.DocumentToString(resources));
      NodeList resourceList = DomHelper.getNodeListTags(resources,"Resource","vr");
      //log.info("the getLength = " + resourceList.getLength());
      //go through all the resources
      for(int i = 0; i < resourceList.getLength();i++) {
         //get the accessurl and invocation type.
         //invocationtype is either WEbService or WebBrowser.
            accessURL = DomHelper.getNodeTextValue((Element)resourceList.item(i),"AccessURL","vr");
            invocationType = DomHelper.getNodeTextValue((Element)resourceList.item(i),"Invocation","vr");
         log.info("The access URL = " + accessURL + " invocationType = " +
                  invocationType);
         if("WEBSERVICE".equals(invocationType)) {
            //call the service
            //remeber to look at the date

            if("?wsdl".indexOf(accessURL) == -1) {
               accessURL += "?wsdl";
            }
            //Read in the wsdl for the endpoint and namespace
            WSDLBasicInformation wsdlBasic = null;
            try {
               wsdlBasic = WSDLInformation.
                           getBasicInformationFromURL(accessURL);
            }catch(RegistryException re) {
               re.printStackTrace();
               log.error(re);
            }
            if(wsdlBasic != null) {
               log.info("calling call obj with endpoint = " +
                   (String)wsdlBasic.getEndPoint().values().iterator().next());
               //create a call object
               Call callObj = getCall((String)wsdlBasic.getEndPoint().
                              values().iterator().next());
               DocumentBuilder registryBuilder = null;
               try {
                  DocumentBuilderFactory dbf = DocumentBuilderFactory.
                                               newInstance();
                  dbf.setNamespaceAware(true);
                  registryBuilder = dbf.newDocumentBuilder();
                  doc = registryBuilder.newDocument();
                  Element root = null;
                  //set the operation name/interface method to ListResources
                  String interfaceMethod = "ListResources";
                  String nameSpaceURI = WSDLInformation.
                            getNameSpaceFromBinding(accessURL,interfaceMethod);

                  root = doc.createElementNS(nameSpaceURI,interfaceMethod);
                  doc.appendChild(root);
                  log.info("Creating soap request for operation name = " +
                            interfaceMethod + " with namespaceuri = " +
                            nameSpaceURI);
                  //log.info("the doc webservice = " + DomHelper.
                  //                                   DocumentToString(doc));
                  //
                  SOAPBodyElement sbeRequest = new SOAPBodyElement(
                                                   doc.getDocumentElement());
                  //sbeRequest.setName("harvestAll");
                  sbeRequest.setName(interfaceMethod);
                  sbeRequest.setNamespaceURI(wsdlBasic.getTargetNameSpace());
                  //invoke the web service call
                  Vector result = (Vector) callObj.invoke
                                           (new Object[] {sbeRequest});
                  //Take the results and harvest.
                  SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
                  (new HarvestThread(ras,sbe.getAsDocument())).start();
               } catch(RemoteException re) {
                 //log error
                 re.printStackTrace();
                 log.error(re);
               }catch(ParserConfigurationException pce) {
                  pce.printStackTrace();
                 log.error(pce);
               }catch(Exception e) {
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
                  ending = "?verb=ListRecords"; //&from=" + date;
               } else {
                  /*
                  if(accessURL.indexOf("verb") == -1)
                     ending = "&verb=ListRecords&from=" + date;
                  else
                     ending = "&from=" + date;
                  */
               }

               log.info("Grabbing Document from this url = " +
                        accessURL + ending);
               doc = DomHelper.newDocument(new URL(accessURL + ending));
               log.info("Okay got this far to reading the url doc = " +
                        DomHelper.DocumentToString(doc));
               (new HarvestThread(ras,doc)).start();
               NodeList moreTokens = null;
               //log.info("resumptionToken length = " +
               //         doc.getElementsByTagName("resumptionToken").
               //         getLength());
               //if there are more paging(next) then keep calling them.
               while( (moreTokens = doc.getElementsByTagName(
                                        "resumptionToken")).getLength() > 0) {
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
      }//for
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
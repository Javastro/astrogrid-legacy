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
//import org.astrogrid.registry.server.RegistryServerHelper;
import org.astrogrid.registry.common.RegistryDOMHelper;
import org.astrogrid.registry.server.QueryHelper;
import org.astrogrid.registry.server.admin.RegistryAdminService;
import org.astrogrid.registry.server.query.RegistryQueryService;
import java.net.URL;
import java.io.Reader;
import java.io.StringReader;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Vector;
import java.util.Hashtable;

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
//import org.apache.axis.AxisFault;

import org.astrogrid.xmldb.client.XMLDBFactory;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;


/**
 *
 * RegistryHarvestService is no longer a web service class, but still posses the 
 * harvesting mechanism that is used by server side servlets which uses
 * automatic harvest mechanism and manual harvest by the user.
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
      onlyRegistries = true;
      Date dt = null;
      boolean harvestEnabled = conf.getBoolean("reg.amend.harvest",false);
      if(!harvestEnabled) {
          return;
      }

      String versionNumber = null;
      //String collectionName = "astrogridv" + versionNumber;
      String collectionName = "";
      //QueryDBService qdb = new QueryDBService();
      XMLDBFactory xdb = new XMLDBFactory();
      Collection coll = null;
      String authorityID = conf.getString("reg.amend.authorityid");
      String tempIdent = null;
      String resKey = null;
      
      //instantiate the Admin service that contains the update methods.c
      RegistryAdminService ras = new RegistryAdminService();
          if(onlyRegistries) {
             //query for all the Registry types which should be all of them with an xsi:type="RegistryType"
             //xqlQuery = "declare namespace vr = \"http://www.ivoa.net/xml/VOResource/v0.9\"; //vr:Resource[@xsi:type='RegistryType']";
             //System.out.println("The harvestDoc = " + DomHelper.DocumentToString(harvestDoc));
             ArrayList versions = null;
             RegistryQueryService rqs = new RegistryQueryService();
             try {
                 versions = rqs.getAstrogridVersions();
             }catch(XMLDBException xdbe) {
                 log.error(xdbe);
                 throw new RegistryException("Could not find any Astrogrid Versions in the Registry");
             }
             
             for(int k = 0;k < versions.size();k++) {
                 log.debug("Start processing Registry Types from version = " + (String)versions.get(k));
                 try {
                     harvestDoc = rqs.getRegistriesQuery((String)versions.get(k));
                 }catch(Exception e) {
                     log.error(e);
                     harvestDoc = null;
                 }
                 if(harvestDoc != null) {
                     NodeList nl = harvestDoc.getElementsByTagNameNS("*","Resource");
                     log.debug("Harvest All found this number of resources = " + nl.getLength());
                     for(int i = 0; i < nl.getLength();i++) {
                       Element elem = (Element) nl.item(i);
                       versionNumber = RegistryDOMHelper.getRegistryVersionFromNode(elem);
                   
                       if(useDates) {
                          String dateString = null;
                          try {
                              coll = xdb.openCollection("statv" + versionNumber.replace('.','_'));
                              tempIdent = RegistryDOMHelper.getAuthorityID(elem);
                              resKey = RegistryDOMHelper.getResourceKey(elem);
                              if(resKey != null && resKey.trim().length() > 0) tempIdent += "/" + resKey;                              
                              XMLResource xmlr = (XMLResource)xdb.getResource(coll,tempIdent.replaceAll("[^\\w*]","_") + ".xml");
                              if(xmlr != null) {
                                  Document statDoc = DomHelper.newDocument(xmlr.getContent().toString());
                                  dateString = DomHelper.getNodeTextValue(statDoc,"StatsDateMillis");
                              }
                          }catch(XMLDBException xdbe) {
                              log.error(xdbe);
                              log.info("xmldb exception thrown when trying to obtain stat and date information. Continue on and do a full harvest of the Registry Type");
                              dateString = null;
                              //throw new RegistryException(xdbe);
                          }catch(IOException ioe) {
                              log.error(ioe);
                              log.info("xmldb exception thrown when trying to obtain stat and date information. Continue on and do a full harvest of the Registry Type");
                              dateString = null;
                              //throw new RegistryException(ioe);
                          } catch(ParserConfigurationException pce) {
                              log.error(pce);
                              dateString = null;
                          } catch(SAXException sax) {
                              log.error(sax);
                              dateString = null;
                          }finally {
                              try {
                                  xdb.closeCollection(coll);
                              }catch(XMLDBException xmldb) {
                                  log.error(xmldb);
                              }
                          }//finally
                          if(dateString != null && dateString.trim().length() > 0) {
                              dt = new Date(Long.parseLong(dateString));
                          }//if
                       }//if
                       try {
                           ident = RegistryDOMHelper.getAuthorityID( elem );                           
                           if(authorityID.equals(ident)) {
                               log.debug("This is our main Registry type do not do a harvest of it");
                           } else {                           
                               beginHarvest(elem,dt,(String)versions.get(k));                  
                               ras.updateNoCheck(DomHelper.newDocument(DomHelper.ElementToString(elem)),(String)versions.get(k));
                           }
                       }catch(RegistryException re) {
                           log.error(re);
                           log.info("still continue to the next Registry type if there are any");
                       }catch(IOException ioe) {
                           log.error(ioe);
                           log.info("still continue to the next Registry type if there are any");
                       }catch(XMLDBException xmldbe) {
                           log.error(xmldbe);
                       }catch(SAXException sax) {
                           log.error(sax);
                       }catch(ParserConfigurationException pce) {
                           log.error(pce);
                       }
                     }//for
                 }//if
             }//for
          }//if
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
   public void beginHarvest(Node resource, Date dt, String version)  throws RegistryException, IOException  {
      log.debug("entered beginharvest");
      int failureCount = 0;
      boolean resumptionSuccess = false;      
      String accessURL = null;
      String invocationType = null;
      boolean isRegistryType;
      Document doc = null;
      NodeList nl = null;
      String soapActionURI = null;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      int threadCount = 0;
      

      //instantiate the Admin service that contains the update methods.
      RegistryAdminService ras = new RegistryAdminService();

      //System.out.println(resource.getNodeName() + " " + resource.getNodeValue());

      NamedNodeMap attributes = resource.getAttributes();

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
      if(nl.getLength() == 0) {
          log.error("Error did not find a AccessURL");
          throw new RegistryException("No accessURL found");
      }
      if(!nl.item(0).hasChildNodes()) {
          log.error("Error did not find any text to the accessURL");
          throw new RegistryException("No text found for the accessURL");          
      }
      accessURL = nl.item(0).getFirstChild().getNodeValue();

      nl = ((Element) resource).getElementsByTagNameNS("*","Invocation");
      if(nl.getLength() == 0) {
          //Need to look for interface here.
          nl = ((Element) resource).getElementsByTagNameNS("*","interface");
          if(nl.getLength() > 0) { 
              typeAttribute = ((Element)nl.item(0)).getAttributes().getNamedItem("xsi:type");
              invocationType = typeAttribute.getNodeValue();
          }
      } else {          
          invocationType = nl.item(0).getFirstChild().getNodeValue();
      }
      
      if(accessURL.indexOf("?wsdl") != -1) {
          accessURL = accessURL.substring(0,accessURL.indexOf("?wsdl"));
      }

//    accessURL = DomHelper.getNodeTextValue((Element)resourceList.item(i),"AccessURL","vr");
//    invocationType = DomHelper.getNodeTextValue((Element)resourceList.item(i),"Invocation","vr");
      log.debug("The access URL = " + accessURL + " invocationType = " + invocationType);
//    System.out.println("The access URL = " + accessURL + " invocationType = " + invocationType);
      
      if(invocationType != null && invocationType.endsWith("WebService")) {
         //call the service
         //remember to look at the date
         Element childElem = null;
         Element root = null;
         
         /*
         if("?wsdl".indexOf(accessURL) == -1) {
            accessURL += "?wsdl";
         }
         */
         //Read in the wsdl for the endpoint and namespace
         /*
         WSDLBasicInformation wsdlBasic = null;
         try {
            wsdlBasic = WSDLInformation.getBasicInformationFromURL(accessURL);
         } catch(RegistryException re) {
            re.printStackTrace();
            log.error(re);
         }
         */
//         if(wsdlBasic != null) {
            //create a call object
            Call callObj = getCall(accessURL);

            try {
               doc = DomHelper.newDocument();
               //set the operation name/interface method to ListResources
               String interfaceMethod = "ListRecords";
               //if(isRegistryType) interfaceMethod = "ListRecords";
               /*
               String nameSpaceURI = WSDLInformation.
                                     getNameSpaceFromBinding(
								        accessURL,interfaceMethod);
               */
               String nameSpaceURI = "http://www.ivoa.net/wsdl/RegistryHarvest/v0.1";
               //soapActionURI = "http://www.ivoa.net/wsdl/reginterface.wsdl#" + interfaceMethod;
               soapActionURI = "http://www.openarchives.org/OAI/2.0/" + interfaceMethod;
               /*
               if(wsdlBasic.getEndPoint().keys().hasMoreElements()) {
                   soapActionURI = wsdlBasic.getSoapActionURI(
                     (String)wsdlBasic.getEndPoint().keys().nextElement() + 
                     "_" + interfaceMethod);
               }
               */
               if(soapActionURI != null) {
                   callObj.setSOAPActionURI(soapActionURI);
               }//if
               log.debug("Calling harvest service for url = " + accessURL + 
                        " interface Method = " + interfaceMethod + 
                        " with soapactionuri = " + soapActionURI);
               root = doc.createElementNS(nameSpaceURI,interfaceMethod);
               String value = "http://www.ivoa.net/xml/VOResource/v" + version;
               root.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vr",value);
               if(dt != null) {
                  childElem = doc.createElementNS(nameSpaceURI,"from");
                  childElem.appendChild(doc.createTextNode(sdf.format(dt)));
                  root.appendChild(childElem);
               }//if               
               doc.appendChild(root);
               //log.info("FULL SOAP REQUEST FOR HARVEST = " + DomHelper.DocumentToString(doc));
               SOAPBodyElement sbeRequest = new SOAPBodyElement(
                                                doc.getDocumentElement());
               //sbeRequest.setName("harvestAll");
               sbeRequest.setName(interfaceMethod);
               sbeRequest.setNamespaceURI(nameSpaceURI);
               //invoke the web service call
               log.debug("Calling invoke on service");
               Vector result = (Vector) callObj.invoke
                                        (new Object[] {sbeRequest});
               //Take the results and harvest.
               if(result.size() > 0) {
                   SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
                   Document soapDoc = sbe.getAsDocument();
                   //log.debug("SOAPDOC RETURNED = " + DomHelper.DocumentToString(soapDoc));
                   //(new HarvestThread(ras,soapDoc.getDocumentElement())).start();
                   ras.updateNoCheck(soapDoc,version);
                   if(isRegistryType) {
                      nl = DomHelper.getNodeListTags(soapDoc,"resumptionToken");
                      while(nl.getLength() > 0) {
                          Document resumeDoc = DomHelper.newDocument();
                          root = resumeDoc.createElementNS(nameSpaceURI,"ListRecords");
                          root.setAttributeNS("http://www.w3.org/2000/xmlns/","xmlns:vr",value);
                          childElem = resumeDoc.createElement("resumptionToken");
                          childElem.appendChild(resumeDoc.createTextNode(nl.item(0).getFirstChild().getNodeValue()));
                          root.appendChild(childElem);
                          resumeDoc.appendChild(root);
                          sbeRequest = new SOAPBodyElement(resumeDoc.getDocumentElement());
                          sbeRequest.setName("ListRecords");
                          sbeRequest.setNamespaceURI(nameSpaceURI);
                          
                          //invoke the web service call
                          result = (Vector) callObj.invoke
    									    (new Object[] {sbeRequest});
                          soapDoc = sbe.getAsDocument();
                          //(new HarvestThread(ras,soapDoc.getDocumentElement().cloneNode(true))).start();
                          ras.updateNoCheck(soapDoc,version);
                           nl = DomHelper.getNodeListTags(soapDoc,"resumptionToken");
                           /*
                           threadCount++;                           
                           if(threadCount > 19) {
                               log.info("20 harvest threads have started recently, sleeping for 5 seconds. ");
                               log.info("The activethread count = " + Thread.activeCount());
                               try {
                                   Thread.sleep(5000);
                               }catch(InterruptedException ie) {
                                   log.info("Possible interruption in the middle of harvest");
                               }
                               threadCount = 0;
                           }//if
                           */
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
      }else if(invocationType != null && 
               (invocationType.endsWith("WebBrowser") || 
                invocationType.endsWith("Extended") ||
                invocationType.endsWith("ParamHTTP"))
               ) {
         //its a web browser so assume for oai.
         try {
            String ending = "";
            //might need to put some oai date stuff on the end.  This is
            //unknown.
            log.debug("A web browser invocation not a web service");

            if(accessURL.indexOf("?") == -1) {
               ending = "?verb=ListRecords&metadataPrefix=ivo_vor"; //&from=" + date;
               if(dt != null) {
                  ending += "&from=" + sdf.format(dt);
               }
            }

            log.debug("Grabbing Document from this url = " + accessURL + ending);
            doc = DomHelper.newDocument(new URL(accessURL + ending));
            //log.info("Okay got this far to reading the url doc = " +
             //         DomHelper.DocumentToString(doc));
            //(new HarvestThread(ras,doc.getDocumentElement().cloneNode(true))).start();
            ras.updateNoCheck(doc,version);
            NodeList moreTokens = null;
            //log.info("resumptionToken length = " +
            //         doc.getElementsByTagName("resumptionToken").
            //         getLength());
            //if there are more paging(next) then keep calling them.
            while( doc != null && (moreTokens = doc.getElementsByTagName("resumptionToken")).
                                     getLength() > 0 && moreTokens.item(0).hasChildNodes()) {
               Node nd = moreTokens.item(0);
               if(accessURL.indexOf("?") != -1) {
                  accessURL = accessURL.substring(0,accessURL.indexOf("?"));
               }
               ending = "?verb=ListRecords&resumptionToken=" +
                         nd.getFirstChild().getNodeValue();
               log.debug(
               "the harvestcallregistry's with resumptionToken accessurl inside the token calls = " +
                          accessURL + ending);
               while(failureCount <= 2 && !resumptionSuccess) {
               try {
                   doc = DomHelper.newDocument(new URL(accessURL + ending));
                   resumptionSuccess = true;
               }catch(Exception e) {
                   log.error("Seemed to fail for = " + accessURL + ending);
                   log.error("Exception: " + e.getMessage());
                   log.info("try another in case web server has not caught up");
                   failureCount++;
                   resumptionSuccess = false;
               }
               }//while
               if(resumptionSuccess) {
                   ras.updateNoCheck(doc,version);
               }else {
                   doc = null;
               }//else
               failureCount = 0;
               resumptionSuccess = false;
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
         }catch(XMLDBException xdbe) {
             xdbe.printStackTrace();
             log.error(xdbe);
         }
      }//elseif
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
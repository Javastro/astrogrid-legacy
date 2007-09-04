package org.astrogrid.registry.server.harvest;

import java.rmi.RemoteException;

import java.io.IOException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ServiceException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;


import org.astrogrid.registry.common.RegistryDOMHelper;
import org.astrogrid.registry.server.query.QueryHelper;
import org.astrogrid.registry.server.query.QueryFactory;
import org.astrogrid.registry.server.query.ISearch;
import org.astrogrid.registry.server.xmldb.XMLDBRegistry;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.astrogrid.registry.server.InvalidStorageNodeException;

import java.net.URL;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.Vector;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.message.SOAPBodyElement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

/**
 *
 * RegistryHarvestService is no longer a web service class, but still posses the 
 * harvesting mechanism that is used by server side servlets which uses
 * automatic harvest mechanism and manual harvest by the user.
 */
public abstract class DefaultHarvestService {

   private static final Log log =
                          LogFactory.getLog(DefaultHarvestService.class);
   
   private static final String MAIN_HARVEST_SET = "ivo_managed";
   

   public static Config conf = null;
   
   // initialize to use a maximum of 8 threads.
   //static PooledExecutor pool = new PooledExecutor(5);
   

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }
   }
   
   protected XMLDBRegistry xdbRegistry;
   protected RegistryHarvestAdmin rha = null;
   private String contractVersion = null;
   private String versionNumber = null;
   
   public DefaultHarvestService(String contractVersion, String versionNumber) {
       xdbRegistry = new XMLDBRegistry();
       rha = new RegistryHarvestAdmin();
       this.contractVersion = contractVersion;
       this.versionNumber = versionNumber;
   }
   
   public void harvestAll()  {
       try {
           harvestAll(true,true);
       }catch(RegistryException re) {
         re.printStackTrace();
         log.error(re);
         //now update the stat info with this exception.
       }
   }
   
   public void harvestURL(URL url, String invocationType, Date dt, String setName) throws RegistryException, IOException {
       beginHarvest(url.toString(), invocationType, dt, null, null,setName);
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
      //onlyRegistries = true;
      Date dt = null;
      boolean harvestEnabled = conf.getBoolean("reg.amend.harvest",false);
      if(!harvestEnabled) {
          return;
      }

      String authorityID = conf.getString("reg.amend.authorityid");
      String tempIdent = null;
      String resKey = null;
      String lastResumptionToken = null;
      
          if(onlyRegistries) {
             //query for all the Registry types which should be all of them with an xsi:type="RegistryType"
             //xqlQuery = "declare namespace vr = \"http://www.ivoa.net/xml/VOResource/v0.9\"; //vr:Resource[@xsi:type='RegistryType']";
                 QueryHelper queryHelper = null;
                 log.debug("Start processing Registry Types from version = " + contractVersion);
                 try {
                     ISearch search = QueryFactory.createQueryService(contractVersion);
                     queryHelper = search.getQueryHelper();
                     harvestDoc = DomHelper.newDocument(queryHelper.getRegistriesQuery().getMembersAsResource().getContent().toString());
                 }catch(Exception e) {
                     log.error(e);
                     harvestDoc = null;
                 }
                 if(harvestDoc != null) {
                     NodeList nl = harvestDoc.getElementsByTagNameNS("*","Resource");
                     log.debug("Harvest All found this number of resources = " + nl.getLength());
                     for(int i = 0; i < nl.getLength();i++) {
                       Element elem = (Element) nl.item(i);
                       //versionNumber = RegistryDOMHelper.getRegistryVersionFromNode(elem);
                       tempIdent = RegistryDOMHelper.getAuthorityID(elem);
                       resKey = RegistryDOMHelper.getResourceKey(elem);
                   
                       if(useDates) {
                          String dateString = null;
                          try {
                              if(resKey != null && resKey.trim().length() > 0) tempIdent += "/" + resKey;
                              Document statDoc = rha.getStatus(tempIdent, versionNumber);
                              dateString = DomHelper.getNodeTextValue(statDoc,"StatsDateMillis");
                              lastResumptionToken = DomHelper.getNodeTextValue(statDoc,"LastResumptionToken");
                          } catch(IOException ioe) {
                                  log.error(ioe);
                                  log.info("xmldb exception thrown when trying to obtain stat and date information. Continue on and do a full harvest of the Registry Type");
                                  dateString = null;
                                  throw new RegistryException(ioe);
                              }                              
                              /*
                          }catch(RegistryE xdbe) {
                              log.error(xdbe);
                              log.info("xmldb exception thrown when trying to obtain stat and date information. Continue on and do a full harvest of the Registry Type");
                              dateString = null;
                              //
                              throw new RegistryException(xdbe);
                          }catch(IOException ioe) {
                              log.error(ioe);
                              log.info("xmldb exception thrown when trying to obtain stat and date information. Continue on and do a full harvest of the Registry Type");
                              dateString = null;
                              throw new RegistryException(ioe);
                          } catch(ParserConfigurationException pce) {
                              log.error(pce);
                              dateString = null;
                              throw new RegistryException(pce);
                          } catch(SAXException sax) {
                              log.error(sax);
                              dateString = null;
                              throw new RegistryException(sax);
                          }
                          */
                          if(dateString != null && dateString.trim().length() > 0) {
                              dt = new Date(Long.parseLong(dateString));
                          }//if
                       }//if
                       try {                           
                           if(authorityID.equals(tempIdent)) {
                               log.debug("This is our main Registry type do not do a harvest of it");
                           } else {
                               rha.clearOAILastUpdateInfo(tempIdent + "/" + resKey,versionNumber);
                               beginHarvest(elem,dt, lastResumptionToken);                  
                               //rha.harvestingUpdate(DomHelper.newDocument(DomHelper.ElementToString(elem)), versionNumber);
                           }
                       }catch(IOException ioe) {
                           log.error(ioe);
                           log.info("still continue to the next Registry type if there are any");
                           //update this reg stat here
                           rha.addStatError(tempIdent + "/" + resKey,versionNumber,ioe.getMessage());
                           //re = new RegistryException(ioe);
                       }/*catch(XMLDBException xmldbe) {
                           log.error(xmldbe);
                           rha.addStatError(tempIdent + "/" + resKey,versionNumber,xmldbe.getMessage());
                           //update this reg stat here
                       }*/catch(InvalidStorageNodeException isne) {
                           log.error(isne);
                           rha.addStatError(tempIdent + "/" + resKey,versionNumber,isne.getMessage());
                           //update this reg stat here
                       }catch(RegistryException ree) {
                           log.error(ree);
                           log.debug("still continue to the next Registry type if there are any");
                           rha.addStatError(tempIdent + "/" + resKey,versionNumber,ree.getMessage());
                           //update this reg stat here
                       }/*catch(SAXException sax) {
                           log.error(sax);
                           rha.addStatError(tempIdent + "/" + resKey,versionNumber,sax.getMessage());
                           //update this reg stat here
                       }catch(ParserConfigurationException pce) {
                           log.error(pce);
                           rha.addStatError(tempIdent + "/" + resKey,versionNumber,pce.getMessage());
                           //update this reg stat here
                       }*/
                     }//for
                 }//if
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
   public abstract void beginHarvest(Node resource, Date dt, String lastResumptionToken) throws RegistryException, IOException;
   
   public String beginHarvest(String accessURL, String invocationType, Date dt, String lastResumptionToken, String identifier, String setName) throws RegistryException, IOException  {
       log.debug("entered beginharvest(url,invocation)");      
       //boolean isRegistryType;
       //Document doc = null;
       String results = null;

       if(setName == null) {
           setName = conf.getString("reg.custom.harvest.set-" + identifier.substring(6,identifier.indexOf('/', 6)),MAIN_HARVEST_SET);
       }
       if(lastResumptionToken != null && lastResumptionToken.equals("NONE"))
           lastResumptionToken = null;
      
      
      if(invocationType != null && (invocationType.endsWith("WebService") || invocationType.endsWith("OAISOAP"))) {
         //call the service
         //remember to look at the date
          results =  runHarvestSoap(accessURL, lastResumptionToken, setName, dt, identifier);
          rha.addResumptionToken(identifier,versionNumber,"NONE");
          return results;
      }else if(invocationType != null && 
               (invocationType.endsWith("WebBrowser") || 
                invocationType.endsWith("Extended") ||
                invocationType.endsWith("ParamHTTP") ||
                invocationType.endsWith("OAIHTTP"))
               ) {
         //its a web browser so assume for oai.
         try {
            String ending = "";
            //might need to put some oai date stuff on the end.  This is
            //unknown.
            log.debug("A web browser invocation not a web service");
            String httpSet = "";
            
            if(!setName.equals("NONE") && hasSetViaHTTP(setName, accessURL)) {
                httpSet = "&set=" + setName;
            }
            ending += "&metadataPrefix=ivo_vor" + httpSet;
            if(dt != null) {
                ending += "&from=" + sdf.format(dt);
            }
            //log.debug("Grabbing Document from this url = " + accessURL + ending);

            results =  runHarvestGet(accessURL + "?verb=ListRecords", ending, lastResumptionToken, identifier);
            rha.addResumptionToken(identifier,versionNumber,"NONE");
            return results;
         }catch(ParserConfigurationException pce) {
            log.error(pce);
            throw new RegistryException(pce);
         }catch(SAXException sax) {
            log.error(sax);
            throw new RegistryException(sax);
         }catch(IOException ioe){
            log.error(ioe);
            throw new RegistryException(ioe);
         }
      }//elseif
      log.debug("end beginHarvest");
      return null;
   }//beginHarvest
       
       public String runHarvestGet(String accessURL, String urlQuery, String resumptionToken, String identifier) throws RegistryException {
           Document doc = null;
           int failureCount = 0;           
           boolean resumptionSuccess = false;
           String results = "";
           while(failureCount <= 2 && !resumptionSuccess) {
               try {
                   log.debug("harvest call url = " + accessURL + urlQuery);
                   if(resumptionToken != null) {
                       doc = DomHelper.newDocument(new URL(accessURL + "&resumptionToken=" + resumptionToken));
                       if(doc.getDocumentElement().getElementsByTagNameNS("*","metadata").getLength() > 0)
                           urlQuery = null;
                       else {
                           if(doc.getDocumentElement().getElementsByTagNameNS("http://www.openarchives.org/OAI/2.0","error").getLength() == 0 &&
                              doc.getDocumentElement().getElementsByTagNameNS("oai","error").getLength() == 0) {
                               urlQuery = null;
                           }
                       }
                   }
                   if(urlQuery != null) {
                	   log.debug("Grabbing doc from this harvest url = " + accessURL + urlQuery);
                       doc = DomHelper.newDocument(new URL(accessURL + urlQuery));
                   }
                   resumptionSuccess = true;                   
               }catch(Exception e) {
                   log.error("Seemed to fail for = " + accessURL + urlQuery);
                   log.error("Exception: " + e.getMessage());
                   log.info("try another in case web server has not caught up");
                   failureCount++;
                   resumptionSuccess = false;
                   throw new RegistryException(e);
               }                   
           }
           try {
               if(doc.getDocumentElement() != null && doc.getDocumentElement().getElementsByTagNameNS("*","metadata").getLength() > 0) {
                   results += rha.harvestingUpdate(doc, versionNumber);
                   /*
                   if(versionNumber.equals(("0.10")) {
                	   log.debug("transform to 1.0 version and do an update for 1.0");                	   
                	   rha.harvestingUpdate(doc, "0.10_1.0");
                   }
                   */
                   
               }
               //rha.addStatInfo()
           }catch(Exception e) {
        	   e.printStackTrace();
               throw new RegistryException(e);
           }
           
           NodeList moreTokens = null;
           //if there are more paging(next) then keep calling them.
           if( doc != null && (moreTokens = doc.getElementsByTagNameNS("*","resumptionToken")).
                                    getLength() > 0 && moreTokens.item(0).hasChildNodes()) {
              Node nd = moreTokens.item(0);
              /*
              if(accessURL.indexOf("?") != -1) {
                 accessURL = accessURL.substring(0,accessURL.indexOf("?"));
              }
              */
              urlQuery = null;
              rha.addResumptionToken(identifier,versionNumber,nd.getFirstChild().getNodeValue());
              runHarvestGet(accessURL, urlQuery, nd.getFirstChild().getNodeValue(), identifier);
           }//if
           return results;
       }//runHarvestGet
   
       SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
       
       private SOAPBodyElement createSoapBody(String accessURL, String resumptionToken, String setName, Date fromDate) throws Exception {
           SOAPBodyElement sbeRequest = null;
           
           try {
               Element childElem = null;
               Element root = null;               
               Document doc = DomHelper.newDocument();
               log.debug("Calling harvest service for url = " + accessURL + 
                        " interface Method = " + OAIINTERFACE_METHOD + 
                        " with soapactionuri = " + SOAPACTION_URI +
                        " resumptionToken = " + resumptionToken +
                        " setName = " + setName);
               if(fromDate != null)
                log.debug(" fromDate = " + sdf.format(fromDate));
               root = doc.createElementNS(NAMESPACE_URI,OAIINTERFACE_METHOD);
               if(resumptionToken != null) {
                   childElem = doc.createElementNS(NAMESPACE_URI,"resumptionToken");
                   childElem.appendChild(doc.createTextNode(resumptionToken));
                   root.appendChild(childElem);                   
               } else {
                   if(!setName.equals("NONE") && hasSetViaWebService(setName, accessURL)) {
                       childElem = doc.createElementNS(NAMESPACE_URI,"set");
                       childElem.appendChild(doc.createTextNode(setName));
                       root.appendChild(childElem);                   
                   }
                   if(fromDate != null) {
                      childElem = doc.createElementNS(NAMESPACE_URI,"from");
                      childElem.appendChild(doc.createTextNode(sdf.format(fromDate)));
                      root.appendChild(childElem);
                   }//if
               }
               doc.appendChild(root);
               //log.info("FULL SOAP REQUEST FOR HARVEST = " + DomHelper.DocumentToString(doc));
               sbeRequest = new SOAPBodyElement(
                                                doc.getDocumentElement());
               //sbeRequest.setName("harvestAll");
               sbeRequest.setName(OAIINTERFACE_METHOD);
               sbeRequest.setNamespaceURI(NAMESPACE_URI);
           } catch(ParserConfigurationException pce) {
               pce.printStackTrace();
               log.error(pce);
               //throw new RegistryException(pce);
           }
           return sbeRequest;           
       }
       
       private static final String SOAPACTION_URI = "http://www.openarchives.org/OAI/2.0/ListRecords";
       private static final String OAIINTERFACE_METHOD = "ListRecords";
       private static final String NAMESPACE_URI = "http://www.ivoa.net/wsdl/RegistryHarvest/v0.1";
       
       public String runHarvestSoap(String accessURL, String resumptionToken, String setName, Date fromDate, String identifier) throws RegistryException {
           
           //create a call object
           String results = "";
           Call callObj = getCall(accessURL);
           callObj.setSOAPActionURI(SOAPACTION_URI);
                 //log.info("FULL SOAP REQUEST FOR HARVEST = " + DomHelper.DocumentToString(doc));                 
                 try {
                     SOAPBodyElement sbeRequest = createSoapBody(accessURL, resumptionToken, setName, fromDate);
                     log.debug("Calling invoke on service");
                     Vector result = (Vector) callObj.invoke
                                              (new Object[] {sbeRequest});
                     //Take the results and harvest.
                     if(result.size() > 0) {
                         SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
                         Document soapDoc = sbe.getAsDocument();
                         if(resumptionToken != null)
                         if(soapDoc.getDocumentElement().getElementsByTagNameNS("http://www.openarchives.org/OAI/2.0","error").getLength() > 0 ||
                            soapDoc.getDocumentElement().getElementsByTagNameNS("oai","error").getLength() > 0) {
                             runHarvestSoap(accessURL, null, setName, fromDate,identifier);
                         }
                         
                         //(new HarvestThread(ras,soapDoc.getDocumentElement())).start();
                         if(soapDoc.getDocumentElement().getElementsByTagNameNS("*","metadata").getLength() > 0)
                             results += rha.harvestingUpdate(soapDoc, versionNumber);
                         //if(isRegistryType) {
                            NodeList nl = DomHelper.getNodeListTags(soapDoc,"resumptionToken");
                            if(nl.getLength() > 0) {
                                runHarvestSoap(accessURL, nl.item(0).getFirstChild().getNodeValue(), setName, fromDate, identifier);
                            }//if
                     }//if
                  } catch(RemoteException re) {
                      //log error
                      re.printStackTrace();
                      log.error(re);
                      throw new RegistryException(re);   
                  } catch(Exception e) {
                	e.printStackTrace();
                	log.error(e);
                    throw new RegistryException(e);
                  }
                  return results;
       }
   
   
   private boolean hasSetViaHTTP(String setName, String accessURL) throws ParserConfigurationException, SAXException, IOException {
       String ending = "";
       if(accessURL.indexOf("?") == -1) {
           ending = "?verb=ListSets";
       }
       log.debug("grabbing document at: " + accessURL + ending);
       Document doc = DomHelper.newDocument(new URL(accessURL + ending));
       log.debug("the spec doc = " + DomHelper.DocumentToString(doc));
       NodeList nl = doc.getElementsByTagNameNS("*","setSpec");
       for(int i = 0;i < nl.getLength();i++) {
           if(setName.equals(nl.item(i).getFirstChild().getNodeValue()))
               return true;
       }//for
       return false;
   }
   
   protected boolean hasSetViaWebService(String setName, String accessURL)  throws ParserConfigurationException, RemoteException, Exception  {
       Element root = null;
       
          //create a call object
       Call callObj = getCall(accessURL);
       String soapActionURI = null;
       
       Document doc = DomHelper.newDocument();
       String interfaceMethod = "ListSets";
       String nameSpaceURI = "http://www.ivoa.net/wsdl/RegistryHarvest/v0.1";
       soapActionURI = "http://www.openarchives.org/OAI/2.0/" + interfaceMethod;
       if(soapActionURI != null) {
           callObj.setSOAPActionURI(soapActionURI);
       }//if
       log.debug("Calling harvest service for url = " + accessURL + 
                " interface Method = " + interfaceMethod + 
                " with soapactionuri = " + soapActionURI);
       
       root = doc.createElementNS(nameSpaceURI,interfaceMethod);       
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
           NodeList nl = soapDoc.getElementsByTagNameNS("*","setSpec");
           for(int i = 0;i < nl.getLength();i++) {
               if(setName.equals(nl.item(i).getFirstChild().getNodeValue()))
                   return true;
           }//for
       }
       return false;
   }

   /**
    * Method to establish a Service and a Call to the server side web service.
    * @return Call object which has the necessary properties set for an Axis message style.
    * @throws Exception
    * @author Kevin Benson
    */
   protected Call getCall(String endPoint) {
      log.debug("start getCall");
      Call _call = null;
      try {
         Service  service = new Service();
         _call = (Call) service.createCall();
         _call.setTargetEndpointAddress(endPoint);
         _call.setSOAPActionURI("");
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
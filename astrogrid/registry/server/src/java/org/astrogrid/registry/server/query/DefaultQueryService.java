package org.astrogrid.registry.server.query;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.registry.server.xmldb.XMLDBRegistry;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.SOAPFaultException;
import org.astrogrid.registry.common.RegistryDOMHelper;


import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;
import org.astrogrid.registry.server.XSLHelper;
import org.astrogrid.store.Ivorn;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

/**
 * Class: RegistryQueryService
 * Description: The main class for all queries to the Registry to go to via Web Service or via internal
 * calls such as jsp pages or other classes.  The main focus is Web Service Interface methods are here
 * such as Search, KeywordSearch, and GetResourceByIdentifier.
 *
 * @author Kevin Benson
 */
public abstract class DefaultQueryService{

   /**
   * Logging variable for writing information to the logs
   */
   private static final Log log = LogFactory.getLog(DefaultQueryService.class);

   /**
    * conf - Config variable to access the configuration for the server normally
    * jndi to a config file.
    * @see org.astrogrid.config.Config
    */   
   public static Config conf = null;
   
   private String queryWSDLNS = null;
   
   private String contractVersion = null;
   
   private String voResourceVersion = null;
   
   private String collectionName = null;

   /**
    * final variable for the default AuthorityID associated to this registry.
    */
   private static final String AUTHORITYID_PROPERTY =
                                          "org.astrogrid.registry.authorityid";   
   
   private XMLDBRegistry xdbRegistry = null;
   
   private QueryHelper queryHelper = null;
   
  
   /**
    * Static to be used on the initiatian of this class for the config
    */   
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }
   }
      
   public DefaultQueryService(String queryWSDLNS, String contractVersion, String voResourceVersion) {
       this.queryWSDLNS = queryWSDLNS;
       this.contractVersion = contractVersion;
       this.voResourceVersion = voResourceVersion;
       collectionName = "astrogridv" + voResourceVersion.replace('.','_');       
       xdbRegistry = new XMLDBRegistry();
       queryHelper = new QueryHelper(queryWSDLNS, contractVersion, voResourceVersion);
   }
   
   protected Document processQueryResults(Node resultDoc, String responseWrapper) {
       return ProcessResults.processQueryResults(resultDoc,queryWSDLNS, contractVersion, responseWrapper);   
   }
   
   /**
    * Method: Search
    * Description: Web Service method to take ADQL DOM and perform a query on the
    * registry.  Takes in a DOM so it can handle multiple versions of ADQL.
    * 
    * @param query - DOM object containing ADQL. Which is xsl'ed into XQuery language for the query.
    * @return - Resource DOM object of the Resources from the query of the registry. 
    * 
    */
   public Document Search(Document query) {
      log.debug("start Search");
      long beginQ = System.currentTimeMillis();

      //transform the ADQL to an XQuery for the registry.
      String xqlQuery = null;
      try {
          xqlQuery = queryHelper.getQuery(query);
      }catch(Exception e) {
          return SOAPFaultException.createQuerySOAPFaultException(e.getMessage(),e.getMessage());
      }
      log.info("The XQLQuery From ADQLSearch = " + xqlQuery);
      //perform the query and log how long it took to query.
      Node resultDoc = queryHelper.queryRegistry(xqlQuery);      
      log.info("Time taken to complete search on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end Search");
      
      //To be correct we need to transform the results, with a correct response element 
      //for the soap message and for the right root element around the resources.
      return processQueryResults(resultDoc,"SearchResponse");      
   }

   
   /**
    * Method: Query
    * Description: More of a convenience method to do direct Xqueries on the registry
    * Gets the XQuery out of the XQLString element which is the wrapped method
    * name in the SOAP body. Currently Not in Use.
    *  
    * @param query - XQuery string to be used directly on the registry.
    * @return - Resource DOM object of the Resources from the query of the registry.
    */
   public Document XQuerySearch(Document query) {
         log.debug("start XQuerySearch");         
         try {
             String xql = DomHelper.getNodeTextValue(query,"XQuery");
             log.info("Found XQuery in XQuerySearch = " + xql);
             ResourceSet rs = xdbRegistry.query(xql,collectionName);
             Document resDoc = null;
             if(rs.getSize() == 0) {
                 resDoc = DomHelper.newDocument("<XQuerySearchResponse />");
             } else {
                 resDoc = DomHelper.newDocument();
                 resDoc.appendChild(resDoc.createElement("XQuerySearchResponse"));
                 for(int j = 0;j < rs.getSize();j++) {
                     //Node importNode = resDoc.importNode(((XMLResource)rs.getResource(j)).getContentAsDOM(),true);
                     Node importNode = resDoc.importNode(DomHelper.newDocument(rs.getResource(j).getContent().toString()).getDocumentElement(),true);
                     resDoc.getDocumentElement().appendChild(importNode);
                 }
             }
             return resDoc;
         }catch(XMLDBException xdbe) {
             xdbe.printStackTrace();
             return SOAPFaultException.createQuerySOAPFaultException(xdbe.getMessage(),xdbe);
         }catch(ParserConfigurationException pce) {
             pce.printStackTrace();
             return SOAPFaultException.createQuerySOAPFaultException(pce.getMessage(),pce);
         }catch(SAXException sax) {
             sax.printStackTrace();
             return SOAPFaultException.createQuerySOAPFaultException(sax.getMessage(),sax);
         }catch(IOException ioe) {
             ioe.printStackTrace();
             return SOAPFaultException.createQuerySOAPFaultException(ioe.getMessage(),ioe);
         }
   }


   /**
    * Method: loadRegistry
    * Description: Grabs the versionNumber from the DOM if possible and call the
    * loadMainRegistry method. If versionNumber is not in the DOM then use the default
    * version number in the properties.  The versionNumber comes from the vr namespace.
    * 
    * @param query actually normally empty/null and is ignored.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document loadRegistry(Document query) {
      log.debug("start loadRegistry");           
      return queryHelper.loadMainRegistry();
   }

      

   /**
    * Method KeywordSearch
    * Description: A Keyword search web service method.  Gets the keywords from the soap body (also if the keywords are to be 'or' together)
    * The paths used for comparison with the keywords are obtained from the JNDI/properties file. The keywords are seperated by spaces.
    * Once data is obtained called the other keywordQuery method below to perform the query.
    * 
    * @param query - The soap body of the web service call, containing sub elements of keywords.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document KeywordSearch(Document query) {
       log.debug("start keywordsearch");                   
       String keywords = null;
       String orValue = null;
       try {
           keywords = DomHelper.getNodeTextValue(query,"keywords");
           orValue = DomHelper.getNodeTextValue(query,"orValue");
       }catch(IOException ioe) {
           return SOAPFaultException.createQuerySOAPFaultException("IO problem trying to get keywords and orValue",ioe);
       }
       boolean orKeywords = new Boolean(orValue).booleanValue();
       return queryHelper.keywordQuery(keywords,orKeywords);
   }
   
   
   
   /**
    * Method: GetResourcesByIdentifier
    * Description: This is the currently used web service method from client (Iteration 0.9).  But is expected to be
    * deprecated.  And to use GetResourceByIdentifier, because an identifier can only return one Resource only.  Currently
    * this method will query for part of the Identifier. From the client perspective it is passing an entire identifier
    * each time and only receiving one resource, but it could pass in ivo://{authorityid} and get all Resources that
    * have that AuthorityID.  Reason it is currently used is eXist seems to have a problem in embedded mode where
    * plainly I have seen it lose some elements.  After getting the identifier call GetResourcesByIdentifier(String,versionNumber).
    * 
    * @param query - A Soap body request containing an identifier element holding the identifier to be queries on.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document GetResource(Document query) {
       log.debug("start GetResource");                   
       String ident = null;
       try {
//           log.info("The soapbody in regserver1 = " + DomHelper.DocumentToString(query));
           ident = DomHelper.getNodeTextValue(query,"identifier");
           log.info("found identifier in web service request = " + ident);
       }catch(IOException ioe) {
           return SOAPFaultException.createQuerySOAPFaultException("IO problem trying to get identifier",ioe);
       }
       return queryHelper.getResourceByIdentifier(ident);
   }   
   
   /**
    * Method: GetResourcesByIdentifier
    * Description: This is the currently used web service method from client (Iteration 0.9).  But is expected to be
    * deprecated.  And to use GetResourceByIdentifier, because an identifier can only return one Resource only.  Currently
    * this method will query for part of the Identifier. From the client perspective it is passing an entire identifier
    * each time and only receiving one resource, but it could pass in ivo://{authorityid} and get all Resources that
    * have that AuthorityID.  Reason it is currently used is eXist seems to have a problem in embedded mode where
    * plainly I have seen it lose some elements.  After getting the identifier call GetResourcesByIdentifier(String,versionNumber).
    * 
    * @param query - A Soap body request containing an identifier element holding the identifier to be queries on.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document GetResourcesByIdentifier(Document query) {
       log.debug("start GetResourcesByIdentifier");                   
       String ident = null;
       try {
  //         log.info("The soapbody in regserver2 = " + DomHelper.DocumentToString(query));
           ident = DomHelper.getNodeTextValue(query,"identifier");
           log.info("found identifier in web service request = " + ident);
       }catch(IOException ioe) {
           return SOAPFaultException.createQuerySOAPFaultException("IO problem trying to get identifier",ioe);
       }
       return queryHelper.getResourcesByIdentifier(ident);
   }
   

   /**
    * Method: GetResourceByIdentifier
    * Description: Web Service interface method. Gets the identifier from a Soap body and extracts
    * it out of the xml database based on the primary key or id. (No query is performed). Actually calls the
    * getResoruceByIdentifier(string,string).
    * 
    * @param query - soab body containing a identifier element for the identifier to query on.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document GetResourceByIdentifier(Document query) {
       log.debug("start GetResourcesByIdentifier");                   
       String ident = null;
       try {
           ident = DomHelper.getNodeTextValue(query,"identifier");
           log.info("found identifier in web service request = " + ident);
       }catch(IOException ioe) {
           return SOAPFaultException.createQuerySOAPFaultException("IO problem trying to get identifier",ioe);
       }
       return queryHelper.getResourceByIdentifier(ident);
   }


   /**
    * Method: GetRegistries
    * Description: Queries and returns all the Resources that are Registry type resources.
    * Calls teh getRegistriesQuery(String)
    * 
    * @param query normally empty and is ignored, it is required though for the
    * Axis Document style method.  At most it will contain nothing more than the method
    * name.
    * @return Resource entries of type Registries.
    */
   public Document GetRegistries(Document query) {
      return queryHelper.getRegistriesQuery();
   }
   
   public QueryHelper getQueryHelper() {
       return this.queryHelper;
   }
  
}
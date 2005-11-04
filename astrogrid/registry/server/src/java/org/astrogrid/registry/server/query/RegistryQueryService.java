package org.astrogrid.registry.server.query;



import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;


import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.xml.sax.InputSource;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;
import java.io.IOException;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;
import org.astrogrid.registry.server.XSLHelper;
import java.net.URL;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
//import org.apache.axis.AxisFault;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.harvest.RegistryHarvestService;
import org.astrogrid.registry.server.RegistryServerHelper;
import org.astrogrid.registry.server.SOAPFaultException;
import org.astrogrid.registry.common.RegistryDOMHelper;
import org.astrogrid.registry.server.QueryHelper;
import org.astrogrid.xmldb.client.QueryService;
import org.astrogrid.xmldb.client.XMLDBFactory;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import java.util.ArrayList;

import org.astrogrid.registry.NoResourcesFoundException;
import org.astrogrid.store.Ivorn;

/**
 * Class: RegistryQueryService
 * Description: The main class for all queries to the Registry to go to via Web Service or via internal
 * calls such as jsp pages or other classes.  The main focus is Web Service Interface methods are here
 * such as Search, KeywordSearch, and GetResourceByIdentifier.
 *
 * @author Kevin Benson
 */
public class RegistryQueryService {

   /**
   * Logging variable for writing information to the logs
   */
   private static final Log log = LogFactory.getLog(RegistryQueryService.class);

   /**
    * conf - Config variable to access the configuration for the server normally
    * jndi to a config file.
    * @see org.astrogrid.config.Config
    */
   public static Config conf = null;

   /**
    * final variable for the default AuthorityID associated to this registry.
    */
   private static final String AUTHORITYID_PROPERTY =
                                          "org.astrogrid.registry.authorityid";

   private XMLDBFactory xdb = new XMLDBFactory();

   /**
    * Static to be used on the initiatian of this class for the config
    */
   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
      }
   }

   private Document processQueryResults(Node resultDoc, String versionNumber, String responseWrapper) {
       Document doc = null;
       //Okay nothing from the query
       try {
           if(resultDoc == null) {
               if(responseWrapper != null) {
                   return DomHelper.newDocument("<"+responseWrapper+">"+"</"+responseWrapper+">");
               }else {
                   return SOAPFaultException.createQuerySOAPFaultException("Nothing to return in the Soap body",
                   "Nothing to return in the soap body, the query returned no resources.");
               }//else
           }//if

           //check if it is a Fault, if so just return the resultDoc;
           if(resultDoc.getNodeName().indexOf("Fault") != -1 ||
              resultDoc.getFirstChild().getNodeName().indexOf("Fault") != -1) {
               //All Faults shoudl have been created by server.SOAPFaultException meaning a Document object.
               return (Document)resultDoc;
           }
           XSLHelper xslHelper = new XSLHelper();
           doc = xslHelper.transformExistResult(resultDoc, versionNumber);

           if(responseWrapper != null && responseWrapper.trim().length() > 0) {
               Element currentRoot = doc.getDocumentElement();
               Element root = doc.createElementNS("http://www.astrogrid.org/registry/wsdl",responseWrapper);
               root.appendChild(currentRoot);
               doc.appendChild(root);
           }//if
       }catch(ParserConfigurationException e) {
         log.error(e);
         doc = SOAPFaultException.createQuerySOAPFaultException(e.getMessage(),e);
       }catch(TransformerConfigurationException e) {
           log.error(e);
           doc = SOAPFaultException.createQuerySOAPFaultException(e.getMessage(),e);
       }catch(TransformerException e) {
           log.error(e);
           doc = SOAPFaultException.createQuerySOAPFaultException(e.getMessage(),e);
       }catch(UnsupportedEncodingException e) {
           log.error(e);
           doc = SOAPFaultException.createQuerySOAPFaultException(e.getMessage(),e);
       }catch(SAXException e) {
           log.error(e);
           doc = SOAPFaultException.createQuerySOAPFaultException(e.getMessage(),e);
       }catch(IOException e) {
           log.error(e);
           doc = SOAPFaultException.createQuerySOAPFaultException(e.getMessage(),e);
       }
       return doc;
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
      //get the version of Resources we are querying on.
      String versionNumber = getRegistryVersion(query);

      //transform the ADQL to an XQuery for the registry.
      String xqlQuery = null;

      try {
          xqlQuery = getQuery(query,versionNumber);
      }catch(Exception e) {
          return SOAPFaultException.createQuerySOAPFaultException(e.getMessage(),e.getMessage());
      }
      log.info("The XQLQuery = " + xqlQuery);
      //perform the query and log how long it took to query.
      Node resultDoc = queryExist(xqlQuery,versionNumber);
      log.debug("Time taken to complete search on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end Search");

      //To be correct we need to transform the results, with a correct response element
      //for the soap message and for the right root element around the resources.
      return processQueryResults(resultDoc,versionNumber,"SearchResponse");
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

         String versionNumber = getRegistryVersion(query);
         //query the eXist db.
         //resultDoc = (Document)queryExist(xql,versionNumber);
         Collection coll = null;
         String collectionName = "astrogridv" + versionNumber.replace('.','_');
         try {
             String xql = DomHelper.getNodeTextValue(query,"XQuery");
             log.info("Found XQuery in XQuerySearch = " + xql);
             coll = xdb.openCollection(collectionName);
             log.debug("Got Collection = " + collectionName);
             //System.out.println("the xql = " + xql + " the collection = " + collectionName);
             QueryService xqs = xdb.getQueryService(coll);

             long beginQ = System.currentTimeMillis();
             ResourceSet rs = xqs.query(xql);
             //System.out.println("the rs size = " + rs.getSize());
             log.debug("Total Query Time = " + (System.currentTimeMillis() - beginQ));
             log.debug("Number of results found in query = " + rs.getSize());
             if(rs.getSize() == 0) {
                 return null;
             }
             Resource xmlr = rs.getMembersAsResource();
             return processQueryResults(DomHelper.newDocument(xmlr.getContent().toString()),
                     versionNumber,"XQuerySearch");
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
         } finally {
             try {
                 log.debug("end XQuerySearch");
                 xdb.closeCollection(coll);
             }catch(XMLDBException xmldb) {
                 log.error(xmldb);
             }//try
         }//finally
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

      //get the default autority id for this registry.
      Document doc = null;
      Document responseDoc = null;
      String versionNumber = getRegistryVersion(query);
      log.debug("the versionNumber in loadRegistry = " + versionNumber);
      return loadMainRegistry(versionNumber);
   }

   /**
    * Method: loadMainRegistry
    * Description: Queries for the Registry Resource element that is tied to this Registry.
    * All Astrogrid Registries have one Registry Resource tied to the Registry.
    * Which defines the AuthorityID's it manages and how to access the Registry.
    *
    * @param version of the schema to be queryied on (the vr namespace); hence the collection
    * @return XML docuemnt object representing the result of the query.
    */
   public Document loadMainRegistry(String versionNumber) {
       long beginQ = System.currentTimeMillis();

       String xqlString = QueryHelper.queryForMainRegistry(versionNumber);
       log.info("XQL String = " + xqlString);
       Node resultDoc = queryExist(xqlString,versionNumber);

       log.info("Time taken to complete loadRegistry on server = " +
               (System.currentTimeMillis() - beginQ));
       log.debug("end loadRegistry");

       //To be correct we need to transform the results, with a correct response element
       //for the soap message and for the right root element around the resources.
       return processQueryResults(resultDoc,versionNumber,null);
   }

   /**
    * Method: queryExist
    * Description: Queries the xml database, on the collection of the registry. This method
    * will read from properties a xql expression and fill out the expression then perform the query. This
    * is a convenience in case of customization for other xml databases.
    * @param xqlString an XQuery to query the database
    * @param collectionName the location in the database to query (sort of like a table)
    * @return xml DOM object returned from the database, which are Resource elements
    */
   private Node queryExist(String xqlString, String versionNumber) {
      log.debug("start queryExist");
      Collection coll = null;
      int tempIndex = 0;
      try {
          String collectionName = "astrogridv" + versionNumber.replace('.','_');
          coll = xdb.openCollection(collectionName);
          log.debug("Got Collection");
          QueryService xqs = xdb.getQueryService(coll);
          //Get the maximum return count.
          String returnCount = conf.getString("reg.amend.returncount","100");
          //get the xquery expression.
          String xqlExpression = conf.getString("reg.custom.query.expression");
          xqlExpression = xqlExpression.replaceAll("__declareNS__", QueryHelper.getXQLDeclarations(versionNumber));
          tempIndex = xqlExpression.indexOf("__query__");
          if(tempIndex == -1) {
              return SOAPFaultException.createQuerySOAPFaultException("XQL Expression has no placement for a Query",
                                                                      "XQL Expression has no placement for a Query");
          }
          //todo: check into this again, for some reason could not do a replaceAll so currently placing
          //in the string the hard way.
          String endString = xqlExpression.substring(tempIndex+9);
          xqlExpression = xqlExpression.substring(0,tempIndex);
          xqlExpression += xqlString + endString;
          xqlExpression = xqlExpression.replaceAll("__returnCount__", returnCount);
          log.debug("Now querying in colleciton = " + collectionName + " query = " + xqlExpression);
          //start a time to see how long the query took.
          long beginQ = System.currentTimeMillis();
          ResourceSet rs = xqs.query(xqlExpression);
          log.debug("Total Query Time = " + (System.currentTimeMillis() - beginQ));
          log.info("Number of results found in query = " + rs.getSize());
          if(rs.getSize() == 0) {
              return null;
          }
          Resource xmlr = rs.getMembersAsResource();
          return DomHelper.newDocument(xmlr.getContent().toString());
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
      } finally {
          try {
              xdb.closeCollection(coll);
          }catch(XMLDBException xmldb) {
              log.error(xmldb);
          }//try
      }//finally
   }

   /**
    * Method: getAstrogridVersions
    * Description: Does not actually do a query, it opens the main root colleciton /db and finds all the child collections
    * associated with astrogridv?? (??=version number) and puts them as strings in an array list to be returned.
    *
    * @return an ArrayList of Strings containging the versions number supported by this registry (or in the xml db).
    */
   public ArrayList getAstrogridVersions() throws XMLDBException {
       ArrayList al = new ArrayList();
       Collection coll = null;
       try {
           coll = xdb.openCollection();
           String []childCollections = coll.listChildCollections();
           for(int i = 0;i < childCollections.length;i++) {
               if(childCollections[i].startsWith("astrogridv")) {
                   al.add(((String)childCollections[i].substring(10).replace('_','.')));
               }
           }
       }finally {
           try {
               xdb.closeCollection(coll);
           }catch(XMLDBException xmldb) {
               log.error(xmldb);
           }

       }
       return al;
   }

   /**
    * Method KeywordSearch
    * Description: A Keyword search web service method.  Gets the keywords from the soap body (also if the keywords are to be 'or'
    together)
    * The paths used for comparison with the keywords are obtained from the JNDI/properties file. The keywords are seperated by
    spaces.
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
       String attrVersion = getRegistryVersion(query);
       boolean orKeywords = new Boolean(orValue).booleanValue();
       return keywordQuery(keywords,orKeywords,attrVersion);
   }

   /**
    * Method: keywordQuery
    * Description: A Keyword search basic method called from jsp pages. And queries on the default version of the
    * registry. The paths used for comparison with the keywords are obtained from the JNDI/properties file.
    * Deprecate should always pass in a version.
    *
    * @deprecated - No longer used, a version Number should always be passed in.
    * @param keywords - A string of keywords seperated by spaces.
    * @param orKeywords - Are the key words to be or'ed together
    * @return XML docuemnt object representing the result of the query.
    */
   public Document keywordQuery(String keywords, boolean orKeywords) {
       return keywordQuery(keywords,orKeywords,RegistryDOMHelper.getDefaultVersionNumber());
   }

   /**
    * Method: keywordQuery
    * Description: A Keyword search method. Splits the keywords and forms a xql query for the key word search.
    * The paths used for comparison with the keywords are obtained from the JNDI/properties file they are a comma
    * seperated xpath form.
    *
    * @param query - String of keywords seperated by spaces.
    * @param orKeywords - Are the key words to be or'ed together
    * @param version - The version number from vr namespace used to form the collection name and get the xpaths from the
    properties.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document keywordQuery(String keywords, boolean orKeywords, String version) {
       long beginQ = System.currentTimeMillis();
       if(version == null || version.trim().length() <= 0) {
           version = RegistryDOMHelper.getDefaultVersionNumber();
       }
       String versionNumber = version;
       //split the keywords from there spaces
       String []keyword = keywords.split(" ");
       //get all the xpaths for the query.
       String xqlPaths = conf.getString("reg.custom.keywordxpaths." + versionNumber);
       //the xpaths are comma seperated split that as well.
       String []xqlPath = xqlPaths.split(",");

       //get the first part of the query which is basically to query on the
       //Resource element.
       String xqlString = QueryHelper.getStartQuery(versionNumber);

       //go through all the xpaths and buildup a keyword string.
       for(int i = 0;i < xqlPath.length;i++) {
           xqlString += " (";
           for(int j = 0;j < keyword.length;j++) {
             xqlString += "$x/" + xqlPath[i] + " &= '*" + keyword[j] + "*'";
             if(j != (keyword.length - 1)) {
                 if(orKeywords) {
                     xqlString += " or ";
                 }else {
                     xqlString += " and ";
                 }
             }//if
           }//for
           xqlString += ") ";
           if(i != (xqlPath.length-1)) {
               xqlString += " or ";
           }
       }//for
       xqlString += " return $x";

       Node resultDoc = queryExist(xqlString,versionNumber);
       log.debug("Time taken to complete keywordsearch on server = " +
               (System.currentTimeMillis() - beginQ));
       log.debug("end keywordsearch");

       //To be correct we need to transform the results, with a correct response element
       //for the soap message and for the right root element around the resources.
       return processQueryResults(resultDoc,
                                             versionNumber,"KeywordSearchResponse");
   }

   /**
    * Method: getAll
    * Description: Conventient method for the browse all jsp page which queries the entire
    * collection in the registry based on a version number.
    * @param versionNumber version number to form the collection(like a table) to query on.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document getAll(String versionNumber) {
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryDOMHelper.getDefaultVersionNumber();
       }

       String xqlString = QueryHelper.getAllQuery(versionNumber);
       Node resultDoc = queryExist(xqlString,versionNumber);
       return processQueryResults(resultDoc,
               versionNumber,"GetAllResponse");
   }

   /**
    * Method: GetResourcesByIdentifier
    * Description: This is the currently used web service method from client (Iteration 0.9).  But is expected to be
    * deprecated.  And to use GetResourceByIdentifier, because an identifier can only return one Resource only.  Currently
    * this method will query for part of the Identifier. From the client perspective it is passing an entire identifier
    * each time and only receiving one resource, but it could pass in ivo://{authorityid} and get all Resources that
    * have that AuthorityID.  Reason it is currently used is eXist seems to have a problem in embedded mode where
    * plainly I have seen it lose some elements.  After getting the identifier call
    GetResourcesByIdentifier(String,versionNumber).
    *
    * @param query - A Soap body request containing an identifier element holding the identifier to be queries on.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document GetResource(Document query) {
       log.info("start GetResource");
       String ident = null;
       try {
           //log.info("The soapbody in regserver = " + DomHelper.DocumentToString(query));
           ident = DomHelper.getNodeTextValue(query,"identifier");
           log.info("Searching for IVO identifier " + ident);
       }catch(IOException ioe) {
           return SOAPFaultException.createQuerySOAPFaultException("IO problem trying to get identifier",ioe);
       }
       String attrVersion = getRegistryVersion(query);
       return getResourcesByIdentifier(ident,attrVersion);
   }

   /**
    * Method: GetResourcesByIdentifier
    * Description: This is the currently used web service method from client (Iteration 0.9).  But is expected to be
    * deprecated.  And to use GetResourceByIdentifier, because an identifier can only return one Resource only.  Currently
    * this method will query for part of the Identifier. From the client perspective it is passing an entire identifier
    * each time and only receiving one resource, but it could pass in ivo://{authorityid} and get all Resources that
    * have that AuthorityID.  Reason it is currently used is eXist seems to have a problem in embedded mode where
    * plainly I have seen it lose some elements.  After getting the identifier call
    * GetResourcesByIdentifier(String,versionNumber).
    *
    * @param query - A Soap body request containing an identifier element holding the identifier to be queries on.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document GetResourcesByIdentifier(Document query) {
       log.info("Start GetResourcesByIdentifier");
       String ident = null;
       try {
           ident = DomHelper.getNodeTextValue(query,"identifier");
           log.info("Searching for IVO identifier " + ident);
       }catch(IOException ioe) {
           return SOAPFaultException.createQuerySOAPFaultException("IO problem trying to get identifier",ioe);
       }
       String attrVersion = getRegistryVersion(query);
       return getResourcesByIdentifier(ident,attrVersion);
   }

   /**
    * Method: GetResourcesByIdentifier
    * Description: Used by JSP pages and the GetResourcesByIdentifier soap call. Currently
    * this method will query for part of the Identifier. From the client perspective it is passing an entire identifier
    * each time and only receiving one resource, but it could pass in ivo://{authorityid} and get all Resources that
    * have that AuthorityID.  Reason it is currently used is eXist seems to have a problem in embedded mode where
    * plainly I have seen it lose some elements.

    * @param ivorn - Identifier String.
    * @param versionNumber - version number to query on, if null use the default version number for the registry.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document getResourcesByIdentifier(String ivorn, String versionNumber) {
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryDOMHelper.getDefaultVersionNumber();
       }
       if(ivorn == null || ivorn.trim().length() <= 0) {
           return SOAPFaultException.createQuerySOAPFaultException("Cannot have empty or null identifier",
                                                                   "Cannot have empty or null identifier");
       }
       String queryIvorn = ivorn;
       //this is a hack for now delete later.  Some old client delegates might not pass the ivorn
       //the new delegates do.
       if(!Ivorn.isIvorn(ivorn))
           queryIvorn = "ivo://" + ivorn;
       String xqlString = QueryHelper.queryForResource(queryIvorn,versionNumber);
       Node resultDoc = queryExist(xqlString,versionNumber);
       return processQueryResults(resultDoc,
               versionNumber,"GetResourcesByIdentifier");
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
       log.info("start GetResourcesByIdentifier");
       String ident = null;
       try {
           ident = DomHelper.getNodeTextValue(query,"identifier");
           log.info("Searching for IVO identifier " + ident);
       }catch(IOException ioe) {
           return SOAPFaultException.createQuerySOAPFaultException("IO problem trying to get identifier",ioe);
       }
       String attrVersion = getRegistryVersion(query);
       return getResourceByIdentifier(ident,attrVersion);
   }


   /**
    * Method: GetResourceByIdentifier
    * Description: Used by JSP's and teh interface method form web service.
    * Takes an identifier and the versionNumber of the registry to be queries on.
    * (No query is actually performed). Grabs the Resource from the database based on
    * identifier, this is because the identifier is the primary key (or id) in the db.
    *
    * @param query - soab body containing a identifier element for the identifier to query on.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document getResourceByIdentifier(String ivorn, String versionNumber) {
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryDOMHelper.getDefaultVersionNumber();
       }
       if(ivorn == null || ivorn.trim().length() <= 0) {
           return SOAPFaultException.createQuerySOAPFaultException("Cannot have empty or null identifier",
                                                                   "Cannot have empty or null identifier");
       }
       String queryIvorn = ivorn;
       if(Ivorn.isIvorn(ivorn)) {
           queryIvorn = ivorn.substring(6);
       }

       String id = queryIvorn.replaceAll("[^\\w*]","_");

       String collectionName = "astrogridv" + versionNumber.replace('.','_');
       Collection coll = null;
       try {
           coll = xdb.openCollection(collectionName);
           XMLResource xmr = (XMLResource)xdb.getResource(coll,id + ".xml");
           //System.out.println(xmr.getContent().toString());
           return processQueryResults(DomHelper.newDocument(xmr.getContent().toString()),
                       versionNumber,"GetResourceByIdentifier");
       }catch(XMLDBException xdbe) {
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
       } finally {
           try {
               xdb.closeCollection(coll);
           }catch(XMLDBException xmldb) {
               log.error(xmldb);
           }
       }
   }

   public Document getResourcesByAnyIdentifier(String ivorn, String versionNumber) {
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryDOMHelper.getDefaultVersionNumber();
       }

       String xqlString = QueryHelper.queryForAllResource(ivorn,versionNumber);
       Node resultDoc = queryExist(xqlString,versionNumber);
       return processQueryResults(resultDoc,
               versionNumber,"GetResourceByIdentifier");
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
      //DomHelper.DocumentToStream(query,System.out);

      String versionNumber = getRegistryVersion(query);
      return getRegistriesQuery(versionNumber);
   }

   /**
    * Method: GetRegistries
    * Description: Queries and returns all the Resources that are Registry type resources.
    * Used by the harvester to find Registry types for harvesting.
    *
    * @param versionNumber - String table or collection
    * @return Resource entries of type Registries.
    * @see org.astrogrid.registry.server.harvest.RegistryHarvestService
    */
   public Document getRegistriesQuery(String versionNumber) {
       long beginQ = System.currentTimeMillis();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryDOMHelper.getDefaultVersionNumber();
       }

       //Get the Xquery String, from the properties.
       String xqlString = QueryHelper.queryForRegistries(versionNumber);
       Node resultDoc = queryExist(xqlString,versionNumber);
       return processQueryResults(resultDoc,
               versionNumber,"GetRegistriesResponse");
   }


   /**
    * Method: getQuery
    * Description: Transforms ADQL to XQuery, uses the namespace of ADQL to allow the
    * transformations to handle different versions.  Transformations are done
    * by XSL stylesheets. XSL is customizable in case you need to change the XQuery or
    * some other type of Query for the database.
    *
    * @param query ADQL DOM object
    * @return xquery string
    */
   private String getQuery(Document query,String resourceVersion) throws Exception {
       XSLHelper xslHelper = new XSLHelper();
       NodeList nl = query.getElementsByTagNameNS("*","Select");
       //Get the main root element Select
       if(nl.getLength() == 0)
           nl = query.getElementsByTagNameNS("*","Where");


       //find the namespace.
       String adqlVersion = null;
       log.info("the adql in getquery nl.getLenth = " + nl.getLength());
       if(nl.getLength() > 0) {
           log.info("the namespaceuri for element = " + ((Element)nl.item(0)).getNamespaceURI());
           adqlVersion = ((Element)nl.item(0)).getNamespaceURI();
       }//if

       //throw an error if no version was found.
       if(adqlVersion == null || adqlVersion.trim().length() == 0) {
           throw new Exception("No ADQL version found");
       }
       //get only the actual version number.
       adqlVersion = adqlVersion.substring(adqlVersion.lastIndexOf("v")+1);
//       adqlVersion = adqlVersion.replace('.','_');
       //make the transformation using an xsl stylesheet.
       return xslHelper.transformADQLToXQL(query, adqlVersion,
                        RegistryServerHelper.getRootNodeName(resourceVersion),"");
                        //QueryHelper.getXQLDeclarations(resourceVersion));
   }

   /**
    * Looks for a registry version number in a DOM object.  By looking at the xmlns
    * (normally vr) of varoius elements. Because a Resource element may be a few
    * elements down it goes through a few child nodes. Calls RegistryDOMHelper which
    * is the actual process of finding the vr namespace and get the version number.
    *
    * @param query XML DOM hopefully with a vr namespace.  The version 0.10 of Resources
    * may be a little lower than the Resource element that is why it goes one child lower.
    * @return version number of the Registry.
    * @see org.astrogrid.registry.common.RegistryDOMHelper.
    */
   private String getRegistryVersion(Document query) {
       log.debug("Start getRegistryVersion()");
       if(query == null) {
           return RegistryDOMHelper.getRegistryVersionFromNode(query);
       }
       Element elem = query.getDocumentElement();
       if(elem == null) {
           return RegistryDOMHelper.getRegistryVersionFromNode(query);
       }
       if(elem.hasChildNodes()) {
           return RegistryDOMHelper.getRegistryVersionFromNode(
                         query.getDocumentElement().getFirstChild());
       }
       return RegistryDOMHelper.getRegistryVersionFromNode(
                                   query.getDocumentElement());
   }
}

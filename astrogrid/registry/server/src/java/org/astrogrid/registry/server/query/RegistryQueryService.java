package org.astrogrid.registry.server.query;



import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;


import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.io.IOException;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;
import org.astrogrid.registry.server.XQueryExecution;
import org.astrogrid.registry.server.XSLHelper;
import java.net.URL;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import org.apache.axis.AxisFault;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.harvest.RegistryHarvestService;
import org.astrogrid.registry.server.RegistryServerHelper;
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
 *
 *
 *
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
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
   
   /**
    * Search - Web Service method to take ADQL DOM and perform a query on the
    * registry.  Takes in a DOM so it can handle multiple versions of ADQL.
    * 
    * @param query - DOM object containing ADQL.
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    * @return - Resource DOM object of the Resources from the query of the registry. 
    * 
    */
   public Document Search(Document query) throws AxisFault {
      log.debug("start Search");
      long beginQ = System.currentTimeMillis();
      XSLHelper xslHelper = new XSLHelper();

      //get the version of Resources we are querying on.
      String versionNumber = getRegistryVersion(query);

      //transform the ADQL to an XQuery for the registry.
      String xqlQuery = getQuery(query,versionNumber);
      log.info("The XQLQuery = " + xqlQuery);
      //perform the query and log how long it took to query.
      Node resultDoc = queryExist(xqlQuery,versionNumber);
      log.info("Time taken to complete search on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end Search");
      
      //To be correct we need to transform the results, with a correct response element 
      //for the soap message and for the right root element around the resources.
      return xslHelper.transformExistResult(resultDoc,
                                            versionNumber,"SearchResponse");
   }

   
   /**
    * More of a convenience method to do direct Xqueries on the registry
    * Gets the XQuery out of the XQLString element which is the wrapped method
    * name in the SOAP body.
    *  
    * @param query - XQuery string to be used directly on the registry.
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry. 
    * @return - Resource DOM object of the Resources from the query of the registry.
    */
   public Document Query(Document query) throws AxisFault {
      log.debug("start Query");
      Document resultDoc = null;
      try {
         //get the xquery.
         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end Query");
         
         String versionNumber = getRegistryVersion(query);
         //query the eXist db.
         resultDoc = (Document)queryExist(xql,versionNumber);
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);
      }
      return resultDoc;
   }

   /**
    * More of a convenience method to do direct Xqueries on the registry
    * and this method is for when a Web Service style does not wrap a method name
    * in the soap body.

    * @param query - XQuery string to be used directly on the registry.
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry. 
    * @return - Resource DOM object of the Resources from the query of the registry.
    */   
   public Document XQLString(Document query) throws AxisFault {
      log.debug("start XQLString");
      Document resultDoc = null;
      try {
         String versionNumber = getRegistryVersion(query);

         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end XQLString");
         resultDoc = (Document)queryExist(xql,versionNumber);
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);         
      }
      return resultDoc;
      
   }
   
   /**
   * submitQuery queries the registry for Resources.  Currently uses
   * an older xml query language that Astrogrid came up with, but will
   * soon be rarely used for the ADQL version to be the standard for the
   * IVOA.  Will be deprecated in itn07.
   * 
   * @deprecated - It is still being used some, but will be factored away in a matter of a couple of weeks Jan 24,2005
   * @param query XML document object representing the query language used on the registry.
   * @throws - AxisFault containing exceptions that might have occurred setting up
   * or querying the registry.
   * @return XML docuemnt object representing the result of the query.
   * @author Kevin Benson 
   */
   public Document submitQuery(Document query) throws AxisFault {
      log.debug("start submitQuery");
      long beginQ = System.currentTimeMillis();
      XSLHelper xslHelper = new XSLHelper();
            
      String versionNumber = getRegistryVersion(query);
      //parse query right now actually does the query.
      String xql = XQueryExecution.createXQL(query,versionNumber);
      log.info("Query to be performed on the db = " + xql);
      Node resultDoc = queryExist(xql,versionNumber);
      log.info("Time taken to complete submitQuery on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end submitQuery");

      //To be correct we need to transform the results, with a correct response element 
      //for the soap message and for the right root element around the resources.
      return xslHelper.transformExistResult(resultDoc,versionNumber,null);
   }
   
   /**
    * Queries for the Registry Resource element that is tied to this Registry.
    * All Astrogrid Registries have one Registry Resource tied to the Registry.
    * Which defines the AuthorityID's it manages and how to access the Registry.
    * 
    * @param query actually normally empty/null and is ignored.
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry. 
    * @return XML docuemnt object representing the result of the query.
    */
   public Document loadRegistry(Document query) throws AxisFault {
      log.debug("start loadRegistry");
      
      //get the default autority id for this registry.      
      Document doc = null;
      Document responseDoc = null;
      String versionNumber = getRegistryVersion(query);
      log.info("the versionNumber in loadRegistry = " + versionNumber);
      return loadMainRegistry(versionNumber);
   }

   /**
    * Queries for the Registry Resource element that is tied to this Registry.
    * All Astrogrid Registries have one Registry Resource tied to the Registry.
    * Which defines the AuthorityID's it manages and how to access the Registry.
    * 
    * @param version of the schema to be queryied on (the vr namespace); hence the collection
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry. 
    * @return XML docuemnt object representing the result of the query.
    */
   public Document loadMainRegistry(String versionNumber) throws AxisFault {
       long beginQ = System.currentTimeMillis();       

       String xqlString = QueryHelper.queryForMainRegistry(versionNumber);
       log.info("XQL String = " + xqlString);
       Node resultDoc = queryExist(xqlString,versionNumber);
       
       XSLHelper xslHelper = new XSLHelper();
       log.info("Time taken to complete loadRegistry on server = " +
               (System.currentTimeMillis() - beginQ));
       log.debug("end loadRegistry");
       
       //To be correct we need to transform the results, with a correct response element 
       //for the soap message and for the right root element around the resources.
       return xslHelper.transformExistResult(resultDoc,versionNumber,null);
   }
   
   /**
    * Queries the xml database, on the collection of the registry.
    * @param xqlString an XQuery to query the database
    * @param collectionName the location in the database to query (sort of like a table)
    * @return xml DOM object returned from the database, which are Resource elements
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    */
   private Node queryExist(String xqlString, String versionNumber) throws AxisFault {
      log.debug("start queryExist");
      Collection coll = null;
      int tempIndex = 0;
      try {
          String collectionName = "astrogridv" + versionNumber.replace('.','_');
          coll = xdb.openCollection(collectionName);
          log.info("Got Collection");
          QueryService xqs = xdb.getQueryService(coll);
          String returnCount = conf.getString("reg.amend.returncount");
          String xqlExpression = conf.getString("reg.custom.query.expression"); 
          xqlExpression = xqlExpression.replaceAll("__declareNS__", QueryHelper.getXQLDeclarations(versionNumber));
          //log.info(" the xqlExpression = " + xqlExpression);
          //xqlExpression = xqlExpression.replaceAll("regquery", xqlString);
          //log.info("the xqlString = " + xqlString);
          //xqlExpression = xqlExpression.replaceAll("__query__", xqlString);
          tempIndex = xqlExpression.indexOf("__query__");
          if(tempIndex == -1) {
              throw AxisFault.makeFault(new RegistryException("XQL Expression has no placement for a Query"));
          }
          String endString = xqlExpression.substring(tempIndex+9);
          xqlExpression = xqlExpression.substring(0,tempIndex);
          xqlExpression += xqlString + endString;
          log.info(" the xqlExpression = " + xqlExpression);
          xqlExpression = xqlExpression.replaceAll("__returnCount__", returnCount);
          log.info("Now querying in colleciton = " + collectionName + " query = " + xqlExpression);
          long beginQ = System.currentTimeMillis(); 
          ResourceSet rs = xqs.query(xqlExpression);
          log.info("Total Query Time = " + (System.currentTimeMillis() - beginQ));
          log.info("Number of results found in query = " + rs.getSize());
          if(rs.getSize() == 0) {
              NoResourcesFoundException nrfe = new NoResourcesFoundException("Nothing found with query = " + xqlExpression + " for collection = " + collectionName);
              throw AxisFault.makeFault(nrfe);
          }
          Resource xmlr = rs.getMembersAsResource();
          return DomHelper.newDocument(xmlr.getContent().toString());
      }catch(XMLDBException xdbe) {
          xdbe.printStackTrace();
          throw AxisFault.makeFault(xdbe);
      }catch(ParserConfigurationException pce) {
          pce.printStackTrace();
          throw AxisFault.makeFault(pce);
      }catch(SAXException sax) {
          sax.printStackTrace();
          throw AxisFault.makeFault(sax);
      }catch(IOException ioe) {
          ioe.printStackTrace();
          throw AxisFault.makeFault(ioe);
      }
      finally {
          try {
              xdb.closeCollection(coll);
          }catch(XMLDBException xmldb) {
              log.error(xmldb);
          }
      }
   }
   
   /**
    * Does not actually do a query, it opens the main root colleciton /db and finds all the child collections
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
    * A Keyword search web service method.  Gets the keywords from the soap body (also if the keywords are to be 'or' together)
    * The paths used for comparison with the keywords are obtained from the JNDI/properties file. The keywords seperated by spaces.
    * 
    * @param query - The soap body of the web service call, containing sub elements of keywords.
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    * @return XML docuemnt object representing the result of the query.
    */
   public Document KeywordSearch(Document query) throws AxisFault {
       log.debug("start keywordsearch");                   
       String keywords = null;
       String orValue = null;
       try {
           keywords = DomHelper.getNodeTextValue(query,"keywords");
           orValue = DomHelper.getNodeTextValue(query,"orValue");
       }catch(IOException ioe) {
           throw new AxisFault("IO problem trying to get keywords and orValue");
       }
       String attrVersion = getRegistryVersion(query);
       boolean orKeywords = new Boolean(orValue).booleanValue();
       return keywordQuery(keywords,orKeywords,attrVersion);
   }
   
   /**
    * A Keyword search baic method called from jsp pages.
    * The paths used for comparison with the keywords are obtained from the JNDI/properties file.
    * 
    * @param keywords - A string of keywords seperated by spaces.
    * @param orKeywords - Are the key words to be or'ed together
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    * @return XML docuemnt object representing the result of the query.
    */   
   public Document keywordQuery(String keywords, boolean orKeywords) throws AxisFault {
       return keywordQuery(keywords,orKeywords,RegistryServerHelper.getDefaultVersionNumber());
   }
   
   /**
    * A Keyword search method. Splits the keywords and forms a xql query for the key word search.
    * The paths used for comparison with the keywords are obtained from the JNDI/properties file.
    * 
    * @param query - The soap body of the web service call, containing sub elements of keywords.
    * @param orKeywords - Are the key words to be or'ed together
    * @param version - The version number from vr namespace used to form the collection name and get the xpaths from the properties. 
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    * @return XML docuemnt object representing the result of the query.
    */   
   public Document keywordQuery(String keywords, boolean orKeywords, String version) throws AxisFault {
       long beginQ = System.currentTimeMillis();
       if(version == null || version.trim().length() <= 0) {
           version = RegistryServerHelper.getDefaultVersionNumber();
       }       
       String versionNumber = version;
       String []keyword = keywords.split(" ");
       String xqlPaths = conf.getString("reg.custom.keywordxpaths." + versionNumber);
       String []xqlPath = xqlPaths.split(",");
       

       String xqlString = QueryHelper.getStartQuery(versionNumber);       
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
       XSLHelper xslHelper = new XSLHelper();
       log.info("Time taken to complete keywordsearch on server = " +
               (System.currentTimeMillis() - beginQ));
       log.debug("end keywordsearch");         
       
       //To be correct we need to transform the results, with a correct response element 
       //for the soap message and for the right root element around the resources.
       return xslHelper.transformExistResult(resultDoc,
                                             versionNumber,"KeywordSearchResponse");
   }
   
   public Document getAll(String versionNumber) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }
       
       String xqlString = QueryHelper.getAllQuery(versionNumber);
       Node resultDoc = queryExist(xqlString,versionNumber);
       return xslHelper.transformExistResult(resultDoc,
               versionNumber,"GetAllResponse");
   }
   
   public Document GetResourcesByIdentifier(Document query) throws AxisFault {
       log.debug("start GetResourcesByIdentifier");                   
       String ident = null;
       try {
           ident = DomHelper.getNodeTextValue(query,"identifier");
           log.info("found identifier in web service request = " + ident);
       }catch(IOException ioe) {
           throw new AxisFault("IO problem trying to get identifier");
       }
       String attrVersion = getRegistryVersion(query);
       return getResourcesByIdentifier(ident,attrVersion);
 }
   
   public Document getResourcesByIdentifier(String ivorn, String versionNumber) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }
       if(ivorn == null || ivorn.trim().length() <= 0) {
           throw new AxisFault("Cannot have empty or null identifier");
       }
       String queryIvorn = ivorn;
       //this is a hack for now delete later.  Some old client delegates might not pass the ivorn
       //the new delegates do.
       if(!Ivorn.isIvorn(ivorn))
           queryIvorn = "ivo://" + ivorn;
       String xqlString = QueryHelper.queryForResource(queryIvorn,versionNumber);
       Node resultDoc = queryExist(xqlString,versionNumber);
       return xslHelper.transformExistResult(resultDoc,
               versionNumber,"GetResourcesByIdentifier");
   }
   
   public Document GetResourceByIdentifier(Document query) throws AxisFault {
       log.debug("start GetResourcesByIdentifier");                   
       String ident = null;
       try {
           ident = DomHelper.getNodeTextValue(query,"identifier");
           log.info("found identifier in web service request = " + ident);
       }catch(IOException ioe) {
           throw new AxisFault("IO problem trying to get identifier");
       }
       String attrVersion = getRegistryVersion(query);
       return getResourceByIdentifier(ident,attrVersion);
   }
   

   public Document getResourceByIdentifier(String ivorn, String versionNumber) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }
       if(ivorn == null || ivorn.trim().length() <= 0) {
           throw new AxisFault("Cannot have empty or null identifier");
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
           XMLResource xmr = (XMLResource)xdb.getResource(coll,id);           
           return xslHelper.transformExistResult(xmr.getContentAsDOM(),
                       versionNumber,"GetResourceByIdentifier");
       }catch(XMLDBException xdbe) {
           throw AxisFault.makeFault(xdbe);
       }finally {
           try {
               xdb.closeCollection(coll);
           }catch(XMLDBException xmldb) {
               log.error(xmldb);
           }
       }
   }
   
   public Document getResourcesByAnyIdentifier(String ivorn, String versionNumber) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }

       String xqlString = QueryHelper.queryForAllResource(ivorn,versionNumber);
       Node resultDoc = queryExist(xqlString,versionNumber);
       return xslHelper.transformExistResult(resultDoc,
               versionNumber,"GetResourceByIdentifier");
   }
      

   /**
    * Queries and returns all the Resources that are Registry type resources.
    * Used by other registries as a convenience query to discover other
    * regiestries not known for harvesting.
    * 
    * @param query normally empty and is ignored, it is required though for the
    * Axis Document style method.  At most it will contain nothing more than the method
    * name.
    * @return Resource entries of type Registries.
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    */
   public Document GetRegistries(Document query) throws AxisFault {
      //DomHelper.DocumentToStream(query,System.out);

      String versionNumber = getRegistryVersion(query);
      return getRegistriesQuery(versionNumber);
   }
   
   public Document getRegistriesQuery(String versionNumber) throws AxisFault {
       long beginQ = System.currentTimeMillis();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }
       
       //Should declare namespaces, but it is not required so will leave out for now.
       String xqlString = QueryHelper.queryForRegistries(versionNumber);
       log.info("XQL String = " + xqlString);      
       Node resultDoc = queryExist(xqlString,versionNumber);
       XSLHelper xslHelper = new XSLHelper();
       log.info("Time taken to complete GetRegistries on server = " +
       (System.currentTimeMillis() - beginQ));
       log.debug("end loadRegistry");
       
       return xslHelper.transformExistResult(resultDoc,
               versionNumber,"GetRegistriesResponse");       
   }
   
   
   /**
    * Transforms ADQL to XQuery, uses the namespace of ADQL to allow the
    * transformations to handle different versions.  Transformations are done
    * by XSL stylesheets.
    * 
    * @param query ADQL DOM object 
    * @return xquery string
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    */   
   private String getQuery(Document query,String resourceVersion) throws AxisFault {
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
           throw new AxisFault("Could not find a version of the ADQL");
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
    * elements down it goes through a few child nodes.
    * 
    * @param query XML DOM of Resources
    * @return version number of the Registry.
    */
   private String getRegistryVersion(Document query) {
       log.info("in getRegistryversion");
       //log.info("print in getRegistryVersion" + DomHelper.DocumentToString(query));
       if(query == null) {
           return RegistryServerHelper.getRegistryVersionFromNode(query);
       }
       Element elem = query.getDocumentElement();
       if(elem == null) {
           return RegistryServerHelper.getRegistryVersionFromNode(query);
       }
       if(elem.hasChildNodes()) {
           return RegistryServerHelper.getRegistryVersionFromNode(
                         query.getDocumentElement().getFirstChild());    
       }
       return RegistryServerHelper.getRegistryVersionFromNode(
                                   query.getDocumentElement());    
   }
      
}
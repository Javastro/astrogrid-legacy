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
import org.astrogrid.registry.common.XSLHelper;
import java.net.URL;

import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.*;
import org.apache.axis.AxisFault;
import org.astrogrid.xmldb.eXist.server.QueryDBService;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.harvest.RegistryHarvestService;
import org.astrogrid.registry.server.RegistryServerHelper;
import org.astrogrid.registry.server.QueryHelper;

import java.util.ArrayList;

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
   private static final Log log = LogFactory.getLog(RegistryService.class);

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
      String attrVersion = getRegistryVersion(query);
      String versionNumber = attrVersion.replace('.','_');

      //transform the ADQL to an XQuery for the registry.
      String xqlQuery = getQuery(query,versionNumber);
      log.info("The XQLQuery = " + xqlQuery);
      
      //the location in the eXist db to be queries on.
      String collectionName = "astrogridv" + versionNumber;
      log.info("Collection Name for query = " + collectionName);

      //perform the query and log how long it took to query.
      Document resultDoc = queryExist(xqlQuery,collectionName);
      log.info("Time taken to complete search on server = " +
              (System.currentTimeMillis() - beginQ));
      log.debug("end Search");
      if(resultDoc != null) {
          log.info("Number of Resources to be returned = " + 
                   resultDoc.getElementsByTagNameNS("*","Resource").getLength());
      }
      
      //To be correct we need to transform the results, with a correct response element 
      //for the soap message and for the right root element around the resources.
      return xslHelper.transformExistResult((Node)resultDoc,
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
         
         String attrVersion = getRegistryVersion(query);
         String versionNumber = attrVersion.replace('.','_');
      
         String collectionName = "astrogridv" + versionNumber;
         log.info("Collection Name for query = " + collectionName);
         //query the eXist db.
         resultDoc = queryExist(xql,collectionName);
         if(resultDoc != null) {
             log.info("Number of Resources to be returned = " + 
                     resultDoc.getElementsByTagNameNS("*","Resource").getLength());
         }         
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
         String attrVersion = getRegistryVersion(query);
         String versionNumber = attrVersion.replace('.','_');

         String collectionName = "astrogridv" + versionNumber;     
         log.info("Collection Name for query = " + collectionName);
         
         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end XQLString");
         resultDoc = queryExist(xql,collectionName);
         if(resultDoc != null) {
             log.info("Number of Resources to be returned = " + 
                     resultDoc.getElementsByTagNameNS("*","Resource").getLength());
         }               
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
      
      String attrVersion = getRegistryVersion(query);
      String versionNumber = attrVersion.replace('.','_');
      String collectionName = "astrogridv" + versionNumber;
      log.info("Collection Name for query = " + collectionName);
      //log.info("received = " + DomHelper.DocumentToString(query));
      //parse query right now actually does the query.
      String xql = RegistryServerHelper.getXQLDeclarations(versionNumber) + XQueryExecution.createXQL(query);
      log.info("Query to be performed on the db = " + xql);
      Document resultDoc = queryExist(xql,collectionName);
      log.info("Time taken to complete submitQuery on server = " +
              (System.currentTimeMillis() - beginQ));
      if(resultDoc != null) {
          log.info("Number of Resources to be returned = " + 
                  resultDoc.getElementsByTagNameNS("*","Resource").getLength());
      }      
      log.debug("end submitQuery");

      //To be correct we need to transform the results, with a correct response element 
      //for the soap message and for the right root element around the resources.
      return xslHelper.transformExistResult((Node)resultDoc,versionNumber,null);
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
      String attrVersion = getRegistryVersion(query);
      String versionNumber = attrVersion.replace('.','_');
      log.info("the versionNumber in loadRegistry = " + versionNumber);
      return loadMainRegistry(versionNumber);
   }
   
   public Document loadMainRegistry(String versionNumber) throws AxisFault {
       long beginQ = System.currentTimeMillis();       
       String collectionName = "astrogridv" + versionNumber;
       log.info("Collection Name for query = " + collectionName);
       String xqlString = QueryHelper.queryForMainRegistry(versionNumber);
       log.info("XQL String = " + xqlString);
       Document resultDoc = queryExist(xqlString,collectionName);
       
       XSLHelper xslHelper = new XSLHelper();
       log.info("Time taken to complete loadRegistry on server = " +
               (System.currentTimeMillis() - beginQ));
       log.debug("end loadRegistry");
       
       //To be correct we need to transform the results, with a correct response element 
       //for the soap message and for the right root element around the resources.
       return xslHelper.transformExistResult((Node)resultDoc,versionNumber,null);
   }
   
   /**
    * Queries the eXist xml database, on the collection of the registry.
    * @param xqlString an XQuery to query the database
    * @param collectionName the location in the database to query (sort of like a table)
    * @return xml DOM object returned from the database, which are Resource elements
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    */
   private Document queryExist(String xqlString, String collectionName) throws AxisFault {
      log.debug("start queryExist");
      QueryDBService qdb = new QueryDBService();
      return qdb.query(collectionName,xqlString);
   }
   
   public ArrayList getAstrogridVersions() {
       QueryDBService qdb = new QueryDBService();
       ArrayList al = new ArrayList();
       try {
           Document doc = qdb.getCollection("");
           String xml = DomHelper.DocumentToString(doc);
           int index = -1;
           int temp = 0;
           
           while((index = xml.indexOf("astrogridv",temp)) != -1) {
               temp = xml.indexOf("\"", index+10);
               System.out.println("adding to the arraylist = " + ((String)xml.substring(index+10,temp)));
               al.add(((String)xml.substring(index+10,temp)));
           }
       }catch(MalformedURLException mue) {
           mue.printStackTrace();
       }catch(ParserConfigurationException pce) {
           pce.printStackTrace();
       }catch(IOException ioe) {
           ioe.printStackTrace();
       }catch(SAXException sax) {
           sax.printStackTrace();
       }
       return al;
   }
   
   public Document keywordQuery(String keywords, boolean orKeywords) throws AxisFault {
       return keywordQuery(keywords,orKeywords,RegistryServerHelper.getDefaultVersionNumber());
   }
   
   public Document keywordQuery(String keywords, boolean orKeywords, String version) throws AxisFault {
       long beginQ = System.currentTimeMillis();
       if(version == null || version.trim().length() <= 0) {
           version = RegistryServerHelper.getDefaultVersionNumber();
       }       
       String versionNumber = version.replace('.','_');
       String []keyword = keywords.split(" ");
       String xqlPaths = conf.getString("keyword.query.path." + versionNumber);
       String []xqlPath = xqlPaths.split(",");
       

       String xqlString = QueryHelper.getStartQuery(versionNumber);       
       for(int i = 0;i < xqlPath.length;i++) {
           xqlString += "(";
           for(int j = 0;j < keyword.length;j++) {
             xqlString += xqlPath[i] + " &= '*" + keyword[j] + "*'";
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
       
       String collectionName = "astrogridv" + versionNumber;
       Document resultDoc = queryExist(xqlString,collectionName); 
       if(resultDoc != null) {
           log.info("Number of Resources to be returned = " + 
                   resultDoc.getElementsByTagNameNS("*","Resource").getLength());
       }
       XSLHelper xslHelper = new XSLHelper();
       log.info("Time taken to complete keywordsearch on server = " +
               (System.currentTimeMillis() - beginQ));
       log.debug("end keywordsearch");         
       
       //To be correct we need to transform the results, with a correct response element 
       //for the soap message and for the right root element around the resources.
       return xslHelper.transformExistResult((Node)resultDoc,
                                             versionNumber,"KeywordSearchResponse");
   }
   
   public Document getAll(String versionNumber) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }
       String queryVersion = versionNumber.replace('.','_');
       String collectionName = "astrogridv" + queryVersion;
       log.info("collname=" + collectionName);
       String xqlString = QueryHelper.getAllQuery(queryVersion);
       Document resultDoc = queryExist(xqlString,collectionName);
       return xslHelper.transformExistResult((Node)resultDoc,
               queryVersion,"GetAllResponse");
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
       String queryVersion = versionNumber.replace('.','_');
       String collectionName = "astrogridv" + queryVersion;
       String xqlString = QueryHelper.queryForResource(ivorn,queryVersion);
       Document resultDoc = queryExist(xqlString,collectionName);
       return xslHelper.transformExistResult((Node)resultDoc,
               queryVersion,"GetResourceByIdentifier");
   }
   
   public Document getResourcesByAnyIdentifier(String ivorn, String versionNumber) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }
       String queryVersion = versionNumber.replace('.','_');
       String collectionName = "astrogridv" + queryVersion;
       String xqlString = QueryHelper.queryForAllResource(ivorn,queryVersion);
       Document resultDoc = queryExist(xqlString,collectionName);
       return xslHelper.transformExistResult((Node)resultDoc,
               queryVersion,"GetResourceByIdentifier");
   }
      
   public Document getResourcesByAuthority(String ivorn, String versionNumber) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }
       String queryVersion = versionNumber.replace('.','_');
       String collectionName = "astrogridv" + queryVersion;
       String xqlString = QueryHelper.queryForResourceByAuthority(ivorn,queryVersion);
       Document resultDoc = queryExist(xqlString,collectionName);
       return xslHelper.transformExistResult((Node)resultDoc,
               queryVersion,"GetResourceByAuthroity");
   }
   
      
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

      String attrVersion = getRegistryVersion(query);
      String versionNumber = attrVersion.replace('.','_');
      return getRegistriesQuery(versionNumber);
   }
   
   public Document getRegistriesQuery(String versionNumber) throws AxisFault {
       long beginQ = System.currentTimeMillis();
       if(versionNumber == null || versionNumber.trim().length() <= 0) {
           versionNumber = RegistryServerHelper.getDefaultVersionNumber();
       }
       String queryVersion = versionNumber.replace('.','_');
       String collectionName = "astrogridv" + queryVersion;
       log.info("Collection Name for query = " + collectionName);
       
       //Should declare namespaces, but it is not required so will leave out for now.
       String xqlString = QueryHelper.queryForRegistries(queryVersion);
       log.info("XQL String = " + xqlString);      
       Document resultDoc = queryExist(xqlString,collectionName);
       XSLHelper xslHelper = new XSLHelper();
       log.info("Time taken to complete GetRegistries on server = " +
       (System.currentTimeMillis() - beginQ));
       log.debug("end loadRegistry");
       
       return xslHelper.transformExistResult((Node)resultDoc,
               queryVersion,"GetRegistriesResponse");       
   }
   
   /**
    * Used by all the OAI required method interfaces to get the OAI
    * conformed Resources from a URL.  This URL is a servlet to query
    * the eXist database and put the XML in a OAI form.  The XML DOM returned
    * are all the Resources managed by this Registry.
    * @param oaiServlet a url string 
    * @return OAI conformed DOM object of all the Resourced managed by this Registry.
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    */
   private Document queryOAI(String oaiServlet) throws AxisFault {
      try {
        log.info("the oaiservlet url = '" + oaiServlet + "'");
        return DomHelper.newDocument(new URL(oaiServlet));
       }catch(MalformedURLException me) {
        throw new AxisFault("Incorrect url for calling oai servlet", me);
       }catch(ParserConfigurationException pce) {
         throw new AxisFault("Parser Config error", pce);
       }catch(SAXException sax) {
         throw new AxisFault("SAX problem parsing xml" , sax);
       }catch(IOException ioe) {
         throw new AxisFault("IO Problem", ioe);
       }    
   }
   
   private String getOAIServletURL(Document query) {
       String attrVersion = getRegistryVersion(query);
       String versionNumber = attrVersion.replace('.','_');       
       return conf.getString("oai.servlet.url." + versionNumber);       
   }

   /**
    * OAI-Identify conformed Web service method.
    * 
    * @param query actually this OAI mehtod requires nothing. 
    * @return XML DOM object conforming to the OAI Identify.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org 
    */
   public Document Identify(Document query) throws AxisFault {
      String oaiServlet = getOAIServletURL(query) + "?verb=Identify";
      Document resultDoc = queryOAI(oaiServlet);
      Element currentRoot = resultDoc.getDocumentElement();
      Element root = resultDoc.createElement("IdentifyResponse");
      root.appendChild(currentRoot);
      resultDoc.appendChild(root);
      return resultDoc;
   }

   /**
    * OAI-ListMetadataFormats conformed Web service method.
    * 
    * @param query contains an optional identifier string. 
    * @return XML DOM object conforming to the OAI ListMetadataFormats.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org 
    */
   public Document ListMetadataFormats(Document query) throws AxisFault {
      String oaiServlet = getOAIServletURL(query) + "?verb=ListMetadataFormats";       
      NodeList nl = null;
      if( (nl = query.getElementsByTagName("identifier")).getLength() > 0  )
           oaiServlet += "&identifier=" + nl.item(0).getFirstChild().getNodeValue(); 
      Document resultDoc = queryOAI(oaiServlet);
      Element currentRoot = resultDoc.getDocumentElement();
      Element root = resultDoc.createElement("ListMetadataFormatsResponse");
      root.appendChild(currentRoot);
      resultDoc.appendChild(root);
      return resultDoc;
   }

   /**
    * OAI-ListSets conformed Web service method. Currently not implemented.
    * 
    * @param query 
    * @return XML DOM object conforming to the OAI OAI-ListSets.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org 
    */   
   public Document ListSets(Document query) throws AxisFault {
    throw new AxisFault("Sorry but this method is currently not implemented");
   }

   /**
    * OAI-ResumeListSets conformed Web service method. Currently not implemented.
    * 
    * @param query 
    * @return XML DOM object conforming to the OAI OAI-ResumeListSets.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org 
    */   
   public Document ResumeListSets(Document query) throws AxisFault {
      throw new AxisFault("Sorry but this method is currently not implemented");
   }

   /**
    * OAI-GetRecord conformed Web service method.
    * 
    * @param query contains an identifier string and metadataPrefix. The prefix
    * is defaulted to the standard registry ivo_vor if not given. 
    * @return XML DOM object conforming to the OAI GetRecord.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org 
    */   
   public Document GetRecord(Document query) throws AxisFault {
       String oaiServlet = getOAIServletURL(query) + "?verb=GetRecord";       
       NodeList nl = null;
       if( (nl = query.getElementsByTagName("identifier")).getLength() > 0  ) 
          oaiServlet += "&identifier=" + nl.item(0).getFirstChild().getNodeValue();
       else
          throw new AxisFault("No Identifier given"); 
       if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
        oaiServlet += "&metadataPrefix=" + nl.item(0).getFirstChild().getNodeValue();
       else
        oaiServlet += "&metadataPrefix=ivo_vor";
       Document resultDoc = queryOAI(oaiServlet);
       //wrap it with a response method element.
       Element currentRoot = resultDoc.getDocumentElement();
       Element root = resultDoc.createElement("GetRecordResponse");
       root.appendChild(currentRoot);
       resultDoc.appendChild(root);
       return resultDoc;
   }

   /**
    * OAI-ListIdentifiers conformed Web service method.
    * 
    * @param query contains a metadataPrefix, and optional from and until 
    * @return XML DOM object conforming to the OAI ListIdentifiers.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org 
    */   
   public Document ListIdentifiers(Document query) throws AxisFault {
      String oaiServlet = getOAIServletURL(query) + "?verb=ListIdentifiers";
      NodeList nl = null;      
      if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
       oaiServlet += "&metadataPrefix=" + nl.item(0).getFirstChild().getNodeValue();
      else 
        oaiServlet += "&metadataPrefix=ivo_vor";        
      if( (nl = query.getElementsByTagName("from")).getLength() > 0  ) 
        oaiServlet += "&from=" + nl.item(0).getFirstChild().getNodeValue();
      if( (nl = query.getElementsByTagName("until")).getLength() > 0  )
        oaiServlet += "&until=" + nl.item(0).getFirstChild().getNodeValue();
      Document resultDoc = queryOAI(oaiServlet);
      Element currentRoot = resultDoc.getDocumentElement();
      Element root = resultDoc.createElement("ListIdentifiersResponse");
      root.appendChild(currentRoot);
      resultDoc.appendChild(root);
      return resultDoc;

   }

   /**
    * OAI-ResumeListIdentifiers conformed Web service method.
    * 
    * @param query contains a resumptionToken 
    * @return XML DOM object conforming to the OAI ResumeListIdentifiers.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org 
    */
   public Document ResumeListIdentifiers(Document query) throws AxisFault {
        String oaiServlet = getOAIServletURL(query);
        NodeList nl = null;
        if( (nl = query.getElementsByTagName("resumptionToken")).getLength() > 0  ) 
          oaiServlet += "?resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();
        else 
          throw new AxisFault("No resumptionToken found in ResumeListIdentifiers");
        Document resultDoc = queryOAI(oaiServlet);
        Element currentRoot = resultDoc.getDocumentElement();
        Element root = resultDoc.createElement("ResumeListIdentifiersResponse");
        root.appendChild(currentRoot);
        resultDoc.appendChild(root);
        return resultDoc;
   }

   /**
    * OAI-ListRecords conformed Web service method.
    * 
    * @param query contains a metadataPrefix, optional from&until elements. 
    * @return XML DOM object conforming to the OAI ListRecords.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org 
    */   
   public Document ListRecords(Document query) throws AxisFault {
        String oaiServlet = getOAIServletURL(query) + "?verb=ListRecords";
        NodeList nl = null;        
        if( (nl = query.getElementsByTagName("metadataPrefix")).getLength() > 0  )
         oaiServlet += "&metadataPrefix=" + nl.item(0).getNodeValue();
        else
         oaiServlet += "&metadataPrefix=ivo_vor";
        if( (nl = query.getElementsByTagName("from")).getLength() > 0  ) 
          oaiServlet += "&from=" + nl.item(0).getFirstChild().getNodeValue();
        if( (nl = query.getElementsByTagName("until")).getLength() > 0  )
          oaiServlet += "&until=" + nl.item(0).getFirstChild().getNodeValue();
        Document resultDoc = queryOAI(oaiServlet);
        Element currentRoot = resultDoc.getDocumentElement();
        Element root = resultDoc.createElement("ListRecordsResponse");
        root.appendChild(currentRoot);
        resultDoc.appendChild(root);
        return resultDoc;
   }

   /**
    * OAI-ResumeListRecords conformed Web service method.
    * 
    * @param query contains a resumptionToken 
    * @return XML DOM object conforming to the OAI ResumeListRecords.
    * @throws - AxisFault containing exceptions that might have occurred
    *  calling the servlet/url.
    * @link http://www.openarchives.org
    */
   public Document ResumeListRecords(Document query) throws AxisFault {
       String oaiServlet = getOAIServletURL(query);
       NodeList nl = null;       
       if( (nl = query.getElementsByTagName("resumptionToken")).getLength() > 0  ) 
         oaiServlet += "?resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();
       else 
           throw new AxisFault("No resumptionToken found in ResumeListRecords");       
       Document resultDoc = queryOAI(oaiServlet);
       Element currentRoot = resultDoc.getDocumentElement();
       Element root = resultDoc.createElement("ResumeListRecordsResponse");
       root.appendChild(currentRoot);
       resultDoc.appendChild(root);
       return resultDoc;          
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
           /*
           adqlVersion = DomHelper.getNodeAttrValue((Element)nl.item(0),"ad","xmlns");
           if(adqlVersion == null || adqlVersion.trim().length() == 0) {
               adqlVersion = DomHelper.getNodeAttrValue((Element)nl.item(0),"xmlns");
           }//if
           */
       }//if
       
       //throw an error if no version was found.
       if(adqlVersion == null || adqlVersion.trim().length() == 0) {
           throw new AxisFault("Could not find a version of the ADQL");
       }
       //get only the actual version number.
       adqlVersion = adqlVersion.substring(adqlVersion.lastIndexOf("v")+1);
       adqlVersion = adqlVersion.replace('.','_');
       //make the transformation using an xsl stylesheet.
       return xslHelper.transformADQLToXQL(query, adqlVersion, 
                        RegistryServerHelper.getRootNodeName(resourceVersion),
                        RegistryServerHelper.getXQLDeclarations(resourceVersion));
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
   
   public Document harvestResource(Document resources)  throws AxisFault {
       RegistryHarvestService rhs = new RegistryHarvestService();
       try {   
          rhs.harvestResource(resources,null);
       }catch(IOException ioe) {
          throw new AxisFault("IOE problem",ioe);
       }catch(RegistryException re) {
        throw new AxisFault("Registry exception", re);
       }
       return resources;      
   }   
   
}
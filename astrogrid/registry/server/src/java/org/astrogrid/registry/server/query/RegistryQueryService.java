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
      
      //transform the ADQL to an XQuery for the registry.
      String xqlQuery = getQuery(query);
      log.info("The XQLQuery = " + xqlQuery);
      
      //get the version of Resources we are querying on.
      String versionNumber = getRegistryVersion(query).replace('.','_');

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
                   resultDoc.getDocumentElement().getChildNodes().getLength());
      }
      
      //To be correct we need to transform the results, with a correct response element 
      //for the soap message and for the right root element around the resources.
      return xslHelper.transformExistResult((Node)resultDoc,
                                            versionNumber,"SearchResponse");
   }

   /**
    * Select - On certain Web Service styles a client may call the
    * Web Service with no method wrapped around in the SOAP body.
    * This method is for those cases when only the parameters are passed
    * in the soap body.  Hence this query method catches those cases and
    * queries the registry.
    * 
    * @param query - DOM object containing ADQL.
    * @throws - AxisFault containing exceptions that might have occurred setting up
    * or querying the registry.
    * @return - Resource DOM object of the Resources from the query of the registry. 
    * 
    */
   public Document Select(Document query) throws AxisFault {
      log.debug("start Select");
      long beginQ = System.currentTimeMillis();
      XSLHelper xslHelper = new XSLHelper();
      
      String versionNumber = getRegistryVersion(query).replace('.','_');      
      String collectionName = "astrogridv" + versionNumber;
      log.info("Collection Name for query = " + collectionName);
      
      //get the XQuery from the ADQL.
      String xqlQuery = getQuery(query);
      log.info("The XQLQuery = " + xqlQuery);
      
      //perform the query and log how long it took to query.      
      Document resultDoc = queryExist(xqlQuery,collectionName);
      log.info("Time taken to complete select on server = " +
              (System.currentTimeMillis() - beginQ));
      if(resultDoc != null) {
          log.info("Number of Resources to be returned = " + 
                   resultDoc.getDocumentElement().getChildNodes().getLength());
      }      
      log.debug("end select");

      //To be correct we need to transform the results, with a correct response element 
      //for the soap message and for the right root element around the resources.
      //With this web service styule it has no method name around the response
      //for the soap.
      return xslHelper.transformExistResult((Node)resultDoc,versionNumber,null);
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
      Document result = null;
      try {
         //get the xquery.
         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end Query");
         
         String versionNumber = getRegistryVersion(query).replace('.','_');      
         String collectionName = "astrogridv" + versionNumber;
         log.info("Collection Name for query = " + collectionName);
         //query the eXist db.
         result = queryExist(xql,collectionName);
         if(result != null) {
             log.info("Number of Resources to be returned = " + 
                      result.getDocumentElement().getChildNodes().getLength());
         }         
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);
      }
      return result;
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
      Document result = null;
      try {
         String versionNumber = getRegistryVersion(query).replace('.','_');
         String collectionName = "astrogridv" + versionNumber;     
         log.info("Collection Name for query = " + collectionName);
         
         String xql = DomHelper.getNodeTextValue(query,"XQLString");
         log.debug("end XQLString");
         result = queryExist(xql,collectionName);
         if(result != null) {
             log.info("Number of Resources to be returned = " + 
                      result.getDocumentElement().getChildNodes().getLength());
         }               
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);         
      }finally {
         return result;
      }
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
      String versionNumber = getRegistryVersion(query).replace('.','_');
      String collectionName = "astrogridv" + versionNumber;
      log.info("Collection Name for query = " + collectionName);
      //log.info("received = " + DomHelper.DocumentToString(query));
      //parse query right now actually does the query.
      String xql = XQueryExecution.createXQL(query);
      log.info("Query to be performed on the db = " + xql);
      Document resultDoc = queryExist(xql,collectionName);
      log.info("Time taken to complete submitQuery on server = " +
              (System.currentTimeMillis() - beginQ));
      if(resultDoc != null) {
          log.info("Number of Resources to be returned = " + 
                   resultDoc.getDocumentElement().getChildNodes().getLength());
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
      long beginQ = System.currentTimeMillis();
      //get the default autority id for this registry.
      String authorityID = conf.getString(AUTHORITYID_PROPERTY);
      authorityID = authorityID.trim();
      
      Document doc = null;
      Document responseDoc = null;
      String versionNumber = getRegistryVersion(query).replace('.','_');      
      String collectionName = "astrogridv" + versionNumber;
      log.info("Collection Name for query = " + collectionName);
      
      String xqlString = "//vr:Resource[vr:Identifier/vr:AuthorityID='" + authorityID +
                         "' and @xsi:type='RegistryType']";      
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

   public Document KeywordSearch(Document query) throws AxisFault {
      //DomHelper.DocumentToStream(query,System.out);
      try {
         String keywords = DomHelper.getNodeTextValue(query,"keywords");
         String orValue = DomHelper.getNodeTextValue(query,"orValue");
         String versionNumber = getRegistryVersion(query);
         String collectionName = "astrogridv" + versionNumber;
         boolean orIt = new Boolean(orValue).booleanValue();
         
      }catch(IOException ioe) {
         throw new AxisFault("IO problem", ioe);
      }
      
      return query;
   }
   
   public Document keywords(Document query) throws AxisFault {
      //return KeyWor
      return null;
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
      long beginQ = System.currentTimeMillis();
      String versionNumber = getRegistryVersion(query).replace('.','_');      
      String collectionName = "astrogridv" + versionNumber;
      log.info("Collection Name for query = " + collectionName);
      
      //Should declare namespaces, but it is not required so will leave out for now.
      String xqlString = "for $x in //vr:Resource where @xsi:type='RegistryType' return $x";
      log.info("XQL String = " + xqlString);
      
      Document resultDoc = queryExist(xqlString,collectionName);
      XSLHelper xslHelper = new XSLHelper();
      log.info("Time taken to complete GetRegistries on server = " +
      (System.currentTimeMillis() - beginQ));
      log.debug("end loadRegistry");
      
      return xslHelper.transformExistResult((Node)resultDoc,
              versionNumber,"GetRegistriesResponse");      
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
      String oaiServlet = conf.getString("oai.servlet.url") + "?verb=Identify";
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
      String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListMetadataFormats";       
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
       String oaiServlet = conf.getString("oai.servlet.url") + "?verb=GetRecord";       
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
      String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListIdentifiers";
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
        String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListIdentifiers";
        NodeList nl = null;
        if( (nl = query.getElementsByTagName("resumptionToken")).getLength() > 0  ) 
          oaiServlet += "&resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();
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
        String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListRecords";
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
       String oaiServlet = conf.getString("oai.servlet.url") + "?verb=ListRecords";
       NodeList nl = null;       
       if( (nl = query.getElementsByTagName("resumptionToken")).getLength() > 0  ) 
         oaiServlet += "&resumptionToken=" + nl.item(0).getFirstChild().getNodeValue();
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
   private String getQuery(Document query) throws AxisFault {
       XSLHelper xslHelper = new XSLHelper();       
       NodeList nl = null;
       //Get the main root element Select
       try {
           nl = DomHelper.getNodeListTags(query,"Select");
       }catch(IOException ioe) {
           throw new AxisFault("IOE problem finding Select element", ioe);
       }
       
       //find the namespace.
       String adqlVersion = null;
       if(nl != null && nl.getLength() > 0) {
           adqlVersion = DomHelper.getNodeAttrValue((Element)nl.item(0),"ad","xmlns");
           if(adqlVersion == null || adqlVersion.trim().length() == 0) {
               adqlVersion = DomHelper.getNodeAttrValue((Element)nl.item(0),"xmlns");
           }//if
       }//if
       
       //throw an error if no version was found.
       if(adqlVersion == null || adqlVersion.trim().length() == 0) {
           throw new AxisFault("Could not find a version of the ADQL");
       }
       //get only the actual version number.
       adqlVersion = adqlVersion.substring(adqlVersion.lastIndexOf("v")+1);
       //make the transformation using an xsl stylesheet.
       return xslHelper.transformADQLToXQL(query, adqlVersion.replace('.','_'));
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
       String regVersion = null;
       regVersion = DomHelper.getNodeAttrValue((Element)query.getDocumentElement(),"vr","xmlns");       
       if((regVersion == null || regVersion.trim().length() == 0) &&
           query.getDocumentElement().hasChildNodes() &&
           Node.ELEMENT_NODE == query.getDocumentElement().getFirstChild().getNodeType()) {           
          regVersion = DomHelper.getNodeAttrValue(
                 (Element)query.getDocumentElement().getFirstChild(),"vr","xmlns");
          if((regVersion == null || regVersion.trim().length() == 0) &&
             query.getDocumentElement().getFirstChild().hasChildNodes() &&
             Node.ELEMENT_NODE == query.getDocumentElement().getFirstChild().getFirstChild().getNodeType()) {             
             regVersion = DomHelper.getNodeAttrValue(
                     (Element)query.getDocumentElement().getFirstChild().getFirstChild(),"vr","xmlns");             
          }
       }
       
       if(regVersion == null || regVersion.trim().length() == 0) {
           regVersion = conf.getString("org.astrogrid.registry.version");
       }       
       regVersion = regVersion.substring((regVersion.lastIndexOf("v")+1));
       return regVersion;
   }
   
}
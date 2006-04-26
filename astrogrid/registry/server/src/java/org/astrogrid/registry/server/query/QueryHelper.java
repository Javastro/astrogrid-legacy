package org.astrogrid.registry.server.query;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import java.io.IOException;

import java.net.URL;
import java.net.MalformedURLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.registry.common.RegistryDOMHelper;
import org.astrogrid.registry.server.xmldb.XMLDBRegistry;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.server.SOAPFaultException;
import org.astrogrid.util.DomHelper;
import org.astrogrid.config.Config;
import org.astrogrid.registry.server.XSLHelper;
import org.astrogrid.store.Ivorn;

import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

public class QueryHelper {
    
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
    
    private XMLDBRegistry xdbRegistry = null;    
    
    /**
     * Logging variable for writing information to the logs
     */
     private static final Log log = LogFactory.getLog(DefaultQueryService.class);    
    
    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
       if(conf == null) {
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
       }
    }    
    
    public QueryHelper(String queryWSDLNS, String contractVersion, String voResourceVersion) {
        this.queryWSDLNS = queryWSDLNS;
        this.contractVersion = contractVersion;
        this.voResourceVersion = voResourceVersion;
        collectionName = "astrogridv" + voResourceVersion.replace('.','_');
        xdbRegistry = new XMLDBRegistry();
    }
    
    public QueryHelper(String voResourceVersion) {
        this(null, null, voResourceVersion);        
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
    public Document loadMainRegistry() {
        String xqlString = QueryConfigExtractor.queryForMainRegistry(voResourceVersion);
        log.info("XQL String = " + xqlString);
        Node resultDoc = queryRegistry(xqlString);        
        log.debug("end loadRegistry");
        
        return processQueryResults(resultDoc, "SearchResponse");
    }
    
        
    /**
     * Method: keywordQuery
     * Description: A Keyword search method. Splits the keywords and forms a xql query for the key word search.
     * The paths used for comparison with the keywords are obtained from the JNDI/properties file they are a comma
     * seperated xpath form.
     * 
     * @param query - String of keywords seperated by spaces.
     * @param orKeywords - Are the key words to be or'ed together
     * @param version - The version number from vr namespace used to form the collection name and get the xpaths from the properties. 
     * @return XML docuemnt object representing the result of the query.
     */   
    public Document keywordQuery(String keywords, boolean orKeywords) {
        long beginQ = System.currentTimeMillis();
        //split the keywords from there spaces
        String []keyword = keywords.split(" ");
        //get all the xpaths for the query.
        String xqlPaths = conf.getString("reg.custom.keywordxpaths." + voResourceVersion);
        //the xpaths are comma seperated split that as well.
        String []xqlPath = xqlPaths.split(",");
        
        //get the first part of the query which is basically to query on the
        //Resource element.
        String xqlString = QueryConfigExtractor.getStartQuery(voResourceVersion);
        
        //go through all the xpaths and buildup a keyword string.
        for(int i = 0;i < xqlPath.length;i++) {
            xqlString += " (";
            for(int j = 0;j < keyword.length;j++) {
              xqlString += "$x/" + xqlPath[i].trim() + " &= '*" + keyword[j] + "*'";
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
        
        Node resultDoc = queryRegistry(xqlString); 
        log.info("Time taken to complete keywordsearch on server = " +
                (System.currentTimeMillis() - beginQ));
        log.debug("end keywordsearch");         
        
        //To be correct we need to transform the results, with a correct response element 
        //for the soap message and for the right root element around the resources.
        return processQueryResults(resultDoc, "SearchResponse");
    }
    
    /**
     * Method: getAll
     * Description: Conventient method for the browse all jsp page which queries the entire
     * collection in the registry based on a version number.
     * @param versionNumber version number to form the collection(like a table) to query on.
     * @return XML docuemnt object representing the result of the query.
     */
    public Document getAll() {    
        String xqlString = QueryConfigExtractor.getAllQuery(voResourceVersion);
        Node resultDoc = queryRegistry(xqlString);
        return processQueryResults(resultDoc, "GetAllResponse");
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
    public Document getResourcesByIdentifier(String ivorn) {
        if(ivorn == null || ivorn.trim().length() <= 0) {
            return SOAPFaultException.createQuerySOAPFaultException("Server Error: Cannot have empty or null identifier","Cannot have empty or null identifier");
        }
        String queryIvorn = ivorn;
        //this is a hack for now delete later.  Some old client delegates might not pass the ivorn
        //the new delegates do.
        if(!Ivorn.isIvorn(ivorn))
            queryIvorn = "ivo://" + ivorn;
        String xqlString = QueryConfigExtractor.queryForResource(queryIvorn,voResourceVersion);
        Node resultDoc = queryRegistry(xqlString);
        return processQueryResults(resultDoc, "GetResourceByIdentifier");
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
    public Document getResourceByIdentifier(String ivorn) {
        if(ivorn == null || ivorn.trim().length() <= 0) {
            return SOAPFaultException.createQuerySOAPFaultException("Server Error: Cannot have empty or null identifier","Cannot have empty or null identifier");
        }
        return getResourcesByIdentifier(ivorn);

        /*
        String queryIvorn = ivorn;
        if(Ivorn.isIvorn(ivorn)) { 
            queryIvorn = ivorn.substring(6);
        }
        
        String id = queryIvorn.replaceAll("[^\\w*]","_");
        try {
            XMLResource xmr = xdbRegistry.getResource(id, collectionName);              
            if(xmr == null || xmr.getContentAsDOM() == null) {
                return SOAPFaultException.createQuerySOAPFaultException("Resource Not Found ivorn = " + ivorn,
                                                                        "Resource Not Found ivorn = " + ivorn);
            }
            Document resDoc = (Document)xmr.getContentAsDOM();
            return processQueryResults(resDoc.getDocumentElement(), "GetResourceByIdentifier");
        }catch(XMLDBException xdbe) {
            return SOAPFaultException.createQuerySOAPFaultException(xdbe.getMessage(),xdbe);
        }
        */
    }
    

    public Document getResourcesByAnyIdentifier(String ivorn) {
        String xqlString = QueryConfigExtractor.queryForAllResource(ivorn,voResourceVersion);
        Node resultDoc = queryRegistry(xqlString);
        return processQueryResults(resultDoc, "GetResourceByIdentifier");
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
    public Document getRegistriesQuery() {
        String xqlString = QueryConfigExtractor.queryForRegistries(voResourceVersion);             
        Node resultDoc = queryRegistry(xqlString);
        return processQueryResults(resultDoc, "GetRegistriesResponse");       
    }
    
    
    
    
    /**
     * Method: getAstrogridVersions
     * Description: Does not actually do a query, it opens the main root colleciton /db and finds all the child collections
     * associated with astrogridv?? (??=version number) and puts them as strings in an array list to be returned.
     * 
     * @return an ArrayList of Strings containging the versions number supported by this registry (or in the xml db).
     */
    public static ArrayList getAstrogridVersions() throws XMLDBException {
        XMLDBRegistry xdbRegistry = new XMLDBRegistry();
        ArrayList al = new ArrayList();
        String []childCollections = xdbRegistry.listRootCollections();
        for(int i = 0;i < childCollections.length;i++) {
            if(childCollections[i].startsWith("astrogridv")) {
                al.add(((String)childCollections[i].substring(10).replace('_','.')));    
            }
        }
        return al;
    }
    
    
    /**
     * Method: queryRegistry
     * Description: Queries the xml database, on the collection of the registry. This method
     * will read from properties a xql expression and fill out the expression then perform the query. This
     * is a convenience in case of customization for other xml databases.
     * @param xqlString an XQuery to query the database
     * @param collectionName the location in the database to query (sort of like a table)
     * @return xml DOM object returned from the database, which are Resource elements
     */
    public Node queryRegistry(String xqlString) {
        
       log.debug("start queryRegistry");
       Collection coll = null;
       int tempIndex = 0;
       try {
           String returnCount = conf.getString("reg.amend.returncount","100");
           //get the xquery expression.
           String xqlExpression = conf.getString("reg.custom.query.expression"); 
           xqlExpression = xqlExpression.replaceAll("__declareNS__", QueryConfigExtractor.getXQLDeclarations(voResourceVersion));
           //log.info(" the xqlExpression = " + xqlExpression);
           //xqlExpression = xqlExpression.replaceAll("regquery", xqlString);
           //log.info("the xqlString = " + xqlString);
           //xqlExpression = xqlExpression.replaceAll("__query__", xqlString);
           tempIndex = xqlExpression.indexOf("__query__");
           if(tempIndex == -1) {
               return SOAPFaultException.createQuerySOAPFaultException("Server Error: XQL Expression has no placement for a Query",
                                                                       "XQL Expression has no placement for a Query");
           }
           //todo: check into this again, for some reason could not do a replaceAll so currently placing
           //in the string the hard way.
           String endString = xqlExpression.substring(tempIndex+9);
           xqlExpression = xqlExpression.substring(0,tempIndex);
           xqlExpression += xqlString + endString;          
           xqlExpression = xqlExpression.replaceAll("__returnCount__", returnCount);
           
           ResourceSet xmlrSet = xdbRegistry.query(xqlExpression, collectionName);
           if(xmlrSet.getSize() == 0) {
               return DomHelper.newDocument();
           }
           Resource xmlr = xmlrSet.getMembersAsResource();          
           return DomHelper.newDocument(xmlr.getContent().toString());
       }catch(XMLDBException xdbe) {
           xdbe.printStackTrace();
           return SOAPFaultException.createQuerySOAPFaultException("Server Error: " + xdbe.getMessage(),xdbe);
       } catch(ParserConfigurationException pce) {
           pce.printStackTrace();
           return SOAPFaultException.createQuerySOAPFaultException("Server Error: " + pce.getMessage(),pce);
       }catch(SAXException sax) {
           sax.printStackTrace();
           return SOAPFaultException.createQuerySOAPFaultException("Server Error: " + sax.getMessage(),sax);
       }catch(IOException ioe) {
           ioe.printStackTrace();
           return SOAPFaultException.createQuerySOAPFaultException("Server Error: " + ioe.getMessage(),ioe);
       }
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
    public String getQuery(Document query) throws Exception {                                               
        //String adqlVersion = org.astrogrid.registry.common.RegistryDOMHelper.findADQLVersionFromNode((Node)query.getDocumentElement());
        String adqlVersion = RegistryDOMHelper.findADQLVersionFromNode(query.getDocumentElement());
        
        //throw an error if no version was found.
        if(adqlVersion == null || adqlVersion.trim().length() == 0) {
            throw new Exception("No ADQL version found, hence do not know how to translate the adql to a xquery");           
        }
        
        XSLHelper xslHelper = new XSLHelper();
        return xslHelper.transformADQLToXQL(query, adqlVersion, 
                         QueryConfigExtractor.getRootNodeName(voResourceVersion),"");
    }    
    
    protected Document processQueryResults(Node resultDoc, String responseWrapper) {
        if(contractVersion == null) {
            //something internal to the registry like updates are getting results, but does not
            //care about the actual contract query performed.  So just return the document.
            return (Document)resultDoc;
        }
        return ProcessResults.processQueryResults(resultDoc,queryWSDLNS, contractVersion, responseWrapper);   
    }    
    
}
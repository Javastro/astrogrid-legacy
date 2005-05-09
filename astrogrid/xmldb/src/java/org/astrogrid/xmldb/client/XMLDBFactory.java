package org.astrogrid.xmldb.client;

import org.astrogrid.config.Config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import java.net.MalformedURLException;

import java.net.URL;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import org.astrogrid.util.DomHelper;
import org.apache.axis.AxisFault;

import java.io.DataOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import org.xmldb.api.modules.XQueryService;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;
import org.xmldb.api.base.Service;

import org.exist.xmldb.DatabaseInstanceManager;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.XMLDBException;
import javax.xml.transform.OutputKeys;


/**
 * Class: XMLDBFactory
 * Purpose: A Factory for storing and querying a XMLDB:API implemented database. 
 * 
 * @author Kevin Benson
 */
public class XMLDBFactory {

    private static final Log log = LogFactory.getLog(XMLDBFactory.class);
        
    private static final String DEFAULT_EXIST_DRIVER_URI = "xmldb:exist://";
    
    public static Config conf = null;
    
    private static String xmldbURI = null;
    
    private static String dbDriver = null;
    
    private static boolean dbInitialized = false;
    
    private static String queryUser = null;
    
    private static String queryPassword = null;
    
    private static String adminUser = null;    
    
    private static String adminPassword = null;    
    
    static {
       if(conf == null) {
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
          //obtain the xmldbur from the properties.  This will determine normally if this is an
          //internal or external db as well.
          xmldbURI = conf.getString("xmldb.uri","xmldb:exist://");
          //database driver for the implenting database. similiar to jdbc driver.
          dbDriver = conf.getString("xmldb.driver","org.exist.xmldb.DatabaseImpl");
          
          adminUser = conf.getString("xmldb.admin.user", null);
          if(adminUser != null && adminUser.trim().length() <= 0) 
              adminUser = null;
          adminPassword = conf.getString("xmldb.admin.password", "");
          
          queryUser = conf.getString("xmldb.query.user", null);
          if(queryUser != null && queryUser.trim().length() <= 0) 
              queryUser = null;

          queryPassword = conf.getString("xmldb.query.password", "");          
       }//if
    }
    
    /**
     * XMLDBFactory constructor of the factory.
     * @throws IllegalStateException if the database has not been initialied.
     */
    public XMLDBFactory()  throws IllegalStateException 
                                  {
        if(!dbInitialized) {
            log.error("Database initialization has not been done, Static methods are provided for doing this or more commonly use the Servlet EXistDatabaeManager.");
            log.error("You may call the static methods registerDB and shutdownDB to register and shutdown the database, shutdown will NOT happen if the database is not internal in-process");
            throw new IllegalStateException("Database inilization has not been done and the database is not registered, A servlet was supposed to initlize the database.");
        }
    }
    
    /**
     * Method: setDBInitlized
     * Purpose: set rather the database is initlized or not.
     * 
     */
    public static void setDBInitialized(boolean dbInit) {
        dbInitialized = dbInit;
    }
    
    /**
     * Method: registerDB
     * Purpose: register a XMLDB database.  And to create the database if necessary on internal
     * db instances.
     * @param confFile - a configuration file for internal database normally used on eXist xmldb.
     * @throws IllegalAccessException cannot access the class of the database driver.
     * @throws ClassNotFoundException cannot find the class of the database driver.
     * @throws InstantiationException cannot instantiate the class of the database driver
     * @throws XMLDBException cannot register the database driver.
     */
    public static void registerDB(String configuration)  throws IllegalAccessException, 
                                     ClassNotFoundException, 
                                     InstantiationException, 
                                     XMLDBException  {
        if(!dbInitialized) {
            Class cl = Class.forName( dbDriver );
            Database database = (Database) cl.newInstance();
            if(runningInternalEXist()) {
                database.setProperty( "create-database", "true" );
                log.info("set config file for reading = " + configuration);
                database.setProperty("configuration",configuration);
            }
            DatabaseManager.registerDatabase( database );
            setDBInitialized(true);
        }//if
    }

    /**
     * Method: shutdownDB
     * Purpose: Only for internal eXist xmldb use.  Shuts down the eXist database on the closing down of the servlet container.
     * @throws XMLDBException cannot shutdown the database or obtain the root collection.
     */    
    public static void shutdownDB() throws XMLDBException {
        if(runningInternalEXist()) {
            log.info("try shutting down database");
            XMLDBFactory xmldbFactory = new XMLDBFactory();
            Collection coll = xmldbFactory.openAdminCollection();
            //Collection coll = DatabaseManager.getCollection( xmldbURI + "/db", "admin", "" );
            DatabaseInstanceManager mgr = (DatabaseInstanceManager)coll.getService("DatabaseInstanceManager", "1.0");
            mgr.shutdown();
            log.info("databae shutdown");
        }
    }
    
    /**
     * Method: runningInternalEXist
     * Purpose: check if were running the eXist XML database internally.
     * @return boolean if the database is eXist and running internally.
     */    
    private static boolean runningInternalEXist() {
        if(DEFAULT_EXIST_DRIVER_URI.equals(xmldbURI)) {
            return true;
        }
        return false;
    }

    /**
     * Method: openCollection
     * Purpsoe: open a basic or query type xmldb collection to the root collection "/db".  Uses standard query admin and password if any given in the properties/JNDI.
     * @return XMLDB Collection object
     */
    public Collection openCollection() throws XMLDBException {
        log.info("Opening Basic Collection (Query/View)");
        return openCollection(null);
    }

    /**
     * Method: openCollection
     * Purpsoe: open a basic or query type xmldb collection to given collection.  Uses standard query admin and password if any given in the properties/JNDI.
     * @param String collection path - after the "/db" root collection.
     * @return XMLDB Collection object
     */
    public Collection openCollection(String collection) throws XMLDBException  {
        log.info("Opening Basic Collection (Query/View)");
        return openXMLDBCollection(collection, queryUser, queryPassword);
    }

    /**
     * Method: openAdminCollection
     * Purpsoe: open a admin type xmldb collection to the root collection "/db".  Uses management/admin  admin and password if any given in the properties/JNDI. Use for storing or managing the database.
     * @return XMLDB Collection object
     * @throws XMLDBException if the collection cannot be open for such things as username and password needed or incorrect. 
     * 
     */    
    public Collection openAdminCollection() throws XMLDBException {
        log.info("Opening Admin Collection (Store/Manage)");
        return openAdminCollection(null);
    }

    /**
     * Method: openAdminCollection
     * Purpsoe: open a admin type xmldb collection to given collection.  Uses management/admin admin and password if any given in the properties/JNDI. Use for storing or managing the database.
     * @param String collection path - after the "/db" root collection.
     * @return XMLDB Collection object
     * @throws XMLDBException if the collection cannot be open for such things as username and password needed or incorrect. 
     */    
    public Collection openAdminCollection(String collection) throws XMLDBException {
        log.info("Opening Admin Collection (Store/Manage)");
        return openXMLDBCollection(collection,adminUser, adminPassword);
    }
    
    /**
     * Method: openXMLDBCollection
     * Purpose: To open a Collection object to the XMLDB database given the collection path, username (if given), and password.  If no collection path is given then open to the root collection "/db".
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database.
     * @param user A username to be used for opening this collection.
     * @param password A password to be used for the user opening this collection. 
     * @return XMLDB Collection object
     * @throws XMLDBException if the collection cannot be open for such things as username and password needed or incorrect. 
     * 
     */
    private Collection openXMLDBCollection(String collection,String user, String password) throws XMLDBException  {
        String collectionPath = xmldbURI + "/db";
        // now add any othe sub-collections if any.
        if(collection != null && collection.trim().length() > 0) {
            collectionPath += "/" + collection;
        }
        log.info("opening the collection = " + collectionPath);
        Collection coll = null;
        if(user == null)
            coll = DatabaseManager.getCollection( collectionPath );
        else
            coll = DatabaseManager.getCollection( collectionPath, user, password );
        
        if(coll == null) {
          // collection does not exist: get root collection and create.
          // for simplicity, we assume that the new collection is a
          // direct child of the root collection, e.g. /db/test.
          // the example will fail otherwise.
          Collection root = null;
          if(user == null)
              root = DatabaseManager.getCollection(xmldbURI + "/db");
          else
              root = DatabaseManager.getCollection(xmldbURI + "/db", user, password);
              
          CollectionManagementService mgtService = 
              (CollectionManagementService)root.getService("CollectionManagementService", "1.0");

          log.info("creating collection = " + collection);
          coll = mgtService.createCollection(collection);
        }
        log.info("opened collection = " + collectionPath); 
        return coll;
    }    
    
    

    /**
     * Method: closeCollection
     * Purpose: Close the given XMLDB collection object and releasing all its resources.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database. 
     * @throws XMLDBException if the collection cannot be closed. 
     */
    public void closeCollection(Collection coll) throws XMLDBException {
        if(coll != null && coll.isOpen()) {
            log.info("closing collection");
            coll.close();
        }
    }

    /**
     * Method: getXUpdateService
     * Purpose: give back the standard xmldb XUpdateQueryService from a given collection.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database. 
     * @throws XMLDBException if the service cannot be found.
     * @return XUpdateQueryService for updating the xmldb.
     */
    public XUpdateQueryService getXUpdateQueryService(Collection coll) throws XMLDBException  {
        // get query-service
        XUpdateQueryService service =
            (XUpdateQueryService) coll.getService( "XUpdateQueryService", "1.0" );
        service.setProperty( OutputKeys.INDENT, "yes" );
        service.setProperty( OutputKeys.ENCODING, "UTF-8" );
        return service;
    }
    
    /**
     * Method: getQueryService
     * Purpose: Give back this common query service that can be used on XQueryService and XPathQueryService types.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database. 
     * @throws XMLDBException if the service cannot be found. 
     * @return comon QueryService to be used for Xpath or Xquery service types.
     */
    public QueryService getQueryService(Collection coll)  throws XMLDBException {
        return new QueryServiceImpl(coll);
    }
    
    /**
     * Method: getXQueryService
     * Purpose: get a XQuery Service type from the XMLDB:API.
     * @throws XMLDBExceptin exception finding a XQueryService
     * @return XQueryService
     */
    public XQueryService getXQueryService(Collection coll)  throws XMLDBException {
        XQueryService service =
            (XQueryService) coll.getService( "XQueryService", "1.0" );
        service.setProperty( OutputKeys.INDENT, "yes" );
        service.setProperty( OutputKeys.ENCODING, "UTF-8" );
        return service;
    }
    
    /**
     * Method: getXPathQueryService
     * Purpose: get a XPath Query Service type from the XMLDB:API.
     * @throws XMLDBExceptin exception finding a XPathQueryService
     * @return XPathQueryService
     */
    public XPathQueryService getXPathQueryService(Collection coll)  throws XMLDBException {
        XPathQueryService service =
            (XPathQueryService) coll.getService( "XPathQueryService", "1.0" );
        service.setProperty( OutputKeys.INDENT, "yes" );
        service.setProperty( OutputKeys.ENCODING, "UTF-8" );
        return service;
    }
    


    /**
     * Method: getResource
     * Purpose: Give back one resource known in the database by its id.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database.
     * @id A uniquie id in the given collection. 
     * @throws XMLDBException if the resource cannot be found (id does not exist); or collection mysteriously closed.
     * @return Resource object from the given id. 
     */
    public Resource getResource(Collection coll, String id)  throws XMLDBException {
        return coll.getResource(id);        
    }
    
    public void removeResource(Collection coll, String id) throws XMLDBException {
        Resource res = getResource(coll,id);
        if(res != null)
            coll.removeResource(res);
        
    }
    
    /**
     * Method: storeXMLResource
     * Purpose: Store a XML based resource from a given node into a given collection. A random id will be assigned.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database.
     * @param Node a w3c.dom.Node object for storing XML into the database.  
     * @throws XMLDBException if something is goes wrong in the storing mechanism. 
     */
    public void storeXMLResource(Collection coll, Node content)  throws XMLDBException {
        storeXMLResource(coll,null,content);        
    }

    /**
     * Method: storeXMLResource
     * Purpose: Store a XML based resource from a given node into a given collection.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database.
     * @param id A given unique name to the XML resource (unique to the collection).
     * @param Node a w3c.dom.Node object for storing XML into the database. 
     * @throws XMLDBException if something is goes wrong in the storing mechanism. 
     */
    public void storeXMLResource(Collection coll, String id, Node content)  throws XMLDBException {
        log.info("enter storeXMLResource for id (if null then will be created) = " + id);
        XMLResource objRes = (XMLResource)coll.createResource(id, "XMLResource");
        
        objRes.setContentAsDOM(content);
        log.info("content is set now storing for id = " + id);
        coll.storeResource(objRes);
    }
    
    /**
     * Method: storeXMLResource
     * Purpose: Store a XML based resource from a given xml string into a given collection. A random id will be assigned.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database.
     * @param String a string of xml.  
     * @throws XMLDBException if something is goes wrong in the storing mechanism. 
     */  
    public void storeXMLResource(Collection coll, String xml) throws XMLDBException {
        storeXMLResource(coll,null,xml);
    }
    
    /**
     * Method: storeXMLResource
     * Purpose: Store a XML based resource from a given xml string into a given collection.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database.
     * @param id A given unique name to the XML resource (unique to the collection).
     * @param String a string of xml. 
     * @throws XMLDBException if something is goes wrong in the storing mechanism. 
     */    
    public void storeXMLResource(Collection coll, String id, String xml) throws XMLDBException {
        log.info("enter storeXMLResource for id (if null then will be created) = " + id);
        XMLResource objRes = (XMLResource)coll.createResource(id, "XMLResource");
        objRes.setContent(xml);
        log.info("content is set now storing for id = " + id);
        coll.storeResource(objRes);        
    }
    

    /**
     * Method: storeResource
     * Purpose: Store a XML/Binary based resource from a given object into a given collection.
     * @param Collection the given open collection to a paticular collection (like table) in the xmldb database.
     * @param id A given unique name to the XML/Binary resource (unique to the collection).
     * @param Object A particular object for storing into the database, depending on the object will be stored as XML/Binary.  May even give File objects for sotring XML. 
     * @throws XMLDBException if something is goes wrong in the storing mechanism. 
     */    
    public void storeResource(Collection coll, String id, Object content)  throws XMLDBException {
        Resource objRes = null;
        if(content instanceof Node) {
            objRes = coll.createResource(id, "XMLResource");
        } else {
            objRes = coll.createResource(id, "BinaryResource");

        }
        objRes.setContent(content);
        coll.storeResource(objRes);
    }    
}
package org.astrogrid.registry.server.admin;

import javax.servlet.*;
import javax.servlet.http.*;

import org.astrogrid.config.Config;
import org.astrogrid.xmldb.client.XMLDBFactory;
import org.exist.xmldb.DatabaseInstanceManager;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class ExistDatabaseManager
 * Purpose: Small servlet class used for registering xmldb databases.  It is a little more specefic to eXist only for shutdown
 * purposes.   This servlet should be defined in a servlet containers web.xml and load-on-startup should be set to 
 * make sure it is loaded.
 * @author Kevin Benson
 *
 */
public class ExistDatabaseManager extends HttpServlet {
   
   //Constant variable to deterimen if this is an internal eXist db being ran. 
   private static final String DEFAULT_EXIST_DRIVER_URI = "xmldb:exist://";

   //instantiate the Logging for the class.
   private static final Log log = LogFactory.getLog(ExistDatabaseManager.class);   

   //xmldb database driver to be registered
   private static String dbDriver = null;

   //xmldb uri to the location of the database for using the XMLDB:API
   private static String xmldbURI = null;   
   
   //Standard configuration class for obtaining entries in JNDI or property files.
   public static Config conf = null;
   

   static {
      if(conf == null) {
         conf = org.astrogrid.config.SimpleConfig.getSingleton();
         xmldbURI = conf.getString("xmldb.uri","xmldb:exist://");
         dbDriver = conf.getString("xmldb.driver","org.exist.xmldb.DatabaseImpl");
      }
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
   public void registerDB(String confFile)  throws IllegalAccessException, 
                                           ClassNotFoundException, 
                                           InstantiationException, 
                                           XMLDBException  {
       log.info("Try registering database");
           Class cl = Class.forName( dbDriver );
           Database database = (Database) cl.newInstance();
           //ClassLoader loader = this.getClass().getClassLoader();
           //return loader.getResourceAsStream(DEFAULT_DATABASE_XSL);
    
           if(runningInternalEXist()) {
               database.setProperty("create-database", "true" );
               log.info("set config file for reading = " + confFile);
               database.setProperty("configuration",confFile);
           }
           DatabaseManager.registerDatabase( database );
           XMLDBFactory.setDBInitialized(true);   
           log.info("database registered");
   }

   /**
    * Method: shutdownDB
    * Purpose: Only for internal eXist xmldb use.  Shuts down the eXist database on the closing down of the servlet container.
    * @throws XMLDBException cannot shutdown the database or obtain the root collection.
    */
   public void shutdownDB() throws XMLDBException {
       if(runningInternalEXist()) {
           log.info("try shutting down database");
           XMLDBFactory xmldbFactory = new XMLDBFactory();
           Collection coll = xmldbFactory.openAdminCollection();
           //Collection coll = DatabaseManager.getCollection( xmldbURI + "/db", "admin", "" );
           DatabaseInstanceManager mgr = (DatabaseInstanceManager)coll.getService("DatabaseInstanceManager", "1.0");
           mgr.shutdown();
           log.info("database shutdown");
       }
   }

   /**
    * Method: runningInternalEXist
    * Purpose: check if were running the eXist XML database internally.
    * @return boolean if the database is eXist and running internally.
    */
   private boolean runningInternalEXist() {
       if(DEFAULT_EXIST_DRIVER_URI.equals(xmldbURI)) {
           return true;
       }
       return false;
   }

   /**
    * Method: destroy
    * Purpose: Common Servlet API destroy method called when servlet container goes down.  Currently shuts down the database.
    */
   public void destroy() {
       super.destroy();
       try {
           shutdownDB();
       }catch(XMLDBException xdbe) {
           log.error("COULD NOT SHUTDOWN THE DATABASE");
           log.error(xdbe);
           log.error(xdbe.toString());
           log.error(xdbe.getMessage());
           xdbe.printStackTrace();
       }
   }

   /**
    * Method init
    * Purpose: Common Servlet API initialization method to initialize a servlet.  Currently registers the database and starts up the database if necessary.
    */
   public void init(ServletConfig config)
                throws ServletException
   {
	    super.init(config);
       //System.out.println("inside init of EXistDatabaseManager try registering databae");
       log.info("inside init of EXistDatabaseManager try registering databae");
       try {
           String dbConfig = conf.getString("reg.custom.exist.configuration",null);
           String configuration = null;
           if(dbConfig == null || dbConfig.trim().length() <= 0) {
               dbConfig = config.getInitParameter("basedir");           
               configuration = config.getInitParameter("configuration");
               if(configuration == null || configuration.trim().length() <= 0) 
                   configuration = "conf.xml";
               dbConfig = (dbConfig == null) ? config.getServletContext().getRealPath(
                ".") : config.getServletContext().getRealPath(dbConfig);
                if(dbConfig.endsWith("/"))
                    dbConfig += configuration;
                else
                    dbConfig += "/" + configuration;
           }
           registerDB(dbConfig);
       }catch(IllegalAccessException iae) {
           log.error(iae);
       }catch(ClassNotFoundException cnfe) {
           log.error(cnfe);
       }catch(InstantiationException ie) {
           log.error(ie);
       }catch(XMLDBException xdbe) {
           log.error(xdbe);
       }
   }
   
}
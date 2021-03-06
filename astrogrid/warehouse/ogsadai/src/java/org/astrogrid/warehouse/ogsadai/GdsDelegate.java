package org.astrogrid.warehouse.ogsadai;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Properties;
import javax.xml.namespace.QName;
import org.apache.axis.client.Stub;
import org.globus.ogsa.utils.AnyHelper;
import org.gridforum.ogsi.EntryType;
import org.gridforum.ogsi.ExtensibilityType;
import org.gridforum.ogsi.HandleType;
import org.gridforum.ogsi.LocatorType;
import org.w3c.dom.Document;
import uk.org.ogsadai.common.XMLUtilities;
import uk.org.ogsadai.wsdl.gds.GDSPortType;
import uk.org.ogsadai.wsdl.gds.GDSServiceGridLocator;
import org.apache.log4j.Logger;
import uk.org.ogsadai.service.OGSADAIConstants;
import uk.org.ogsadai.service.daiservicegroups.DAIServiceGroupRegistrationPortType;
import uk.org.ogsadai.service.daiservicegroups.DAIServiceGroupRegistrationServiceLocator;
import uk.org.ogsadai.service.daiservicegroups.helpers.DAIServiceGroupQueryHelper;


/**
 * A delegate for the Grid Data Service (GDS) of OGSA-DAI.
 * This delegate provides a sub-set of the GDS' functions
 * that are useful to AstroGrid's data warehouse.
 *
 * This class is based on GridServiceDelegate in the same package,
 * the latter being copied verbatim from the "Anglo-Australian
 * Demonstration" (AAD) produced by AstroGrid and AusVO in 2003.
 *
 * On construction, the GSH of the service factory is not set
 * in the delegate.  The caller must either set this GSH directly,
 * using {@link setFactoryHandle} or tell the delegate to obtain
 * it from the OGSA-DAI registry using {@link setFactoryGshFromRegistry}.
 *
 * THIS CLASS HAS BEEN DEPRECATED AND IS NO LONGER IN USE.
 *
 * @deprecated
 */
public class GdsDelegate extends GridServiceDelegate {

  /**
   * Configuration properties for the service.
   * These use the GdsDelegate.properties file to discover
   * the location of the OGSA-DAI warehouse services, configure
   * OGSA-DAI input etc.
   */
  private Properties serviceProperties = null;
  static Logger logger = Logger.getLogger("GdsLogger");

  /**
   * Grid Service Handle for the registry of 
   * Grid Data Service Factories.  This is used in the
   * no-argument form of {@link setFactoryGshFromRegistry}.
   */
  private String registryGsh = null;

  private ExtensibilityType factoryHandle = null;

	    
  /**
   * Initialises the GdsDelegate using values in its properties file
   * GdsDelegate.properties.
   */
  public GdsDelegate() throws Exception {

    // Create and load default properties
    serviceProperties = new Properties();
    try {
      serviceProperties.load(GdsDelegate.class.getResourceAsStream(
          "GdsDelegate.properties"));
    }
    catch (FileNotFoundException e) {
	  logger.error("File not found. Couldn't find properties file " +	  	"GdsDelegate.properties: " + e);
      throw new Exception(
        "Couldn't find properties file GdsDelegate.properties", e);
    }
    catch (IOException e) {
      logger.error("IOException. Couldn't load properties from " +      	"GdsDelegate.properties: " + e.getMessage(), e);
      throw new Exception(
        "Couldn't load properties from GdsDelegate.properties: " +
           e.getMessage(), e);
    }
  }


  /**
   * Returns the Grid Service Handle for the registry of 
   * service factories.  This property is used by the
   * no-argument form of {@link setFactoryGshFromRegistry}.
   */
  public String getRegistryGsh() {
    return this.registryGsh;
  }

  /**
   * Sets the Grid Service Handle for the registry of 
   * service factories.  This property is used by the
   * no-argument form of {@link setFactoryGshFromRegistry}.
   */
  public void setRegistryGsh(String gsh) {
    this.registryGsh = gsh;
  }


  /**
   * Invokes an SQL select statement on the GDS instance. The
   * SQL is passed  to the GDS in a "perform" document that is 
   * created internally. The GDS' reply, is a "response" and this
   * is returned as the method result
   *
   * @param query The SQL SELECT statment.
   *
   * @return The OGSA-DAI response document.
   */  
  public ExtensibilityType performSelect (String query) throws Exception {
  
    // Build the perform document.
    ExtensibilityType document = null;
    try {
      String performDoc = this.makeXMLPerformDoc(query);
      Document msgDoc = XMLUtilities.xmlStringToDOM(performDoc, false);
      document = AnyHelper.getExtensibility(msgDoc.getDocumentElement());
    }
    catch (Exception e) {
      logger.error("Cannot build the perform document "
	  + "for the Grid Data Service; query aborted.", e);
      throw new Exception("Cannot build the perform document "
                          + "for the Grid Data Service; query aborted.", e);
    }

    // Submit the document and get the response.
    // applicationPort is the stub for the GDS set in place by
    // {@link connect}. Use a long timeout because the call is synchronous
    // and the query may take a while.
    ExtensibilityType result = null;
    try {
      ((Stub) this.applicationPort).setTimeout(30*60*1000);  // 30 min timeout
      result = ((GDSPortType) this.applicationPort).perform(document);
    }
    catch (Exception e) {
      String errorMessage =
           "The Grid Data Service failed to execute the query: " +
           "OGSA-DAI error is '" + e.getMessage() + "'";
       logger.error(errorMessage);
       throw new Exception(errorMessage,e);
    }
    return result;
  }
  
  /**
   * Creates a GDS instance and locates its application port.
   * Records the port in the applicationPort attribute.
   */
  public void connect () throws Exception {
    
    logger.info("GdsDelegate: connecting...");
    logger.info("Finding the factory port...");
    this.findFactoryPort();
    logger.info("Creating the service instance");
    this.createServiceInstance();
	logger.info("Locating the service...");
    GDSServiceGridLocator g = new GDSServiceGridLocator();
    GDSPortType port = g.getGDSPort(this.instanceLocator);
    this.applicationPort = port;
	logger.info("GdsDelegate: connected.");
  }
  
  /**
   * Imports a credential and attaches it to the service.
   */
  /*
  public void attachGss() throws Exception {
    GSSCredential cred = null;
    ExtendedGSSManager manager =
       (ExtendedGSSManager)ExtendedGSSManager.getInstance();
    cred = manager.createCredential(GSSCredential.INITIATE_AND_ACCEPT);

    if ( cred == null ) {
      throw new Exception("Cannot create a GSS Credential "
                          + "for the Grid Data Service; operation aborted.");
    }

    ((Stub) this.applicationPort)._setProperty(
             Constants.GSI_SEC_CONV, Constants.SIGNATURE );
    ((Stub) this.applicationPort)._setProperty(
            GSIConstants.GSI_MODE, GSIConstants.GSI_MODE_LIMITED_DELEG );
    ((Stub) this.applicationPort)._setProperty(
            Constants.AUTHORIZATION, NoAuthorization.getInstance());
    ((Stub) this.applicationPort)._setProperty(
            GSIConstants.GSI_CREDENTIALS, cred );
   }
   */


  /**
   * Obtains the grid data service factory handle from the specified 
   * registry and records it in the delegate.
   *
   * The GSH for the registry must previously have been set by
   * a call to {@link setRegistryGsh}.
   * 
   * @throws Exception
   */
  public void setFactoryGshFromRegistry () throws Exception {
    this.setFactoryGshFromRegistry(this.registryGsh, 15);
  }


  /**
   * Obtains the grid data service factory handle from the specified 
   * registry and sets it to a factory service handle.
   * 
   * @param registryUrl The URL of the registry.
   * @param timeoutValue The timeout value in seconds
   * @return void
   * @throws Exception
   */
  public void setFactoryGshFromRegistry( 
	  String registryUrl, int timeoutValue ) throws Exception {

    // Ask the GDSR for information about registered GDSFs 
    DAIServiceGroupRegistrationServiceLocator gdsrLocator = null;
    DAIServiceGroupRegistrationPortType gdsrGpt = null;
    try {
      gdsrLocator = new DAIServiceGroupRegistrationServiceLocator();
      gdsrGpt = gdsrLocator.getDAIServiceGroupRegistrationPort(
          new URL(registryUrl));
   
      // Set timeout of SOAP calls
      ((Stub) gdsrGpt).setTimeout(timeoutValue * 1000);

      QName[] portTypes = new QName[1];
      portTypes[0] = OGSADAIConstants.GDSF_PORT_TYPE;
      ExtensibilityType query = 
          DAIServiceGroupQueryHelper.getPortTypeQuery(portTypes);
      ExtensibilityType result = gdsrGpt.findServiceData(query);
      String gsh = this.chooseFactoryFromRegistry(result);
      this.setFactoryHandle(gsh);
    }
    catch (Exception e) {
      throw new Exception("Can't get the GSH for the GDS factory "
                          + " from the OGSA-DAI registry at "
                          + registryUrl);
    }

  }


  /**
   * Chooses a GDS factory from a list of factories.
   * The list is in the form returned by the registry.
   *
   * @param queryResult the structure returned from the 
   *                    query on the registry.
   * @return the factory GSH.
   */
  String chooseFactoryFromRegistry (ExtensibilityType queryResult)
      throws Exception {

    String factoryURLString = null;
    boolean haveFoundFactoryUrl = false;
    try {
      Object[] entries = AnyHelper.getAsObject(queryResult, EntryType.class);

        if (entries == null || entries.length == 0){
                        logger.error("No locators.");
            throw new Exception("No locators.");
        }

        // Chose which factory to use.  If message level security is
        // on prefer URLs that contain the work secure.  If message
        // level security is off prefer URLs that do not contain the
        // work secure.
        for( int i = 0; i<entries.length && !haveFoundFactoryUrl; ++i )
        {
            EntryType someEntry = (EntryType) entries[i];
            LocatorType locator = someEntry.getMemberServiceLocator();
            HandleType[] handles = locator.getHandle();
            if (handles == null || handles.length == 0)
            {
                logger.error("No handles.");
                throw new Exception("No handles.");
            }
            else {
               factoryURLString = handles[0].toString();
            }

            // Check to see if finished looking for factory URLs
            if ( factoryURLString.toUpperCase().indexOf( "SECURE") >= 0 ) {
                // This is a factory for a secure GDS.  We will
                // consider this to be the best URL if message level
                // security is set.
                //if ( mIsMessageLevelSecurity ) //TOFIX - security flag
                if ( false ) {
                    haveFoundFactoryUrl = true;
                }
            }
            else
            {
                // This is a factory for a non-secure GDS.  We will
                // consider this to be the best bet if message level
                // security is off.
                //if ( !mIsMessageLevelSecurity ) //TOFIX - security flag
                if ( true ) {
                    haveFoundFactoryUrl = true;
                }
            }
        }
    }
    catch (Exception e) {
      logger.error("No factories registered at the OGSA-DAI registry.", e);
      throw new Exception(
         "No factories registered at the OGSA-DAI registry.", e);
    }
    if (!haveFoundFactoryUrl) {
      logger.error("Couldn't find factory URL at the OGSA-DAI registry.");
      throw new Exception(
         "Couldn't find factory URL at the OGSA-DAI registry at ");
    }

    logger.info("Chosen GDSF: " + factoryURLString);    
    return factoryURLString;
  }





  /**
   * Converts an SQL string into an XML Perform document for OGSA-DAI.
   * Doesn't touch the actual SQL query, just wraps it up in suitable XML.
   *
   * @param sqlString  A string containing a pure SQL query.
   */
  private String makeXMLPerformDoc(String sqlString) {

    return

      serviceProperties.getProperty(
          "PERFORM_HEAD", DEFAULT_PERFORM_HEAD) +

      serviceProperties.getProperty(
          "PERFORM_QUERY_START", DEFAULT_PERFORM_QUERY_START) +

      sqlString +

      serviceProperties.getProperty(
          "PERFORM_QUERY_END", DEFAULT_PERFORM_QUERY_END) +

      serviceProperties.getProperty(
          "PERFORM_FOOT", DEFAULT_PERFORM_FOOT);
  }


  private final String DEFAULT_PERFORM_HEAD = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<gridDataServicePerform " +
      "xmlns=\"http://ogsadai.org.uk/namespaces/2003/07/gds/types\" " +
      "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
      "xsi:schemaLocation=\"http://ogsadai.org.uk/namespaces/2003/07/gds/types"+
      //" ../../../../schema/ogsadai/xsd/activities/activities.xsd\">";
      // TOFIX HOW SHOULD WE SET THIS PATH?  VIA PROPERTIES?
      // SHOULD WE PUT THE SCHEMA ON AN ASTROGRID URL?
      " /data/cass123a/kea/ogsadai-src/schema/ogsadai/xsd/activities/activities.xsd\">";

  private final String DEFAULT_PERFORM_QUERY_START = 
      "<sqlQueryStatement name=\"statement\"><expression>";

  private final String DEFAULT_PERFORM_QUERY_END = 
      "</expression><webRowSetStream name=\"statementOutput\"/>" +
      "</sqlQueryStatement>";

  private final String DEFAULT_PERFORM_FOOT = "</gridDataServicePerform>";
}

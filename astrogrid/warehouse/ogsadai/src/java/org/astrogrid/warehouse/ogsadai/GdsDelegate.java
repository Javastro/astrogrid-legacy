package org.astrogrid.warehouse.ogsadai;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Properties;
import org.globus.ogsa.utils.AnyHelper;
import org.gridforum.ogsi.ExtensibilityType;
import org.w3c.dom.Document;
import uk.org.ogsadai.common.XMLUtilities;
import uk.org.ogsadai.wsdl.gds.GDSPortType;
import uk.org.ogsadai.wsdl.gds.GDSServiceGridLocator;
import org.apache.log4j.Logger;
import uk.org.ogsadai.service.OGSADAIConstants;
import uk.org.ogsadai.service.daiservicegroups.DAIServiceGroupRegistrationPortType;
import uk.org.ogsadai.service.daiservicegroups.DAIServiceGroupRegistrationServiceLocator;
import uk.org.ogsadai.service.daiservicegroups.helpers.DAIServiceGroupQueryHelper;

import java.net.URL;
import org.apache.axis.client.Stub;
import javax.xml.namespace.QName;

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
    // {@link connect}.
    ExtensibilityType result = null;
    try {
      result = ((GDSPortType) this.applicationPort).perform(document);
    }
    catch (Exception e) {
      logger.error("The Grid Data Service failed to "
	  + "execute the query.", e);
      throw new Exception ("The Grid Data Service failed to "
                           + "execute the query.", e);
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
	  String registryUrl, int timeoutValue ) throws Exception
  {
	// Ask the GDSR for information about registered GDSFs 
	DAIServiceGroupRegistrationServiceLocator gdsrLocator = null;
	DAIServiceGroupRegistrationPortType gdsrGpt = null;
  
	try {
		gdsrLocator = new DAIServiceGroupRegistrationServiceLocator();
		gdsrGpt = gdsrLocator.getDAIServiceGroupRegistrationPort(
			  new URL(registryUrl));
   
		// Set timeout of SOAP calls
		((Stub) gdsrGpt).setTimeout(timeoutValue * 1000);
	}
	catch (Exception e) {
	  logger.error("Could not locate registry at: " + registryUrl,e);
	  throw new Exception(
		"Could not locate registry at: " + registryUrl,e);
	}
      
	QName[] portTypes = new QName[1];
	portTypes[0] = OGSADAIConstants.GDSF_PORT_TYPE;
	ExtensibilityType query = 
		DAIServiceGroupQueryHelper.getPortTypeQuery(portTypes);
	ExtensibilityType result;
	result = gdsrGpt.findServiceData(query);          
    factoryHandle = result;

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

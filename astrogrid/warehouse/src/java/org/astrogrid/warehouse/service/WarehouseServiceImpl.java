/*
 * $Id: WarehouseServiceImpl.java,v 1.8 2003/11/06 12:55:39 kea Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.warehouse.service;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.rmi.RemoteException;

import org.w3c.dom.Element;

import java.util.Properties;

// Java Classes from OGSA-DAI

import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.namespace.QName;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Stub;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.globus.ogsa.utils.AnyHelper;
import org.globus.ogsa.utils.QueryHelper;
import org.gridforum.ogsi.EntryType;
import org.gridforum.ogsi.ExtendedDateTimeType;
import org.gridforum.ogsi.ExtensibilityType;
import org.gridforum.ogsi.HandleType;
import org.gridforum.ogsi.LocatorType;
import org.gridforum.ogsi.TerminationTimeType;
import org.gridforum.ogsi.holders.ExtensibilityTypeHolder;
import org.gridforum.ogsi.holders.LocatorTypeHolder;
import org.gridforum.ogsi.holders.TerminationTimeTypeHolder;
import org.w3c.dom.Document;

/*
//security classes 
import org.globus.axis.gsi.GSIConstants;
import org.globus.ogsa.impl.security.Constants;
import org.globus.ogsa.impl.security.authorization.NoAuthorization;
import org.ietf.jgss.GSSCredential;
import org.gridforum.jgss.ExtendedGSSManager;
*/

//ogsa-dai classes
import uk.org.ogsadai.common.XMLUtilities;
import uk.org.ogsadai.service.OGSADAIConstants;
import uk.org.ogsadai.service.daiservicegroups.DAIServiceGroupRegistrationPortType;
import uk.org.ogsadai.service.daiservicegroups.DAIServiceGroupRegistrationServiceLocator;
import uk.org.ogsadai.service.daiservicegroups.helpers.DAIServiceGroupQueryHelper;
import uk.org.ogsadai.wsdl.gds.GDSPortType;
import uk.org.ogsadai.wsdl.gds.GDSServiceGridLocator;
import uk.org.ogsadai.wsdl.gdsf.GridDataServiceFactoryPortType;
import uk.org.ogsadai.wsdl.gdsf.GridDataServiceFactoryServiceLocator;

/**
 * In-progress web service front-end for OGSA-DAI-based data warehouse.
 *  
 * The interface to the service has been designed to be compatible with
 * the datacentre interface.
 * 
 * This implementation involves a nasty hack to get around incompatibilities
 * between vanilla axis and ogsa axis:  when this class runs in Axis,
 * it needs to shell out to the command-line and re-run itself using a 
 * separate JVM (which knows where to find all of the OGSA-DAI jars).
 *
 * @author K Andrews
 * @author Guy Rixon
 */
public class WarehouseServiceImpl
{
    /**
     * If true, running as a webservice, if false, running from the 
     * commandline.
     * We can't run the OGSA-DAI client code from within a webservice 
     * because of incompatibilities between vanilla and OGSA-DAI axis.
     * The current workaround is for this class to invoke itself in
     * a separate JVM (outside axis/tomcat) via a system call.
     */ 
    protected boolean invokedViaAxis = true;

    /**
     * Configuration properties for this service.
     * These use the WarehouseServiceImpl.properties file to discover
     * the location of the OGSA-DAI warehouse services, configure 
     * OGSA-DAI input etc.
     */
    protected Properties serviceProperties = null;

    // ----------------------------------------------------------
    // TOFIX - HORRIBLE HACKS FOR TRACKING QUERY STATUS ETC

    // NO PROPER JOB IDs YET - ONLY ONE USER OR WE BREAK HORRIBLY!!
    //private boolean queryFinished = false;

    // DESTINATION OF RESULTS (INSTEAD OF MYSPACE URL)
    //private String destFile = "";
    // ----------------------------------------------------------

 /**
  * Default constructor loads properties from co-located file.
  * These properties are installation-dependent but user-independent.
  */
  public WarehouseServiceImpl() throws RemoteException {

  // create and load default properties
    serviceProperties = new Properties();
    try {
     serviceProperties.load(WarehouseServiceImpl.class.getResourceAsStream(
        "WarehouseServiceImpl.properties")); 
    }
    catch (FileNotFoundException e) {
      throw new RemoteException(
          "Couldn't find properties file WarehouseServiceImpl.properties", e);
    }
    catch (IOException e) {
      throw new RemoteException(
          "Couldn't load properties from WarehouseServiceImpl.properties: " + 
           e.getMessage(), e);
    }
  }

 /**
  * Returns the metadata file
  * @soap
  */
  public Element getMetadata() throws java.rmi.RemoteException {
      throw new RemoteException("Method not implemented yet\n");
  }

   /**
    * Returns metadata in a format suitable for a VO Registry
    * @soap
    */
  public Element getVoRegistryMetadata() throws java.rmi.RemoteException {
      throw new RemoteException("Method not implemented yet\n");
  }

 /**
  * Returns VOTable
  */
  /*
  public Element coneSearch(double ra, double dec, double radius) 
                            throws java.rmi.RemoteException {
    throw new RemoteException("Method not implemented yet\n");
   }
 */

 /**
 * takes an adql document and returns an id for the query.
 */
  public String makeQuery(Element adql, String resultsFormat) 
                          throws java.rmi.RemoteException {
      return "123456";
  }

 /**
  * set the myspace server where the results should be stored, for the 
  * given query id.
  */
  public void setResultsDestination(String id, String myspaceUrl) 
                                    throws java.rmi.RemoteException {
    //destFile = "/tmp/" + myspaceUrl;
  }

 /** 
  * starts given query running
  */
  public void startQuery(String id) throws java.rmi.RemoteException {

    // TOFIX - HORRIBLE HACK FOR TRACKING QUERY STATUS 
    // NO PROPER JOB IDs YET - ONLY ONE USER OR WE BREAK HORRIBLY!!
    /*
    queryFinished = false;
    if (destFile.equals("")) {
      throw new RemoteException(
        "Please set a destination for your query results (just a path-free filename for now).");
    }
    */

    // Check if we're running in Axis or not - if so, shell out to
    // a new JVM (vanilla axis and ogsa axis are incompatible, it seems).

    if (invokedViaAxis) {

      String[] cmdArgs = new String[5];
      cmdArgs[0] = serviceProperties.getProperty(
                    "WAREHOUSE_JVM", DEFAULT_WAREHOUSE_JVM);
      cmdArgs[1] = "-cp";
      cmdArgs[2] = serviceProperties.getProperty(
                    "WAREHOUSE_CLASSPATH", DEFAULT_WAREHOUSE_CLASSPATH);
      cmdArgs[3] = serviceProperties.getProperty(
                    "WAREHOUSE_SERVICE", DEFAULT_WAREHOUSE_SERVICE); 
      cmdArgs[4] = "startQuery";
      // TOFIX SHOULD HAVE JOB ID HERE
      cmdArgs[5] = "654321";

      SystemTalker talker = new SystemTalker();
      TalkResult result = talker.talk(cmdArgs, "");

      if (result.getErrorCode() != 0) {
        throw new RemoteException(
          "External call failed: " + result.getStdout() + " " +
          result.getStderr());
      }
      return; // Finished external call successfully
    }

    // Fallthrough case - not running in Axis, so do actual query!

    String registryURLString = 
        serviceProperties.getProperty(
            "HOST_STRING", DEFAULT_HOST_STRING) + 
        serviceProperties.getProperty(
            "REGISTRY_STRING", DEFAULT_REGISTRY_STRING);
  
    int timeout = 300;  // TOFIX configurable?

    //TOFIX Just a hardwired query for initial testing
    String query="SELECT * FROM catalogue WHERE POS_EQ_DEC > 89.9";

    // Do a synchronous query using the GDS.
    try {
    
      // Look at the registry to get the factory URL
      String factoryURLString = 
          getFactoryUrlFromRegistry(registryURLString,timeout);
      System.out.println("GDSF is " + factoryURLString);
   
      // Create a grid-service delegate for the GDS.  This handles the
      // awkward semantics of the grid-service, including creating
      // the grid-service instance.
      System.out.println("Creating the GDS delegate...");
      GdsDelegate gds = new GdsDelegate();
      gds.setFactoryHandle(factoryURLString);
      System.out.println("Connecting to the GDS...");
      gds.connect();

      // Run the query in the GDS.  
      // Receive in return an OGSA-DAI "response" document.
      ExtensibilityType result = gds.performSelect(query);


      // Output the results
      // TOFIX: Temporary hack until we can get GridFTP transfers
      // (a la MySpace) working.
      try {
        FileWriter writer = new FileWriter("/tmp/WS_OGSA_OUTPUT_GTR");
        writer.write(AnyHelper.getAsString(result));
        writer.close();
      }
      catch (IOException e) {
        throw new RemoteException(
            "Couldn't open destination file /tmp/WS_OGSA_OUTPUT_GTR", e);
      }

      // Finished with the GDS.
      gds.destroyServiceInstance();
    }
    catch (AxisFault e) {
        throw new RemoteException(
          "Problem with Axis", e);
    }
    catch (Exception e) {
      throw new RemoteException(
          "Unspecified exception", e);
    }
    //queryFinished = true;
  }

 /**
  * - stops the given query
  */
  public void abortQuery(String id) throws java.rmi.RemoteException {
    throw new RemoteException("Method not implemented yet\n");
  }

 /**
  * - gets info on the query status
  */
  public Element getStatus(String id) throws java.rmi.RemoteException {
    throw new RemoteException("Method not implemented yet\n");
  }



  /**
   * Obtains the factory URL from the specified registry.
   * This is lifted DIRECTLY from package uk.org.ogsadai.client.Client.
   * @param registryUrl The URL of the registry.
   * @param timeoutValue The timeout value in seconds
   * @return The URL of the factory
   * @throws Exception
   */
    private static String getFactoryUrlFromRegistry( 
        String registryUrl, 
        int timeoutValue ) throws Exception
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
        throw new RemoteException(
          "Could not locate registry at: " + registryUrl,e);
      }
      
      QName[] portTypes = new QName[1];
      portTypes[0] = OGSADAIConstants.GDSF_PORT_TYPE;
      ExtensibilityType query = 
          DAIServiceGroupQueryHelper.getPortTypeQuery(portTypes);
      ExtensibilityType result;
      result = gdsrGpt.findServiceData(query);
          
      String factoryURLString = "";
      boolean haveFoundFactoryUrl = false;
      try {
        Object[] entries = AnyHelper.getAsObject(result, EntryType.class);

        if (entries == null || entries.length == 0)
            throw new Exception("No locators.");
                    
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
                throw new Exception("No handles.");
            }

            factoryURLString = handles[0].getValue().toString();
                    
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
      throw new RemoteException(
         "No factories registered at the DAIRegistry at " + registryUrl, 
         e);
    }
    if (!haveFoundFactoryUrl) {
      throw new RemoteException(
         "Couldn't find factory URL at the DAIRegistry at " + registryUrl); 
    }
    // If we got here, we found the factory 
    return factoryURLString;
  }

  /**
   * Main function for invocation outside axis/tomcat (running at the
   * command-line).  
   * We can't run the OGSA-DAI client code from within a webservice 
   * because of incompatibilities between vanilla and OGSA-DAI axis.
   * The current workaround is for this class to invoke itself in
   * a separate JVM (outside axis/tomcat) via a system call, using
   * this main() function.
   */
  public static void main(String args[]) throws Exception {

    WarehouseServiceImpl service = new WarehouseServiceImpl();

    // We're not running in axis
    service.invokedViaAxis = false;

    // This main() hack currently supports startQuery(), so check that
    // this is the method requested.
    String methodName;
    String jobID;
    try {
      methodName = args[0];
      if (! methodName.equals("startQuery")) {
        throw new RemoteException(
           "Method " + methodName + "is not supported at the command-line.");
      }
      jobID = args[1];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      throw new RemoteException(
          "Unexpected number of command-line arguments (" + 
         Integer.toString(args.length) + ")", e);
    }

    // TOFIX - need to have proper destination URL from command-line
    service.startQuery(jobID);
  }

  // ----------------------------------------------------------
  // Fallback defaults for values that should be configured on a
  // per-installation basis in the WarehouseServiceImpl.properties 

  private final String DEFAULT_HOST_STRING = 
        "http://astrogrid.ast.cam.ac.uk:4040";
  private final String DEFAULT_REGISTRY_STRING = 
        "/ogsa/services/ogsadai/DAIServiceGroupRegistry";

  private final String DEFAULT_WAREHOUSE_JVM = 
        "/data/cass123a/gtr/jdk-ogsa/bin/java";
  private final String DEFAULT_WAREHOUSE_CLASSPATH =
        "/data/cass123a/kea/tomcat_cass111/webapps/axis/WEB-INF/classes";
  private final String DEFAULT_WAREHOUSE_SERVICE =
        "org.astrogrid.warehouse.service.WarehouseServiceImpl";
}
/*
$Log: WarehouseServiceImpl.java,v $
Revision 1.8  2003/11/06 12:55:39  kea
Moved various string constants into properties files.
Took out OGSA-DAI cruft from WarehouseServiceImpl (OGSA-DAI-specific
code had already moved to GdsDelegate).

Revision 1.7  2003/11/05 20:38:11  gtr
Access to the GDS is now through a GdsDelegate. The invokedViaAxis attribute
is changed to protected so that the Server class can set it.

Revision 1.6  2003/10/30 21:11:06  kea
Moved properties to properties file.

Revision 1.5  2003/10/29 17:01:05  kea
Due to problems with axis/ogsa-axis incompatibilities, altered web
service implementation to shell out to separate JVM outside tomcat
to make call to OGSA-DAI grid service - hopefully just a temporary
fix...

Revision 1.4  2003/10/24 20:11:34  kea
More work on webservice to call OGSA-DAI.  This was actually working
as a webservice but suddenly stopped for no apparent reason;  the
OGSA-DAI command-line client broke at the same time.  (This webservice
still works when invoked at the command-line, just not via tomcat.
Beats me.)

Revision 1.3  2003/10/23 17:19:45  kea
Starting to add OGSA-DAI functionality to webservice implementation,
to allow webservice to perform queries on OGSA-DAI database.
Mostly commented out at the moment because tomcat is giving me big
heap trouble.

Revision 1.2  2003/10/08 15:25:35  kea
Finalised interface classes required for end IT4 wk 2:
    org.astrogrid.warehouse.delegate.WarehouseDelegateIfc
    org.astrogrid.warehouse.service.WarehouseServiceIfc

Changed URL parameter to String parameter in setResultsDestination()
  methods to help with wsdl2java/java2wsdl auto-tooling.

Added package-specific WarehouseException.

Added wsdd files for deploying to Tomcat/Axis, and added temporary
testing harness just to be sure we can deploy to Axis and talk to the
(mostly unimplemented!) web service.

Revision 1.1  2003/10/07 17:09:51  kea
Adding webservice / webdelegate skeletons.
Having diffs with wsdl2java-generated class names/structure.
*/

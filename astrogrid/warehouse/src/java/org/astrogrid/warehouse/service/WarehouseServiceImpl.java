/*
 * $Id: WarehouseServiceImpl.java,v 1.3 2003/10/23 17:19:45 kea Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.warehouse.service;

import java.io.IOException;
import java.rmi.RemoteException;

import org.w3c.dom.Element;


// Java Classes from OGSA-DAI

/*
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

//security classes 
import org.globus.axis.gsi.GSIConstants;
import org.globus.ogsa.impl.security.Constants;
import org.globus.ogsa.impl.security.authorization.NoAuthorization;
import org.ietf.jgss.GSSCredential;
import org.gridforum.jgss.ExtendedGSSManager;

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
*/

/**
 * At the moment, basically a clone of the WarehouseDelegate interface.
 * Can add service-specific functionality in here later.
 *
 * @author K Andrews 
 */
public class WarehouseServiceImpl
{
    // ----------------------------------------------------------
    // TOFIX: All these strings should be in a Properties file
    //
    private final String HOST_STRING = 
        "http://astrogrid.ast.cam.ac.uk:4040";
    private final String REGISTRY_STRING = 
        "/ogsa/services/ogsadai/DAIServiceGroupRegistry";
    private final String FACTORY_STRING = 
        "/ogsa/services/ogsadai/GridDataServiceFactory";

    private final String PERFORM_HEAD = 
      "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + 
      "<gridDataServicePerform" +
      "xmlns=\"http://ogsadai.org.uk/namespaces/2003/07/gds/types\"" +
      "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"" +
      "xsi:schemaLocation=\"http://ogsadai.org.uk/namespaces/2003/07/gds/types"+
        "../../../../schema/ogsadai/xsd/activities/activities.xsd\">";

    private final String PERFORM_QUERY_START = 
      "<sqlQueryStatement name=\"statement\"><expression>";

    private final String PERFORM_QUERY_END = 
      "</expression><webRowSetStream name=\"statementOutput\"/>" +
      "</sqlQueryStatement>";

    private final String PERFORM_FOOT = "</gridDataServicePerform>";
    // ----------------------------------------------------------

    // ----------------------------------------------------------
    // TOFIX - HORRIBLE HACKS FOR TRACKING QUERY STATUS ETC

    // NO PROPER JOB IDs YET - ONLY ONE USER OR WE BREAK HORRIBLY!!
    private boolean queryFinished = false;

    // DESTINATION OF RESULTS (INSTEAD OF MYSPACE URL)
    private String destFile = "";
    // ----------------------------------------------------------

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
    destFile = "/tmp/" + myspaceUrl;
  }

 /** 
  * starts given query running
  */
  public void startQuery(String id) throws java.rmi.RemoteException {
    return;
/*
    // TOFIX - HORRIBLE HACK FOR TRACKING QUERY STATUS 
    // NO PROPER JOB IDs YET - ONLY ONE USER OR WE BREAK HORRIBLY!!
    queryFinished = false;
    if (destFile == null) {
      throw new RemoteException(
        "Please set a destination for your query (a filename for now).");
    }

    String registryURLString = HOST_STRING + REGISTRY_STRING;
    int timeout = 300;  // TOFIX configurable?

    //TOFIX Just a hardwired query for initial testing
    String query="SELECT * FROM catalogue WHERE POS_EQ_DEC > 89.9";
    // 

    // All of the following lifted from package uk.org.ogsadai.client.Client.
    // I don't understand it yet :-)
    try {
      // Look at the registry to get the factory URL
      String factoryURLString = 
          getFactoryUrlFromRegistry(registryURLString,timeout);
      //String factoryURLString = HOST_STRING + FACTORY_STRING;

      // Get the factory portType and the grid service portType for
      // the GDSF
      GridDataServiceFactoryServiceLocator gdsfLocator = null;
      GridDataServiceFactoryPortType gdsfFpt = null;
      try {
        gdsfLocator = new GridDataServiceFactoryServiceLocator();
        gdsfFpt = gdsfLocator.getGridDataServiceFactoryPort(
                    new URL(factoryURLString));

        // Set timeout of SOAP calls
        ((Stub) gdsfFpt).setTimeout(timeout * 1000);
      }
      catch (Exception e) {
        throw new RemoteException(
          "Could not locate factory at: " + factoryURLString, e);
      }
      // Set up a CreationType for the creation of a GDS
      // (includes a GDS Create document)
      LocatorTypeHolder locator = new LocatorTypeHolder();
      Calendar term = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
      term.add(Calendar.HOUR, 2);
      TerminationTimeType terminationTime = new TerminationTimeType();
      terminationTime.setBefore(new ExtendedDateTimeType(term));
      terminationTime.setAfter(new ExtendedDateTimeType(term));

      ExtensibilityType creationParameters = null;
      TerminationTimeTypeHolder currentTerminationTime = 
          new TerminationTimeTypeHolder();
      ExtensibilityTypeHolder extensibilityOutput = 
          new ExtensibilityTypeHolder();

      gdsfFpt.createService(terminationTime, creationParameters, locator,
          currentTerminationTime, extensibilityOutput);

      GDSServiceGridLocator gds = new GDSServiceGridLocator();
      GDSPortType griddataservice = gds.getGDSPort(locator.value);

      // Set timeout of SOAP calls
      ((Stub) griddataservice).setTimeout(timeout * 1000 );

      // Set up the script
      Document msgDoc = 
        XMLUtilities.xmlFileToDOM(mGDSPerformDocuments[i], false);
                  document =
      AnyHelper.getExtensibility(msgDoc.getDocumentElement());

        // Run the script
      result = griddataservice.perform(document);
      griddataservice.destroy();
    }
    catch (AxisFault e) {
        throw new RemoteException(
          "Problem with Axis :" + e.getMessage(), e);
    }
    catch (Exception e) {
      throw new RemoteException(
          "Unspecified exception: + e.getMessage()", e);
    }
    queryFinished = true;
*/
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
  * Converts an SQL string into an XML Perform document for OGSA-DAI.
  * Doesn't touch the actual SQL query, just wraps it up in suitable XML.
  *
  * @param sqlString  A string containing a pure SQL query.
  */
  private String makeXMLPerformDoc(String sqlString) {
    return PERFORM_HEAD + PERFORM_QUERY_START  + sqlString +
        PERFORM_QUERY_END  + PERFORM_FOOT;
  }

  /**
   * Obtains the factory URL from the specified registry.
   * This is lifted DIRECTLY from package uk.org.ogsadai.client.Client.
   * @param registryUrl The URL of the registry.
   * @param timeoutValue The timeout value in seconds
   * @return The URL of the factory
   * @throws Exception
   */
   /*
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
            System.out.println("Could not locate registry at: " + registryUrl);
            throw e;
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
    */
}
/*
$Log: WarehouseServiceImpl.java,v $
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

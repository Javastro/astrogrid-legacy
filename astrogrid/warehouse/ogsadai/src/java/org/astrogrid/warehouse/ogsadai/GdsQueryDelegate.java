/*
 * $Id: GdsQueryDelegate.java,v 1.14 2004/03/24 12:35:18 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.warehouse.ogsadai;

import org.astrogrid.warehouse.ogsadai.XSLTransform;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.apache.log4j.Logger;

import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import org.globus.ogsa.utils.AnyHelper;
import org.globus.ogsa.GridServiceException;
import org.gridforum.ogsi.ExtendedDateTimeType;
import org.gridforum.ogsi.TerminationTimeType;
import org.gridforum.ogsi.ExtensibilityType;
import org.apache.axis.AxisFault;

import uk.org.ogsadai.client.toolkit.ActivityRequest;
import uk.org.ogsadai.client.toolkit.ActivityOutput;
import uk.org.ogsadai.client.toolkit.GridDataService;
import uk.org.ogsadai.client.toolkit.GridDataServiceFactory;
import uk.org.ogsadai.client.toolkit.MessageLevelSecurityProperty;
import uk.org.ogsadai.client.toolkit.Response;
import uk.org.ogsadai.client.toolkit.ServiceFetcher;
import uk.org.ogsadai.client.toolkit.ServiceGroupRegistry;
import uk.org.ogsadai.client.toolkit.GridServiceMetaData;
import uk.org.ogsadai.client.toolkit.activity.sql.SQLQuery;
import uk.org.ogsadai.client.toolkit.activity.delivery.DeliverToGFTP;
import uk.org.ogsadai.client.toolkit.activity.delivery.DeliverToURL;
import uk.org.ogsadai.client.toolkit.activity.delivery.DeliverFromURL;
import uk.org.ogsadai.service.OGSADAIConstants;

import org.ietf.jgss.GSSCredential;
import org.globus.ogsa.utils.QueryHelper;
import org.globus.util.GlobusURL;

import java.util.Calendar;
import java.util.TimeZone;
                                                                                
/**
 *
 * @author K Andrews
 */

/**
 * A high-level delegate for invoking a query on an OGSA-DAI Grid Data Service.
 * 
 * This class uses the OGDA-DAI client toolkit, introduced in OGSA-DAI 3.1,
 * to interact with the OGSA-DAI GDS.  The toolkit abstracts away most of
 * the detail of the underlying service, which is good as the whole grid 
 * infrastructure is about to change out from under us.
 */
public class GdsQueryDelegate 
{
  /**
   * Default empty constructor.
   * 
   * @throws Exception
   * @throws IOException
   * @throws SAXException
   */
  
  static Logger logger = Logger.getLogger("GdsQueryLogger");
  
  public GdsQueryDelegate() 
      throws Exception, IOException, SAXException {
    super();    //Can throw IOException and SAXException
  }

  /*
   * Uses an OGSA-DAI Grid Data Service to perform the supplied SQL query.
   *
   * @returns Document containing XML RowSet database query results
   * @throws Exception

   */
  public void doRealQuery(String sql, String registryUrlString, 
        String outputUrl) throws Exception {

    int timeout = 300;  // TOFIX configurable?

    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    String xmlString ="";

    // TOFIX CHECK FOR NULL PARAMETERS HERE 

    // Parse the delivery URL.  GlobusURL is used insted of java.net.URL
    // in order to include gsiftp URLs.
    GlobusURL url = new GlobusURL(outputUrl);

    // Do we need a secure service?
    boolean needSecure = false;
    if (url.getProtocol().equalsIgnoreCase("gsiftp") ||
          url.getProtocol().equalsIgnoreCase("gridftp")) {
      needSecure = true;  
    }

    // Do a synchronous query using the GDS.
    try {

      // Get the registry
      ServiceGroupRegistry registry = 
            ServiceFetcher.getRegistry(registryUrlString);

      // Get the factory from the registry
      String factoryHandle = getFactoryHandle(registry, needSecure);

      // Locate the Factory
      GridDataServiceFactory factory = 
              ServiceFetcher.getFactory(factoryHandle);      
      logger.info("Found GDSF at " + factoryHandle); 

      // Create a GridDataService
      GridDataService gds = factory.createGridDataService();
      logger.info("Created GDS");

      // Set initial termination time min 2Hrs, max 3Hrs after now
      refreshTermination(gds, 2);

      // Construct an Activity request
      // Set the query
      SQLQuery query = new SQLQuery(sql);
      ActivityRequest request = new ActivityRequest();
      request.addActivity(query);
      logger.info("Sending SQL Query \"" + sql + "\" to GDS");                                                                                
      // Set up the XSLT transform at the server end
      // First, delivery of the stylesheet
      DeliverFromURL xsltDelivery = new DeliverFromURL(
            "http://astrogrid.ast.cam.ac.uk/xslt/ag-warehouse-first.xsl");
      request.addActivity(xsltDelivery);

      // Second, the actual transformation activity
      ActivityOutput queryOutput = query.getOutput();
      ActivityOutput xslDelOutput = xsltDelivery.getOutput();
      XSLTransform xslTransform = new XSLTransform(queryOutput,xslDelOutput);
      request.addActivity(xslTransform);
      
      // Now set the delivery URL
      // This could be a file:// or a gsiftp:// url.
  
      ActivityOutput transformOutput = xslTransform.getOutput();

      if (url.getProtocol().equalsIgnoreCase("gsiftp") ||
          url.getProtocol().equalsIgnoreCase("gridftp")) {
         // Got a GridFTP URL
        DeliverToGFTP delivery = new DeliverToGFTP(
              url.getHost(), url.getPort(), url.getPath());
        delivery.setInput(transformOutput);
        request.addActivity(delivery);
        logger.info("Adding delivery address 'gsiftp://"
            +  url.getHost() + ":" 
            + Integer.toString(url.getPort()) + url.getPath());   

      }
      else if (url.getProtocol().equalsIgnoreCase("file")) {
        // Got a local file URL
        DeliverToURL delivery = new DeliverToURL(outputUrl);
        delivery.setInput(transformOutput);
        request.addActivity(delivery);
      }
      else {
        String errMess =
           "Unknown or unsupported protocol for results delivery: " +
            url.getProtocol() +
           "Expected 'file://'' or 'gsiftp://' or 'gridftp://'";
        logger.error(errMess);
        throw new Exception(errMess);
      }

      if (url.getProtocol().equalsIgnoreCase("gsiftp") ||
          url.getProtocol().equalsIgnoreCase("gridftp")) {
        // Set up the authorization if using GridFTP 
        MessageLevelSecurityProperty security = 
              new MessageLevelSecurityProperty();
        CredentialHolder credentialHolder = null;
        // KLUDGE- hardwired locations
        try {
          credentialHolder = new CredentialHolder(
              "/etc/grid-security/hostcert.pem",
              "/etc/grid-security/hostkey.pem");
        }
        catch (Exception e) {
          throw new Exception("No identity credentials could be obtained "
                + e.getMessage()
                +").");
        }
        security.setCredential(credentialHolder.getCredential());
        gds.configure(security);
      }

      // Perform the request
      logger.info("Performing request.");
      Response response = null;
      try {
        response = gds.perform(request);
      }
      catch (Exception e) {
        //TOFIX MSG
        logger.error("Perform crashed: " + e.getMessage());
      }

      // Poll for completion (horrible hack, should use notifications
      // really, but they'll be superseded in the new Grid specs :-(
      int multFac = 1;
      logger.debug("Waiting for results delivery to finish...");
      while (true) {
        ExtensibilityType result2 = gds.findServiceData(
           QueryHelper.getNamesQuery(OGSADAIConstants.GDS_SDE_REQUEST_STATUS));
        String status = AnyHelper.getAsString(result2);
        if (status.indexOf(OGSADAI_STATUS_COMPLETE) != -1) {  //Completed
          break;  // Out of while loop
        }
        else if (status.indexOf(OGSADAI_STATUS_INCOMPLETE) == -1) {  
          // Unknown status
          String errMess = "blah";
          logger.error(errMess);
          throw new Exception(errMess);
        }
        else {
          //Min termination 2 hours from now
          refreshTermination(gds,2);  

          //Wait for a while
          Thread.currentThread().sleep(1000*multFac);

          if (multFac < 300) { //Arbitrary max wait of 5 minutes 
            multFac = multFac + 1;  //Wait longer next time
          }
        }
      }
      //Got here, so request has completed successfully and can return
      logger.debug("Results delivery has finished.");

    }
    catch (AxisFault e) {
      String errorMessage = "Problem with Axis: " + e.getMessage();
      logger.error(errorMessage);
      throw new Exception(errorMessage);
    }
    catch (Exception e) {
      String errorMessage = "Unspecified exception: " + e.getMessage();
      logger.error(errorMessage);
      throw new Exception(errorMessage);
    }
  }

  protected void refreshTermination( GridDataService gds, 
        int offsetHours) throws Exception 
  {
    // Set initial termination time min 2Hrs, max 3Hrs after now
    Calendar term = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
    term.add(Calendar.HOUR, offsetHours);
    gds.requestTerminationAfter(new ExtendedDateTimeType(term));
    term.add(Calendar.HOUR, 1);
    gds.requestTerminationBefore(new ExtendedDateTimeType(term));
  }

  protected String getFactoryHandle(
      ServiceGroupRegistry registry, boolean needSecure) throws Exception
  {
    // Get the factory from the registry
    // If we want to use GridFTP delivery, look for a secure factory.
    // Otherwise, look for an ordinary factory
    //
    GridServiceMetaData gsmd[] = 
        registry.listServices(OGSADAIConstants.GDSF_PORT_TYPE);
    String factoryHandle = null;

    if (needSecure) {
      for (int i = 0; i < gsmd.length; i++) {
        String handle = gsmd[i].getHandle();
        if (handle.toUpperCase().indexOf("SECURE") != -1) { //Secure found
          factoryHandle = handle;
          break;
        }
      }
      if (factoryHandle == null) {
        String errMess = 
          "Couldn't find secure Grid Data Service Factory to use; " +
          "please choose a different results delivery method";
        logger.error(errMess);
        throw new Exception(errMess);
      }
    }
    else {
      for (int i = 0; i < gsmd.length; i++) {
        String handle = gsmd[i].getHandle();
        String secureHandle = null;
        if (handle.toUpperCase().indexOf("SECURE") != -1) { //Secure found
          secureHandle = handle;
        }
        else {
          factoryHandle = handle;
          break;
        }
        if (factoryHandle == null) {
          if (secureHandle == null) {
            String errMess = 
              "Couldn't find any Grid Data Service Factory to use; " +
              "please check you are using the correct DAI registry";
            logger.error(errMess);
            throw new Exception(errMess);
          }
          else {
            // Use secure handle if no insecure available
            logger.warn(
              "Couldn't find standard Grid Data Service Factory to use; " +
              "using Secure GDSF instead.");
          }
        }
      }
    }
    return factoryHandle;
  }

  /**
   */
  public static void main(String args[]) throws Exception {

    GdsQueryDelegate gdsQueryDelegate = new GdsQueryDelegate();

    String sql;
    String registryUrlString;
    String outputFileUrl = null;

    try {
      int len = args.length;

      // Get SQL query (first parameter)
      if ((len == 0) || (args[0] == null)) {
		    String errorMessage = 
          "No SQL (first parameter) supplied to GdsQueryDelegate";
        logger.error(errorMessage);
        throw new Exception(errorMessage);
      }
      sql = args[0];

      // Get URL for ogsa-dai registry (second parameter)
      if (len < 2) {
		    String errorMessage = 
          "No Registry URL (second parameter) supplied to GdsQueryDelegate";
        logger.error(errorMessage);
        throw new Exception(errorMessage);
      }
      registryUrlString = args[1];

      // Get results filename if supplied (third parameter)
      if (len < 3) {
		    String errorMessage = 
          "No results destination URL (third parameter) supplied "
          + "to GdsQueryDelegate";
        logger.error(errorMessage);
        throw new Exception(errorMessage);
      }
      outputFileUrl = args[2];

      if (len > 3) {
		    String errorMessage = 
           "Too many parameters (" + Integer.toString(len) + 
           ") supplied to GdsQueryDelegate";
        logger.error(errorMessage);
        throw new Exception(errorMessage);
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {
		  String errorMessage = 
           "Unexpected number of parameters supplied to GdsQueryDelegate";
        logger.error(errorMessage + ": " + e.getMessage());
        throw new Exception(errorMessage);
    }
    //Do actual query 
    gdsQueryDelegate.doRealQuery(sql, registryUrlString, outputFileUrl);
  }

  // ----------------------------------------------------------
  // Fallback defaults for values that should be configured on a
  // per-installation basis in the WarehouseServiceImpl.properties 

  private final String DEFAULT_HOST_STRING = 
        "http://astrogrid.ast.cam.ac.uk:4040";
  private final String DEFAULT_REGISTRY_STRING = 
        "/gdw/services/ogsadai/DAIServiceGroupRegistry";

  // Other utility strings
  private final String TEMP_RESULTS_FILENAME = "ws_output.xml";
  private final String WAREHOUSE_RESULT_START = "WAREHOUSE_RESULT_START";
  private final String WAREHOUSE_RESULT_END = "WAREHOUSE_RESULT_END";

  private final String OGSADAI_STATUS_INCOMPLETE = 
      "Request is being processed asynchronously";
  private final String OGSADAI_STATUS_COMPLETE = 
      "Available for processing";
//================================================================

}
/*
$Log: GdsQueryDelegate.java,v $
Revision 1.14  2004/03/24 12:35:18  kea
Proper lifetime management; selective use of secure services (only for
GridFTP delivery, not for file delivery).

Revision 1.13  2004/03/16 14:03:36  kea
Temporary hack to unit tests to avoid build break - to fix later.

Revision 1.12  2004/03/15 12:31:32  kea
Changed QueryDelegate to use the new OGSA-DAI toolkit, rather than our
own Grid delegates.
Added support for delivering to either GridFTP url or File url.

Revision 1.11  2004/03/04 18:23:09  kea
Made member public to help test datacenter direct access.

Revision 1.10  2004/03/04 15:33:47  kea
Start of integration with ogsa-dai 3.1, including using new ogsa-dai
client toolkit rather than our GdsDelegate.  (These changes initially
made on branch GDW_KEA_144, which has been abandoned cos I'm too
chicken to merge it).

Revision 1.8.6.1  2004/03/04 15:20:17  kea
Changed to use new OGSA-DAI client interface toolkit.
Our GdsDelegate is no longer used.

Revision 1.8  2003/12/15 15:36:09  kea
Tidying up GdsQueryDelegate to remove some old cruft.

Revision 1.7  2003/12/15 14:39:20  kea
Fixed parameter count checks.

Revision 1.6  2003/12/15 14:23:09  kea
Restoring changes buggered up by CVS, bastard bastard bastard.
No properties file for GdsQueryDelegate anymore, registry URL for
ogsa-dai passed in as parameter.

Revision 1.4  2003/12/11 16:17:54  eca
Logging comments for System.out.println and in conjunction with 
thrown exceptions now in GdsDelegate, GdsQueryDelegate, and 
GridServiceDelegate 

Added setFactoryHandleFromRegistry method; factory handle 
result returned as ExtensibilityType.

Elizabeth Auden, 11 Dec 2003

Revision 1.1  2003/12/02 13:56:51  kea
Delegate for performing OGSA-DAI queries, in separate package space from
Datacenter Querier module to isolate incompatible OGSA-DAI axis from
datacenter.

*/

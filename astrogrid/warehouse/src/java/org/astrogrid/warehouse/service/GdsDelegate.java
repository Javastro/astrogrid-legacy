package org.astrogrid.warehouse.service;

import java.util.Calendar;
import java.util.TimeZone;
import org.globus.ogsa.impl.core.service.ServiceLocator;
import org.globus.ogsa.utils.AnyHelper;
import org.globus.ogsa.utils.GridServiceFactory;
import org.globus.ogsa.wsdl.GSR;
import org.gridforum.ogsi.ExtendedDateTimeType;
import org.gridforum.ogsi.ExtensibilityType;
import org.gridforum.ogsi.holders.ExtensibilityTypeHolder;
import org.gridforum.ogsi.holders.LocatorTypeHolder;
import org.gridforum.ogsi.holders.TerminationTimeTypeHolder;
import org.gridforum.ogsi.OGSIServiceGridLocator;
import org.gridforum.ogsi.TerminationTimeType;
import org.w3c.dom.Document;
import uk.org.ogsadai.wsdl.gds.GDSPortType;
import uk.org.ogsadai.wsdl.gds.GDSServiceGridLocator;
import uk.org.ogsadai.common.XMLUtilities;

/**
 * A delegate for the Grid Data Service (GDS) of OGSA-DAI.
 * This delegate provides a sub-set of the GDS' functions
 * that are useful to AstroGrid's data warehouse.
 *
 * This class is based on GridServiceDelegate in the same package,
 * the latter being copied verbatim from the "Anglo-Australian
 * Demonstration" (AAD) produced by AstroGrid and AusVO in 2003.
 *
 * The delegate assumes that the GSH of the Grid Data Service
 * factory (GDSF) is known to the calling application; the
 * delgate does not know how to find the GDSF using
 * OGSA-DAI's service-group registry. As with all delegates
 * based on AAD, the caller must set the GSH of the factory after
 * constructing the delegate by calling 
 * {@link GridServiceDelegate.setFactoryHandle}; that method is
 * inherited by the current class.
 */
public class GdsDelegate extends GridServiceDelegate {

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
    
    System.out.println("GdsDelegate: connecting...");
    System.out.println("Finding the factory port...");
    this.findFactoryPort();
    System.out.println("Creating the service instance...");
    this.createServiceInstance();
    System.out.println("Locating the service...");
    GDSServiceGridLocator g = new GDSServiceGridLocator();
    GDSPortType port = g.getGDSPort(this.instanceLocator);
    this.applicationPort = port;
    System.out.println("GdsDelegate: connected.");
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
      "<gridDataServicePerform " +
      "xmlns=\"http://ogsadai.org.uk/namespaces/2003/07/gds/types\" " +
      "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
      "xsi:schemaLocation=\"http://ogsadai.org.uk/namespaces/2003/07/gds/types"+
      //" ../../../../schema/ogsadai/xsd/activities/activities.xsd\">";
      // TOFIX HOW SHOULD WE SET THIS PATH?  VIA PROPERTIES?
      // SHOULD WE PUT THE SCHEMA ON AN ASTROGRID URL?
      " /data/cass123a/kea/ogsadai-src/schema/ogsadai/xsd/activities/activities.xsd\">";

    private final String PERFORM_QUERY_START = 
      "<sqlQueryStatement name=\"statement\"><expression>";

    private final String PERFORM_QUERY_END = 
      "</expression><webRowSetStream name=\"statementOutput\"/>" +
      "</sqlQueryStatement>";

    private final String PERFORM_FOOT = "</gridDataServicePerform>";

  
}

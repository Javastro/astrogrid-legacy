/*
 * $Id: GdsQueryDelegate.java,v 1.9 2004/03/03 12:14:26 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.warehouse.ogsadai;

import org.astrogrid.warehouse.ogsadai.GdsDelegate;

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
import org.gridforum.ogsi.ExtensibilityType;
import org.apache.axis.AxisFault;

/**
 *
 * @author K Andrews
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
  protected Document doRealQuery(String sql, String registryUrlString, 
        OutputStream output) throws Exception {

    int timeout = 300;  // TOFIX configurable?

    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    String xmlString ="";

    // Do a synchronous query using the GDS.
    try {

      // Create a grid-service delegate for the GDS.  This handles the
      // awkward semantics of the grid-service, including creating
      // the grid-service instance.
      logger.info("Creating the GDS delegate...");
      GdsDelegate gds = new GdsDelegate();
      gds.setRegistryGsh(registryUrlString);
      gds.setFactoryGshFromRegistry();

      // Run the query in the GDS.  
      // Receive in return an OGSA-DAI "response" document.
      logger.info("Connecting to the GDS...");
      gds.connect();

      logger.info("Query is " + sql);
      ExtensibilityType result = gds.performSelect(sql);

      // OGSA-DAI results wrap up the actual XML RowSet as a CDATA node
      // embedded within some ogsa-dai related XML, so extract the actual
      // results.
      Node cdataNode = getResultsRowset(result);
      xmlString = cdataNode.getNodeValue();

      // Print start tag to stdout just in case we're shipping results 
      // via stdout (this might happen if the invoking WarehouseQuerier 
      // couldn't create a temporary file for results in its workspace).  
      // Putting custom start and end tags into the output stream makes
      // it possible for the WarehouseQuerier to extract the actual data 
      // from any other messages being spat to stdout.
      System.out.println(WAREHOUSE_RESULT_START); //DON'T REMOVE THIS LINE!!

      // Print byte stream to output stream (might be stdout).
      // We do this as well as returning a document to handle cases where 
      // this class is invoked from the command line and can't pass a 
      // Document object back to its caller (eg WarehouseQuerier).
      output.write(xmlString.getBytes());

      // Print this to stdout just in case we're shipping results via stdout
      System.out.println(WAREHOUSE_RESULT_END); // DON'T REMOVE THIS LINE!!
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
    // Parse XML and return proper Document (as well as printing XML
    // to supplied output stream above).
    DOMParser parser = new DOMParser();
    try {
      parser.parse(new InputSource(new StringReader(xmlString)));
    }
    catch (Exception e) {//SAXException, IOException
      String errorMessage = 
          "Couldn't parse results XML Rowset: " + e.getMessage();
      logger.error(errorMessage);
      throw new Exception(errorMessage);
    }  
    return parser.getDocument();
  }

  protected Node getResultsRowset(ExtensibilityType results) 
        throws Exception
  {
    Element element[];
    try {
      element = AnyHelper.getAsElement(results);
    }
    catch (GridServiceException e) {
      logger.error(
	  "Couldn't parse OGSA-DAI response document, giving up: " +
	  e.getMessage());
      throw new Exception(
          "Couldn't parse OGSA-DAI response document, giving up: " +
          e.getMessage());
    }

    // Toplevel element should be gridDataServiceResponse
    Node node = (Node)element[0];
    if (node == null) {
      logger.error(
	  "Couldn't parse OGSA-DAI response document, giving up");
      throw new Exception(
          "Couldn't parse OGSA-DAI response document, giving up");
    }
    String nodeName = node.getNodeName();
    if ( !nodeName.equals("gridDataServiceResponse")) {
      logger.error(
	  "Couldn't parse OGSA-DAI response document, giving up");
      throw new Exception(
          "Couldn't parse OGSA-DAI response document, giving up");
    }
    NodeList children = node.getChildNodes();
    if (children == null) {
      logger.error(
	  "Couldn't parse OGSA-DAI response document, giving up");
      throw new Exception(
          "Couldn't parse OGSA-DAI response document, giving up");
    }
    // Now look for result node containing CData RowSet results
    // This is a loose parse that looks for the type of node that 
    // we want (and accepts the first one found) and simply ignores 
    // other nodes.
    Node dataNode = null;
    for (int i=0; i < children.getLength(); i++) {
      // Examine each node in turn
      Node childNode = children.item(i);
      nodeName = childNode.getNodeName();
      boolean gotResult = false;
      boolean resultComplete = false;

      if (nodeName.equals("result")) {
        //Got a result node - is it the one we want?
        NamedNodeMap attributes = childNode.getAttributes();
        for (int j=0; j<attributes.getLength(); j++) {
          Node attr = attributes.item(j);
          String attrName = attr.getNodeName();
          String attrVal = attr.getNodeValue();
          if ((attrName.equals("name")) && 
                  (attrVal.equals("statementOutput"))) {
            gotResult = true;
          }
          else if ( attrName.equals("status") &&
              ( attrVal.equals("COMPLETED") ||
                  attrVal.equals("COMPLETE")) ) {
            resultComplete = true;
          }
        }//end of for(int j=0...)
        // Is it a complete statementOutput node?
        if (gotResult) {
          if (!resultComplete) {
          	logger.error(
			"Got incomplete results from OGSA-DAI, giving up");
            throw new Exception(
              "Got incomplete results from OGSA-DAI, giving up");
          }
          dataNode = childNode;
          // Use the first statementOutput node we find
          break;    //out of for(int i=0...) loop
        }
      }
    } //end of for(int i=0...)

    if (dataNode == null) { //Didn't find statementOutput node
      logger.error(
	  "Got no RowSet results from OGSA-DAI, giving up");
      throw new Exception(
        "Got no RowSet results from OGSA-DAI, giving up");
    }
    // Now get CDATA node 
    children = dataNode.getChildNodes();
    if (children == null) {
     logger.error(
	 "Couldn't parse OGSA-DAI response document, giving up");
     throw new Exception(
         "Couldn't parse OGSA-DAI response document, giving up");
    }
    // Now look for CDATA child containing CData RowSet results
    // Again this is a loose parse that looks for the CDATA node
    // (and accepts the first one found) and simply ignores 
    // other nodes.
    for (int i=0; i < children.getLength(); i++) {
      // Examine each node in turn
      Node childNode = children.item(i);
      if (childNode.getNodeType() == Node.CDATA_SECTION_NODE) {
        return childNode;
      }
    }
    //If we got here, we didn't find the CDATA node
    logger.error(
	"Got no CDATA RowSet results from OGSA-DAI, giving up");
    throw new Exception(
        "Got no CDATA RowSet results from OGSA-DAI, giving up");
  }

  /**
   */
  public static void main(String args[]) throws Exception {

    GdsQueryDelegate gdsQueryDelegate = new GdsQueryDelegate();

    String sql;
    String registryUrlString;
    String outputFileName = null;

    try {
      int len = args.length;

      // Get SQL query (first parameter)
      if ((len == 0) || (args[0] == null)) {
		    String errorMessage = 
          "No SQL (parameter 1) supplied to GdsQueryDelegate";
        logger.error(errorMessage);
        throw new Exception(errorMessage);
      }
      sql = args[0];

      // Get URL for ogsa-dai registry (third parameter)
      if (len < 2) {
		    String errorMessage = 
          "No Registry URL (parameter 2) supplied to GdsQueryDelegate";
        logger.error(errorMessage);
        throw new Exception(errorMessage);
      }
      registryUrlString = args[1];

      // Get results filename if supplied (third parameter)
      if (len == 3) {
        outputFileName = args[2];
      }
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

    OutputStream output;
    if (outputFileName == null) {
      output = System.out;
    }
    else {
      output = new FileOutputStream(outputFileName);
    }
    //Do actual query 
    Document result = gdsQueryDelegate.doRealQuery(
      sql, registryUrlString, output);
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
//================================================================

}
/*
$Log: GdsQueryDelegate.java,v $
Revision 1.9  2004/03/03 12:14:26  kea
Minor fix to match OGSA-DAI 3.1.  Decreased timeout to help with unit
testing in nightly build.

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

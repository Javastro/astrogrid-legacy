/*
 * $Id: GdsQueryDelegate.java,v 1.3 2003/12/11 13:22:29 kea Exp $
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


import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/*
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMResult;
*/

import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.StringReader;
import java.io.FileReader;

import java.net.URL;

import org.xml.sax.SAXException;

import org.globus.ogsa.utils.AnyHelper;
import org.globus.ogsa.GridServiceException;
import org.gridforum.ogsi.ExtensibilityType;
import org.gridforum.ogsi.HandleType;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Stub;

/**
 *
 * @author K Andrews
 */

public class GdsQueryDelegate 
{
  /**
   * Default constructor, which loads run-time installation-specific
   * configuration properties (which must be supplied in the co-located
   * "WarehouseQuerier.properties" file).
   * 
   * @throws Exception
   * @throws IOException
   * @throws SAXException
   */
  public GdsQueryDelegate() throws Exception, IOException, SAXException {
    super();    //Can throw IOException and SAXException
  }

  /*
   * Use an OGSA-DAI Grid Data Service to perform the supplied SQL query.
   */
  protected Document doRealQuery(String sql, String registryUrlString, 
        OutputStream output, boolean invokedFromMain) throws Exception {

    int timeout = 300;  // TOFIX configurable?

    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    String xmlString ="";

    // Do a synchronous query using the GDS.
    try {

      // Look at the registry to get the factory URL
      String factoryUrlString = 
          GdsDelegate.getFactoryUrlFromRegistry(registryUrlString,timeout);
      System.out.println("GDSF is " + factoryUrlString);
    
      // Create a grid-service delegate for the GDS.  This handles the
      // awkward semantics of the grid-service, including creating
      // the grid-service instance.
      System.out.println("Creating the GDS delegate...");
      GdsDelegate gds = new GdsDelegate();
      gds.setFactoryHandle(factoryUrlString);
      System.out.println("Connecting to the GDS...");
      gds.connect();

      // Run the query in the GDS.  
      // Receive in return an OGSA-DAI "response" document.
      System.out.println("Query is " + sql);
      ExtensibilityType result = gds.performSelect(sql);

      // Output the results as an XML rowset
      Node cdataNode = getResultsRowset(result);
      xmlString = cdataNode.getNodeValue();

      // Print this to stdout just in case we're shipping results via stdout
      System.out.println(WAREHOUSE_RESULT_START);

      //Print byte stream to output stream
      output.write(xmlString.getBytes());

      // Print this to stdout just in case we're shipping results via stdout
      System.out.println(WAREHOUSE_RESULT_END);
    }
    catch (AxisFault e) {
        throw new Exception(
          "Problem with Axis: " + e.getMessage());
    }
    catch (Exception e) {
      throw new Exception(
          "Unspecified exception: " + e.getMessage());
    }
    //TOFIX OUGHT TO RETURN DOCUMENT HERE JUST IN CASE
    //WE;RE INVOKED DIERECTLY
    if (invokedFromMain) {
      return null;    //No point returning a document in shelled-out mode
    }
    else {
      DOMParser parser = new DOMParser();
      try {
        parser.parse(new InputSource(
               new StringReader(xmlString)));
      }
      catch (SAXException e) {
        throw new Exception(
            "Couldn't parse results XML Rowset: " + e.getMessage());
      }
      catch (IOException e) {
        throw new Exception(
            "Couldn't parse results XML Rowset: " + e.getMessage());
      }
      return parser.getDocument();
    }
  }

  protected Node getResultsRowset(ExtensibilityType results) 
        throws Exception
  {
    Element element[];
    try {
      element = AnyHelper.getAsElement(results);
    }
    catch (GridServiceException e) {
      throw new Exception(
          "Couldn't parse OGSA-DAI response document, giving up: " +
          e.getMessage());
    }

    // Toplevel element should be gridDataServiceResponse
    Node node = (Node)element[0];
    if (node == null) {
      throw new Exception(
          "Couldn't parse OGSA-DAI response document, giving up");
    }
    String nodeName = node.getNodeName();
    if ( !nodeName.equals("gridDataServiceResponse")) {
      throw new Exception(
          "Couldn't parse OGSA-DAI response document, giving up");
    }
    NodeList children = node.getChildNodes();
    if (children == null) {
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
          else if ((attrName.equals("status")) && 
                  (attrVal.equals("COMPLETE"))) {
            resultComplete = true;
          }
        }//end of for(int j=0...)
        // Is it a complete statementOutput node?
        if (gotResult) {
          if (!resultComplete) {
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
      throw new Exception(
        "Got no RowSet results from OGSA-DAI, giving up");
    }
    // Now get CDATA node 
    children = dataNode.getChildNodes();
    if (children == null) {
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
      if (len == 0) {
        throw new Exception("No SQL query supplied to GdsQueryDelegate");
      }
      sql = args[0];
      if (sql == null) {
        throw new Exception("No SQL query supplied to GdsQueryDelegate");
      }
      if (len == 1) {
        throw new Exception("No OGSA-DAI URL supplied to GdsQueryDelegate");
      }
      registryUrlString = args[1];
      if (registryUrlString == null) {
        throw new Exception("No OGSA-DAI URL supplied to GdsQueryDelegate");
      }
      if (len > 2) {
        outputFileName = args[2];
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {  //Shouldn't get here
      throw new Exception(
          "Unexpected number of command-line arguments (" + 
         Integer.toString(args.length) + ")");
    }
    OutputStream output;
    if (outputFileName == null) {
      output = System.out;
    }
    else {
      output = new FileOutputStream(outputFileName);
    }
    //Do real query in shelled-out mode
    Document result = gdsQueryDelegate.doRealQuery(
          sql, registryUrlString, output, false);
  }

  // ----------------------------------------------------------
  // Other utility strings
  private final String TEMP_RESULTS_FILENAME = "ws_output.xml";
  private final String WAREHOUSE_RESULT_START = "WAREHOUSE_RESULT_START";
  private final String WAREHOUSE_RESULT_END = "WAREHOUSE_RESULT_END";
//================================================================

}
/*
$Log: GdsQueryDelegate.java,v $
Revision 1.3  2003/12/11 13:22:29  kea
Removed GdsQueryDelegate.properties.

Revision 1.2  2003/12/11 13:21:19  kea
OGSA-DAI registry now passed in as command-line parameter.  Will remove
GdsQueryDelegate.properties file shortly.

Revision 1.1  2003/12/02 13:56:51  kea
Delegate for performing OGSA-DAI queries, in separate package space from
Datacenter Querier module to isolate incompatible OGSA-DAI axis from
datacenter.

*/

/*
 * $Id: WarehouseQuerier.java,v 1.4 2003/12/02 12:00:11 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.warehouse.queriers.ogsadai;

import org.astrogrid.warehouse.service.SystemTalker;
import org.astrogrid.warehouse.service.TalkResult;
import org.astrogrid.warehouse.ogsadai.GdsDelegate;

import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Query;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.sql.SqlQuerierSPI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.dom.DOMResult;

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

import java.util.Properties;

import java.net.URL;

import org.xml.sax.SAXException;

import org.globus.ogsa.utils.AnyHelper;
import org.globus.ogsa.GridServiceException;
import org.gridforum.ogsi.ExtensibilityType;
import org.gridforum.ogsi.HandleType;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Stub;

//import java.sql.ResultSet;
//uk.org.ogsadai.porttype.gds.activity.sql.XMLRowsetOutputStream;

//================= DOM TEMP
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Element;
import javax.xml.parsers.*;
//================= DOM TEMP

/**
 * A querier that works with the OGSA-DAI Grid Data Warehouse.
 *
 * @author K Andrews
 */

public class WarehouseQuerier extends SqlQuerierSPI
{
  /**
   * If true, running as an axis webservice, 
   * if false, running from the commandline.
   *
   * We can't run the OGSA-DAI client code from within a webservice 
   * because of incompatibilities between vanilla and OGSA-DAI axis.
   * The current workaround is for this class to invoke itself in
   * a separate JVM (outside axis/tomcat) via a system call.
   */ 
  protected boolean invokedViaAxis = true;

  /**
   * Configuration properties for this service.
   * These use the WarehouseQuerier.properties file to discover
   * the location of the OGSA-DAI warehouse services, configure 
   * OGSA-DAI input etc.
   */
  protected Properties serviceProperties = null;

  /**
   * Default constructor, which loads run-time installation-specific
   * configuration properties (which must be supplied in the co-located
   * "WarehouseQuerier.properties" file).
   * 
   * @throws DatabaseAccessException
   * @throws IOException
   * @throws SAXException
   */
  public WarehouseQuerier() 
      throws DatabaseAccessException, IOException, SAXException {
    super();    //Can throw IOException and SAXException

    try {
      // Load installation-specific runtime properties
      serviceProperties = new Properties();
      InputStream s = WarehouseQuerier.class.getResourceAsStream(
            "WarehouseQuerier.properties"); 
      if (s == null) {
        throw new DatabaseAccessException(
          "Couldn't find properties file WarehouseQuerier.properties");
      }
      serviceProperties.load(s);
    }
    catch (IOException e) {
      throw new DatabaseAccessException(
          "Couldn't load properties from WarehouseQuerier.properties: " + 
           e.getMessage());
    }
  }

  /**
   * Performs an actual database query (first converting input ADQL into
   * SQL).
   * 
   * If invocation is via Axis, the query is shelled out to a new JVM to
   * get around vanilla Axis / OGSA Axis incompatibilities.
   *
   * The datacenter uses this method as the hook into the warehouse
   * functionality. 
   *
  public QueryResults queryDatabase(Query aQuery) 
        throws DatabaseAccessException,Exception {    //TOFIX REMOVE Exception

    String sql = "";
    OutputStream output = null;
    File tempFile = null;

    //Set up outputs
    if (this.workspace != null) {
      tempFile = makeWorkFile(TEMP_RESULTS_FILENAME);
      output = new FileOutputStream(tempFile);
    }
    else {
      tempFile = null;
      output = System.out;
    }
    // First convert ADQL query into pure SQL for use with OGSA-DAI.
    QueryTranslator trans = createQueryTranslator();
    try {
      sql = aQuery.toSql(trans);
    }
    catch (ADQLException e) {
      throw new DatabaseAccessException(
          "Couldn't translate ADQL query to SQL: " + 
           e.getMessage());
    }
    // Check if we're running in Axis or not - if so, shell out to
    // a new JVM.
    if (invokedViaAxis) {
      Document results = doShelledOutQuery(sql, tempFile);
      return new WarehouseResults(results);
    }
    else {
      //Do real query in not-shelled-out mode
      Document results = doRealQuery(sql, output, false);
      return new WarehouseResults(results);
    }
  }


  /** 
   * Factory method to create the appropriate query translator, which will
   * be used to translate from an ADQL object model to the correct SQL for
   * the warehouse database.
   *
   * @tofix : currently we're only providing one translator, but maybe
   * we'll need more to handle different SQL flavours in different 
   * backends (if we're using other DBMSs than PostgreSQL in future).
   *
   * @return adql to sql translator for the warehouse DBMS.
   */
  protected QueryTranslator createQueryTranslator() {
    return new WarehouseQueryTranslator();
  }

  /* 
   * Shells out to the command line to create a new instance of 
   * WarehouseQuerier *not* running inside Axis.  This new version
   * will perform the OGSA-DAI query.
   */
  protected Document doShelledOutQuery(String sql, File tempFile) 
      throws DatabaseAccessException, Exception { //TOFIX REMOVE EXception

    // Get class path for the Warehouse classes (eg this class)
    String classPath = serviceProperties.getProperty(
                  "WAREHOUSE_CLASSPATH", DEFAULT_WAREHOUSE_CLASSPATH);

    // Now assemble classpath for all the OGSA(-DAI) jars etc.
    // These are assumed to be in a single directory specified by
    // the property WAREHOUSE_JARDIR.

    // First extract the name of the jar directory
    String jarDir = serviceProperties.getProperty(
                  "WAREHOUSE_JARDIR");
    
    // Now find the jars within it
    if (!jarDir.equals(null)) {   // If it exists, use it.
      File dirFile = new File(jarDir);
      if (dirFile.isDirectory()) {
        File contents[] = dirFile.listFiles();  // Get directory contents
        int len = contents.length;
        for (int i = 0; i < len; i++) {
          //Is this a jar?  If so, add it to classpath
          String path = contents[i].getAbsolutePath();
          int pathlen = path.length();
          if (path.substring(pathlen-4,pathlen).equalsIgnoreCase(".jar")) {
            if (!classPath.equals("")) {
              classPath = classPath + ":";  // Add separator if needed
            }
            classPath = classPath + contents[i].getAbsolutePath();
          }
        }
      }
    }

    // Configure parameters for external call
    String[] cmdArgs;

    if (tempFile == null) {
      cmdArgs = new String[5];
    }
    else {
      cmdArgs = new String[6];
    }
    cmdArgs[0] = serviceProperties.getProperty(
                  "WAREHOUSE_JVM", DEFAULT_WAREHOUSE_JVM);
    cmdArgs[1] = "-cp";
    cmdArgs[2] = classPath;
    cmdArgs[3] = serviceProperties.getProperty(
                  "WAREHOUSE_QUERIER", DEFAULT_WAREHOUSE_QUERIER); 
    cmdArgs[4] = sql;
    if (tempFile != null) {
      cmdArgs[5] = tempFile.getAbsolutePath();
    } 
    //System.out.println("COMMAND IS:");
    //System.out.println(
    //  cmdArgs[0] + " " + 
    //  cmdArgs[1] + " " + 
    //  cmdArgs[2] + " " + 
    //  cmdArgs[3] + " " + 
    //  cmdArgs[4] + " ");

    // Use utility helper to perform call
    //System.out.print("Doing real query...");
    SystemTalker talker = new SystemTalker();
    TalkResult result = talker.talk(cmdArgs, "");
    //System.out.println("Done.");

    if (result.getErrorCode() != 0) {
      throw new DatabaseAccessException(
        "External call failed: " + result.getStderr());
    }
    // Finished external call successfully
    DOMParser parser = new DOMParser();
    try {
      if (tempFile == null) {
        // Had no temp file : expect results in stdout stream
        // Need to extract the actual results 
        String stdoutString = result.getStdout();
        int start = stdoutString.indexOf(WAREHOUSE_RESULT_START);
        int end = stdoutString.indexOf(WAREHOUSE_RESULT_END);
        if ((start == -1) || (end == -1)) {
          throw new DatabaseAccessException(
             "Couldn't read results from shelled out query's output stream");
        }
        int realstart = stdoutString.indexOf('<',start);
        if (realstart == -1) {
          throw new DatabaseAccessException(
             "Couldn't read results from shelled out query's output stream");
        }
        String realResult = stdoutString.substring(realstart, end);
        parser.parse(new InputSource(new StringReader(realResult)));
      }
      else {
        try {
          parser.parse(new InputSource(new FileReader(tempFile)));
        }
        catch (FileNotFoundException e) {
          throw new DatabaseAccessException(
              "Couldn't open results file " + tempFile.getAbsolutePath());
        }
      }
    }
    catch (SAXException e) {
      throw new DatabaseAccessException(
          "Couldn't parse results VOTable: " + "");//e.getMessage());
    }
    catch (IOException e) {
      throw new DatabaseAccessException(
          "Couldn't parse results VOTable: " + "");//e.getMessage());
    }
    return parser.getDocument();
  }

  /*
   * Use an OGSA-DAI Grid Data Service to perform the supplied SQL query.
   */
  protected Document doRealQuery(String sql, OutputStream output, 
        boolean isShelledOut)
      throws DatabaseAccessException {

    //Sanitycheck that we're not running in axis
    if (invokedViaAxis) {
      //We are running in axis - complain vociferously
      throw new DatabaseAccessException(
            "Attempting to perform real OGSA-DAI query from within Axis -" +
            "something is misconfigured!");
    }
    // Not running in axis, so do actual query!
    String registryURLString = 
        serviceProperties.getProperty(
            "HOST_STRING", DEFAULT_HOST_STRING) + 
        serviceProperties.getProperty(
            "REGISTRY_STRING", DEFAULT_REGISTRY_STRING);
  
    int timeout = 300;  // TOFIX configurable?

    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

    // Do a synchronous query using the GDS.
    try {

      // Look at the registry to get the factory URL
      String factoryURLString = 
          GdsDelegate.getFactoryUrlFromRegistry(registryURLString,timeout);
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
      System.out.println("Query is " + sql);
      ExtensibilityType result = gds.performSelect(sql);

      // Output the results
      // Convert to VOTable using XSLT
      // TOFIX make this a parameter
      String XSL_TRANSFORM = 
            "http://astrogrid.ast.cam.ac.uk/xslt/ag-warehouse-first.xsl";

      Node cdataNode = getResultsRowset(result);
      String xmlString = cdataNode.getNodeValue();

      TransformerFactory tFactory = TransformerFactory.newInstance();
      Transformer transformer = tFactory.newTransformer(
            new StreamSource(XSL_TRANSFORM));

      // Do actual transformation of XML Rowset -> VOTable
      // Put the result in a byteStream so we can access it 
      // multiple times if we need to.
      transformer.transform(new StreamSource(new StringReader(xmlString)),
          //new StreamResult(output));
          new StreamResult(byteStream));

      // Print this to stdout just in case we're shipping results via stdout
      System.out.println(WAREHOUSE_RESULT_START);

      //Print byte stream to output stream
      output.write(byteStream.toByteArray());

      // Print this to stdout just in case we're shipping results via stdout
      System.out.println(WAREHOUSE_RESULT_END);

/*
      //------------
      // TEMPORARY HACK - WRITE TO OUTPUT FILES AS WELL
      FileWriter writer = new FileWriter("/tmp/WS_OGSA_XML");
      writer.write(xmlString);
      writer.close();

      File file = new File("/tmp/WS_OGSA_VOT");
      transformer.transform(new StreamSource(new StringReader(xmlString)),
          new StreamResult(file));
      // END TEMPORARY HACK
      //------------
*/
    }
    catch (AxisFault e) {
        throw new DatabaseAccessException(
          "Problem with Axis: " + e.getMessage());
    }
    catch (Exception e) {
      throw new DatabaseAccessException(
          "Unspecified exception: " + e.getMessage());
    }
    //TOFIX OUGHT TO RETURN DOCUMENT HERE JUST IN CASE
    //WE;RE INVOKED DIERECTLY
    if (isShelledOut) {
      return null;    //No point returning a document in shelled-out mode
    }
    else {
      DOMParser parser = new DOMParser();
      try {
        parser.parse(new InputSource(
               new StringReader(byteStream.toString())));
      }
      catch (SAXException e) {
        throw new DatabaseAccessException(
            "Couldn't parse results VOTable: " + e.getMessage());
      }
      catch (IOException e) {
        throw new DatabaseAccessException(
            "Couldn't parse results VOTable: " + e.getMessage());
      }
      return parser.getDocument();
    }
  }


  protected Node getResultsRowset(ExtensibilityType results) 
        throws DatabaseAccessException
  {
    Element element[];
    try {
      element = AnyHelper.getAsElement(results);
    }
    catch (GridServiceException e) {
      throw new DatabaseAccessException(
          "Couldn't parse OGSA-DAI response document, giving up: " +
          e.getMessage());
    }

    // Toplevel element should be gridDataServiceResponse
    Node node = (Node)element[0];
    if (node == null) {
      throw new DatabaseAccessException(
          "Couldn't parse OGSA-DAI response document, giving up");
    }
    String nodeName = node.getNodeName();
    if ( !nodeName.equals("gridDataServiceResponse")) {
      throw new DatabaseAccessException(
          "Couldn't parse OGSA-DAI response document, giving up");
    }
    NodeList children = node.getChildNodes();
    if (children == null) {
      throw new DatabaseAccessException(
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
            throw new DatabaseAccessException(
              "Got incomplete results from OGSA-DAI, giving up");
          }
          dataNode = childNode;
          // Use the first statementOutput node we find
          break;    //out of for(int i=0...) loop
        }
      }
    } //end of for(int i=0...)

    if (dataNode == null) { //Didn't find statementOutput node
      throw new DatabaseAccessException(
        "Got no RowSet results from OGSA-DAI, giving up");
    }
    // Now get CDATA node 
    children = dataNode.getChildNodes();
    if (children == null) {
     throw new DatabaseAccessException(
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
    throw new DatabaseAccessException(
        "Got no CDATA RowSet results from OGSA-DAI, giving up");
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

    WarehouseQuerier querier = new WarehouseQuerier();

    // We're not running in axis
    querier.invokedViaAxis = false;

    String sql;
    String outputFileName = null;
    try {
      int len = args.length;
      if (len == 0) {
        //TOFIX 
        throw new DatabaseAccessException(
            "No SQL supplied for shelled-out query at command-line");
      }
      sql = args[0];
      if (sql.equals(null)) {
        throw new DatabaseAccessException(
            "No SQL supplied for shelled-out query at command-line");
      }
      if (len > 1) {
        outputFileName = args[1];
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {
      throw new DatabaseAccessException(
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
    Document result = querier.doRealQuery(sql, output, false);
  }

  // ----------------------------------------------------------
  // Fallback defaults for values that should be configured on a
  // per-installation basis in the WarehouseServiceImpl.properties 

  private final String DEFAULT_HOST_STRING = 
        "http://astrogrid.ast.cam.ac.uk:4040";
  private final String DEFAULT_REGISTRY_STRING = 
        "/gdw/services/ogsadai/DAIServiceGroupRegistry";

  private final String DEFAULT_WAREHOUSE_JVM = 
        "/data/cass123a/gtr/jdk-ogsa/bin/java";
  private final String DEFAULT_WAREHOUSE_CLASSPATH =
        "/data/cass123a/kea/tomcat_cass111/webapps/axis/WEB-INF/classes";
  private final String DEFAULT_WAREHOUSE_QUERIER =
        "org.astrogrid.warehouse.queriers.ogsadai.WarehouseQuerier";

  // Other utility strings
  private final String TEMP_RESULTS_FILENAME = "ws_output.xml";
  private final String WAREHOUSE_RESULT_START = "WAREHOUSE_RESULT_START";
  private final String WAREHOUSE_RESULT_END = "WAREHOUSE_RESULT_END";
//================================================================

}
/*
$Log: WarehouseQuerier.java,v $
Revision 1.4  2003/12/02 12:00:11  gtr
GdsDelegate is imported from its new package org.astrogrid.warehouse.ogsadai.

Revision 1.3  2003/12/02 10:00:33  gtr
Refectored to org.astrogrid.warehouse.ogsadai.

Revision 1.2  2003/11/26 19:46:19  kea
Basic datacenter-style querier, now fully integrated so it should
when invoked via datacenter framework.
Needs a lot of cleaning up and documentation added.
May need revision to meet forthcoming datacenter codebase changes.

Revision 1.1  2003/11/19 17:33:40  kea
Initial Querier functionality for integration with the database.
This compiles and sort-of runs at the command-line;  however, we
currently have no means of getting the OGSA-DAI results into the
format expected by the datacenter.  (They currently get dumped to
/tmp/WS_OGSA_OUTPUT).

*/

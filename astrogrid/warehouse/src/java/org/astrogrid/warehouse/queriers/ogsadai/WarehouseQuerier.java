/*
 * $Id: WarehouseQuerier.java,v 1.7 2003/12/08 20:16:54 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.warehouse.queriers.ogsadai;

import org.astrogrid.warehouse.service.SystemTalker;
import org.astrogrid.warehouse.service.TalkResult;

import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.sql.AdqlQueryTranslator;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.axisdataserver.types._query;
import org.astrogrid.util.DomLoader;

import org.w3c.dom.Document;

//import org.apache.xerces.parsers.DOMParser;
//import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.ByteArrayOutputStream;
import java.io.FileReader;
import java.io.StringReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Properties;

/**
 * An AstroGrid datacenter plugin Querier that provides access 
 * to the AstroGrid OGSA-DAI Grid Data Warehouse.
 *
 * Due to incompatibilities between vanilla Axis and the customised 
 * version of Axis used by OGSA-DAI, this plugin querier does not talk
 * directly to the OGSA-DAI installation.  (This allows us to insulate
 * the datacenter runtime from the customised Axis classes).
 *
 * Instead, it shells out to the command-line and invokes a 
 * {@link GdsQueryDelegate} (running in a new JVM) to perform 
 * the query and produce XML RowSet results.  
 * This <code>WarehouseQuerier</code> then converts these results to 
 * VOTable for passing back up into the datacenter.
 *
 * Communication between this <code>WarehouseQuerier</code> and 
 * a {@link GdsQueryDelegate} is via temporary workspace files if available, 
 * or via process input and output streams otherwise.  
 * I suspect this will have poor speed and scalability implications.
 *
 * @TOFIX The conversion from XML RowSet to VOTable perhaps involves
 * too many different intermediate IO/stream classes;  can it be made
 * simpler?
 * 
 * @TOFIX  XSLT conversion currently uses the same stylesheet for
 * all datasets in the warehouse, and thus may be lying about what
 * the COOSYS is.  
 *
 * @author K Andrews
 * @version 1.0
 * @see GdsQueryDelegate
 */
public class WarehouseQuerier extends Querier
{
  /**
   * Configuration properties for this service.
   * These are found in the 'WarehouseQuerier.properties' file,
   * which provides the following properties:
   *
   *   - DatabaseQuerierClass: Fully qualified classname for this class
   *   - WarehouseJvm: Full path to Java JRE for shelled-out call 
   *   - WarehouseQuerier: Fully qualified classname of GdsQueryDelegate
   *   - WarehouseClasspath: 
   *       Classpath to be used by GdsQueryDelegate (if any)
   *   - WarehouseJarDir: Directory containing jars needed by
   *       GdsQueryDelegate (if any)
   */
  protected Properties serviceProperties = null;

  /**
   * Default constructor initialises parent with query data and 
   * loads run-time, installation-specific configuration properties 
   * (which must be supplied in a 'WarehouseQuerier.properties' file).
   * 
   * @param queryId  String identifier for tracking this query
   * @param query  Query representation in datacenter internal format
   * @throws DatabaseAccessException
   * @throws IOException
   * @throws SAXException
   */
  public WarehouseQuerier(String queryId, _query query) 
      throws DatabaseAccessException, IOException, SAXException {
    super(queryId, query);
    log.debug("Constructing WarehouseQuerier");

    try {
      // Load installation-specific runtime properties
      serviceProperties = new Properties();
      InputStream s = WarehouseQuerier.class.getResourceAsStream(
            "WarehouseQuerier.properties"); 
      if (s == null) {
        log.error("WarehouseQuerier couldn't find " +
          "properties file 'WarehouseQuerier.properties'");
        throw new DatabaseAccessException(
          "Couldn't find properties file WarehouseQuerier.properties");
      }
      serviceProperties.load(s);
    }
    catch (IOException e) {
      log.error("WarehouseQuerier couldn't load properties from " +
          "properties file 'WarehouseQuerier.properties'");
      throw new DatabaseAccessException(
          "Couldn't load properties from WarehouseQuerier.properties: " + 
           e.getMessage());
    }
    log.debug("Successfully loaded WarehouseQuerier properties");
  }

  /**
   * Performs an actual database query (by shelling out to a
   * GdsQueryDelegate running in a separate JVM). 
   *
   * Converts input ADQL query into the SQL expected by the
   * GdsQueryDelegate, and converts GdsQueryDelegate's XML RowSet
   * results into the VOTable expected by the datacenter.
   *
   * @TOFIX  XSLT conversion currently uses the same stylesheet for
   * all datasets in the warehouse, and thus may be lying about what
   * the COOSYS is.  
   * 
   * @TOFIX The conversion from XML RowSet to VOTable perhaps involves
   * too many different intermediate IO/stream classes;  can it be made
   * simpler?
   *
   * @return a QueryResults object wrapping the VOTable Document results.
   * @throws DatabaseAccessException
   */
  public QueryResults doQuery() throws DatabaseAccessException {

    String sql;
    AdqlQueryTranslator translator = new AdqlQueryTranslator();
    try {
      sql = (String) translator.translate(getQueryingElement());
      System.out.println("SQL is " + sql);
    }
    catch (Exception e) {
      log.error("ADQLQueryTranslator couldn't get SQL query from input ADQL");
      throw new DatabaseAccessException("Couldn't get SQL query:" + 
            e.getMessage());
    }
    log.debug("Successfully created SQL query from input ADQL");

    OutputStream output = null;
    File tempFile = null;
    //Set up outputs
    if (this.workspace != null) {
      try {
        tempFile = workspace.makeWorkFile(TEMP_RESULTS_FILENAME);
        output = new FileOutputStream(tempFile);
      }
      catch (Exception e) { //IOException or FileNotFoundException
        // Couldn't create temporary workspace file, use stdout instead
        log.warn("Couldn't open temporary workspace file, "+
            "using stdin/stdout instead.");
        tempFile = null;
        output = System.out;
      }
    }
    else {
      log.info("WarehouseQuerier workspace is null, using stdin/stdout");
      tempFile = null;
      output = System.out;
    }
    Document results = doShelledOutQuery(sql, tempFile);
    return new WarehouseResults(results);
  }

  /* 
   * Shells out to the command line to delegate the query operation to
   * a GdsQueryDelegate running in a separate JVM.
   * 
   * The GdsQueryDelegate accepts SQL, performs the actual query via
   * OGSA-DAI, and returns XML RowSet results.
   *
   * @param sql  String containing the SQL query to be performed
   * @param tempFile  File to hold the GdsQueryDelegate's (XML RowSet) results
   * @return Document containing VOTable-ised query results
   */
  protected Document doShelledOutQuery(String sql, File tempFile) 
      throws DatabaseAccessException { 

    log.debug("Commencing doShelledOutQuery");
    // Get class path for the Warehouse classes (eg this class)
    String classPath = serviceProperties.getProperty(
                  "WarehouseClasspath", DEFAULT_WAREHOUSE_CLASSPATH);

    // Extract the name of the jar directory, containing jars to be
    // included in the shelled-out classpath
    String jarDir = serviceProperties.getProperty("WarehouseJarDir");
    // If it exists, use it.
    if ((!jarDir.equals("")) && (!jarDir.equals(null))) { 
      classPath = getJarDirClasspath(classPath,jarDir);
    }
    log.debug("Parameters assembled, preparing shelled-out call");

    // Configure parameters for external call
    String[] cmdArgs;
    if (tempFile == null) {
      cmdArgs = new String[5];
    }
    else {
      cmdArgs = new String[6];
    }
    cmdArgs[0] = serviceProperties.getProperty(
                  "WarehouseJvm", DEFAULT_WAREHOUSE_JVM);
    cmdArgs[1] = "-cp";
    cmdArgs[2] = classPath;
    cmdArgs[3] = serviceProperties.getProperty(
                  "WarehouseQuerier", DEFAULT_WAREHOUSE_QUERIER); 
    cmdArgs[4] = sql;
    if (tempFile != null) {
      cmdArgs[5] = tempFile.getAbsolutePath();
    } 

    // Use utility helper to perform call
    log.info("Commencing shelled-out query");
    SystemTalker talker = new SystemTalker();
    TalkResult result = talker.talk(cmdArgs, "");

    if (result.getErrorCode() != 0) {
      log.error("Shelled-out query failed:" + result.getStderr());
      throw new DatabaseAccessException(
        "External call failed: " + result.getStderr());
    }
    // Finished external call successfully
    log.info("Shelled-out query succeeded");

    // Get XML rowset results
    TransformerFactory tFactory;
    Transformer transformer;
    try {
      tFactory = TransformerFactory.newInstance();
      transformer = tFactory.newTransformer(
           new StreamSource(serviceProperties.getProperty(
              "XslTransform", DEFAULT_XSL_TRANSFORM)));
    }
    catch (Exception e){
      String errorMessage = 
        "Couldn't create XML->VOTable XSLT transformer: " + e.getMessage();
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }

    ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
    if (tempFile == null) {
      // Had no temp file : expect results in stdout stream
      // Need to extract the actual results 
      String stdoutString = result.getStdout();
      int start = stdoutString.indexOf(WAREHOUSE_RESULT_START);
      int end = stdoutString.indexOf(WAREHOUSE_RESULT_END);
      int realstart = stdoutString.indexOf('<',start);

      // Check we can find the results in the output stream
      if ((start == -1) || (end == -1) || (realstart == -1)) {
        String errorMessage = 
          "Couldn't read results from shelled out query's output stream";
        log.error(errorMessage);
        throw new DatabaseAccessException(errorMessage);
      }
      String realResult = stdoutString.substring(realstart, end);
      // Do actual transformation of XML Rowset -> VOTable
      try {
        transformer.transform(new StreamSource(new StringReader(realResult)),
           new StreamResult(byteStream));
      } 
      catch (Exception e){ // SAXException, IOException
        String errorMessage = 
            "Couldn't transform XML to VOTable using XSLT transformer: " + 
            e.getMessage();
        log.error(errorMessage);
        throw new DatabaseAccessException(errorMessage);
      }
    }
    else {
      // Have a temp file : this is where the XML RowSet results are.
      // Do actual transformation of XML Rowset -> VOTable
      try {
        transformer.transform(new StreamSource(new FileReader(tempFile)),
           new StreamResult(byteStream));
      } 
      catch (FileNotFoundException e) {
        throw new DatabaseAccessException(
            "Couldn't open results file " + tempFile.getAbsolutePath());
      }
      catch (Exception e){ // SAXException, IOException
        throw new DatabaseAccessException(
            "Couldn't transform XML to VOTable using XSLT transformer: " + 
            e.getMessage());
      }
    }
    log.info("Converted XML RowSet to VOTable successfully");
    //System.out.println(byteStream.toString());
    //log.debug(byteStream.toString()); //COULD BE VERY VERBOSE!!

    // Now parse VOTable XML data stream into Document
    try {
      Document resultsDoc = DomLoader.readDocument(byteStream.toString());

      log.info("Parsed converted VOTable successfully");
      return resultsDoc;
    }
    catch (Exception e) { //ParserConfigurationException, SAXException, 
                          //IOException
      String errorMessage = 
          "Couldn't parse results VOTable from XSLT conversion";
      log.error(errorMessage + ": " + e.getMessage());
      throw new DatabaseAccessException(errorMessage);
    }

    /*
    // OLD VERSION - USED XERCES, NOT GENERIC DomLoader
    DOMParser parser = new DOMParser();
    try {
      parser.parse(new InputSource(
          new StringReader(byteStream.toString())));
    }
    catch (Exception e) { //SAXException, IOException
      String errorMessage = 
          "Couldn't parse results VOTable from XSLT conversion";
      log.error(errorMessage + ": " + e.getMessage());
      throw new DatabaseAccessException(errorMessage);
    }
    log.info("Parsed converted VOTable successfully");
    return parser.getDocument();
    */
  }

    // Now assemble classpath for all the OGSA(-DAI) jars etc.
    // These are assumed to be in a single directory specified by
    // the property WarehouseJarDir.

  /* 
   * Assembles a Java classpath by extending the supplied classpath to
   * include all the jars in the supplied directory.
   * 
   * @param classPath  String containing the initial classpath (if any)
   * @param jarDir  String providing full path to jar directory
   * @return  String containing extended classpath
   */
  protected String getJarDirClasspath(String classPath, String jarDir) {
    if (classPath.equals(null)) {
      classPath = "";
    }
    if ((!jarDir.equals("")) && (!jarDir.equals(null))) {  //Sanitycheck
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
    return classPath;
  }

  // ----------------------------------------------------------
  // Fallback defaults for values that should be configured on a
  // per-installation basis in the WarehouseServiceImpl.properties 
  private final String DEFAULT_WAREHOUSE_JVM = 
        "/usr/bin/java";
  private final String DEFAULT_WAREHOUSE_CLASSPATH =
        ".";
  private final String DEFAULT_WAREHOUSE_QUERIER =
        "org.astrogrid.warehouse.ogsadai.GdsQueryDelegate";

  private final String DEFAULT_XSL_TRANSFORM = 
        "http://astrogrid.ast.cam.ac.uk/xslt/ag-warehouse-first.xsl";

  // Other utility strings
  private final String TEMP_RESULTS_FILENAME = "ws_output.xml";
  private final String WAREHOUSE_RESULT_START = "WAREHOUSE_RESULT_START";
  private final String WAREHOUSE_RESULT_END = "WAREHOUSE_RESULT_END";
//================================================================

}
/*
$Log: WarehouseQuerier.java,v $
Revision 1.7  2003/12/08 20:16:54  kea
Added JavaDoc.  Changed properties to use Java-style capitalisation.
Misc. small tidyings.

Revision 1.6  2003/12/04 18:54:56  kea
Minimal logging added - more to follow.

Revision 1.5  2003/12/02 16:22:13  kea
Moved ogsa-dai functionality out into GdsQuerierDelegate.
Misc. changes to accommodate changes in datacenter codebase.

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

/*
 * $Id: WarehouseQuerier.java,v 1.10 2003/12/11 13:14:48 kea Exp $
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
      log.info("SQL query is " + sql);
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
   * @throws DatabaseAccessException
   */
  protected Document doShelledOutQuery(String sql, File tempFile) 
      throws DatabaseAccessException { 

    log.debug("Commencing doShelledOutQuery");

    // Configure parameters for external call
    String[] cmdArgs;
    if (tempFile == null) {
      cmdArgs = new String[5];
    }
    else {
      cmdArgs = new String[6];
    }
    //cmdArgs[0] = serviceProperties.getProperty(
     //             "WarehouseJvm", DEFAULT_WAREHOUSE_JVM);
    cmdArgs[0] = getJavaBinary();
    cmdArgs[1] = "-jar";
    cmdArgs[2] = getExecutableJar();
    cmdArgs[3] = sql;
    cmdArgs[4] = getOgsaDaiRegistryString();
    if (tempFile != null) {
      cmdArgs[5] = tempFile.getAbsolutePath();
      log.debug("Command is: " + cmdArgs[0] + " " + cmdArgs[1] + 
            " " + cmdArgs[2] + " " + cmdArgs[3] + " " + cmdArgs[4]);
      // TOFIX REMOVE
      System.out.println("Command is: " + cmdArgs[0] + " " + cmdArgs[1] + 
            " " + cmdArgs[2] + " " + cmdArgs[3] + " " + cmdArgs[4]);
    } 
    else {
      log.debug("Command is: " + cmdArgs[0] + " " + cmdArgs[1] + 
            " " + cmdArgs[2] + " " + cmdArgs[3]);
      // TOFIX REMOVE
      System.out.println("Command is: " + cmdArgs[0] + " " + cmdArgs[1] + 
            " " + cmdArgs[2] + " " + cmdArgs[3]);
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
  }

  /* 
   * Assembles the URL of the OGSA-DAI registry to be used by the 
   * GdsQueryDelegate.
   * 
   * @return  String holding full URL of the OGSA-DAI registry
   * @throws DatabaseAccessException
   */
  protected String getOgsaDaiRegistryString() throws DatabaseAccessException {
    String host = serviceProperties.getProperty("OgsaDaiHostString");
    if (host == null) {
      String errorMessage = 
        "Fatal error: Property 'OgsaDaiHostString' not found in file " +
        "'WarehouseQuerier.properties'";
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }
    String registry = serviceProperties.getProperty("OgsaDaiRegistryString");
    if (registry == null) {
      String errorMessage = 
        "Fatal error: Property 'OgsaDaiHostString' not found in file " +
        "'WarehouseQuerier.properties'";
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }
    return host + registry;
  }

  /* 
   * Assembles the fully-qualified path of the Java JVM to be shelled
   * out to.
   * 
   * By default, looks for the environment variable JAVA_HOME and figures
   * out the path from that.
   *
   * If JAVA_HOME is not defined, looks for a local property instead.
   * 
   * @return  String holding full path of the Java JVM binary
   * @throws DatabaseAccessException
   */
  protected String getJavaBinary() throws DatabaseAccessException {
    // First, check if user has customised the JVM location 
    //  in the WarehouseQuerier.properties file
    String customJVM = serviceProperties.getProperty("WarehouseJvm");
    if (customJVM != null) {
      return customJVM;  // Use customised JVM if it exists
    }
    else {
      // No custom JVM - use JVM in JAVA_HOME
      String javaHome = System.getProperty("java.home");
      if (javaHome == null) {  //Shouldn't happen
        String errorMessage = 
          "Fatal error: System property 'java.home' not defined! "+
             "Please set WarehouseJvm property in " +
             "'WarehouseQuerier.properties' file";
        log.error(errorMessage);
        throw new DatabaseAccessException(errorMessage);
      }
      String separator = System.getProperty("file.separator");
      if (separator == null) {
        log.warn("Warning, couldn't get system file.separator, assuming '/'");
        separator = "/";
      }
      // Assume JVM binary is called java and is in bin dir of JAVA_HOME
      String fullPath = javaHome + separator + "bin" + separator + "java"; 
      File testFile = new File(fullPath);
      if (!(testFile.exists())) {
        //Try looking for a .exe version - might be running under Windows
        testFile = new File(fullPath + ".exe");
        if (!(testFile.exists())) {
          String errorMessage = 
            "Fatal error: Java binary '" + fullPath + "[.exe]' not found! "+
               "Please set WarehouseJvm property in " +
               "'WarehouseQuerier.properties' file";
          log.error(errorMessage);
          throw new DatabaseAccessException(errorMessage);
        }
        else {
          return fullPath + ".exe";
        }
      }
      return fullPath;
    }
  }

  /* 
   * Assembles the fully-qualified path of the Java executable jar 
   * containing the OGSA-DAI GdsQueryDelegate.
   * 
   * The location and name of the jar can be customised for a given
   * installation in the WarehouseQuerier.properties file.
   * 
   * @return  String holding full path of the GdsQueryDelegate executable jar
   * @throws DatabaseAccessException
   */
  protected String getExecutableJar() throws DatabaseAccessException {
    // Extract path to directory containing executable jar 
    String jarPath = serviceProperties.getProperty("ExecutableJarPath");
    if (jarPath == null) {
      String errorMessage = "Property 'ExecutableJarPath' not set in " +
          "properties file 'WarehouseQuerier.properties'";
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }
    String sep = System.getProperty("file.separator");
    if (sep == null) {
      log.warn("Warning, couldn't get system file.separator, assuming " +
          "ExecutableJarPath is properly terminated with file separator");
    }
    else {
      int pathlen = jarPath.length();
      // If last char in path not separator, add separator
      if (!(jarPath.substring(pathlen-1,pathlen).equalsIgnoreCase(sep))) {
        jarPath = jarPath + sep;
      }
    }
    // Extract name of executable jar
    String jarName = serviceProperties.getProperty("ExecutableJarName");
    if (jarName == null) {
      String errorMessage = "Property 'ExecutableJarName' not set in " +
          "properties file 'WarehouseQuerier.properties'";
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }
    return jarPath + jarName;
  }

  // ----------------------------------------------------------
  // Fallback defaults for values that should be configured on a
  // per-installation basis in the WarehouseServiceImpl.properties 
  private final String DEFAULT_WAREHOUSE_JVM = 
        "/usr/bin/java";
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
Revision 1.10  2003/12/11 13:14:48  kea
Moved OGSA-DAI host and registry location into WarehouseQuerier properties
file so they can be edited more easily in a tomcat inst.

Revision 1.9  2003/12/10 12:27:56  kea
Finding JVM to shell out to from JAVA_HOME.

Revision 1.8  2003/12/09 12:19:34  kea
Changed shelling-out to use executable jar (including adjustments
to properties file).

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

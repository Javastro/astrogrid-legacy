/*
 * $Id: WarehouseQuerier.java,v 1.6 2003/12/04 18:54:56 kea Exp $
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

import org.w3c.dom.Document;

import org.apache.xerces.parsers.DOMParser;
import org.xml.sax.InputSource;
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
 * A querier that works with the OGSA-DAI Grid Data Warehouse.

 * @TOFIX All this IO/streams stuff seems a bit overcomplicated, is this
 *  the simplest way to do things?

 * @author K Andrews
 */

public class WarehouseQuerier extends Querier
{
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
  public WarehouseQuerier(String queryId, _query query) 
      throws DatabaseAccessException, IOException, SAXException {
  //public WarehouseQuerier() 
   //   throws DatabaseAccessException, IOException, SAXException {
    super(queryId, query);
    log.info("Got into WarehouseQuerier constructor.");

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
    log.info("Successfully loaded properties in WarehouseQuerier constructor.");
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
   */
  public QueryResults doQuery() throws DatabaseAccessException {

    String sql;
    AdqlQueryTranslator translator = new AdqlQueryTranslator();
    try {
      sql = (String) translator.translate(getQueryingElement());
    }
    catch (Exception e) {
      throw new DatabaseAccessException("Couldn't get SQL query:" + 
            e.getMessage());
    }

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
        tempFile = null;
        output = System.out;
      }
    }
    else {
      tempFile = null;
      output = System.out;
    }
    Document results = doShelledOutQuery(sql, tempFile);
    return new WarehouseResults(results);
  }

  /* 
   * Shells out to the command line to create a new instance of 
   * WarehouseQuerier *not* running inside Axis.  This new version
   * will perform the OGSA-DAI query.
   */
  protected Document doShelledOutQuery(String sql, File tempFile) 
      throws DatabaseAccessException { 

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

    // Use utility helper to perform call
    SystemTalker talker = new SystemTalker();
    TalkResult result = talker.talk(cmdArgs, "");

    if (result.getErrorCode() != 0) {
      throw new DatabaseAccessException(
        "External call failed: " + result.getStderr());
    }
    // Finished external call successfully
    // Get XML rowset results

    TransformerFactory tFactory;
    Transformer transformer;
    try {
      tFactory = TransformerFactory.newInstance();
      transformer = tFactory.newTransformer(
           new StreamSource(serviceProperties.getProperty(
              "XSL_TRANSFORM", DEFAULT_XSL_TRANSFORM)));
    }
    catch (Exception e){
      throw new DatabaseAccessException(
        "Couldn't create XML->VOTable XSLT transformer: " + e.getMessage());
    }

    DOMParser parser = new DOMParser();
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
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      // Do actual transformation of XML Rowset -> VOTable
      try {
        transformer.transform(new StreamSource(new StringReader(realResult)),
           new StreamResult(byteStream));
      } 
      catch (Exception e){
        throw new DatabaseAccessException(
            "Couldn't transform XML to VOTable using XSLT transformer: " + 
            e.getMessage());
      }
      System.out.println(byteStream.toString());
      try {
        parser.parse(new InputSource(
            new StringReader(byteStream.toString())));
      }
      catch (SAXException e) {
        throw new DatabaseAccessException(
        "Couldn't parse results VOTable: " + "");//e.getMessage());
      }
      catch (IOException e) {
        throw new DatabaseAccessException(
            "Couldn't parse results VOTable: " + "");//e.getMessage());
      }
    }
    else {
      ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
      // Do actual transformation of XML Rowset -> VOTable
      try {
        transformer.transform(new StreamSource(new FileReader(tempFile)),
           new StreamResult(byteStream));
      } 
      catch (FileNotFoundException e) {
        throw new DatabaseAccessException(
            "Couldn't open results file " + tempFile.getAbsolutePath());
      }
      catch (Exception e){
        throw new DatabaseAccessException(
            "Couldn't transform XML to VOTable using XSLT transformer: " + 
            e.getMessage());
      }
      System.out.println(byteStream.toString());
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
    }
    return parser.getDocument();
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

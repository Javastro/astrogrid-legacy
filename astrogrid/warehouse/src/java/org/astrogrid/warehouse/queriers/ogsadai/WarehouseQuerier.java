/*
 * $Id: WarehouseQuerier.java,v 1.1 2003/11/19 17:33:40 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.warehouse.queriers.ogsadai;

import org.astrogrid.warehouse.service.SystemTalker;
import org.astrogrid.warehouse.service.TalkResult;
import org.astrogrid.warehouse.service.GdsDelegate;

import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Query;
import org.astrogrid.datacenter.queriers.QueryTranslator;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.sql.SqlQuerier;

import java.io.IOException;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;

import java.util.Properties;

import java.net.URL;

import javax.xml.namespace.QName;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import org.globus.ogsa.utils.AnyHelper;
import org.gridforum.ogsi.ExtensibilityType;
import org.gridforum.ogsi.HandleType;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Stub;

//import java.sql.ResultSet;
//uk.org.ogsadai.porttype.gds.activity.sql.XMLRowsetOutputStream;


/**
 * A querier that works with the OGSA-DAI Grid Data Warehouse.
 *
 * @author K Andrews
 */

public class WarehouseQuerier extends SqlQuerier
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
        throws DatabaseAccessException {

    String sql = "";
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
      return doShelledOutQuery(sql);
    }
    else {
      return doRealQuery(sql);
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
  protected QueryResults doShelledOutQuery(String sql) 
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
            if (!classPath.equals("") && (i != len-1)) {
              classPath = classPath + ":";  // Add separator if needed
            }
            classPath = classPath + contents[i].getAbsolutePath();
          }
        }
      }
    }

    // Configure parameters for external call
    String[] cmdArgs = new String[5];
    cmdArgs[0] = serviceProperties.getProperty(
                  "WAREHOUSE_JVM", DEFAULT_WAREHOUSE_JVM);
    cmdArgs[1] = "-cp";
    cmdArgs[2] = classPath;
    cmdArgs[3] = serviceProperties.getProperty(
                  "WAREHOUSE_QUERIER", DEFAULT_WAREHOUSE_QUERIER); 
    cmdArgs[4] = sql;

    // Use utility helper to perform call
    SystemTalker talker = new SystemTalker();
    TalkResult result = talker.talk(cmdArgs, "");

    if (result.getErrorCode() != 0) {
      throw new DatabaseAccessException(
        "External call failed: " + result.getStdout() + " " +
        result.getStderr());
    }
    // Finished external call successfully
    //TOFIX HOW ARE WE GOING TO CONVERT THE XML ROWSET TO SqlResults ? 
    //XMLRowsetOutputStream results = new XMLRowsetOutputStream(result.getStdout());
    //return new SqlResults(results, workspace);
    return null;
  }

  /*
   * Use an OGSA-DAI Grid Data Service to perform the supplied SQL query.
   */
  protected QueryResults doRealQuery(String sql)
      throws DatabaseAccessException {

    //Sanitycheck that we're not running in axis
    if (invokedViaAxis) {
      //We are running in axis - so shell out to a new JVM
      return doShelledOutQuery(sql);
    }
    // Not running in axis, so do actual query!
    String registryURLString = 
        serviceProperties.getProperty(
            "HOST_STRING", DEFAULT_HOST_STRING) + 
        serviceProperties.getProperty(
            "REGISTRY_STRING", DEFAULT_REGISTRY_STRING);
  
    int timeout = 300;  // TOFIX configurable?

    // Do a synchronous query using the GDS.
    try {
    
      // Create a grid-service delegate for the GDS.  This handles the
      // awkward semantics of the grid-service, including creating
      // the grid-service instance.
      System.out.println("Creating the GDS delegate...");
      GdsDelegate gds = new GdsDelegate();

      // Look at the registry to get the factory URL
      String factoryURLString = 
          gds.getFactoryUrlFromRegistry(registryURLString,timeout);
      System.out.println("GDSF is " + factoryURLString);
      gds.setFactoryHandle(factoryURLString);
      System.out.println("Connecting to the GDS...");
      gds.connect();

      // Run the query in the GDS.  
      // Receive in return an OGSA-DAI "response" document.
      ExtensibilityType result = gds.performSelect(sql);

      // Output the results
      //TOFIX how to convert from ExtensibilityType to ResultSet??
      //ResultSet results = new ResultSet(AnyHelper.getAsString(result));
      //return new SqlResults(results, workspace);

      // TEMPORARY HACK - WRITE TO OUTPUT FILE 
      try {
        FileWriter writer = new FileWriter("/tmp/WS_OGSA_OUTPUT");
        writer.write(AnyHelper.getAsString(result));
        writer.close();
      }
      catch (IOException e) {
        throw new DatabaseAccessException(
            "Couldn't open destination file /tmp/WS_OGSA_OUTPUT");
      }
      // TOFIX SHOULD BE RETURNING SqlResults HERE
      return null;
    }
    catch (AxisFault e) {
        throw new DatabaseAccessException(
          "Problem with Axis: + e.getMessage()");
    }
    catch (Exception e) {
      throw new DatabaseAccessException(
          "Unspecified exception: + e.getMessage()");
    }
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
    try {
      sql = args[0];
      if (sql.equals(null)) {
        //TOFIX
        throw new DatabaseAccessException("No query!");
      }
    }
    catch (ArrayIndexOutOfBoundsException e) {
      throw new DatabaseAccessException(
          "Unexpected number of command-line arguments (" + 
         Integer.toString(args.length) + ")");
    }
    querier.doRealQuery(sql);
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
  private final String DEFAULT_WAREHOUSE_QUERIER =
        "org.astrogrid.warehouse.queriers.ogsadai.WarehouseQuerier";
}
/*
$Log: WarehouseQuerier.java,v $
Revision 1.1  2003/11/19 17:33:40  kea
Initial Querier functionality for integration with the database.
This compiles and sort-of runs at the command-line;  however, we
currently have no means of getting the OGSA-DAI results into the
format expected by the datacenter.  (They currently get dumped to
/tmp/WS_OGSA_OUTPUT).

*/

/*
 * $Id: WarehouseQuerier.java,v 1.6 2004/03/17 12:20:54 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.ogsadai;

import java.io.*;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.queriers.DatabaseAccessException;
import org.astrogrid.datacenter.queriers.Querier;
import org.astrogrid.datacenter.queriers.QuerierPlugin;
import org.astrogrid.datacenter.queriers.ogsadai.PostgresAdqlQueryTranslator;
import org.astrogrid.datacenter.queriers.status.QuerierComplete;
import org.astrogrid.util.DomLoader;
import org.astrogrid.util.Workspace;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

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
 * @author K Andrews
 * @version 1.0
 * @see GdsQueryDelegate
 */
public class WarehouseQuerier extends QuerierPlugin {
   
   Workspace workspace = null;
   
  /**
   * Default constructor initialises parent with query data.
   * Datacenter infrastructure takes care of loading config parameters
   * from toplevel AstroGridConfig.properties file.
   *
   * @param queryId  String identifier for tracking this query
   * @param query  Query representation in datacenter internal format
   * @throws DatabaseAccessException
   * @throws IOException
   * @throws SAXException
   */
  public WarehouseQuerier(Querier querier) throws IOException {
    super(querier);
    log.debug("Constructing WarehouseQuerier");
  }

  /**
   * Performs an actual database query (by shelling out to a
   * GdsQueryDelegate running in a separate JVM).
   *
   * Converts input ADQL query into the SQL expected by the
   * GdsQueryDelegate. and converts GdsQueryDelegate's XML RowSet
   * results into the VOTable expected by the datacenter.
   *
   * @TOFIX  We really need customised ADQL->SQL translation customised
   * for our DBMS / table indices.
   *
   * @return a QueryResults object wrapping the VOTable Document results.
   * @throws DatabaseAccessException
   */
  public void askQuery() throws IOException {

    //convert to SQL - throws QueryException out of this method if a problem with it
    PostgresSqlMaker sqlMaker = new PostgresSqlMaker();
    String sql = escapeXmlSpecialChars(sqlMaker.getSql(querier.getQuery()));
     
    log.info("SQL query is " + sql);
    log.debug("Successfully created SQL query from input ADQL");

    // Get temp file to pass to Query delegate 
     try {
       workspace = new Workspace("Warehouse_"+querier.getId());
     }
     catch (Exception th) {
        log.error(th);
     }
     
    File tempFile = null;
    //Set up outputs
    if (this.workspace != null) {
      try {
        tempFile = workspace.makeWorkFile(TEMP_RESULTS_FILENAME);
      }
      catch (Exception e) { //IOException or FileNotFoundException
        // Couldn't create temporary workspace file, use stdout instead
        String errMess = "Couldn't open temporary workspace file: "
            + e.getMessage();
        log.error(errMess);
        throw new DatabaseAccessException(errMess);
      }
    }
    else {
      log.error("WarehouseQuerier workspace is null");
      throw new IOException("WarehouseQuerier workspace is null");
    }
    doShelledOutQuery(sql, tempFile);
    processResults(new WarehouseResults(tempFile));
    workspace.close();
  }

   /** Abort - if this is called, try and top the query and tidy up */
   public void abort()  {
      //don't forget to workspace.close();
      // TODO
   }
   
 /**
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

    if (sql == null) {
      String errMess = "Empty sql query string supplied to WarehouseQuerier";
      log.error(errMess);
      throw new DatabaseAccessException(errMess);
    }
    if (tempFile == null) {
      String errMess = "Null results destination supplied to WarehouseQuerier";
      log.error(errMess);
      throw new DatabaseAccessException(errMess);
    }

    log.debug("Commencing doShelledOutQuery");

    // Configure parameters for external call
    String[] cmdArgs;
    if (tempFile == null) {
      cmdArgs = new String[5];
    }
    else {
      cmdArgs = new String[6];
    }
    cmdArgs[0] = getJavaBinary();
    cmdArgs[1] = "-jar";
    cmdArgs[2] = getExecutableJar();
    cmdArgs[3] = sql;
    cmdArgs[4] = getOgsaDaiRegistryString();
    cmdArgs[5] = "file://" + tempFile.getAbsolutePath();
    log.debug("Command is: " + cmdArgs[0] + " " + cmdArgs[1] +
          " " + cmdArgs[2] + " " + cmdArgs[3] + " " + cmdArgs[4]);
    // TOFIX REMOVE
    System.out.println("Command is: " + cmdArgs[0] + " " + cmdArgs[1] +
          " " + cmdArgs[2] + " " + cmdArgs[3] + " " + cmdArgs[4]);

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
  }

  /**
   * Assembles the URL of the OGSA-DAI registry to be used by the
   * GdsQueryDelegate.
   *
   * @return  String holding full URL of the OGSA-DAI registry
   * @throws DatabaseAccessException
   */
  protected String getOgsaDaiRegistryString() throws DatabaseAccessException {
    String host = SimpleConfig.getProperty("WAREHOUSE_OgsaDaiHostString");
    if (host == null) {
      String errorMessage =
        "Fatal error: Property 'WAREHOUSE_OgsaDaiHostString' not found " +
        " in file 'AstroGridConfig.properties'";
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }
    String registry = SimpleConfig.getProperty("WAREHOUSE_OgsaDaiRegistryString");
    if (registry == null) {
      String errorMessage =
        "Fatal error: Property 'WAREHOUSE_OgsaDaiRegistryString' not found" +
        " in file 'WarehouseQuerier.properties'";
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }
    return host + registry;
  }

  /**
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
    String customJVM = SimpleConfig.getProperty("WAREHOUSE_WarehouseJvm");
    if (customJVM != null) {
      return customJVM;  // Use customised JVM if it exists
    }
    else {
      // No custom JVM - use JVM in JAVA_HOME
      String javaHome = System.getProperty("java.home");
      if (javaHome == null) {  //Shouldn't happen
        String errorMessage =
          "Fatal error: System property 'java.home' not defined! "+
             "Please set WAREHOUSE_WarehouseJvm property in " +
             "'AstroGridConfig.properties' file";
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
               "Please set WAREHOUSE_WarehouseJvm property in " +
               "'AstroGridConfig.properties' file";
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

  /**
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
    String jarPath = SimpleConfig.getProperty("WAREHOUSE_ExecutableJarPath");
    if (jarPath == null) {
      String errorMessage = "Property 'WAREHOUSE_ExecutableJarPath' not set" +
      " in properties file 'AstroGridConfig.properties'";
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }
    String sep = System.getProperty("file.separator");
    if (sep == null) {
      log.warn("Warning, couldn't get system file.separator, assuming " +
          "WAREHOUSE_ExecutableJarPath is properly terminated " +
          "with file separator");
    }
    else {
      int pathlen = jarPath.length();
      // If last char in path not separator, add separator
      if (!(jarPath.substring(pathlen-1,pathlen).equalsIgnoreCase(sep))) {
        jarPath = jarPath + sep;
      }
    }
    // Extract name of executable jar
    String jarName = SimpleConfig.getProperty("WAREHOUSE_ExecutableJarName");
    if (jarName == null) {
      String errorMessage =
          "Property 'WAREHOUSE_ExecutableJarName' not set in " +
          "properties file 'AstroGridConfig.properties'";
      log.error(errorMessage);
      throw new DatabaseAccessException(errorMessage);
    }
    return jarPath + jarName;
  }


  /**
   * Adjust input string (SQL query) to escape XML special characters
   * likely to cause problems.
   * @return  Version of string with escaped XML special characters
   */
  protected String escapeXmlSpecialChars(String inString) {
    String outString = "";
    for (int i = 0; i < inString.length(); i++) {
      char c = inString.charAt(i);
      if (c == '<') {
        outString = outString + "&lt;";
      }
      else if (c == '>') {
        outString = outString + "&gt;";
      }
      else if (c == '&') {
        outString = outString + "&amp;";
      }
      else {
        outString = outString + c;
      }
    }
    return outString;
  }

  // ----------------------------------------------------------
  // Fallback defaults for values that should be configured on a
  // per-installation basis in the WarehouseServiceImpl.properties
  private final String DEFAULT_WAREHOUSE_JVM =
        "/usr/bin/java";

  private final String TEMP_RESULTS_FILENAME = "warehouseResults.xml";
//================================================================

}
/*
$Log: WarehouseQuerier.java,v $
Revision 1.6  2004/03/17 12:20:54  kea
Removing XSLT rowset->VOTable conversions, now done in OGSA-DAI.
Interim checkin, end-to-end not working yet.

Revision 1.5  2004/03/15 17:11:14  mch
'botch' fix for duplicate Workspace id

Revision 1.4  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.3  2004/03/12 04:45:26  mch
It05 MCH Refactor

Revision 1.2  2004/01/24 20:44:25  gtr
Merged from GDW-integration branch.

Revision 1.1.2.3  2004/01/24 20:37:33  gtr
Merged from GDW-integration branch.

Revision 1.1.2.2  2004/01/24 17:38:00  gtr
Datacenter client has changed the Query class back to _query.

Revision 1.1.2.1  2004/01/22 14:56:32  gtr
Transfered from astrogrid/warehouse, with package name changed to be in
org.astrogrid.datacenter.*.

Revision 1.18  2004/01/21 15:59:40  gtr
_query class is changed to Query.

Revision 1.17  2004/01/19 12:47:54  kea
Giving proper WAREHOUSE_ prefix to toplevel warehouse config properties
in AstroGridConfig.properties file.

Revision 1.16  2004/01/16 12:19:09  kea
Fixing broken Javadoc comments.

Revision 1.15  2004/01/15 18:36:57  kea
Now looks for properties in toplevel AstroGridConfig.properties,
using SimpleConfig initialised by datacenter infrastructure.

Revision 1.14  2004/01/08 21:41:34  kea
Horrid kludge to convert datacenter "t.DEC" to "t.decl" for warehouse tables.

Revision 1.13  2004/01/08 20:00:42  kea
Added XML special char escaping to sql strings returned by datacenter
ADQL->SQL conversion (otherwise OGSA-DAI perform docs get broken).

Revision 1.12  2003/12/15 15:45:35  kea
Correcting misleading error message.

Revision 1.11  2003/12/12 14:42:03  gtr
Unused import statements were removed.

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

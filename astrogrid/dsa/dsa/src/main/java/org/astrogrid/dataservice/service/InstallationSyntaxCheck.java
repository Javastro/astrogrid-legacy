/*$Id: InstallationSyntaxCheck.java,v 1.1 2009/05/13 13:20:29 gtr Exp $
 * Created on 28-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.InputStreamReader;
import java.security.Principal;
import org.astrogrid.io.Piper;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.WriterTarget;
import org.astrogrid.tableserver.jdbc.AdqlSqlMaker;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.dataservice.metadata.MetadataException;

import org.astrogrid.cfg.PropertyNotFoundException;
import org.astrogrid.tableserver.test.SampleStarsPlugin;

// For validation
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Document;
import org.astrogrid.test.AstrogridAssert;
import org.astrogrid.contracts.SchemaMap;



/** Unit test for checking an installation - runs a number of sample queries
 * to check that they produce SYNTACTICALLY valid SQL for the installation 
 * DBMS.
 *
 * @author K Andrews
 */
public class InstallationSyntaxCheck {
   
   public static final String QUERYDIR_PREFIX = "adql/testqueries/";
   public static final String[] testSuiteQueries = {
      "selectAllAllow.xml",
      "selectAllLimit.xml",        
      "selectAggregateFuncs.xml",        
      "selectBinaries.xml",
      "selectDistinct.xml",        
      "selectExpr1.xml",           
      "selectExpr2.xml",          
      "selectExprMixed1.xml",     
      "selectExprMultiAlias.xml",  
      "selectAliasExpr.xml",  
      "selectExprUnary.xml",
      "selectGroupBy.xml",
      "selectLogPowMathsFuncs.xml",
      "selectSomeGroup.xml",
      "selectOrderByCol.xml",
      "selectOrderByComplex.xml",
      "selectSome.xml",
      "selectTrigFuncsDeg.xml",
      "selectTrigFuncsRad.xml",
      "selectUnaries.xml",
      "selectValueTweakMathsFuncs.xml",
      "selectComparisonOps.xml",
      "selectBetweenOps.xml",
      "selectBoolOps.xml",
      "selectAllNoAlias.xml",
      "selectSomeNoAlias.xml",
      "selectColourCutter.xml",
      "selectFromInnerJoin.xml",
      "selectFromOuterJoin.xml",
      "selectFromRightOuterJoin.xml"
      // This one will definitely fail if the same table is used 
      // for both tables in the join for testing purposes
      //"selectFromOuterJoinNoAlias.xml"
   };

   private Principal testPrincipal = new LoginAccount("SelfTest", "localhost");
   
   public String runAllTests() 
   {
      /* Initialise plugin if required, just in case the user runs
       * this test first */
      String plugin = "";
      try {
         plugin = ConfigFactory.getCommonConfig().getString(
            "datacenter.querier.plugin");
      }
      catch (PropertyNotFoundException e) {
        // Ignore this one in this context - just want to initialise
        // samplestars if needed
      } 
      if ("org.astrogrid.tableserver.test.SampleStarsPlugin".equals(plugin)) {
         SampleStarsPlugin.initConfig();
      }

      StringBuffer failQueries = new StringBuffer();
      StringBuffer succeedQueries = new StringBuffer();

      int numTests = testSuiteQueries.length;
      int successes = 0;
      int failures = 0;
      for (int i = 0; i < numTests; i++) {
         String queryFile = "(unknown)";  // Default
         try {
            queryFile = getQueryName(i);
         }
         catch (QueryException e) { 
            // NB Shouldn't be possible to get here!  
         }  
         StringBuffer temphtml = new StringBuffer();
         temphtml.append("<h2>Test no. " + Integer.toString(i+1) + 
             " : Query file is " + queryFile +"</h2>");
         try {
            runTest(i, temphtml);
            successes = successes + 1;
            temphtml.append("<font color=\"green\"><strong>QUERY SUCCEEDED</strong></font>");
            succeedQueries.append(temphtml.toString());
         }
         catch (Throwable e) {
            failures = failures + 1;
            temphtml.append("<font color=\"red\"><strong>QUERY FAILED</strong>");
            String message = e.getMessage();
            if (message == null) {
               message = "[NO MESSAGE]";
               System.out.println(message);
            }
            message = message.replaceAll("<","\\&lt;");
            message = message.replaceAll(">","\\&gt;");
            temphtml.append("<p>" + message + "</p></font>");
            failQueries.append(temphtml.toString());
         }
      }
      String report = 
          "<h2>Results summary :<br/>" +
          "<font color=\"green\">SUCCESSFUL QUERIES: " + Integer.toString(successes) + "</font></br>" +
          "<font color=\"red\">FAILED QUERIES: " + Integer.toString(failures) + "</font></h2><p><strong>Please see detailed reports below for more information; any failing queries are listed first.</strong></p>";
      return report + failQueries.toString() + succeedQueries.toString();
   }

   protected String getQueryName(int i)  throws QueryException
   {
     if ((i < 0) || (i >= testSuiteQueries.length)) {
       throw new QueryException("Invalid test query index (" +
          Integer.toString(i) + "), number of test queries is " +
          Integer.toString(testSuiteQueries.length));
     }
     return testSuiteQueries[i];
   }

   protected void runTest(int i, StringBuffer html) throws Throwable
   {
      DataServer server = new DataServer();
      StringWriter sw = new StringWriter(); 
      Query query = getTestSuiteQuery(
          i, new ReturnTable(new WriterTarget(sw), ReturnTable.VOTABLE));

      // Report input ADQL
      html.append("<p>Input ADQL query is:</p>\n<pre>\n");
      String adql = query.getAdqlString();
      adql = adql.replaceAll("<","\\&lt;");
      adql = adql.replaceAll(">","\\&gt;");
      html.append(adql + "\n</pre>");

      // Report translated SQL
      String sql = new AdqlSqlMaker().makeSql(query);
      sql = sql.replaceAll("<","\\&lt;");
      sql = sql.replaceAll(">","\\&gt;");
      html.append("<p>Translated SQL query is:</p>\n<pre>\n");
      html.append(sql + "\n</pre>");

      server.askQuery(testPrincipal, query, this);

      Document doc = DomHelper.newDocument(sw.toString());
      String rootElement = doc.getDocumentElement().getLocalName();
      if(rootElement == null) {
        rootElement = doc.getDocumentElement().getNodeName();
      }
      // This throws an exception if the returned votable is invalid
      AstrogridAssert.assertSchemaValid(doc,rootElement,SchemaMap.ALL);
   }

   protected Query getTestSuiteQuery(int i, ReturnTable returns) throws QueryException
   {
      if ((i < 0) || (i >= testSuiteQueries.length)) {
         throw new QueryException("Invalid test query index (" +
            Integer.toString(i) + "), number of test queries is " +
            Integer.toString(testSuiteQueries.length));
      }
      return new Query(InstallationSyntaxCheck.getTestSuiteAdql(testSuiteQueries[i]), returns);
   }

   /*
   protected String getTestSuiteAdql(String filename) throws QueryException
   {
      InputStream queryIn = null;
      if ((filename == null) || (filename.trim().equals(""))) {
         throw new QueryException(
             "Null/empty name specified for test query to run");
      }
      //find specified sheet as resource of this class
      queryIn = InstallationSyntaxCheck.class.getResourceAsStream(
          "./" + QUERYDIR_PREFIX + filename);
      String whereIsDoc = InstallationSyntaxCheck.class+" resource " +
          "./" + QUERYDIR_PREFIX + filename;
      // Leave these print statements here to get log output in 
      // catalina.out (useful since this is a testing function)
      System.out.println("Trying to load test query as resource of class: " +whereIsDoc);

      //if above doesn't work, try doing by hand for Tomcat ClassLoader
      if (queryIn == null) {
         // Extract the package name, turn it into a filesystem path by 
         // replacing .'s with /'s, and append the path to the xslt.
         // Hopefully this will mean tomcat can locate the file within
         // the bundled jar file.  
         String path = 
           InstallationSyntaxCheck.class.getPackage().getName().replace('.', '/') +
               "/" + QUERYDIR_PREFIX + filename;
         queryIn = InstallationSyntaxCheck.class.getClassLoader().getResourceAsStream(path);
         System.out.println("Trying to load test query via classloader : " +path);
      }

      //Last ditch, look in class path.  However *assume* it's in classpath, 
      //as we don't know what the classpath is during unit tests.
      if (queryIn == null) {
         //log.warn("Could not find test query '"
          //   +whereIsDoc+"', looking in classpath...");

         queryIn = InstallationSyntaxCheck.class.getClassLoader().getResourceAsStream(
               QUERYDIR_PREFIX + filename);
         whereIsDoc = QUERYDIR_PREFIX + filename+" in classpath at "+
           InstallationSyntaxCheck.class.getClassLoader().getResource(filename);
         System.out.println("Trying to load test query  via classpath : " +whereIsDoc);
      }
      if (queryIn == null) {
          throw new QueryException(
              "Could not find test query "+filename);
      }
      // Now we have the query, let's get it as a string.
      String adqlString = null;
      try {
         StringWriter sw = new StringWriter();
         Piper.bufferedPipe(new InputStreamReader(queryIn), sw);
         adqlString = sw.toString();
      }
      catch (IOException e) {
        throw new QueryException("Couldn't load test query " + filename +
            ": " + e.getMessage(), e);
      }

      // Extract table, ra and dec names to use in test from config
      String defaultTableName = ConfigFactory.getCommonConfig().getString(
          "datacenter.self-test.table", null);
      String colRaName = ConfigFactory.getCommonConfig().getString(
          "datacenter.self-test.column1", null);
      String colDecName = ConfigFactory.getCommonConfig().getString(
          "datacenter.self-test.column2", null);
      if (defaultTableName == null) {
         throw new QueryException(
            "DataCentre is misconfigured - datacenter.self-test.table property is null");
      }
      if (colRaName == null) {
         throw new QueryException("DataCentre is misconfigured - datacenter.self-test.column1 property is null");
      }
      if (colDecName == null) {
         throw new QueryException("DataCentre is misconfigured - datacenter.self-test.column2 property is null");
      }
      // Perform necessary substitutions
      adqlString = adqlString.replaceAll("INSERT_TABLE",defaultTableName);
      adqlString = adqlString.replaceAll("INSERT_RA",colRaName);
      adqlString = adqlString.replaceAll("INSERT_DEC",colDecName);

      return adqlString;
   }
   */

   /* A public method to extract a templated query of the specified name;
    * this should probably be in a separate class, but hohum.
    */
   public static String getTestSuiteAdql(String filename) throws QueryException
   {
      /* Initialise plugin if required: the JSP pages make use of this
       * function */
      String plugin = "";
      try {
         plugin = ConfigFactory.getCommonConfig().getString(
            "datacenter.querier.plugin");
      }
      catch (PropertyNotFoundException e) {
        // Ignore this one in this context - just want to initialise
        // samplestars if needed
      } 
      if ("org.astrogrid.tableserver.test.SampleStarsPlugin".equals(plugin)) {
         SampleStarsPlugin.initConfig();
      }

      InputStream queryIn = null;
      if ((filename == null) || (filename.trim().equals(""))) {
         throw new QueryException(
             "Null/empty name specified for test query to run");
      }
      //find specified sheet as resource of this class
      queryIn = InstallationSyntaxCheck.class.getResourceAsStream(
          "./" + QUERYDIR_PREFIX + filename);
      String whereIsDoc = InstallationSyntaxCheck.class+" resource " +
          "./" + QUERYDIR_PREFIX + filename;
      // Leave these print statements here to get log output in 
      // catalina.out (useful since this is a testing function)
      System.out.println("Trying to load test query as resource of class: " +whereIsDoc);

      //if above doesn't work, try doing by hand for Tomcat ClassLoader
      if (queryIn == null) {
         // Extract the package name, turn it into a filesystem path by 
         // replacing .'s with /'s, and append the path to the xslt.
         // Hopefully this will mean tomcat can locate the file within
         // the bundled jar file.  
         String path = 
           InstallationSyntaxCheck.class.getPackage().getName().replace('.', '/') +
               "/" + QUERYDIR_PREFIX + filename;
         queryIn = InstallationSyntaxCheck.class.getClassLoader().getResourceAsStream(path);
         System.out.println("Trying to load test query via classloader : " +path);
      }
      //Last ditch, look in class path.  However *assume* it's in classpath, 
      //as we don't know what the classpath is during unit tests.
      if (queryIn == null) {
         //log.warn("Could not find test query '"
          //   +whereIsDoc+"', looking in classpath...");

         queryIn = InstallationSyntaxCheck.class.getClassLoader().getResourceAsStream(
               QUERYDIR_PREFIX + filename);
         whereIsDoc = QUERYDIR_PREFIX + filename+" in classpath at "+
           InstallationSyntaxCheck.class.getClassLoader().getResource(filename);
         System.out.println("Trying to load test query  via classpath : " +whereIsDoc);
      }
      if (queryIn == null) {
          throw new QueryException(
              "Could not find test query "+filename);
      }
      // Now we have the query, let's get it as a string.
      String adqlString = null;
      try {
         StringWriter sw = new StringWriter();
         Piper.bufferedPipe(new InputStreamReader(queryIn), sw);
         adqlString = sw.toString();
      }
      catch (IOException e) {
        throw new QueryException("Couldn't load test query " + filename +
            ": " + e.getMessage(), e);
      }


      // Extract catalog, table, ra and dec names to use in test from config
      String defaultCatalogID = ConfigFactory.getCommonConfig().getString(
          "datacenter.self-test.catalog", null);
      String defaultTableID = ConfigFactory.getCommonConfig().getString(
          "datacenter.self-test.table", null);
      String colRaID = ConfigFactory.getCommonConfig().getString(
          "datacenter.self-test.column1", null);
      String colDecID = ConfigFactory.getCommonConfig().getString(
          "datacenter.self-test.column2", null);

      // Perform some checks
      try {
         TableMetaDocInterpreter.isValid();
      }
      catch (Exception e) {
         throw new QueryException(
               "DSA metadoc cannot be validated: " + e.getMessage());
      }
      if (defaultCatalogID == null) {
         try {
            TableMetaDocInterpreter.isValid();
         }
         catch (Exception e) {
            throw new QueryException(
                  "DSA metadoc cannot be validated: " + e.getMessage());
         }
         String[] catIDs = new String[0];
         try {
            catIDs = TableMetaDocInterpreter.getCatalogIDs();
         }
         catch (MetadataException e) {
            throw new QueryException(
                  "Problem with DSA metadoc: " + e.getMessage());
         }
         if (catIDs.length > 1) {
            throw new QueryException(
               "DataCentre is misconfigured - 'datacenter.self-test.catalog' property is not set, and there are multiple catalogs to choose from in the metadoc");
         }
         else {
            defaultCatalogID = catIDs[0];
         }
      }
      if (defaultTableID == null) {
         throw new QueryException(
            "DataCentre is misconfigured - datacenter.self-test.table property is not set");
      }
      if (colRaID == null) {
         throw new QueryException("DataCentre is misconfigured - datacenter.self-test.column1 property is not set");
      }
      if (colDecID == null) {
         throw new QueryException("DataCentre is misconfigured - datacenter.self-test.column2 property is not set");
      }

      String defaultCatalogName, defaultTableName, colRaName, colDecName;
      try {
         defaultCatalogName = TableMetaDocInterpreter.getCatalogNameForID(
               defaultCatalogID); 
         defaultTableName = TableMetaDocInterpreter.getTableNameForID(
               defaultCatalogID,defaultTableID); 
         colRaName = TableMetaDocInterpreter.getColumnNameForID(
               defaultCatalogID, defaultTableID, colRaID); 
         colDecName = TableMetaDocInterpreter.getColumnNameForID(
               defaultCatalogID, defaultTableID, colDecID); 
      }
      catch (MetadataException me) {
         throw new QueryException(
               me.getMessage()+", while searching in DSA metadoc", me);
      }
      // Perform necessary substitutions
      adqlString = adqlString.replaceAll("INSERT_CATALOG",
         "Archive=\""+defaultCatalogName+"\"");
      adqlString = adqlString.replaceAll("INSERT_TABLE",defaultTableName);
      adqlString = adqlString.replaceAll("INSERT_RA",colRaName);
      adqlString = adqlString.replaceAll("INSERT_DEC",colDecName);
      return adqlString;
   }
}

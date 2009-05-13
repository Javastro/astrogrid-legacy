/*
 * $Id: SimpleQueryMaker.java,v 1.1 2009/05/13 13:20:38 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

//import org.astrogrid.query.condition.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import org.astrogrid.io.Piper;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;


/**
 * Used to make simple searches, such as cone, keyword lists, etc.
 *
 * @author M Hill
 * @author K Andrews
 */

public class SimpleQueryMaker  {

  /** A generic ADQL query that should run against any database.
   */
  private static final String SIMPLE_QUERY = 
    "<Select xsi:type=\"selectType\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\">\n  <Restrict xsi:type=\"selectionLimitType\" Top=\"100\"/>\n  <SelectionList xsi:type=\"selectionListType\">\n     <Item xsi:type=\"allSelectionItemType\"/> </SelectionList>\n  <From xsi:type=\"fromType\">\n    <Table xsi:type=\"tableType\" Alias=\"a\" Name=\"INSERT_TABLE\"/>\n  </From>\n</Select>";
  private static final String SIMPLE_CATALOG_QUERY = 
    "<Select xsi:type=\"selectType\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\">\n  <Restrict xsi:type=\"selectionLimitType\" Top=\"100\"/>\n  <SelectionList xsi:type=\"selectionListType\">\n     <Item xsi:type=\"allSelectionItemType\"/> </SelectionList>\n  <From xsi:type=\"fromType\">\n    <Table xsi:type=\"tableType\" Alias=\"a\" Name=\"INSERT_TABLE\" Archive=\"INSERT_ARCHIVE\"/>\n  </From>\n</Select>";

  private static final String SIMPLE_TINY_CATALOG_QUERY = 
    "<Select xsi:type=\"selectType\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\">\n  <Restrict xsi:type=\"selectionLimitType\" Top=\"1\"/>\n  <SelectionList xsi:type=\"selectionListType\">\n     <Item xsi:type=\"allSelectionItemType\"/> </SelectionList>\n  <From xsi:type=\"fromType\">\n    <Table xsi:type=\"tableType\" Alias=\"a\" Name=\"INSERT_TABLE\" Archive=\"INSERT_ARCHIVE\"/>\n  </From>\n</Select>";


   /** Convenience routine to construct a test query returning 100 entries,
    * using the specified table.
    */
   public static Query makeTestQuery(String tableName, ReturnSpec returns) 
      throws QueryException {
      if ((tableName == null) || (tableName.equals(""))) {
         throw new QueryException(
           "Cannot make a test query using a null or empty table name");
      }
      String queryString = SIMPLE_QUERY.replaceAll("INSERT_TABLE",tableName);
      return new Query(queryString, returns);
   }

   /** Convenience routine to construct a test query returning 100 entries,
    * using the specified table and catalog.
    */
   public static Query makeTestQuery(String catalogName, String tableName, 
         ReturnSpec returns) throws QueryException {
      if ((catalogName == null) || (catalogName.equals(""))) {
         throw new QueryException(
           "Cannot make a test query using a null or empty catalog name");
      }
      if ((tableName == null) || (tableName.equals(""))) {
         throw new QueryException(
           "Cannot make a test query using a null or empty table name");
      }
      String queryString = SIMPLE_CATALOG_QUERY.replaceAll(
             "INSERT_ARCHIVE",catalogName);
      queryString = queryString.replaceAll("INSERT_TABLE",tableName);
      return new Query(queryString, returns);
   }

   /** Convenience routine to construct a test query returning 1 entry,
    * using the specified table and catalog.
    */
   public static Query makeTinyTestQuery(String catalogName, String tableName, 
         ReturnSpec returns) throws QueryException {
      if ((catalogName == null) || (catalogName.equals(""))) {
         throw new QueryException(
           "Cannot make a test query using a null or empty catalog name");
      }
      if ((tableName == null) || (tableName.equals(""))) {
         throw new QueryException(
           "Cannot make a test query using a null or empty table name");
      }
      String queryString = SIMPLE_TINY_CATALOG_QUERY.replaceAll(
             "INSERT_ARCHIVE",catalogName);
      queryString = queryString.replaceAll("INSERT_TABLE",tableName);
      return new Query(queryString, returns);
   }


   /** Convenience routine to construct a test query returning 100 entries,
    * and return it as an ADQL/xml string.
    */
   public static String makeTestQueryString(String tableName) throws QueryException {
      if ((tableName == null) || (tableName.equals(""))) {
         throw new QueryException(
           "Cannot make a test query using a null or empty table name");
      }
      return SIMPLE_QUERY.replaceAll("INSERT_TABLE",tableName);
   }

   /** Convenience routine to construct a test query returning 100 entries,
    * using the specified table and catalog, and return it as an ADQL/xml 
    * string.
    */
   public static String makeTestQueryString(String catalogName, 
         String tableName) throws QueryException 
   {
      if ((catalogName == null) || (catalogName.equals(""))) {
         throw new QueryException(
           "Cannot make a test query using a null or empty catalog name");
      }
      if ((tableName == null) || (tableName.equals(""))) {
         throw new QueryException(
           "Cannot make a test query using a null or empty table name");
      }
      String queryString = SIMPLE_CATALOG_QUERY.replaceAll(
            "INSERT_ARCHIVE",catalogName);
      queryString = queryString.replaceAll("INSERT_TABLE",tableName);
      return queryString;
   }
}

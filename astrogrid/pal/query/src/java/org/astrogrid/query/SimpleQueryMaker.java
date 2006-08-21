/*
 * $Id: SimpleQueryMaker.java,v 1.4 2006/08/21 15:39:30 clq2 Exp $
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

   /** Convenience routine to construct a test query returning 100 entries.
    * The table to be searched is specified using the 
    * "datacenter.self-test.table" property.
    */
   public static Query makeTestQuery(ReturnSpec returns) throws QueryException {
      String tableName = ConfigFactory.getCommonConfig().getString(
            "datacenter.self-test.table", null);
      if ((tableName == null) || (tableName.equals(""))) {
         throw new QueryException(
           "DSA local property 'datacenter.self-test.table' is not set, " +
           "please check your configuration");
      }
      String queryString = SIMPLE_QUERY.replaceAll("INSERT_TABLE",tableName);
      return new Query(queryString, returns);
   }
   /** Convenience routine to construct a test query returning 100 entries,
    * and return it as an ADQL/xml string.
    * The table to be searched is specified using the 
    * "datacenter.self-test.table" property.
    */
   public static String makeTestQueryString() throws QueryException {
      String tableName = ConfigFactory.getCommonConfig().getString(
            "datacenter.self-test.table", null);
      if ((tableName == null) || (tableName.equals(""))) {
         throw new QueryException(
           "DSA local property 'datacenter.self-test.table' is not set, " +
           "please check your configuration");
      }
      return SIMPLE_QUERY.replaceAll("INSERT_TABLE",tableName);
   }

   /** this routine creates an incomplete query (no return specification), but it's left in for backwards
    * compatibility with the older web/etc interfaces that take a query and separate return spec parameters.
    . Used quite a bit in tests - for the real world, you should really use the constructors with ReturnSpecs included
    * ReturnSpec defaults to string writer
    */
   public static Query makeConeQuery(double givenRa, double givenDec, 
       double givenRadius) throws QueryException
   {
      String coneTable = ConfigFactory.getCommonConfig().getString(
            "conesearch.table", null);
      if ((coneTable == null) || (coneTable.equals(""))) {
         throw new QueryException(
           "DSA local property 'conesearch.table' is not set, " +
           "please check your configuration");
      }
      String coneRA = ConfigFactory.getCommonConfig().getString(
            "conesearch.ra.column", null);
      if ((coneRA == null) || (coneRA.equals(""))) {
         throw new QueryException(
             "DSA local property 'conesearch.ra.column' is not set, " +
             "please check your configuration");
      }

      String coneDec = ConfigFactory.getCommonConfig().getString(
            "conesearch.dec.column", null);
      if ((coneDec == null) || (coneDec.equals(""))) {
         throw new QueryException(
             "DSA local property 'conesearch.dec.column' is not set, " +
             "please check your configuration");
      }
      return new Query(coneTable, coneRA, coneDec, 
          givenRa, givenDec, givenRadius, 
          new ReturnTable(new WriterTarget(new StringWriter()))
      );
   }

   /** Convenience routine to constructs a conesearch query of the given 
    * parameters, returning the given returnspec.
    * The table and columns to be searched are loaded from the configuration 
    * file, if given
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius, ReturnSpec returns) throws QueryException {
      Query query = makeConeQuery(givenRa, givenDec, givenRadius);
      query.setResultsDef(returns);
      return query;
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius, TargetIdentifier target, String format) throws QueryException {
      return makeConeQuery(givenRa, givenDec, givenRadius, new ReturnTable(target, format));
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius, TargetIdentifier target) throws QueryException {
      return makeConeQuery(givenRa, givenDec, givenRadius, new ReturnTable(target));
   }

   /** Constructs condition from a list of keywords and values which will be ANDed
    */
   /*
   public static Condition makeKeywordCondition(Hashtable keywords) {
      Enumeration keys = keywords.keys();
      Intersection intersection = null;
      while (keys.hasMoreElements()) {
         String key = (String) keys.nextElement();
         String value = (String) keywords.get(key);

         Condition comparison = new StringComparison(new RawSearchField(key), StringCompareOperator.LIKE, new LiteralString(value));
         
         //add condition to previous
         if (intersection == null) {
            intersection = new Intersection(comparison);
         }
         else {
            intersection.addCondition(comparison);
         }
      }
      return intersection;
   }
   */
}

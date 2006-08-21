/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import java.io.InputStream;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Document;

/* For DOM comparisons */
import org.custommonkey.xmlunit.*;

/**
 * Tests utility functions of the Query class.
 * The Query class's ADQL translation testing is done in the 
 * AdqlTest.java class.
 */
public class QueryTest extends XMLTestCase   {

   AdqlTestHelper helper = new AdqlTestHelper();
   
   protected void setUp()
   {
      // Set property "default.table", needed for handling ADQL 
      // with missing FROM clause
      ConfigFactory.getCommonConfig().setProperty(
          "default.table","catalogue");
   }

   /* 
    * These tests test particular Query class functionality. 
    */ 
   public void testGetLimit() throws Exception
   {
      String name = "selectAllLimit";
      String version =  "v1_0";
      printHelpfulStuff(name, "getLimit");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      long limit = query.getLimit();
      assertTrue(limit == 100);
   }
   /*
    *NB setLimit() is now private
   public void testSetLimit1() throws Exception
   {
     // This one tests with a limit clause already present
      String name = "selectAllLimit";
      String version =  "v1_0";
      printHelpfulStuff(name, "setLimit1");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      query.setLimit(251);
      long limit = query.getLimit();
      assertTrue(limit == 251);
   }
    *NB setLimit() is now private
   public void testSetLimit2() throws Exception
   {
     // This one tests with a limit clause NOT already present
      String name = "selectAll";
      String version =  "v1_0";
      printHelpfulStuff(name, "setLimit2");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      query.setLimit(468);
      long limit = query.getLimit();
      assertTrue(limit == 468);
   }
   */

   public void testSetSelectDocument1() throws Exception {
      try {
        Query query = new Query((String)null);
      }
      catch (QueryException e) {
        return;   //  Error as expected
      }
      fail("Null query string should have caused an exception");   
   }
   public void testSetSelectDocument2() throws Exception {
      try {
        Query query = new Query((InputStream)null);
      }
      catch (QueryException e) {
        return;   //  Error as expected
      }
      fail("Null query input stream should have caused an exception");   
   }
   public void testSetSelectDocument3() throws Exception {
      try {
        Query query = new Query("");
      }
      catch (QueryException e) {
        return;   //  Error as expected
      }
      fail("Empty query string should have caused an exception");   
   }

   public void testGetTables1() throws Exception {
      String name = "selectOneTableTwoCols";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables1");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      String[] tables = query.getTableReferences();
      assert(tables.length == 1);
      assert(tables[0].equals("catalogue"));
   }
   public void testGetTables2() throws Exception {
      String name = "selectTwoTablesFourCols";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables2");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      String[] tables = query.getTableReferences();
      assert(tables.length == 2);
      assert(
          (tables[0].equals("catalogueA")) ||
          (tables[0].equals("catalogueB"))
      );  
      assert(
          (tables[1].equals("catalogueA")) ||
          (tables[1].equals("catalogueB"))
      );  
   }
   public void testGetTables3() throws Exception {
     // This one uses only one table, with 4 distinct aliases
      String name = "selectExprMultiAlias";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables3");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      String[] tables = query.getTableReferences();
      assert(tables.length == 1);
      assert(tables[0].equals("catalogue"));
   }
   public void testGetTables4() throws Exception {
     // This one uses only two tables, with 2 distinct aliases each
      String name = "selectMultiTabMultiAlias1";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables4");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      String[] tables = query.getTableReferences();
      assert(tables.length == 2);
      assert(
          (tables[0].equals("catalogueA")) ||
          (tables[0].equals("catalogueB"))
      );  
      assert(
          (tables[1].equals("catalogueA")) ||
          (tables[1].equals("catalogueB"))
      );  
   }



   public void testGetCols1() throws Exception {
      String name = "selectOneTableTwoCols";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables1");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      String[] columns = query.getColumnReferences();
      assert(columns.length == 2);
      assert(
          (columns[0].equals("POS_EQ_RA")) ||
          (columns[0].equals("POS_EQ_DEC"))
      );  
      assert(
          (columns[1].equals("POS_EQ_RA")) ||
          (columns[1].equals("POS_EQ_DEC"))
      );  
   }
   public void testGetCols2() throws Exception {
      String name = "selectAll";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables1");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      String[] columns = query.getColumnReferences();
      assert(columns.length == 0);
   }
   public void testGetCols3() throws Exception {
      String name = "selectTwoTablesFourCols";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables2");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      String[] columnsA = query.getColumnReferences("catalogueA");
      String[] columnsB = query.getColumnReferences("catalogueB");
      String[] columnsC = query.getColumnReferences("nosuchtable");
      assert(columnsA.length == 2);
      assert(columnsB.length == 2);
      assert(columnsC.length == 0);
      assert(
          (columnsA[0].equals("POS_EQ_RA")) ||
          (columnsA[0].equals("POS_EQ_DEC"))
      );  
      assert(
          (columnsA[1].equals("POS_EQ_RA")) ||
          (columnsA[1].equals("POS_EQ_DEC"))
      );  
      assert(
          (columnsB[0].equals("ID_FIELD")) ||
          (columnsB[0].equals("CODE_QUALITY"))
      );  
      assert(
          (columnsB[1].equals("ID_FIELD")) ||
          (columnsB[1].equals("CODE_QUALITY"))
      );  
   }
   public void testGetTableAlias1() throws Exception {
      String name = "selectTwoTablesFourCols";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables1");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      assert(query.getTableAlias("catalogueA").equals("a"));
      assert(query.getTableAlias("catalogueB").equals("b"));
      assertNull(query.getTableAlias("nosuchtable"));
   }
   public void testGetTableAlias2() throws Exception {
      String name = "selectTwoTablesFourColsNoAlias";
      String version =  "v1_0";
      printHelpfulStuff(name, "testGetTables1");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      // If no alias is given, alias is set to same as table name
      assert(query.getTableAlias("catalogueA").equals("catalogueA"));
      assert(query.getTableAlias("catalogueB").equals("catalogueB"));
      assertNull(query.getTableAlias("nosuchtable"));
   }

   public void testNoAlias() throws Exception {
      String name = "selectFromNoAlias";
      String version =  "v1_0";
      printHelpfulStuff(name, "testNoAlias");
      String adql = helper.getSuiteAdqlString(name, version); 
      Query query = new Query(adql);
      query.getTableName("catalogue");
      query.getTableAlias("catalogue");
   }
   public void testEmptyAlias() throws Exception {
      String name = "BAD_selectEmptyAlias";
      String version =  "v1_0";
      printHelpfulStuff(name, "testEmptyAlias");
      String adql = helper.getSuiteAdqlString(name, version); 
      try {
        Query query = new Query(adql);
      }
      catch (QueryException e) {
        return;   //  Error as expected
      }
      fail("Query with empty alias should have caused an exception");   
   }
   //----------------------------------------------------------------------
   /* Utility functions */

   protected void printHelpfulStuff(String filename) {
      System.out.println("------------------------------------------------");
      System.out.println("Testing query " + filename);
      System.out.println("------------------------------------------------");
   }
   protected void printHelpfulStuff(String filename, String extraStuff) {
      System.out.println("------------------------------------------------");
      System.out.println("Testing query " + filename + " for " + extraStuff);
      System.out.println("------------------------------------------------");
   }


   /* Junit stuff */

   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(QueryTest.class);
   }
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

/*
 * $Id: AdqlTest.java,v 1.1 2009/05/13 13:21:01 gtr Exp $
 *
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
 * Round-trip tests - can we accept ADQL queries, parse them 
 * & produce matching ADQL?
 */

public class AdqlTest extends XMLTestCase   {

   protected void setUp()
   {
      // Set property "default.table", needed for handling ADQL 
      // with missing FROM clause
      ConfigFactory.getCommonConfig().setProperty(
          "default.table","catalogue");
   }

   /* Actual tests */
   /* TOFIX PUT THESE BACK BEFORE MERGING!!! */
  /*
   public void testNvo1() throws Exception {
      roundParse(new AdqlTestHelper().getNvo1());
   }
   public void testNvo2() throws Exception {
      roundParse(new AdqlTestHelper().getNvo2());
   }
   public void testNvo3() throws Exception {
      roundParse(new AdqlTestHelper().getNvo3());
   }
   public void testNvo4() throws Exception {
      roundParse(new AdqlTestHelper().getNvo4());
   }
   */
   
   /* 
    * These tests run through a comprehensive ADQL query suite.
    * Sadly I'm not enough of a junit guru to know how to auto-generate 
    * the test functions from a directory of xml files, so I'm doing
    * it the dumb manual way, one explicit test per file. 
    */
   public void testSelectAll_1_0() throws Exception {
     suiteTest("selectAll", "v1_0", true);
   }
   public void testSelectAll_0_7_4() throws Exception {
     suiteTest("selectAll", "v074", true);
   }
   public void testSelectAllAllow_1_0() throws Exception {
     suiteTest("selectAllAllow", "v1_0", false);
   }
   public void testSelectAllAllow_0_7_4() throws Exception {
     suiteTest("selectAllAllow", "v074", false);
   }
   public void testSelectAllLimit_1_0() throws Exception {
     suiteTest("selectAllLimit", "v1_0", false);
   }
   public void testSelectAllLimit_0_7_4() throws Exception {
     suiteTest("selectAllLimit", "v074", false);
   }
   public void testSelectDictinct_1_0() throws Exception {
     suiteTest("selectDistinct", "v1_0", true);
   }
   public void testSelectDictinct_0_7_4() throws Exception {
     suiteTest("selectDistinct", "v074", true);
   }
   public void testSelectGroupBy_1_0() throws Exception {
     suiteTest("selectGroupBy", "v1_0", true);
   }
   public void testSelectGroupBy_0_7_4() throws Exception {
     suiteTest("selectGroupBy", "v074", true);
   }
   public void testSelectOrderByCol_1_0() throws Exception {
     suiteTest("selectOrderByCol", "v1_0", true);
   }
   public void testSelectOrderByCol_0_7_4() throws Exception {
     suiteTest("selectOrderByCol", "v074", true);
   }
   public void testSelectOrderByComplex_1_0() throws Exception {
     suiteTest("selectOrderByComplex", "v1_0", true);
   }
   public void testSelectOrderByComplex_0_7_4() throws Exception {
     suiteTest("selectOrderByComplex", "v074", true);
   }
   public void testSelectSome_1_0() throws Exception {
     suiteTest("selectSome", "v1_0", true);
   }
   public void testSelectSome_0_7_4() throws Exception {
     suiteTest("selectSome", "v074", true);
   }
   public void testSelectExpr1_1_0() throws Exception {
     suiteTest("selectExpr1", "v1_0", true);
   }
   public void testSelectExpr1_0_7_4() throws Exception {
     suiteTest("selectExpr1", "v074", true);
   }
   public void testSelectExpr2_1_0() throws Exception {
     suiteTest("selectExpr2", "v1_0", true);
   }
   public void testSelectExpr2_0_7_4() throws Exception {
     suiteTest("selectExpr2", "v074", true);
   }
   public void testSelectExprUnary_1_0() throws Exception {
     suiteTest("selectExprUnary", "v1_0", true);
   }
   public void testSelectExprUnary_0_7_4() throws Exception {
     suiteTest("selectExprUnary", "v074", true);
   }
   public void testSelectExprSum_1_0() throws Exception {
     suiteTest("selectExprSum", "v1_0", true);
   }
   public void testSelectExprSum_0_7_4() throws Exception {
     suiteTest("selectExprSum", "v074", true);
   }
   public void testSelectExprMixed1_1_0() throws Exception {
     suiteTest("selectExprMixed1", "v1_0", true);
   }
   public void testSelectExprMixed1_0_7_4() throws Exception {
     suiteTest("selectExprMixed1", "v074", true);
   }
   public void testSelectExprMultiAlias_1_0() throws Exception {
     suiteTest("selectExprMultiAlias", "v1_0", true);
   }
   public void testSelectExprMultiAlias_0_7_4() throws Exception {
     suiteTest("selectExprMultiAlias", "v074", true);
   }
   public void testSelectAliasExpr() throws Exception {
     suiteTest("selectAliasExpr", "v1_0", true);
   }
   public void testSelectOneTableTwoCols() throws Exception {
     suiteTest("selectOneTableTwoCols", "v1_0", true);
   }
   public void testSelectTwoTablesFourCols() throws Exception {
     suiteTest("selectTwoTablesFourCols", "v1_0", true);
   }
   public void testSelectValueTweakMathsFuncs() throws Exception {
     suiteTest("selectValueTweakMathsFuncs", "v1_0", true);
   }
   public void testSelectLogPowMathsFuncs() throws Exception {
     suiteTest("selectLogPowMathsFuncs", "v1_0", true);
   }
   public void testSelectAggregateFuncs() throws Exception {
     suiteTest("selectAggregateFuncs", "v1_0", true);
   }
   public void testSelectUnaries() throws Exception {
     suiteTest("selectUnaries", "v1_0", true);
   }
   public void testSelectBinaries() throws Exception {
     suiteTest("selectBinaries", "v1_0", true);
   }
   public void testSelectTrigFuncsDeg() throws Exception {
     suiteTest("selectTrigFuncsDeg", "v1_0", true);
   }
   public void testSelectTrigFuncsRad() throws Exception {
     suiteTest("selectTrigFuncsRad", "v1_0", true);
   }
   public void testSelectComparisonOps() throws Exception {
     suiteTest("selectComparisonOps", "v1_0", true);
   }
   public void testSelectBetweenOps() throws Exception {
     suiteTest("selectBetweenOps", "v1_0", true);
   }
   public void testSelectBoolOps() throws Exception {
     suiteTest("selectBoolOps", "v1_0", true);
   }
   public void testSelectFromNoAlias() throws Exception {
     suiteTest("selectFromNoAlias", "v1_0", false);
   }
   public void testSelectAllArchive() throws Exception {
      suiteTest("selectAllArchive", "v1_0", true);
   }
   public void testSelectTwoTablesFourColsArchive() throws Exception {
      suiteTest("selectTwoTablesFourColsArchive", "v1_0", true);
   }

   //----------------------------------------------------------------------
   /* Utility functions */

   public void roundParse(String adqlString, String comparisonString) throws Exception {
      System.out.print(adqlString);
      //Element fileAdqlElement = fileAdqlDom.getDocumentElement();
      Query query = new Query(adqlString);
      Document writtenAdqlDom = DomHelper.newDocument(comparisonString);
      Document queryAdqlDom = DomHelper.newDocument(query.getAdqlString());

      // Normalize just to be sure 
      queryAdqlDom.normalize();
      writtenAdqlDom.normalize();

      // Using xmlunit to compare documents
      setIgnoreWhitespace(true);
      assertXMLEqual("Documents are not equal!!",queryAdqlDom, writtenAdqlDom);
   }

   private void suiteTest(String name, String version, boolean expectIdentical)   throws Exception
   {
      AdqlTestHelper helper = new AdqlTestHelper();
      printHelpfulStuff(name);

      String adql = helper.getSuiteAdqlString(name, version); 
      String compareAdql;
      if (expectIdentical) {
         compareAdql = helper.getSuiteAdqlString(name, "v1_0");
      }
      else {
         compareAdql = helper.getSuiteAdqlStringExpected(name, "v1_0");
      }
      roundParse(adql, compareAdql);

   }

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
      return new TestSuite(AdqlTest.class);
   }
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
}

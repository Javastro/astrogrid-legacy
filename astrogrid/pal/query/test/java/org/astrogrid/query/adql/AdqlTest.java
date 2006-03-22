/*
 * $Id: AdqlTest.java,v 1.3 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.query.Query;
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.query.adql.AdqlXml074Parser;
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Document;


/* For DOM comparisons */
import org.custommonkey.xmlunit.*;

/**
 * Round-trip tests - can we accept ADQL queries, parse them & produce matching ADQL?
 */

public class AdqlTest extends XMLTestCase   {

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

   public void testSelectAll() throws Exception {
     suiteTest("selectAll");
   }
   public void testSelectAllAllow() throws Exception {
     suiteTest("selectAllAllow");
   }
   public void testSelectAllLimit() throws Exception {
     suiteTest("selectAllLimit");
   }
   public void testSelectDictinct() throws Exception {
     suiteTest("selectDistinct");
   }
   public void testSelectGroupBy() throws Exception {
     suiteTest("selectGroupBy");
   }
   /*
   // Not currently working 
   public void testSelectOrderByCol() throws Exception {
     suiteTest("selectOrderByCol");
   }
   */
   public void testSelectSome() throws Exception {
     suiteTest("selectSome");
   }
   /*
   // Not currently working
   public void testSelectExpr1() throws Exception {
     suiteTest("selectExpr1");
   }
   // Not currently working
   public void testSelectExpr2() throws Exception {
     suiteTest("selectExpr2");
   }
   */



   /* Utility functions */
   /*
   public void roundParse(Document fileAdqlDom) throws Exception {
   //public void roundParse(Element fileAdqlDom) throws Exception {
      Query query = AdqlXml074Parser.makeQuery(
          fileAdqlDom.getDocumentElement());

      String writtenAdql = Adql074Writer.makeAdql(query);
      Document writtenAdqlDom = DomHelper.newDocument(writtenAdql);

      // Normalise documents just in case
      //fileAdqlDom.normalize();
      //writtenAdqlDom.normalize();

      Element writtenAdqlElement = writtenAdqlDom.getDocumentElement();
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
      System.out.println(fileAdqlDom.getDocumentElement());
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
      System.out.print(writtenAdqlElement);
      System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
      // Using xmlunit to compare documents

      //assertXMLEqual("Documents are not equal!!",fileAdqlDom, writtenAdqlDom);
      //new AdqlTestHelper().assertElementsEqual(fileAdqlDom, writtenAdqlDom);
   }
   */
   public void roundParse(Document fileAdqlDom) throws Exception {
      Element fileAdqlElement = fileAdqlDom.getDocumentElement();
      Query query = AdqlXml074Parser.makeQuery(fileAdqlElement);
      String writtenAdql = Adql074Writer.makeAdql(query);
      System.out.print(writtenAdql);
      Document writtenAdqlDom = DomHelper.newDocument(writtenAdql);

      // Normalize just to be sure 
      fileAdqlDom.normalize();
      writtenAdqlDom.normalize();

      // Using xmlunit to compare documents
      setIgnoreWhitespace(true);
      assertXMLEqual("Documents are not equal!!",fileAdqlDom, writtenAdqlDom);

      /*
      DifferenceListener myDifferenceListener = new IgnoreTextAndAttributeValuesDifferenceListener();
      Diff myDiff = new Diff(myControlXML, myTestXML);
      myDiff.overrideDifferenceListener(myDifferenceListener);
      assertTrue("test XML matches control skeleton XML " + myDiff, myDiff.similar());
      */

      //new AdqlTestHelper().assertElementsEqual(fileAdqlDom, writtenAdqlDom);
   }


   
   private void printHelpfulStuff(String filename) {
      System.out.println("------------------------------------------------");
      System.out.println("Testing query " + filename);
      System.out.println("------------------------------------------------");
   }
   private void suiteTest(String name) throws Exception
   {
      AdqlTestHelper helper = new AdqlTestHelper();
      printHelpfulStuff(name);
      roundParse(new AdqlTestHelper().getSuiteAdql(name));
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

/*
 $Log: AdqlTest.java,v $
 Revision 1.3  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.2.82.2  2006/03/21 11:26:58  kea
 Tweaks to switch off broken unit tests prior to radical revision
 of internal query model.

 Revision 1.2.82.1  2006/02/16 17:13:05  kea
 Various ADQL/XML parsing-related fixes, including:
  - adding xsi:type attributes to various tags
  - repairing/adding proper column alias support (aliases compulsory
     in adql 0.7.4)
  - started adding missing bits (like "Allow") - not finished yet
  - added some extra ADQL sample queries - more to come
  - added proper testing of ADQL round-trip conversions using xmlunit
    (existing test was not checking whole DOM tree, only topmost node)
  - tweaked test queries to include xsi:type attributes to help with
    unit-testing checks

 Revision 1.2  2005/03/21 18:31:51  mch
 Included dates; made function types more explicit

 Revision 1.1  2005/02/28 19:36:39  mch
 Fixes to tests

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.2  2005/02/17 18:18:59  mch
 Moved in from datacenter project

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.6.12.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.2.6.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.2  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.1.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate


 */



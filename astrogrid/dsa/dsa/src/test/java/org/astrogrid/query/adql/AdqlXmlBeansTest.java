/*
 * $Id: AdqlXmlBeansTest.java,v 1.1 2009/05/13 13:21:01 gtr Exp $
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import java.net.URL;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
//import org.astrogrid.query.Query;
//import org.astrogrid.query.SimpleQueryMaker;
//import org.astrogrid.query.adql.AdqlXml074Parser;
//import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;

import org.apache.xmlbeans.* ;
import org.astrogrid.adql.v1_0.beans.*;
// For validation of beans
import java.util.ArrayList;
import java.util.Iterator;

/* For DOM comparisons */
import org.custommonkey.xmlunit.*;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.astrogrid.xml.DomHelper;

/* For xslt */
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/*
 * KEA NOTES
 *
 * - "From" node is optional - need to add default table programmatically if 
 * missing to get valid sql?
 * - Need to incorporate DSA's own limit into limit clause
 * - Consider issue of coordinate system (use IRCS by default) - webservice
 * to convert?
 * - Change 0.7.4 to 1.0 in schema namespace by default?  (CHECK that 0.7.4
 *   is functionally strict subset of 1.0)
 *
/**
 * Round-trip tests - can we accept ADQL queries, parse them & produce matching ADQL?
 */

public class AdqlXmlBeansTest extends XMLTestCase   {

  // In the live dsa, this would be set as "conesearch.table".
  // Use this property as the default table when no table has
  // been specified in the input adql.
  //private static String DEFAULT_FROM = 
   //"<From xsi:type=\"fromType\"><Table xsi:type=\"tableType\" Alias=\"a\" Name=\"catalogue\"/></From>";
   //Note: Need to put all the namespace stuff in the fragment if you
   //want to have control over the prefices etc
  private static String DEFAULT_FROM = 
   "<Table xsi:type=\"tableType\" Alias=\"a\" Name=\"catalogue\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\"/>";

  private AdqlTestHelper adqlTestHelper = new AdqlTestHelper("v1_0");

  private static XmlOptions xmlOptions;
   /* Actual tests */
  /*
   public void testNvo1() throws Exception {
      SelectDocument sDoc = 
          testParse(adqlTestHelper.getNvo1Stream());
      compareIdentical(sDoc, adqlTestHelper.getNvo1());
      transformToSql(sDoc);
   }
   public void testNvo2() throws Exception {
      SelectDocument sDoc = 
          testParse(adqlTestHelper.getNvo2Stream());
      compareIdentical(sDoc, adqlTestHelper.getNvo2());
      transformToSql(sDoc);
   }
   public void testNvo3() throws Exception {
      SelectDocument sDoc = 
          testParse(adqlTestHelper.getNvo3Stream());
      compareIdentical(sDoc, adqlTestHelper.getNvo3());
      transformToSql(sDoc);
   }
   public void testNvo4() throws Exception {
      SelectDocument sDoc = 
          testParse(adqlTestHelper.getNvo4Stream());
      compareIdentical(sDoc, adqlTestHelper.getNvo4());
      transformToSql(sDoc);
   }
   */
   
   public void testSelectAll_1_0() throws Exception {
     suiteTest("selectAll", "v1_0", true);
   }
   public void testSelectAllWithNamespaces1_0() throws Exception {
     suiteTest("selectAllWithNamespaces", "v1_0", true);
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
   public void testSelectAllArchive() throws Exception {
     suiteTest("selectAllArchive", "v1_0", true);
   }
   public void testSelectTwoTablesFourColsArchive() throws Exception {
     suiteTest("selectTwoTablesFourColsArchive", "v1_0", true);
   }
   public void testSelectFromInnerJoin() throws Exception {
     suiteTest("selectFromInnerJoin", "v1_0", true);
   }
   public void testSelectFromOuterJoin() throws Exception {
     suiteTest("selectFromOuterJoin", "v1_0", true);
   }
   public void testSelectFromRightOuterJoin() throws Exception {
     suiteTest("selectFromRightOuterJoin", "v1_0", true);
   }
   public void testSelectSomeNoAlias() throws Exception {
     suiteTest("selectSomeNoAlias", "v1_0", true);
   }

   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(AdqlXmlBeansTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }

   /**
    * Validates an input adql document 
    */
   public boolean isValidAdql(SelectDocument selectDoc) 
   {
     // Check that the document is schema-valid
     // Set up the validation error listener.
     ArrayList validationErrors = new ArrayList();
     xmlOptions = new XmlOptions();
     xmlOptions.setErrorListener(validationErrors);
      
     boolean isValid = selectDoc.validate(xmlOptions);
     if (!isValid) {
       System.out.println("!!!! XML is not schema-valid !!!!! ");
       Iterator iter = validationErrors.iterator();
       while (iter.hasNext()) {
         System.out.println(">> " + iter.next() + "\n");
       }
     }
     return isValid;
   }

   public SelectDocument testParse(String adqlString) throws Exception 
   {
     // TOFIX: Probably ought to check for known/allowed namespaces
    
     // Change the 0.7.4 namespace to 1.0 
     // TOFIX: 0.7.4 is functionally a subset of 1.0, yes?
     String fixedString = adqlString.replaceAll(
         "http://www\\.ivoa\\.net/xml/ADQL/v0\\.7\\.4",
         "http://www.ivoa.net/xml/ADQL/v1.0"
     );
     System.out.println("Fixed string is :\n" + fixedString);

     // Now, parse the XML into an xmlbeans structure.  This step
     // DOESN'T validate against schema.
     SelectDocument selectDoc = SelectDocument.Factory.parse(fixedString);
      
     boolean isValid = isValidAdql(selectDoc);
     if (!isValid) {
       System.out.println("!!!! XML is not schema-valid !!!!! ");
       fail();
     }
     // Make sure it has a FROM clause, and insert a default one if it 
     // doesn't
     SelectType selectType = null;
     selectType = selectDoc.getSelect();
     assertNotNull(selectType);

     FromType fromType = null;
     fromType = selectType.getFrom();
     if (!selectType.isSetFrom()) {
       //System.out.println("!!!! DIDN'T GET FROM !!!!! ");
       fromType = FromType.Factory.parse(DEFAULT_FROM);
       selectType.setFrom(fromType);
       //Check still valid
       isValid = isValidAdql(selectDoc);
       if (!isValid) {
         //System.out.println("!!!! Broke ADQL adding FROM!!!!! ");
         fail();
       }
     }
     System.out.println(selectDoc.toString());
     return selectDoc;
   }

   public void compareIdentical(SelectDocument adqlBeanDoc, Document fileAdqlDom) throws Exception 
   {
      //SelectDocument selectDocument = null ;
      //selectDocument = SelectDocument.Factory.parse( adqlStream ) ;

/*
      Element fileAdqlElement = fileAdqlDom.getDocumentElement();
      Query query = AdqlXml074Parser.makeQuery(fileAdqlElement);
      String writtenAdql = Adql074Writer.makeAdql(query);
      System.out.print(writtenAdql);
      */
      Document beanDom = DomHelper.newDocument(adqlBeanDoc.toString());

      // Normalize just to be sure 
      fileAdqlDom.normalize();
      beanDom.normalize();

      // Using xmlunit to compare documents
      setIgnoreWhitespace(true);
      assertXMLEqual("Documents are not equal!!",fileAdqlDom, beanDom);
   }
   private void transformToSql(SelectDocument adqlBeanDoc) throws Exception
   {
	   // EXAMPLE OF HOW TO GET AT THE Top attribute
      /*
      SelectType selectType = null;
      selectType = adqlBeanDoc.getSelect();
      assertNotNull(selectType);
      if (selectType.isSetRestrict()) {
         SelectionLimitType restrict = selectType.getRestrict();
         if (restrict.isSetTop()) {
           long top = restrict.getTop();
           System.out.println("Top is " + Long.toString(top));
         }
      }
      */
      //find specified sheet as resource of this class
      InputStream xsltIn = 
        //AdqlXmlBeansTest.class.getResourceAsStream("./adql074-2-sql.xsl");
        AdqlXmlBeansTest.class.getResourceAsStream("./ADQL10_MYSQL.xsl");
     
   	TransformerFactory tFactory = TransformerFactory.newInstance();
       try {
          tFactory.setAttribute("UseNamespaces", Boolean.FALSE);
       }
       catch (IllegalArgumentException iae) {
          //ignore - if UseNamepsaces is unsupported, it will chuck an exception, and
          //we don't want to use namespaces anyway so taht's fine
       }
       Transformer transformer = tFactory.newTransformer(new StreamSource(xsltIn));
       //transform
       StringWriter sw = new StringWriter();
       Document beanDom = DomHelper.newDocument(adqlBeanDoc.toString());
       // NOTE: Seem to require a DOMSource rather than a StreamSource
       // here or the transformer barfs - no idea why
       //StreamSource source = new StreamSource(adqlBeanDoc.toString());
       DOMSource source = new DOMSource(beanDom);
       transformer.transform(source, new StreamResult(sw));
       String sql = sw.toString();

       //tidy it up - remove new lines and double spaces
       /*
       sql = sql.replaceAll("\n","");
       sql = sql.replaceAll("\r","");
       while (sql.indexOf("  ")>-1) { sql = sql.replaceAll("  ", " "); }
       */
       System.out.println("Sql is:");
       System.out.println(sql);
   }
   
   private void printHelpfulStuff(String filename) {
      System.out.println("------------------------------------------------");
      System.out.println("Testing query " + filename);
      System.out.println("------------------------------------------------");
   }

   private void suiteTest(String name, String version, boolean expectIdentical) throws Exception
   {
      printHelpfulStuff(name);
      SelectDocument sDoc= testParse(
          adqlTestHelper.getSuiteAdqlString(name, version));
      if (expectIdentical) {
         compareIdentical(sDoc, adqlTestHelper.getSuiteAdql(name, "v1_0"));
      }
      else {
         compareIdentical(sDoc, adqlTestHelper.getSuiteAdqlExpected(name, "v1_0"));
      }
      transformToSql(sDoc);
   }

}

/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import java.io.StringReader;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.query.Query;
import org.astrogrid.query.ConeConverter;
import org.astrogrid.cfg.ConfigFactory;

import org.astrogrid.adql.AdqlCompiler;
// XMLBeans stuff
import org.apache.xmlbeans.* ;
import org.astrogrid.adql.v1_0.beans.*;



/* For DOM comparisons */
import org.custommonkey.xmlunit.*;

/**
 * Unit tests for the ConeConverter class, which produces
 * a Query from conesearch input parameters (ra, dec, radius).
 */
public class ConeConverterTest extends TestCase   {

   AdqlTestHelper helper = new AdqlTestHelper();

 /** For converting from ADQL/sql strings to XML Beans */
   private AdqlCompiler compiler;
   
   private String coneCatalog = "SampleStarsCat";
   private String coneTable = "SampleStars";
   private String coneRACol = "ColName_RA";
   private String coneDecCol = "ColName_Dec";
   private String coneUnits = "deg";

   protected void setUp()
   {
      // Set conesearch table properties.
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.radius.limit", "5.0");

      // Default config here is trig in radians,
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");
   }

   /* 
    * Construct a default conesearch query.
    */ 
   public void testHaversineQuery() throws Exception
   {
      // Small radius uses haversine version 
      String adql = ConeConverter.getAdql(
            coneCatalog, coneTable, coneUnits, coneRACol, coneDecCol,
            1.0, 1.0, 0.01);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }
   public void testGreatCircleQuery() throws Exception
   {
      // Large radius uses great circle version 
      String adql = ConeConverter.getAdql(
            coneCatalog, coneTable, coneUnits, coneRACol, coneDecCol,
            1.0, 1.0, 0.5);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }

   /* The following suite of tests checks all possible combinations
    * of trig function units (deg or rad) and DB column units (deg or rad).
    */
   public void testDegDegHaversineQuery() throws Exception
   {
      // Set trig to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "false");

      // Small radius uses haversine version 
      // Set cols to use degrees
      String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, "deg", coneRACol, coneDecCol,
           1.0, 1.0, 0.01);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }
   public void testDegDegGreatCircleQuery() throws Exception
   {
      // Set trig to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "false");

      // Large radius uses great circle version 
      // Set cols to use degrees
      String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, "deg", coneRACol, coneDecCol,
           1.0, 1.0, 0.5);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }
   public void testDegRadHaversineQuery() throws Exception
   {
      // Set trig to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "false");

      // Small radius uses haversine version 
      // Set cols to use radians
      String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, "rad", coneRACol, coneDecCol,
           1.0, 1.0, 0.01);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Should have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") != -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }
   public void testDegRadGreatCircleQuery() throws Exception
   {
      // Set trig to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "false");

      // Large radius uses great circle version 
      // Set cols to use radians
      String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, "rad", coneRACol, coneDecCol,
           1.0, 1.0, 0.5);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Should have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") != -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }
   public void testRadDegHaversineQuery() throws Exception
   {
      // Set trig to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");

      // Small radius uses haversine version 
      // Set cols to use degrees
      String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, "deg", coneRACol, coneDecCol,
           1.0, 1.0, 0.01);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }
   public void testRadDegGreatCircleQuery() throws Exception
   {
      // Set trig to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");

      // Large radius uses great circle version 
      // Set cols to use degrees
      String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, "deg", coneRACol, coneDecCol,
           1.0, 1.0, 0.5);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }
   public void testRadRadHaversineQuery() throws Exception
   {
      // Set trig to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");

      // Small radius uses haversine version 
      // Set cols to use radians
      String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, "rad", coneRACol, coneDecCol,
           1.0, 1.0, 0.01);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }
   public void testRadRadGreatCircleQuery() throws Exception
   {
      // Set trig to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");

      // Large radius uses great circle version 
      // Set cols to use radians
      String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, "rad", coneRACol, coneDecCol,
           1.0, 1.0, 0.5);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      StringReader source = new StringReader(adql) ;
      Query query = new Query(
          (SelectDocument)getCompiler(source).compileToXmlBeans() );
   }


   public void testBadRa1()
   {
      try {
         String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, coneUnits, coneRACol, coneDecCol,
           -1.0, 1.0, 0.01);
         fail("This conesearch had bad RA");
      }
      catch (Exception e) {
      }
   }
   public void testBadRa2()
   {
      try {
         String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, coneUnits, coneRACol, coneDecCol,
           362.0, 1.0, 0.01);
         fail("This conesearch had bad RA");
      }
      catch (Exception e) {
      }
   }
   public void testBadDec1()
   {
      try {
         String adql = ConeConverter.getAdql(
           coneCatalog, coneTable, coneUnits, coneRACol, coneDecCol,
           1.0, -91.0, 0.01);
         fail("This conesearch had bad Dec");
      }
      catch (Exception e) {
      }
   }
   public void testBadDec2()
   {
      try {
         String adql = ConeConverter.getAdql(
            coneCatalog, coneTable, coneUnits, coneRACol, coneDecCol,
            1.0, 91.0, 0.01);
         fail("This conesearch had bad Dec");
      }
      catch (Exception e) {
      }
   }
   public void testBadRadius()
   {
      try {
         String adql = ConeConverter.getAdql(
            coneCatalog, coneTable, coneUnits, coneRACol, coneDecCol,
            1.0, 1.0, -0.01);
         fail("This conesearch had bad radius");
      }
      catch (Exception e) {
      }
   }
   public void testBadCatalogName1()
   {
      try {
         String adql = ConeConverter.getAdql(
           "", coneTable, coneUnits, coneRACol, coneDecCol,
           1.0, 1.0, 0.01);
         fail("This conesearch had empty catalog name");
      }
      catch (Exception e) {
      }
   }
   public void testBadCatalogName2()
   {
      try {
         String adql = ConeConverter.getAdql(
           null, coneTable, coneUnits, coneRACol, coneDecCol,
             1.0, 1.0, 0.01);
         fail("This conesearch had null catalog name");
      }
      catch (Exception e) {
      }
   }
   public void testBadTableName1()
   {
      try {
         String adql = ConeConverter.getAdql(
           coneCatalog, "", coneUnits, coneRACol, coneDecCol,
           1.0, 1.0, 0.01);
         fail("This conesearch had empty table name");
      }
      catch (Exception e) {
      }
   }
   public void testBadTableName2()
   {
      try {
         String adql = ConeConverter.getAdql(
           coneCatalog, null, coneUnits, coneRACol, coneDecCol,
             1.0, 1.0, 0.01);
         fail("This conesearch had null table name");
      }
      catch (Exception e) {
      }
   }
   public void testBadRaName1()
   {
      try {
         String adql = ConeConverter.getAdql(
             coneCatalog, coneTable, coneUnits, "", coneDecCol,
             1.0, 1.0, 0.01);
         fail("This conesearch had empty RA column name");
      }
      catch (Exception e) {
      }
   }
   public void testBadRaName2()
   {
      try {
         String adql = ConeConverter.getAdql(
             coneCatalog, coneTable, coneUnits, null, coneDecCol,
             1.0, 1.0, 0.01);
         fail("This conesearch had null RA column name");
      }
      catch (Exception e) {
      }
   }
   public void testBadDecName1()
   {
      try {
         String adql = ConeConverter.getAdql(
             coneCatalog, coneTable, coneUnits, coneRACol, "",
             1.0, 1.0, 0.01);
         fail("This conesearch had empty Dec column name");
      }
      catch (Exception e) {
      }
   }
   public void testBadDecName2()
   {
      try {
         String adql = ConeConverter.getAdql(
             coneCatalog, coneTable, coneUnits, coneRACol, null,
             1.0, 1.0, 0.01);
         fail("This conesearch had null Dec column name");
      }
      catch (Exception e) {
      }
   }
   public void testBadUnits1()
   {
      try {
         String adql = ConeConverter.getAdql(
             coneCatalog, coneTable, "", coneRACol, coneDecCol,
             1.0, 1.0, 0.01);
         fail("This conesearch had empty units name");
      }
      catch (Exception e) {
      }
   }
   public void testBadUnits2()
   {
      try {
         String adql = ConeConverter.getAdql(
             coneCatalog, coneTable, null, coneRACol, coneDecCol,
             1.0, 1.0, 0.01);
         fail("This conesearch had null units name");
      }
      catch (Exception e) {
      }
   }
   public void testBadUnits3()
   {
      try {
         String adql = ConeConverter.getAdql(
             coneCatalog, coneTable, "blah", coneRACol, coneDecCol,
             1.0, 1.0, 0.01);
         fail("This conesearch had unrecognized units name");
      }
      catch (Exception e) {
      }
   }
   public void testBadConesearchLimit()
   {
      try {
         // Large radius uses great circle version 
         String adql = ConeConverter.getAdql(
            coneCatalog, coneTable, coneUnits, coneRACol, coneDecCol,
            1.0, 1.0, 10.0);
         fail("This conesearch had a too-large radius");
      }
      catch (Exception e) {
      }
   }

   /* Junit stuff */

   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(ConeConverterTest.class);
   }
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
   /** Creates an ADQL/sql compiler where required, and/or compiles
    *     * a given ADQL/sql fragment.  */
  private AdqlCompiler getCompiler(StringReader source)
  {
     if (compiler == null) {
        compiler = new AdqlCompiler(source);
     }
     else {
        compiler.ReInit(source);
     }
     return compiler;
  }

}

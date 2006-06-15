/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.adql;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.query.Query;
import org.astrogrid.query.ConeConverter;
import org.astrogrid.cfg.ConfigFactory;
/*
import org.astrogrid.xml.DomHelper;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
*/

/* For DOM comparisons */
import org.custommonkey.xmlunit.*;

/**
 * Unit tests for the ConeConverter class, which produces
 * a Query from conesearch input parameters (ra, dec, radius).
 */
public class ConeConverterTest extends TestCase   {

   AdqlTestHelper helper = new AdqlTestHelper();
   
   protected void setUp()
   {
      // Set conesearch table properties.
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.table","catalogue");
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.ra.column","RA");
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.dec.column","DEC");

      // Default config here is trig in radians,
      // columns in degrees (pretty common)
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","deg");
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");
   }

   /* 
    * Construct a default conesearch query.
    */ 
   public void testDefaultHaversineQuery() throws Exception
   {
      // Small radius uses haversine version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.01);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testDefaultGreatCircleQuery() throws Exception
   {
      // Large radius uses great circle version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.5);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testParameterizedHaversineQuery() throws Exception
   {
      // Small radius uses haversine version 
      String adql = ConeConverter.getAdql(
          "testcat", "testra", "testdec", 
          1.0, 1.0, 0.01);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testParameterizedGreatCircleQuery() throws Exception
   {
      // Large radius uses great circle version 
      String adql = ConeConverter.getAdql(
          "testcat", "testra", "testdec", 
          1.0, 1.0, 0.5);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }

   /* The following suite of tests checks all possible combinations
    * of trig function units (deg or rad) and DB column units (deg or rad).
    */
   public void testDegDegHaversineQuery() throws Exception
   {
      // Set trig to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "false");
      // Set cols to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","deg");

      // Small radius uses haversine version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.01);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testDegDegGreatCircleQuery() throws Exception
   {
      // Set trig to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "false");
      // Set cols to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","deg");

      // Large radius uses great circle version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.5);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testDegRadHaversineQuery() throws Exception
   {
      // Set trig to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "false");
      // Set cols to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","rad");

      // Small radius uses haversine version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.01);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Should have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") != -1);

      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testDegRadGreatCircleQuery() throws Exception
   {
      // Set trig to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "false");
      // Set cols to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","rad");

      // Large radius uses great circle version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.5);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Should have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") != -1);

      // Test that adql is valid by creating Query with it
      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testRadDegHaversineQuery() throws Exception
   {
      // Set trig to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");
      // Set cols to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","deg");

      // Small radius uses haversine version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.01);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testRadDegGreatCircleQuery() throws Exception
   {
      // Set trig to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");
      // Set cols to use degrees
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","deg");

      // Large radius uses great circle version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.5);

      // Should have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") != -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testRadRadHaversineQuery() throws Exception
   {
      // Set trig to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");
      // Set cols to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","rad");

      // Small radius uses haversine version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.01);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }
   public void testRadRadGreatCircleQuery() throws Exception
   {
      // Set trig to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "db.trigfuncs.in.radians", "true");
      // Set cols to use radians
      ConfigFactory.getCommonConfig().setProperty(
          "conesearch.columns.units","rad");

      // Large radius uses great circle version 
      String adql = ConeConverter.getAdql(1.0, 1.0, 0.5);

      // Shouldn't have conversion to radians here
      assertTrue(adql.indexOf("RADIANS") == -1);
      // Shouldn't have conversion to degrees here
      assertTrue(adql.indexOf("DEGREES") == -1);

      // Test that adql is valid by creating Query with it
      // Test that adql is valid by creating Query with it
      Query query = new Query(adql);
   }


   public void testBadRa1()
   {
      try {
         String adql = ConeConverter.getAdql(-1.0, 1.0, 0.01);
         fail("This conesearch had bad RA");
      }
      catch (Exception e) {
      }
   }
   public void testBadRaParam1()
   {
      try {
         String adql = ConeConverter.getAdql(
             "testcat", "testra", "testdec", 
             -1.0, 1.0, 0.01);
         fail("This conesearch had bad RA");
      }
      catch (Exception e) {
      }
   }
   public void testBadRa2()
   {
      try {
         String adql = ConeConverter.getAdql(362.0, 1.0, 0.01);
         fail("This conesearch had bad RA");
      }
      catch (Exception e) {
      }
   }
   public void testBadRaParam2()
   {
      try {
         String adql = ConeConverter.getAdql(
             "testcat", "testra", "testdec", 
             362.0, 1.0, 0.01);
         fail("This conesearch had bad RA");
      }
      catch (Exception e) {
      }
   }
   public void testBadDec1()
   {
      try {
         String adql = ConeConverter.getAdql(1.0, -91.0, 0.01);
         fail("This conesearch had bad Dec");
      }
      catch (Exception e) {
      }
   }
   public void testBadDecParam1()
   {
      try {
         String adql = ConeConverter.getAdql(
             "testcat", "testra", "testdec", 
             1.0, -91.0, 0.01);
         fail("This conesearch had bad Dec");
      }
      catch (Exception e) {
      }
   }
   public void testBadDec2()
   {
      try {
         String adql = ConeConverter.getAdql(1.0, 91.0, 0.01);
         fail("This conesearch had bad Dec");
      }
      catch (Exception e) {
      }
   }
   public void testBadDecParam2()
   {
      try {
         String adql = ConeConverter.getAdql(
             "testcat", "testra", "testdec", 
             1.0, 91.0, 0.01);
         fail("This conesearch had bad Dec");
      }
      catch (Exception e) {
      }
   }
   public void testBadRadius()
   {
      try {
         String adql = ConeConverter.getAdql(1.0, 1.0, -0.01);
         fail("This conesearch had bad radius");
      }
      catch (Exception e) {
      }
   }
   public void testBadRadiusParam()
   {
      try {
         String adql = ConeConverter.getAdql(
             "testcat", "testra", "testdec", 
             1.0, 1.0, -0.01);
         fail("This conesearch had bad radius");
      }
      catch (Exception e) {
      }
   }
   public void testBadTableName1()
   {
      try {
         String adql = ConeConverter.getAdql(
             "", "testra", "testdec", 
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
             null, "testra", "testdec", 
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
             "testcat", "", "testdec", 
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
             "testcat", null, "testdec", 
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
             "testcat", "testra", "", 
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
             "testcat", "testra", null, 
             1.0, 1.0, 0.01);
         fail("This conesearch had null Dec column name");
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
   
}

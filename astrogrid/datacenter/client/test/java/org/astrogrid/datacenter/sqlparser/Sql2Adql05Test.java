/*
 * $Id: Sql2Adql05Test.java,v 1.1 2004/08/25 23:38:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.util.DomHelper;
import org.xml.sax.SAXException;


/**
 * Tests SQL - ADQL translator. Doesn't actually check the results properly...
 */

public class Sql2Adql05Test extends TestCase   {
   
   public void assertValidXml(String s) throws ParserConfigurationException, IOException {
      try {
         DomHelper.newDocument(s);
      }
      catch (SAXException e) {
         fail("Invalid XML generated: "+e);
      }
   }
   
   public void testSelectAll() throws IOException, ParserConfigurationException {
      String s = "SELECT * FROM CHARLIE";
      
      String adql = Sql2Adql05.translate(s);
      
      assertValidXml(adql);
   }
   
   public void testConditions() throws IOException, ParserConfigurationException {
      String s = "SELECT S.RA,    T.WIBBLE, UNDIE.PANTS, ETC.ETC FROM A, B,  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
      
      String adql = Sql2Adql05.translate(s);
      
      assertValidXml(adql);
   }
   public void testConditions2() throws IOException, ParserConfigurationException {
      String s = "SELECT t.a, g.d FROM Tab as a, Tab as d WHERE a.d < d.e AND a.f < d.f";
      assertValidXml(Sql2Adql05.translate(s));
   }
   
   public void testFuncs() throws IOException, ParserConfigurationException {
      String s = "SELECT t.a, g.d FROM Tab as a, Tab as d WHERE AVG(a.d) < SUM(d.e) AND a.f < d.f";
      assertValidXml(Sql2Adql05.translate(s));
   }
   
   public void testCircle() throws IOException, ParserConfigurationException {
      String s = "SELECT * WHERE CIRCLE(J2000, 25, 35, 6)";
      assertValidXml(Sql2Adql05.translate(s));
   }

   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(Sql2Adql05Test.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

/*
 $Log: Sql2Adql05Test.java,v $
 Revision 1.1  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 */



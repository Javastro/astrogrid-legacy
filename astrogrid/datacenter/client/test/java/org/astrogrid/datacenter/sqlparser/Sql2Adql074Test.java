/*
 * $Id: Sql2Adql074Test.java,v 1.1 2004/08/25 23:38:34 mch Exp $
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

public class Sql2Adql074Test extends TestCase   {
   
   public void assertValidXml(String s) throws ParserConfigurationException, IOException {
      try {
         DomHelper.newDocument(s);
         //print out for human checking...
         System.out.println(s);
      }
      catch (SAXException e) {
         fail("Invalid XML generated: "+e);
      }
   }
   
   public void testSelectAll() throws IOException, ParserConfigurationException {
      String s = "SELECT * FROM CHARLIE";
      
      String adql = Sql2Adql074.translate(s);
      
      assertValidXml(adql);
   }
   
   public void testConditions() throws IOException, ParserConfigurationException {
      String s = "SELECT S.RA,    T.WIBBLE, UNDIE.PANTS, ETC.ETC FROM A, B,  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
      
      String adql = Sql2Adql074.translate(s);
      
      assertValidXml(adql);
   }
   public void testConditions2() throws IOException, ParserConfigurationException {
      String s = "SELECT t.a, g.d FROM Tab as a, Tab as d WHERE a.d < d.e AND a.f < d.f";
      assertValidXml(Sql2Adql074.translate(s));
   }
   
   public void testFuncs() throws IOException, ParserConfigurationException {
      String s = "SELECT t.a, g.d FROM Tab as a, Tab as d WHERE AVG(a.d) < SUM(d.e) AND a.f < d.f";
      assertValidXml(Sql2Adql074.translate(s));
   }
   
   public void testCircle() throws IOException, ParserConfigurationException {
      String s = "SELECT * WHERE CIRCLE(J2000, 25, 35, 6)";
      assertValidXml(Sql2Adql074.translate(s));
   }

   /* The NVO translations are based on 'std' SQL examples generated on the page
   at http://openskyquery.net/AdqlTranslator/Convertor.aspx, however the SQL here
    is slightly modified to fit with the parser limitations */
   
   public void testNvo1() throws IOException, ParserConfigurationException {
      assertValidXml(Sql2Adql074.translate(
         "Select * From Tab as t"
      ));
   }
   public void testNvo2() throws IOException, ParserConfigurationException {
      assertValidXml(Sql2Adql074.translate(
         "Select t.a, g.d from Tab as a, Tab as d where a.d < d.e and a.f < d.f"
      ));
   }
   public void testNvo3() throws IOException, ParserConfigurationException {
      assertValidXml(Sql2Adql074.translate(
         "Select t.*, b.* From Tab as t, Bob as b Where t.g <> b.g and Circle(J2000,12.5,23.0,5.0)"
      ));
   }
   public void testNvo4() throws IOException, ParserConfigurationException {
      assertValidXml(Sql2Adql074.translate(
         "Select t.b,b.d From Tab as t, Bob as b"
      ));
   }
   public void testNvo5() throws IOException, ParserConfigurationException {
      assertValidXml(Sql2Adql074.translate(
         "Select t.b, b.* from Tab as t, Bob as b Where Avg(b.dd) < 4.56"
      ));
   }
   public void testNvo6() throws IOException, ParserConfigurationException {
      assertValidXml(Sql2Adql074.translate(
         "Select sin(A.b), A.* From Tab as A where Circle(J2000, 1.2, 2.4, 0.2) And (log(A.d) < 1.24)"
      ));
   }
   public void testNvo7() throws IOException, ParserConfigurationException {
      assertValidXml(Sql2Adql074.translate(
         "Select a.* from Tab as a where Circle(Cartesian, 1.2, 2.4,3.6,0.2)"
      ));
   }
   public void testNvo8() throws IOException, ParserConfigurationException {
      assertValidXml(Sql2Adql074.translate(
         "Select (a.b - c.d), atwo.* From Tab as a, Bob as b, b.e as atwo"
      ));
   }

   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(Sql2Adql074Test.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}

/*
 $Log: Sql2Adql074Test.java,v $
 Revision 1.1  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 */



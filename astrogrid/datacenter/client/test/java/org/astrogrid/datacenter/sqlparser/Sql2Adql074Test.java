/*
 * $Id: Sql2Adql074Test.java,v 1.10 2004/10/25 13:14:19 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.datacenter.query.Adql074Writer;
import org.astrogrid.datacenter.query.SqlQueryMaker;
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
      
      String adql = translate(s);
      
      assertValidXml(adql);
   }
   
   public void testConditions() throws IOException, ParserConfigurationException {
      String s = "SELECT S.RA,    T.WIBBLE, UNDIE.PANTS, ETC.ETC FROM A, B,  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
      
      String adql = translate(s);
      
      assertValidXml(adql);
   }
   public void testConditions2() throws IOException, ParserConfigurationException {
      String s = "SELECT t.a, g.d FROM Tab as a, Tab as d WHERE a.d < d.e AND a.f < d.f";
      assertValidXml(translate(s));
   }
   
   public void testFuncs() throws IOException, ParserConfigurationException {
      String s = "SELECT t.a, g.d FROM Tab as a, Tab as d WHERE AVG(a.d) < SUM(d.e) AND a.f < d.f";
      assertValidXml(translate(s));
   }
   
   public void testCircle() throws IOException, ParserConfigurationException {
      String s = "SELECT * FROM table WHERE CIRCLE('J2000', 25, 35, 6)";
      assertValidXml(translate(s));
   }

   /* need to move this to the server side
   public void testConvertedCircle() throws IOException, ParserConfigurationException {
      SimpleConfig.setProperty(StdSqlMaker.DB_TRIGFUNCS_IN_RADIANS, "true");
      SimpleConfig.setProperty(StdSqlMaker.CONE_SEARCH_TABLE_KEY, "ConeTable");
      SimpleConfig.setProperty(StdSqlMaker.CONE_SEARCH_RA_COL_KEY, "RA");
      SimpleConfig.setProperty(StdSqlMaker.CONE_SEARCH_DEC_COL_KEY, "DEC");
      SimpleConfig.setProperty(StdSqlMaker.CONE_SEARCH_COL_UNITS_KEY, "deg");
      String s = "SELECT * FROM table WHERE "+new StdSqlMaker().makeSqlCircleCondition(new Angle(25.0), new Angle(35), new Angle(6));
      assertValidXml(translate(s));
   }
    */

   /* The NVO translations are based on 'std' SQL examples generated on the page
   at http://openskyquery.net/AdqlTranslator/Convertor.aspx, however the SQL here
    is slightly modified to fit with the parser limitations */
   
   public void testNvo1() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select * From Tab as t"
      ));
   }
   public void testNvo2() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select t.a, g.d from Tab as a, Tab as d where a.d < d.e and a.f < d.f"
      ));
   }
   public void testNvo3() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select t.*, b.* From Tab as t, Bob as b Where t.g <> b.g and Circle('J2000',12.5,23.0,5.0)"
      ));
   }
   public void testNvo4() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select t.b,b.d From Tab as t, Bob as b"
      ));
   }
   public void testNvo5() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select t.b, b.* from Tab as t, Bob as b Where Avg(b.dd) < 4.56"
      ));
   }
   /* Not yet implemented
   public void testNvo6() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select sin(A.b), A.* From Tab as A where Circle(J2000, 1.2, 2.4, 0.2) And (log(A.d) < 1.24)"
      ));
   }
    /**/
   
   public void testNvo7() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
//         "Select a.* from Tab as a where Circle('Cartesian', 1.2, 2.4,3.6,0.2)"
         "Select a.* from Tab as a where Circle('J2000', 1,2,3)"
      ));
   }
   
   /* Not yet implemented
   public void testNvo8() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select (a.b - c.d), atwo.* From Tab as a, Bob as b, b.e as atwo"
      ));
   }
    /**/

   public void testSsa() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT ra,dec, sCorMagB, sCorMagR1, sCorMagR2, sCorMagI\n"+
         "FROM Source\n"+
         "WHERE\n"+
         "ellipR1 < 0.33 AND qualR1 < 2048 AND (prfstatR1 > -5 AND prfstatR1 < +5) AND\n"+
         "ellipR2 < 0.33 AND qualR2 < 2048 AND (prfstatR2 > -5 AND prfstatR2 < +5) AND\n"+
         "ABS(0 + sCorMagR1 - sCorMagR2) > 3 AND scorMagR1 > 0.0 AND sCorMagR2 > 0.0\n"+
         "ORDER BY ra,dec"));
   }
   
   public void testPat1() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT * from IX/35/xmm1obs AS p where p.RA > 10"));
   }
      
   public void testLimit1() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT * LIMIT 12 from Table AS p where p.RA > 10"));
   }

   public void testLimit2() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT p.RA, p.DEC LIMIT   12   from IX/35/xmm1obs AS p where p.RA > 10"));
   }

   public String translate(String sql) throws IOException {
       return Adql074Writer.makeAdql(SqlQueryMaker.makeQuery(sql), "from SQL "+sql);
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
 Revision 1.10  2004/10/25 13:14:19  jdt
 Merges from branch PAL_MCH - another attempt

 Revision 1.7.6.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.7  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.6.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.6  2004/10/12 23:09:53  mch
 Lots of changes to querying to get proxy querying to SSA, and registry stuff

 Revision 1.5  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.4  2004/09/08 21:24:14  mch
 Commented out tests on things not yet implemented

 Revision 1.3  2004/08/27 09:31:16  mch
 Added limit, order by, some page tidying, etc

 Revision 1.2  2004/08/26 11:47:16  mch
 Added tests based on Patricios errors and other SQl statements, and subsequent fixes...

 Revision 1.1  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 */



/*
 * $Id: SqlParserTest.java,v 1.4 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.xml.DomHelper;
import org.xml.sax.SAXException;


/**
 * Tests SQL - ADQL translator.
 */

public class SqlParserTest extends TestCase   {
   
   public void assertValidXml(String s) throws IOException {
      try {
         DomHelper.newDocument(s);
         //print out for human checking...
         System.out.println(s);
      }
      catch (SAXException e) {
         fail("Invalid XML generated: "+e);
      }
   }
   
   public void testSelectAll() throws IOException {
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
      //String s = "SELECT a.x, b.y FROM Tab1 as a, Tab2 as b WHERE a.d < b.e AND a.f < b.f";
      assertValidXml(translate(s));
   }
   
   public void testFuncs() throws IOException, ParserConfigurationException {
      String s = "SELECT t.a, g.d FROM Tab as a, Tab as d WHERE AVG(a.d) < SUM(d.e) AND a.f < d.f";
      assertValidXml(translate(s));
   }
   
   public void testCircle() throws IOException, ParserConfigurationException {
      String s = "SELECT * FROM table WHERE CIRCLE('J2000', 25, 35, 0.1)";
      assertValidXml(translate(s));
   }

   /* need to move this to the server side
   public void testConvertedCircle() throws IOException, ParserConfigurationException {
      ConfigFactory.getCommonConfig().setProperty(StdSqlMaker.DB_TRIGFUNCS_IN_RADIANS, "true");
      ConfigFactory.getCommonConfig().setProperty(StdSqlWriter.CONE_SEARCH_TABLE_KEY, "ConeTable");
      ConfigFactory.getCommonConfig().setProperty(StdSqlWriter.CONE_SEARCH_RA_COL_KEY, "RA");
      ConfigFactory.getCommonConfig().setProperty(StdSqlWriter.CONE_SEARCH_DEC_COL_KEY, "DEC");
      ConfigFactory.getCommonConfig().setProperty(StdSqlMaker.CONE_SEARCH_COL_UNITS_KEY, "deg");
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
   /* Not yet implemented */
   public void testNvo6() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select sin(A.b), A.* From Tab as A where Circle('J2000', 1.2, 2.4, 0.2) And (log(A.d) < 1.24)"
      ));
   }
    /**/

   
   
   public void testNvo7() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
//         "Select a.* from Tab as a where Circle('Cartesian', 1.2, 2.4,3.6,0.2)"
         "Select a.* from Tab as a where Circle('J2000', 1,2,3)"
      ));
   }
   
   /* Not yet implemented */
   public void testNvo8() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "Select (a.b - c.d), atwo.* From Tab as a, Bob as b, b.e as atwo"
      ));
   }

   public void testDate() throws IOException {
      assertValidXml(translate(
         "Select * From Tab as a, Bob as b, b.e as atwo WHERE atwo = 2004-12-03"
      ));
   }

   public void testDateTime() throws IOException {
      assertValidXml(translate(
         "Select * From Tab as a, Bob as b, b.e as atwo WHERE atwo > 2004-12-03T00:12:00"
      ));
   }
   
   public void testSsa() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT s.ra,s.dec, s.sCorMagB, s.sCorMagR1, s.sCorMagR2, s.sCorMagI\n"+
         "FROM Source as s\n"+
         "WHERE\n"+
         "s.ellipR1 < 0.33 AND s.qualR1 < 2048 AND (s.prfstatR1 > -5 AND s.prfstatR1 < +5) AND\n"+
         "s.ellipR2 < 0.33 AND s.qualR2 < 2048 AND (s.prfstatR2 > -5 AND s.prfstatR2 < +5) AND\n"+
         "ABS(0 + s.sCorMagR1 - s.sCorMagR2) > 3 AND s.scorMagR1 > 0.0 AND s.sCorMagR2 > 0.0\n"+
         "ORDER BY s.ra,s.dec"));
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

   
   //-- proper SQL --
   public void testAvg() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT * FROM TABLE WHERE AVG(TABLE.RA) > 12"));
   }

   // -- test case & whitespace --
   public void testWhitespace() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT * from\n TABLE \nwhere AVG(TABLE.RA) > 12"));
   }

   //-- proper SQL --
   public void testFuncs2() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT * FROM TABLE WHERE SUM(TABLE.RA * 2) > 12 AND (TABLE.T * 4 > LOG(TABLE.V) )"));
   }
   
   /**  see if Min/Max and subtring works --
   public void testMinMaxSubstring() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT * FROM TABLE WHERE MIN(TABLE.RA * 2, 4) > 12 AND MAX(TABLE.T * 4, 10) > 3 OR SUBSTRING('Wibble',12) )"));
   }
    */
   
   //--- Savas' SQL ---
   public void testSavas() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT CrossNeighboursEDR.sdssID, ReliableStars.objID, \n"+
         "       ReliableStars.ra, ReliableStars.dec, \n"+
         "       ReliableStars.sCorMagR2, ReliableStars.sCorMagI,\n"+
         "       ReliableStars.sCorMagB, ReliableStars.sigMuD,\n"+
         "       ReliableStars.sigMuAcosD, ReliableStars.muD,\n"+
         "       ReliableStars.muAcosD \n"+
         "FROM ReliableStars, CrossNeighboursEDR \n"+
         "WHERE (CrossNeighboursEDR.ssaID = ReliableStars.objID) AND \n"+
         "      (CrossNeighboursEDR.SdssPrimary = 1) AND \n"+
         "      (CrossNeighboursEDR.sdsstype = 6) AND \n"+
         "      (ReliableStars.ra >= 0) AND \n"+
         "      (ReliableStars.ra <= 1) AND \n"+
         "      (ReliableStars.dec >= 2) AND \n"+
         "      (ReliableStars.dec <= 3) AND \n"+
         "      (ReliableStars.sCorMagR2 > - 99) AND \n"+
         "      (ReliableStars.sCorMagI > - 99) AND \n"+
         "      (POWER(muAcosD,2) + POWER(muD,2) > 4 * \nSQRT(POWER(muAcosD * sigMuAcosD,2) + POWER(muD * sigMuD,2)))   \n"));
   }
      
      //-- Some tree-like queries ---
   public void testTreelikeDates() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT * FROM fits_trace AS T1 where "+
         "T1.Keywords/img_time >= '2002-03-14T01:00:00' "+
         "and T1.Keywords/img_time < '2002-03-14T03:00:00' "));
   }
      
      //-- Some tree-like queries ---
   public void testSubstitution() throws IOException, ParserConfigurationException {
      assertValidXml(translate(
         "SELECT * FROM fits_trace AS T1 where "+
         "T1.Keywords/img_time >= '${somevar}' "+
         "and T1.Keywords/img_time < '${someothervar}' "));
   }
   
   
   public String translate(String sql) throws IOException {
     //System.out.println("****Translating string " + sql);
     //Query query = SqlParser.makeQuery(sql);
     //System.out.println("****Made query successfully ");
     //String adql = Adql074Writer.makeAdql(query);
     //System.out.println("****Made ADQL successfully ");
     //System.out.println("****ADQL IS " + adql);
     //return adql;
      return Adql074Writer.makeAdql(SqlParser.makeQuery(sql), "from SQL "+sql);
   }
   
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(SqlParserTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}




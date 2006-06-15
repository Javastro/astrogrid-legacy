/*$Id: KeywordMakerTest.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.keyword;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Hashtable;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.geom.Angle;
import org.astrogrid.query.OldQuery;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.condition.CircleCondition;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.keyword.KeywordMaker;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.slinger.targets.WriterTarget;

/** Test the Keyword maker from various queries
 */
public class KeywordMakerTest extends TestCase
{

   public void testJustCircle() throws IOException
   {
      OldQuery query = new OldQuery(
          new CircleCondition("J2000", 12,20, 3),
          new ReturnTable(new WriterTarget(new StringWriter())));
      KeywordMaker maker = new KeywordMaker(query);
      
      assertEquals(12.0, Angle.parseAngle(maker.getValue(KeywordMaker.RA_KEYWORD)).asDegrees(), 0.000001); //equal within 0.0..1 to allow for rounding errors
      assertEquals(20.0, Angle.parseAngle(maker.getValue(KeywordMaker.DEC_KEYWORD)).asDegrees(), 0.000001);
      assertEquals(3.0, Angle.parseAngle(maker.getValue(KeywordMaker.RADIUS_KEYWORD)).asDegrees(), 0.000001);
   }
      
   public void testJustKeyword() throws IOException
   {
      Hashtable testKeys = new Hashtable();
      testKeys.put("Name", "Arthur");
      testKeys.put("Catalogue", "SSA");
      testKeys.put("Wavelength", "optical");
      OldQuery query = new OldQuery(SimpleQueryMaker.makeKeywordCondition(testKeys), null);
      KeywordMaker maker = new KeywordMaker(query);
      
      assertEquals("Arthur", maker.getValue("NAME"));
      assertEquals("SSA", maker.getValue("CATALOGUE"));
      assertEquals("optical", maker.getValue("WAVELENGTH"));
   }

   public void testSql1() throws IOException
   {
      OldQuery query = SqlParser.makeQuery("SELECT * FROM ANYWHERE WHERE Name='Arthur' AND Catalogue='SSA' AND Wavelength=12");
      KeywordMaker maker = new KeywordMaker(query);
      
      assertEquals("Arthur", maker.getValue("NAME"));
      assertEquals("SSA", maker.getValue("CATALOGUE"));
      assertEquals(12.0, Double.parseDouble(maker.getValue("WAVELENGTH")), 0.0000001);
   }
   
   /** Check that ORs fail */
   public void testNotOr() throws IOException
   {
      OldQuery query = SqlParser.makeQuery("SELECT * FROM ANYWHERE WHERE Name='Arthur' OR Catalogue='SSA' OR Wavelength='optical'");
      try {
         new KeywordMaker(query);
         fail("Should have failed due to OR");
      }
      catch (IllegalArgumentException iae) {
         //correct behaviour to fail
      }
   }
   
   
   public static Test suite()
   {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(KeywordMakerTest.class);
   }
   
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) throws IOException
   {
      junit.textui.TestRunner.run(suite());
   }
   
}


/*
 $Log: KeywordMakerTest.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/20 15:23:08  kea
 Checking old sources in in oldserver directory (rather than just
 deleting them, might still be useful).

 Revision 1.4.84.1  2006/04/19 13:57:32  kea
 Interim checkin.  All source is now compiling, using the new Query model
 where possible (some legacy classes are still using OldQuery).  Unit
 tests are broken.  Next step is to move the legacy classes sideways out
 of the active tree.

 Revision 1.4  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.3  2005/02/28 19:36:39  mch
 Fixes to tests

 Revision 1.2  2005/02/28 18:47:05  mch
 More compile fixes

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:25  mch
 Initial checkin

 Revision 1.2.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.2  2004/11/12 13:49:12  mch
 Fix where keyword maker might not have had keywords made

 Revision 1.1  2004/11/03 05:14:33  mch
 Bringing Vizier back online


 */


/*$Id: KeywordMakerTest.java,v 1.2 2005/02/28 18:47:05 mch Exp $
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
import java.util.Hashtable;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.astrogrid.query.Query;
import org.astrogrid.query.SimpleQueryMaker;
import org.astrogrid.query.keyword.KeywordMaker;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.test.OptionalTestCase;

/** Test the Keyword maker from various queries
 */
public class KeywordMakerTest extends OptionalTestCase
{

   public void testJustCircle() throws IOException
   {
      Query query = SimpleQueryMaker.makeConeQuery(12, 20, 3);
      KeywordMaker maker = new KeywordMaker(query);
      
      assertTrue("RA not set correctly", maker.getValue(KeywordMaker.RA_KEYWORD).equals("12.0"));
      assertTrue("DEC not set correctly", maker.getValue(KeywordMaker.DEC_KEYWORD).equals("20.0"));
      assertTrue("Radius not set correctly", maker.getValue(KeywordMaker.RADIUS_KEYWORD).equals("3.0"));
   }
      
   public void testJustKeyword() throws IOException
   {
      Hashtable testKeys = new Hashtable();
      testKeys.put("Name", "Arthur");
      testKeys.put("Catalogue", "SSA");
      testKeys.put("Wavelength", "optical");
      Query query = new Query(SimpleQueryMaker.makeKeywordCondition(testKeys), null);
      KeywordMaker maker = new KeywordMaker(query);
      
      assertTrue(maker.getValue("NAME").equals("Arthur"));
      assertTrue(maker.getValue("CATALOGUE").equals("SSA"));
      assertTrue(maker.getValue("WAVELENGTH").equals("optical"));
   }

   public void testSql1() throws IOException
   {
      Query query = SqlParser.makeQuery("SELECT * FROM ANYWHERE WHERE Name='Arthur' AND Catalogue='SSA' AND Wavelength=12");
      KeywordMaker maker = new KeywordMaker(query);
      
      assertTrue(maker.getValue("NAME").equals("Arthur"));
      assertTrue(maker.getValue("CATALOGUE").equals("SSA"));
      assertTrue(maker.getValue("WAVELENGTH").equals("12"));
   }
   
   /** Check that ORs fail */
   public void testNotOr() throws IOException
   {
      Query query = SqlParser.makeQuery("SELECT * FROM ANYWHERE WHERE Name='Arthur' OR Catalogue='SSA' OR Wavelength='optical'");
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


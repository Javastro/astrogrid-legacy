/*$Id: KeywordMakerTest.java,v 1.1 2004/11/03 05:14:33 mch Exp $
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
import org.astrogrid.datacenter.impl.cds.KeywordMaker;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SimpleQueryMaker;
import org.astrogrid.datacenter.query.SqlQueryMaker;
import org.astrogrid.test.OptionalTestCase;

/** Test the Keyword maker from various queries
 */
public class KeywordMakerTest extends OptionalTestCase
{

   public void testJustCircle() throws IOException
   {
      Query query = SimpleQueryMaker.makeConeQuery(12, 20, 3);
      KeywordMaker maker = new KeywordMaker();
      Hashtable keywords = maker.makeKeywords(query);
      
      assertTrue("RA not set correctly", keywords.get(KeywordMaker.RA_KEYWORD).equals("12.0"));
      assertTrue("DEC not set correctly", keywords.get(KeywordMaker.DEC_KEYWORD).equals("20.0"));
      assertTrue("Radius not set correctly", keywords.get(KeywordMaker.RADIUS_KEYWORD).equals("3.0"));
   }
      
   public void testJustKeyword() throws IOException
   {
      Hashtable testKeys = new Hashtable();
      testKeys.put("Name", "Arthur");
      testKeys.put("Catalogue", "SSA");
      testKeys.put("Wavelength", "optical");
      Query query = new Query(SimpleQueryMaker.makeKeywordCondition(testKeys), null);
      KeywordMaker maker = new KeywordMaker();
      Hashtable keywords = maker.makeKeywords(query);
      
      assertTrue(keywords.get("NAME").equals("Arthur"));
      assertTrue(keywords.get("CATALOGUE").equals("SSA"));
      assertTrue(keywords.get("WAVELENGTH").equals("optical"));
   }

   public void testSql1() throws IOException
   {
      Query query = SqlQueryMaker.makeQuery("SELECT * FROM ANYWHERE WHERE Name='Arthur' AND Catalogue='SSA' AND Wavelength=12");
      KeywordMaker maker = new KeywordMaker();
      Hashtable keywords = maker.makeKeywords(query);
      
      assertTrue(keywords.get("NAME").equals("Arthur"));
      assertTrue(keywords.get("CATALOGUE").equals("SSA"));
      assertTrue(keywords.get("WAVELENGTH").equals("12"));
   }
   
   /** Check that ORs fail */
   public void testNotOr() throws IOException
   {
      Query query = SqlQueryMaker.makeQuery("SELECT * FROM ANYWHERE WHERE Name='Arthur' OR Catalogue='SSA' OR Wavelength='optical'");
      KeywordMaker maker = new KeywordMaker();
      try {
         Hashtable keywords = maker.makeKeywords(query);
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
 Revision 1.1  2004/11/03 05:14:33  mch
 Bringing Vizier back online


 */


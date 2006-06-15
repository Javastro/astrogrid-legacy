/*
 * $Id: KeywordTest.java,v 1.2 2006/06/15 16:50:10 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.keyword;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.astrogrid.oldquery.OldQuery;
import org.astrogrid.oldquery.SqlParserTest;
import org.astrogrid.oldquery.adql.Adql074Writer;
import org.astrogrid.oldquery.sql.SqlParser;


/**
 * Tests the keyword generator
 */

public class KeywordTest extends TestCase   {
   
   public void testSimple() throws IOException {
      
      OldQuery query = SqlParser.makeQuery("SELECT * FROM CHARLIE WHERE T = 2");
      
      KeywordMaker maker = new KeywordMaker(query);

      //nb the parser will convert numbers to reals throughout
      assertEquals(maker.getValue("T"), "2.0");
   }
   
   
   
   public static Test suite() {
      // Reflection is used here to add all the testXXX() methods to the suite.
      return new TestSuite(KeywordTest.class);
   }
   
   /**
    * Runs the test case.
    */
   public static void main(String args[]) {
      junit.textui.TestRunner.run(suite());
   }
   
}



